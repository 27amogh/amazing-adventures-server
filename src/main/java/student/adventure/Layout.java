package student.adventure;


/**
 * The layout class; Holds the highest grouping of JSON info.
 * Contains strings with the starting room and ending room
 * and the rooms array through which the rest of game info is accessible.
 */

public class Layout {
    //Fields
    private String startingRoom;
    private String endingRoom;
    private Room[] rooms;

    // Getters
    public String getStartingRoom() {
        return startingRoom;
    }

    public String getEndingRoom() {
        return endingRoom;
    }

    public Room[] getRooms() {
        return rooms;
    }

    // Setters
    public void setStartingRoom(String startingRoom) {
        this.startingRoom = startingRoom;
    }

    public void setEndingRoom(String endingRoom) {
        this.endingRoom = endingRoom;
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
    }








}

