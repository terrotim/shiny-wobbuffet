package fireemblemfates.app;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

public class UnitProfile extends Image{
	Image image;
	int isDragging;
	int originalX, originalY;
	int currentX, currentY;
	Rectangle profileShape;
	
	public UnitProfile(Image img, int x, int y, Rectangle profShape){
		this.setImage(img);
		this.setIsDragging(0);
		this.setOriginalX(x);
		this.setCurrentX(x);
		this.setOriginalY(y);
		this.setCurrentY(y);
		this.setProfileShape(profShape);
	}

	public Rectangle getProfileShape() {
		return profileShape;
	}

	public void setProfileShape(Rectangle profileShape) {
		this.profileShape = profileShape;
	}

	public int getOriginalX() {
		return originalX;
	}

	public void setOriginalX(int originalX) {
		this.originalX = originalX;
	}

	public int getOriginalY() {
		return originalY;
	}

	public void setOriginalY(int originalY) {
		this.originalY = originalY;
	}

	public int getCurrentX() {
		return currentX;
	}

	public void setCurrentX(int currentX) {
		this.currentX = currentX;
	}

	public int getCurrentY() {
		return currentY;
	}

	public void setCurrentY(int currentY) {
		this.currentY = currentY;
	}

	public int getIsDragging() {
		return isDragging;
	}

	public void setIsDragging(int isDragging) {
		this.isDragging = isDragging;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}
