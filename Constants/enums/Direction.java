package enums;

public enum Direction {
	EAST (0),
	NORTH (1),
	NORTHEAST (2),
	NORTHWEST (3),
	SOUTH (4),
	SOUTHEAST (5),
	SOUTHWEST (6),
	WEST (7),
	IDLE(8);
	
	public final int index;
	
	private Direction(int i) {
		this.index = i;
	}
}
