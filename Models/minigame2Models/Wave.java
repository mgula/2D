package minigame2Models;

/**
 * Wave is the instantiated object by all waves. It has a certain speed and damage depending upon the boat type.
 * The wave is how damage is done to the beach during the game.
 * @author npompetti
 *
 */
public class Wave implements Minigame2Model{
	private int xLoc;
	private int yLoc;
	private boolean drawable = false;
	private boolean released = false;
	private int waveSpeed;
	private int waveDamage;
	private int residualWaveDamage;
	private boolean weakened;
	private int waveBreakYCoord;
	private int xSpan;
	
	private boolean waveHit = false;
	
	/**
	 * Constructor for the wave.
	 * @param x 	The x location where the wave will be generated.
	 * @param y		The y location where the wave will be generated.
	 * @param waveDamage 	How much damage the wave has.
	 * @param waveBreakYCoord 	This is a constant and determined by the length of the beach.
	 * @param waveSpeed 		The speed of how fast the wave moves across the screen.
	 * @param xSpan				The span of the wave across x coordinates for collision detection purposes.
	 */
	private Wave(int x, int y, int waveDamage, int waveBreakYCoord, int waveSpeed, int xSpan){
		this.xLoc = x;
		this.yLoc = y;
		this.waveDamage = waveDamage;
		this.waveBreakYCoord = waveBreakYCoord;
		this.waveSpeed = waveSpeed;
		this.xSpan = xSpan;
	}
	
	/**
	 * A static generator of waves that is used within the game and passes the parameters to the wave constructor.
	 * 
	 * @param xLoc The x location where the wave will be generated.
	 * @param yLoc The y location where the wave will be generated.
	 * @param waveDamage 	The amount damage the wave will apply.
	 * @param waveBreakYCoord 	A constant which is determined by the length of the beach and is where the waves disappear.
	 * @param waveSpeed 		How fast the wave will move.
	 * @param xSpan				How large the wave is for collision purposes.
	 * 
	 * @return 		Returns a wave with the specified parameters.
	 */
	public static Wave generateWave(int xLoc, int yLoc, int waveDamage, int waveBreakYCoord, int waveSpeed, int xSpan){
		return new Wave(xLoc, yLoc, waveDamage, waveBreakYCoord, waveSpeed, xSpan);
	}

	@Override
	public boolean getDrawable() {
		return this.drawable;
	}

	@Override
	public void setDrawable(boolean b) {
		this.drawable = b;
	}
	
	
	/**
	 * Increases the waves respective y coordinate and moves it across the screen. WavebreakYcoord is where the wave will crash and inflict damage.
	 */
	@Override
	public void move() {
		this.yLoc += waveSpeed;
		if(this.yLoc > this.waveBreakYCoord){
			this.setDrawable(false);
		}
	}

	@Override
	public int getXloc() {
		return this.xLoc;
	}

	@Override
	public int getYloc() {
		return this.yLoc;
	}
	
	
	public int getWaveDamage(){
		return this.waveDamage;
	}
	
	public void setWaveDamage(int damage){
		this.waveDamage = damage;
	}
	
	public int getResidualDamage(){
		return this.residualWaveDamage;
	}
	
	public void setResidualWaveDamage(int residual){
		if(residual < 0){
			this.residualWaveDamage = 0;
		}
		else if(residual >= 0){
		this.residualWaveDamage = residual;
		}
	}
	
	public void setWaveHit(boolean b){
		this.waveHit = b;
	}
	
	public boolean getWaveHit(){
		return this.waveHit;
	}
	
	
	public int getWaveReleaseXCoord(){
		return this.xLoc;
	}
	
	public boolean getReleased(){
		return this.released;
	}
	
	public void setReleased(boolean b){
		this.released = b;
	}
	
	public int getXSpan(){
		return this.xSpan;
	}
	
	public int getXPlusSpan(){
		return this.xLoc+this.xSpan;
	}
	
	public boolean getWeakend(){
		return this.weakened;
	}
	
	public void setWeakened(boolean bool){
		this.weakened = bool;
	}

	@Override
	public boolean calcRange(int xRange, int yRange, int xClick, int yClick) {
		// TODO Auto-generated method stub
		return false;
	}

}
