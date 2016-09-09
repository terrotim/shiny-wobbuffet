package fireemblemfates.app;

import org.newdawn.slick.geom.Rectangle;

public class PairupBox {
	int x,y,width,height,originalX,originalY;
	String constPar;
	String varPar;
	int constClassClicked, varClassClicked;
	String constClass, varClass;
	int[] constParSkillsChoices = new int[]{0,0,0,0,0};
	String[] constParSkillPool = new String[50];
	int[] varParSkillsChoices = new int[]{0,0,0,0,0};
	String[] varParSkillPool = new String[50];
	
	public PairupBox(int x, int y, int w, int h) {
		setX(x);
		setY(y);
		setOriginalX(x);
		setOriginalY(y);
		setWidth(w);
		setHeight(h);
		setConstPar("");
		setVarPar("");
		setConstClassClicked(-1);
		setVarClassClicked(-1);
		setVarClass("");
		setConstClass("");
	}
	
	public Rectangle getRectangle(int i){
		setX(getOriginalX()-300*i);
		return new Rectangle(getX(),getY(),getWidth(),getHeight());
	}
	
	public Rectangle getRectangle(){
		return new Rectangle(getX(),getY(),getWidth(),getHeight());
	}

	public int[] getConstParSkillsChoices() {
		return constParSkillsChoices;
	}

	public void setConstParSkillsChoices(int[] constParSkillsChoices) {
		this.constParSkillsChoices = constParSkillsChoices;
	}

	public int[] getVarParSkillsChoices() {
		return varParSkillsChoices;
	}

	public void setVarParSkillsChoices(int[] varParSkillsChoices) {
		this.varParSkillsChoices = varParSkillsChoices;
	}

	public String[] getConstParSkillPool() {
		return constParSkillPool;
	}

	public void setConstParSkillPool(String[] constParSkillPool) {
		this.constParSkillPool = constParSkillPool;
	}

	public String[] getVarParSkillPool() {
		return varParSkillPool;
	}

	public void setVarParSkillPool(String[] varParSkillPool) {
		this.varParSkillPool = varParSkillPool;
	}

	public int getConstClassClicked() {
		return constClassClicked;
	}

	public void setConstClassClicked(int constClassClicked) {
		this.constClassClicked = constClassClicked;
	}

	public int getVarClassClicked() {
		return varClassClicked;
	}

	public void setVarClassClicked(int varClassClicked) {
		this.varClassClicked = varClassClicked;
	}

	public String getConstClass() {
		return constClass;
	}

	public void setConstClass(String constClass) {
		this.constClass = constClass;
	}

	public String getVarClass() {
		return varClass;
	}

	public void setVarClass(String varClass) {
		this.varClass = varClass;
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

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getConstPar() {
		return constPar;
	}

	public void setConstPar(String constPar) {
		this.constPar = constPar;
	}

	public String getVarPar() {
		return varPar;
	}

	public void setVarPar(String varPar) {
		this.varPar = varPar;
	}

}
