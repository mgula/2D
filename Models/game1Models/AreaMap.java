package game1Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import enums.MapID;
import enums.RoomID;

public class AreaMap implements Serializable {
	private static final long serialVersionUID = 1L;
	private MapID mapID;
	private RoomID[] roomIDs;
	
	/*Better way then 3 maps?*/
	private HashMap<RoomID, int[]> roomData;
	private HashMap<RoomID, ArrayList<Game1Model>> roomEnvs;
	private HashMap<RoomID, ArrayList<Exit>> roomLinks;
	
	private int numRooms;
	
	public AreaMap(MapID ID, RoomID[] rooms) {
		this.mapID = ID;
		this.roomIDs = rooms;
		this.numRooms = rooms.length;
		
		this.roomData = new HashMap<RoomID, int[]>();
		this.roomEnvs = new HashMap<RoomID, ArrayList<Game1Model>>();
		this.roomLinks = new HashMap<RoomID, ArrayList<Exit>>();
	}
	
	public int[] accessRoomData(RoomID key) {
		return this.roomData.get(key);
	}
	
	public ArrayList<Game1Model> accessRoomEnvs(RoomID key) {
		return this.roomEnvs.get(key);
	}
	
	public ArrayList<Exit> accessRoomLinks(RoomID key) {
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
	public void addRoomLinks(RoomID roomID, ArrayList<Exit> links) {
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
		String info = "Map ID: " + this.mapID + "\n";
		
		for (RoomID r : this.roomIDs) {
			info += "\nRoom ID: " + r +
					"\n\tX Location: " + this.accessRoomData(r)[0] +
					"\n\tY Location: " + this.accessRoomData(r)[1] +
					"\n\tHeight: " + this.accessRoomData(r)[2] +
					"\n\tWidth: " + this.accessRoomData(r)[3];
			
			info += "\n\tEnvironment Info: ";
			
			for (Game1Model m : this.accessRoomEnvs(r)) {
				info += m.toStringForAreaMap() + "\n";
			}
			
			info += "\n\tExit Info: ";
			
			for (Exit e : this.accessRoomLinks(r)) {
				info += e.toStringForAreaMap() + "\n";
			}
		}
		
		return info;
	}
}
