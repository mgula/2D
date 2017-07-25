package game1Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import enums.MapID;
import enums.RoomID;

public class AreaMap {
	private MapID mapID;
	private RoomID[] roomIDs;
	
	/*Better way then 3 maps?*/
	private HashMap<RoomID, int[]> roomData;
	private HashMap<RoomID, ArrayList<Game1Model>> roomEnvs;
	private HashMap<RoomID, RoomID[]> roomLinks;
	
	private int numRooms;
	
	public AreaMap(MapID ID, RoomID[] rooms) {
		this.mapID = ID;
		this.roomIDs = rooms;
		this.numRooms = rooms.length;
		
		this.roomData = new HashMap<RoomID, int[]>();
		this.roomEnvs = new HashMap<RoomID, ArrayList<Game1Model>>();
		this.roomLinks = new HashMap<RoomID, RoomID[]>();
	}
	
	public int[] accessRoomData(RoomID key) {
		return this.roomData.get(key);
	}
	
	public ArrayList<Game1Model> accessRoomEnvs(RoomID key) {
		return this.roomEnvs.get(key);
	}
	
	public RoomID[] accessRoomLinks(RoomID key) {
		return this.roomLinks.get(key);
	}
	
	/* int[] roomDims data structure:
	 * [x loc, y loc, height, width]
	 */
	public void addRoomData(RoomID roomID, int[] roomDims) {
		this.roomData.put(roomID, roomDims);
	}
	
	public void addRoomEnv(RoomID roomID, ArrayList<Game1Model> env) {
		this.roomEnvs.put(roomID, env);
	}
	
	/* String[] links data structure:
	 * [west room, east room, north room, south room]
	 */
	public void addRoomLinks(RoomID roomID, RoomID[] links) {
		this.roomLinks.put(roomID, links);
	}
	
	public MapID getMapID() {
		return this.mapID;
	}
	
	public RoomID[] getRoomIDs() {
		return this.roomIDs;
	}
	
	public int getNumRooms() {
		return this.numRooms;
	}
	
	@Override
	public String toString() {
		String info = "";
		
		for (RoomID r : this.roomIDs) {
			info += "Room ID: " + r +
					"\nRoom X Loc: " + this.accessRoomData(r)[0] +
					"\nRoom Y Loc: " + this.accessRoomData(r)[1] + 
					"\nRoom Height: " + this.accessRoomData(r)[2] +
					"\nRoom Width: " + this.accessRoomData(r)[3] + 
					"\nEnvironment Contents:\n";
		
			for (Game1Model m : this.accessRoomEnvs(r)) {
				info += "\n\t" + m.toString() + "\n";
			}
			
			info += "\nWest Room ID: " + this.accessRoomLinks(r)[0] +
					"\nEast Room ID: " + this.accessRoomLinks(r)[1] +
					"\nNorth Room ID: " + this.accessRoomLinks(r)[2] +
					"\nSouth Room ID: " + this.accessRoomLinks(r)[3] +
					"\n\n";
		}
		
		return info;
	}
}
