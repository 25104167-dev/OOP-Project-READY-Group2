package game.world;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Owns every {@link Room} in the game and tracks which one is currently
 * active. Rooms are registered once at startup by {@link RoomFactory}.
 */
public class World {

    private final Map<String, Room> rooms = new LinkedHashMap<>();
    private String currentRoomId;

    public void addRoom(Room room) {
        rooms.put(room.getId(), room);
        if (currentRoomId == null) {
            currentRoomId = room.getId();
        }
    }

    public Room getCurrentRoom() {
        return rooms.get(currentRoomId);
    }

    public Room getRoom(String id) {
        return rooms.get(id);
    }

    public void goToRoom(String id) {
        if (rooms.containsKey(id)) {
            currentRoomId = id;
        }
    }

    public int getRoomCount() {
        return rooms.size();
    }
}
