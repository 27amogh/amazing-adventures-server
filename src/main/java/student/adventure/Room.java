package student.adventure;


/**
 * Rooms class; Middle grouping of JSON info.
 * Contains separate strings with current room name and room description.
 * Also contains array with all possible directions you can head from current room
 * and array with all items in the room.
 */


public class Room {
    // Fields
    private String name;
    private String description;
    private Direction[] directions;
    //Hashmap instead of array - how to map
    private String[] items;


    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Direction[] getDirections() {
        return directions;
    }

    public String[] getItems() {
        return items;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDirections(Direction[] directions) {
        this.directions = directions;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    // Helper functions

    // Made helper functions to retrieve info by key? (not sure right terminology here) because of bug noticed with Json
    //      For some room the direction to head from room A to B does not match opposite direction to go from B to A

    /**
     * Helper function to get the potential movable directions from a room based off the current room.
     * @return String following proper format of where to go.
     */
    // Manual appending is bad use StringBuilder.
    public String getDirectionAsString() {
        StringBuilder outputDirections = new StringBuilder("From here, you can go: ");
        // Careful of indices in the method.
        // Go to directions.length - 1 as max and then include the last direction manually so the extra comma does
        // not appear after last direction.
        int numOfDirections = directions.length;
        for (int i = 0; i < directions.length - 1; i++) {
            outputDirections.append(directions[i].getDirectionName()).append(", ");
        }
        // If more than one direction add or after the above string before the final direction is added.
        if (numOfDirections > 1) {
            outputDirections.append("or ");
        }
        // Adds the final direction.
        // >0 ! >1 since first loop subtracts 1 so >1 here would skip printing entirely.
        if (numOfDirections > 0) {
            outputDirections.append(directions[directions.length - 1].getDirectionName());
            //outputDirections.append(directions[directions.length - 1].getDirectionName() + " to " + directions[directions.length - 1].getRoom());
        }
        return outputDirections.toString();
    }

    /**
     * Helper function created not required by spec of game. It lists which room each direction heads to.
     * @return A line by line list of which room each of the potential directions head to.
     */
    public String getDirectionToLocation() {
        StringBuilder outputDirections = new StringBuilder("");
        for (int i = 0; i < directions.length - 1; i++) {
            outputDirections.append(directions[i].getDirectionName()).append(" heads to ").append(directions[i].getRoom()).append("\n");
        }
        if (directions.length > 1) {
            outputDirections.append("or ");
        }
        if (directions.length > 0) {
            outputDirections.append(directions[directions.length - 1].getDirectionName() + " heads to " + directions[directions.length - 1].getRoom());
        }
        return outputDirections.toString();
    }



}
