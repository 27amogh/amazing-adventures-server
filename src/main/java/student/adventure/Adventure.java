package student.adventure;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Adventure {


    private static Layout layout;
    // <-
    private static Room current;
    //String,Room Hash-map


    private static String usedInGameJson = null;

    private final static String DEFAULT_JSON = "src/main/resources/siebel.json";
    private final static String VALID_FILE = "Valid file!";
    private final static String CONGRATS_GAME_OVER = "You have reached the end, congrats.";

    private static boolean quitGame = false;
    static Scanner scanner = new Scanner(System.in);




    public static boolean isOver () {
        return quitGame;
    }

    public static void main(String[] args) throws IOException, NullPointerException {
        File file;
        boolean commandLine = false;


        // If command line is blank, skip to user input.
        // If not blank test whether valid.
        // If command line argument is valid, usedInGameJson will get set. This skips it straight to game.
        // If command line arg is not valid the usedInGameJSON stays null asking for user Input.
        if (args.length != 0) {
            commandLine = true;
        }
        if (commandLine) {
            try {
                //0 here for first arg
                if (testJsonFile(args[0])) {
                    System.out.println(VALID_FILE);
                    usedInGameJson = args[0];
                }
            } catch (Exception e) {
                System.exit(0);
                return;
            }
        }

        // Asking user for new game file, block ends with a string set to Used in Game Json.
        setJsonString(usedInGameJson);

        // Tests whether json file is valid.
        try {
            file = new File(usedInGameJson);
            if (!file.exists() || !testJsonFile(usedInGameJson))  {
                throw new IOException();
            }
            layout = new ObjectMapper().readValue(file, Layout.class);
        } catch (Exception e) {
            System.out.println("Resorting to default Siebel Game.");
            file = new File(DEFAULT_JSON);
            layout = new ObjectMapper().readValue(file, Layout.class);
        }

        // Prints out initial information.
        try {
            startingSetUpStructure(layout);
        } catch (Exception e) {
            System.out.println("Game data is invalid, quitting game.");
        }


        // Starting check that end room != start room.
        if (layout.getStartingRoom().equals(layout.getEndingRoom())) {
            System.out.println("You are already at the final destination!");
            return;
        }

        // Game logic.
        // Sets starting room to current room, works kind of like linked list where
        // each room changes by setting new room to current room.
        while (!quitGame) {
            //positive variable names
            if (!layout.getEndingRoom().equals(current.getName())) {
                quitGame = false;
            }
            String userCommands = scanner.nextLine();
            String in = gamePlayLogic(userCommands);
            if (checkGameOver(current)) {
                System.exit(0);
            }
            System.out.println(in);
        }


    }

    // Helper Functions
    // Access left as package private so they can be tested
    // Functions all set to returning strings for easier testing

    /**
     * READ CAREFULLY *
     *
     * Helper function created to make code testable. Takes in user input from command line as argument as process it.
     * Sets a static boolean to false if user intends to quit game, else it outputs potential directions and descriptions
     * following set specifications.
     *
     * Does step 1 first, step 2, then step 3.
     * @param userCommands The input string from scanner
     * @return 3) A string with the expected output : implemented like this for testability.
     *         2) Sets a boolean : sets static boolean to false if user intends to exit game
     *         1) Prints string to console : Prints string to console so user can see result.
     */
    static String gamePlayLogic(String userCommands) {
        //.toString gets outputted; function designed like this to make conducive for testing.
        StringBuilder output = new StringBuilder("");
        String[] input = userCommands.toLowerCase().split(" ");

        if (input.length > 0) {
            if (input[0].equals("quit") || input[0].equals("exit")) {
                output.append("Thanks for playing.");
                quitGame = true;
                return output.toString();
            } else if (input.length == 2 && input[0].equals("go")) {
                String nextRoom = getRoomNameStringByDirectionString(input[1]);
                if (nextRoom == null) {
                    output.append("I can't ").append(userCommands).append("!");
                    //System.out.println(output);
                    return output.toString();
                } else {
                    if (current == null  || layout.getEndingRoom().equals(current.getName())) {
                        quitGame = false;
                        System.exit(0);
                    }
                    current = getRoomByStringName(nextRoom);
                    output.append(current.getDescription()).append("\n")
                            .append(current.getDirectionAsString()).append("\n")
                            .append(current.getDirectionToLocation());
                    return output.toString();
                }
            } else {
                output.append("I don't understand ' ").append(userCommands).append(" ' ");
                return output.toString();
            }
        }
        return null;
    }

    /**
     * Takes care of initial setup : printing current room and associated info.
     * @param layout takes a layout corresponding to the current game.
     * @return A string with the initial description set up needed
     */
    static String startingSetUpStructure(Layout layout) {
        current = getRoomByStringName(layout.getStartingRoom());
        StringBuilder output = new StringBuilder("");
        output.append(current.getDescription()).append("\n")
                .append("Your journey begins here").append("\n")
                .append(current.getDirectionAsString()).append("\n")
                .append(current.getDirectionToLocation());
        System.out.println(output);
        return output.toString();
    }


    /**
     * Gives the current room based off the string version of the room's name
     * @param currentRoomName string of room's name
     * @return room variable attached to the inputted string
     */
    static Room getRoomByStringName(String currentRoomName) {
        for (int i = 0; i < layout.getRooms().length; i++) {
            if (layout.getRooms()[i].getName().equals(currentRoomName)) {
                return layout.getRooms()[i];
            }
        }
        return null;
    }


    /**
     * Gets a room name as a string based on an input direction as a string.
     * @param inputDirection direction as a string which user wishes to head to.
     * @return room which direction leads to.
     */
    static String getRoomNameStringByDirectionString(String inputDirection) {
        Direction[] directions = current.getDirections();
        for (Direction dir : directions) {
            if (dir.getDirectionName().toLowerCase().equals(inputDirection.toLowerCase())) {
                return dir.getRoom();
            }
        }
        return null;
    }

    /**
     * Takes in current room as Room object and checks if it equal to the ending room object.
     * @param curr : current room
     * @return boolean indicating if Game is over
     */
    static boolean checkGameOver (Room curr) {
        Room current = curr;
        Room endRoom = getRoomByStringName(layout.getEndingRoom());
        if (current.equals(endRoom)) {
            System.out.println(CONGRATS_GAME_OVER);
            return true;
        }
        return false;
    }

    public static Room getCurrentRoom () {
        return getRoomByStringName(layout.getStartingRoom());
    }



    /**
     * Sets Json to a valid file if inputted json is not valid or leaves as is if valid
     * @param inputJson user inputted json if such or null if no json was entered
     * @return Json leading to valid file path
     * @throws IOException if file is invalid
     */
    static String setJsonString(String inputJson) throws IOException {
        if (usedInGameJson == null) {
            System.out.println("Would you like to enter a new Json? (Yes/No)");
            String response = scanner.nextLine();
            if (response.toLowerCase().equals("no")) {
                usedInGameJson = DEFAULT_JSON;
            }
            if (response.toLowerCase().equals("yes")) {
                System.out.println("Please enter a valid file path.");
                String newFile = scanner.nextLine();
                if (testJsonFile(newFile)) {
                    usedInGameJson = newFile;
                }
            }
        }
        return usedInGameJson;
    }


    /**
     * Takes in a string associated to the filepath for a schema and checks to make sure its valid.
     * Returns true if it is and false if not.
     * @param inputFileString string associated to a file which user wishes to input to the game.
     * @return A boolean on whether the JSON file exists,  is of valid schema, and the entries are populated.
     * @throws IOException                    for invalid file.
     * @throws ArrayIndexOutOfBoundsException for blank command line argument.
     */
    static boolean testJsonFile(String inputFileString) throws IOException, ArrayIndexOutOfBoundsException, NullPointerException {
        if (inputFileString == null || inputFileString.length() == 0) {
            return false;
        }
        try {
            File file = new File(inputFileString);
            layout = new ObjectMapper().readValue(file, Layout.class);
        } catch (IOException | ArrayIndexOutOfBoundsException ex) {
            System.out.println("File is invalid");
            return false;
        }

        try {
            String startingRoom = layout.getStartingRoom();
            String endingRoom = layout.getEndingRoom();
            Room[] roomsArray = layout.getRooms();
            if (startingRoom.length() == 0 || endingRoom.length() == 0 || roomsArray.length == 0) {
                return false;
            }
            String name;
            String description;
            Direction[] directions;
            String directionName;
            String room;
            for (int i = 0; i < layout.getRooms().length; i++) {
                name = layout.getRooms()[i].getName();
                description = layout.getRooms()[i].getDescription();
                directions = layout.getRooms()[i].getDirections();
                if (name.length() == 0 || description.length() == 0 || directions.length == 0) {
                    return false;
                }
                for (int j = 0; j < directions.length; j++) {
                    directionName = directions[j].getDirectionName();
                    room = directions[j].getRoom();
                    if (directionName.length() == 0 || room.length() == 0) {
                        return false;
                    }
                }
            }
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
}
