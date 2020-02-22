package student.adventure;

/**
 * Directions class; Lowest grouping of JSON info.
 * Contains the fields reachable from the room arrays.
 * Can access the names of directions you can head from the given room and which rooms the directions head to.
 */

public class Direction {
    //Fields
    private String directionName;
    private String room;

    //Getters
    public String getDirectionName() {
        return directionName;
    }

    public String getRoom() {
        return room;
    }

    //Setters
    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
