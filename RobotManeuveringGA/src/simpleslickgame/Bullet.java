package simpleslickgame;

import org.newdawn.slick.geom.Circle;

public class Bullet {
	private Circle hitBox;
	private float angle;
	private String firer;
	private int travelTime;
	
	public Bullet(float centerPointX, float centerPointY, float a, String f){
		hitBox = new Circle(centerPointX, centerPointY, 2);
		angle = a;
		firer = f;
		travelTime = 350;
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

	public String getFirer() {
		return firer;
	}

	public void setFirer(String firer) {
		this.firer = firer;
	}

	public int getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(int travelTime) {
		this.travelTime = travelTime;
	}
	
}
