package simpleslickgame;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;

public class Bot{
	
	private Circle hitBox;
	private Polygon leftSight;
	private Polygon rightSight;
	private int leftSightSpotted, rightSightSpotted;
	private float angle;
	private int bulletsLeft;
	private String team;
	private int bulletCoolTime;
	private int rightscope, leftscope;

	public Bot(float centerPointX, float centerPointY, float radius, float a, String t) {
		hitBox = new Circle(centerPointX, centerPointY, radius);

		setAngle(a);
		setBulletsLeft(3);
		setTeam(t);
		setRightScope(0);
		setLeftScope(0);
		
		leftSight = new Polygon();
		leftSight.addPoint((float)(getHitBox().getCenterX()-(1.2*getHitBox().getRadius()*Math.cos(Math.toRadians(getAngle())))),
				(float)(getHitBox().getCenterY()-(1.2*getHitBox().getRadius()*Math.sin(Math.toRadians(getAngle())))));
		leftSight.addPoint((float)(getHitBox().getCenterX()-(40*getHitBox().getRadius()*Math.cos(Math.toRadians(getAngle())))),
				(float)(getHitBox().getCenterY()-(40*getHitBox().getRadius()*Math.sin(Math.toRadians(getAngle())))));
		leftSight.addPoint((float)(getHitBox().getCenterX()-(40*getHitBox().getRadius()*Math.cos(-(Math.PI/16)+Math.toRadians(getAngle())))),
				(float)(getHitBox().getCenterY()-(40*getHitBox().getRadius()*Math.sin(-(Math.PI/16)+Math.toRadians(getAngle())))));
		rightSight = new Polygon();
		rightSight.addPoint((float)(getHitBox().getCenterX()-(1.2*getHitBox().getRadius()*Math.cos(Math.toRadians(getAngle())))),
				(float)(getHitBox().getCenterY()-(1.2*getHitBox().getRadius()*Math.sin(Math.toRadians(getAngle())))));
		rightSight.addPoint((float)(getHitBox().getCenterX()-(40*getHitBox().getRadius()*Math.cos(Math.toRadians(getAngle())))),
				(float)(getHitBox().getCenterY()-(40*getHitBox().getRadius()*Math.sin(Math.toRadians(getAngle())))));
		rightSight.addPoint((float)(getHitBox().getCenterX()-(40*getHitBox().getRadius()*Math.cos((Math.PI/16)+Math.toRadians(getAngle())))),
				(float)(getHitBox().getCenterY()-(40*getHitBox().getRadius()*Math.sin((Math.PI/16)+Math.toRadians(getAngle())))));
		
		setLeftSightSpotted(0);
		setRightSightSpotted(0);
	
	}

	public Polygon getLeftSight() {
		return leftSight;
	}

	public void setLeftSight(Polygon leftSight) {
		this.leftSight = leftSight;
	}

	public Polygon getRightSight() {
		return rightSight;
	}

	public void setRightSight(Polygon rightSight) {
		this.rightSight = rightSight;
	}

	public Circle getHitBox() {
		return hitBox;
	}

	public void setHitBox(Circle hitBox) {
		this.hitBox = hitBox;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public int getBulletsLeft() {
		return bulletsLeft;
	}

	public void setBulletsLeft(int bulletsLeft) {
		this.bulletsLeft = bulletsLeft;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public void cooldown() {
		setBulletCoolTime(75);
	}
	
	public void reduceCooldown(){
		if(getBulletCoolTime()>0){
		setBulletCoolTime(getBulletCoolTime()-1);
		}
	}

	public int getBulletCoolTime() {
		return bulletCoolTime;
	}

	public void setBulletCoolTime(int bulletCoolTime) {
		this.bulletCoolTime = bulletCoolTime;
	}

	public void setSights() {
		Polygon ls = new Polygon();
		Polygon rs = new Polygon();
		ls.addPoint((float)(getHitBox().getCenterX()-(1.2*getHitBox().getRadius()*-Math.cos(Math.toRadians(getAngle())))),
				(float)(getHitBox().getCenterY()-(1.2*getHitBox().getRadius()*Math.sin(Math.toRadians(getAngle())))));
		ls.addPoint((float)(getHitBox().getCenterX()-((40+getLeftScope()*2)*getHitBox().getRadius()*-Math.cos(Math.toRadians(getAngle())))),
				(float)(getHitBox().getCenterY()-((40+getLeftScope()*2)*getHitBox().getRadius()*Math.sin(Math.toRadians(getAngle())))));
		ls.addPoint((float)(getHitBox().getCenterX()-((40+getLeftScope()*2)*getHitBox().getRadius()*-Math.cos(-Math.PI/(16+getLeftScope())+Math.toRadians(getAngle())))),
				(float)(getHitBox().getCenterY()-((40+getLeftScope()*2)*getHitBox().getRadius()*Math.sin(-Math.PI/(16+getLeftScope())+Math.toRadians(getAngle())))));
		rs = new Polygon();
		rs.addPoint((float)(getHitBox().getCenterX()-(1.2*getHitBox().getRadius()*-Math.cos(Math.toRadians(getAngle())))),
				(float)(getHitBox().getCenterY()-(1.2*getHitBox().getRadius()*Math.sin(Math.toRadians(getAngle())))));
		rs.addPoint((float)(getHitBox().getCenterX()-((40+getRightScope()*2)*getHitBox().getRadius()*-Math.cos(Math.toRadians(getAngle())))),
				(float)(getHitBox().getCenterY()-((40+getRightScope()*2)*getHitBox().getRadius()*Math.sin(Math.toRadians(getAngle())))));
		rs.addPoint((float)(getHitBox().getCenterX()-((40+getRightScope()*2)*getHitBox().getRadius()*-Math.cos(Math.PI/(16+getRightScope())+Math.toRadians(getAngle())))),
				(float)(getHitBox().getCenterY()-((40+getRightScope()*2)*getHitBox().getRadius()*Math.sin(Math.PI/(16+getRightScope())+Math.toRadians(getAngle())))));
		setLeftSight(ls);
		setRightSight(rs);
	}

	public int getLeftSightSpotted() {
		return leftSightSpotted;
	}

	public void setLeftSightSpotted(int leftSightSpotted) {
		this.leftSightSpotted = leftSightSpotted;
	}

	public int getRightSightSpotted() {
		return rightSightSpotted;
	}

	public void setRightSightSpotted(int rightSightSpotted) {
		this.rightSightSpotted = rightSightSpotted;
	}

	public int getRightScope() {
		return rightscope;
	}

	public void setRightScope(int scope) {
		this.rightscope = scope;
	}
	
	public int getLeftScope() {
		return leftscope;
	}

	public void setLeftScope(int scope) {
		this.leftscope = scope;
	}

	public void zoomInRight() {
		int newScope = getRightScope()+1;
		if(newScope<=16){
			setRightScope(newScope);
		}
	}
	public void zoomInLeft() {
		int newScope = getLeftScope()+1;
		if(newScope<=16){
			setLeftScope(newScope);
		}
	}

	public void zoomOutRight() {
		int newScope = getRightScope()-1;
		if(newScope>=-13){
			setRightScope(newScope);
		}
	}
	
	public void zoomOutLeft() {
		int newScope = getLeftScope()-1;
		if(newScope>=-13){
			setLeftScope(newScope);
		}
	}
}
