package game1Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AreaMap {
	private int width;
	private int height;
	
	private String[] roomIDs;
	private String startRoomID;
	
	/*Better way then 3 maps?*/
	private Map<String, int[]> roomData;
	private Map<String, ArrayList<Game1Model>> roomEnvs;
	private Map<String, String[]> roomLinks;
	
	private int numRooms;
	
	public AreaMap(int width, int height, String[] rooms, String startingRoom, int numRooms) {
		this.width = width;
		this.height = height;
		this.roomIDs = rooms;
		this.startRoomID = startingRoom;
		this.numRooms = numRooms;
		
		this.roomData = new HashMap<String, int[]>();
		this.roomEnvs = new HashMap<String, ArrayList<Game1Model>>();
		this.roomLinks = new HashMap<String, String[]>();
	}
	
	public int[] accessRoomData(String key) {
		return this.roomData.get(key);
	}
	
	public ArrayList<Game1Model> accessRoomEnvs(String key) {
		return this.roomEnvs.get(key);
	}
	
	public String[] accessRoomLinks(String key) {
		return this.roomLinks.get(key);
	}
	
	/* int[] roomDims data structure:
	 * [x loc, y loc, height, width]
	 */
	public void addRoomData(String roomID, int[] roomDims) {
		this.roomData.put(roomID, roomDims);
	}
	
	public void addRoomEnv(String roomID, ArrayList<Game1Model> env) {
		this.roomEnvs.put(roomID, env);
	}
	
	/* String[] links data structure:
	 * [west room, east room, north room, south room]
	 */
	public void addRoomLinks(String roomID, String[] links) {
		this.roomLinks.put(roomID, links);
	}
	
	/*public boolean mapCheck() {
		int totalArea = 0;
		
		for (Room r : this.rooms) {
			totalArea += (r.getWidth() * r.getHeight());
		}
		
		if (totalArea == (this.width * this.height)) {
			return true;
		} else {
			return false;
		}
	}*/
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public String[] getRoomIDs() {
		return this.roomIDs;
	}
	
	public int getNumRooms() {
		return this.numRooms;
	}
	
	public String getStartRoomID() {
		return this.startRoomID;
	}
	
	@Override
	public String toString() {
		String info = "";
		
		for (String s : this.roomIDs) {
			info += "Room ID: " + s +
					"\nRoom X Loc: " + this.accessRoomData(s)[0] +
					"\nRoom Y Loc: " + this.accessRoomData(s)[1] + 
					"\nRoom Height: " + this.accessRoomData(s)[2] +
					"\nRoom Width: " + this.accessRoomData(s)[3] + 
					"\nEnvironment Contents:\n";
		
			for (Game1Model m : this.accessRoomEnvs(s)) {
				info += "\n\t" + m.toString() + "\n";
			}
			
			info += "\nWest Room ID: " + this.accessRoomLinks(s)[0] +
					"\nEast Room ID: " + this.accessRoomLinks(s)[1] +
					"\nNorth Room ID: " + this.accessRoomLinks(s)[2] +
					"\nSouth Room ID: " + this.accessRoomLinks(s)[3] +
					"\n\n";
		}
		
		return info;
	}
}
