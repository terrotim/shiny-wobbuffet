package fireemblemfates.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.geom.Rectangle;

import fireemblemfates.objects.AvatarUnit;
import fireemblemfates.objects.ChildCharacter;
import fireemblemfates.objects.FirstGenCharacter;
import fireemblemfates.objects.UnitClass;

public class PairingPlanner extends BasicGame{
	private static final int WIDTH = 800;
	private static final int HEIGHT = 800;
	HashMap<String, UnitClass> classList;
	HashMap<String, FirstGenCharacter> firstGenList;
	HashMap<String, ChildCharacter> childList;
	HashMap<String, UnitProfile> unitProfiles;
	HashMap<String, String> parentToChild;
	int startingPoint=0;
	int statView = 0;
	int NUM_PAIRUPS = 30;
	PairupBox[] pairupBoxes= new PairupBox[NUM_PAIRUPS];
	String[][] pairups;
	HashMap<Integer, String> boonMap;
	HashMap<Integer, String> baneMap;
	HashMap<Integer, String> talentMap;
	HashMap<String, String> talentToClass;
	int avatarBoon;
	int avatarBane;
	String avatarTalent = new String();
	String unitCompatible = new String();
	FirstGenCharacter tempUnit;
	
	public PairingPlanner(String gamename){
		super(gamename);
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		unitProfiles = new HashMap<String,UnitProfile>();
		parentToChild = setParentWithChild();
		classList = initializeClasses();
		firstGenList = initializeCharacters();
		unitCompatible = "";
		avatarBoon = 4;
		avatarBane = 6;
		avatarTalent = "Ninja";
		((AvatarUnit) firstGenList.get("AvatarM")).setBoon(avatarBoon);
		((AvatarUnit) firstGenList.get("AvatarM")).setBane(avatarBane);
		((AvatarUnit) firstGenList.get("AvatarM")).setTalent(avatarTalent);
		((AvatarUnit) firstGenList.get("AvatarF")).setBoon(avatarBoon);
		((AvatarUnit) firstGenList.get("AvatarF")).setBane(avatarBane);
		((AvatarUnit) firstGenList.get("AvatarF")).setTalent(avatarTalent);
		childList = initializeChildren();
		boonMap = new HashMap<Integer,String>();
		baneMap = new HashMap<Integer, String>();
		talentMap = new HashMap<Integer, String>();
		talentToClass = new HashMap<String, String>();
		generateAvatarOptions();
		for (FirstGenCharacter unit : firstGenList.values()){
			unit.setSkillSet(updateSkills(unit.getClassSet()));
		}
		pairups = new String[][] {{"",""},{"",""},{"",""},{"",""},{"",""},{"",""},{"",""},{"",""},{"",""},{"",""},{"",""},
				{"",""},{"",""},{"",""},{"",""},{"",""},{"",""},{"",""},{"",""},{"",""},{"",""},{"",""},{"",""},{"",""},
				{"",""},{"",""},{"",""},{"",""},{"",""},{"",""},{"",""},{"",""}};
		
		for(int i = 0; i<NUM_PAIRUPS;i++){
			pairupBoxes[i] = new PairupBox(30+300*i,30,290,70);
		}
		
		int i = 0;
		int j = 0;
		int tempX, tempY;
		Image tempImage;
		for(String unit : firstGenList.keySet()){
			tempImage = new Image("res/" + unit + ".jpg");
			tempX = 51*i;
			tempY = 525+51*j;
			unitProfiles.put(unit, new UnitProfile(tempImage,tempX,tempY,new Rectangle(tempX,tempY,tempImage.getWidth(),tempImage.getHeight())));
			i++;
			if(i>14){
				i=0;
				j++;
			}
		}
		for(String unit : childList.keySet()){
			tempImage = new Image("res/" + unit + ".jpg");
			tempX = 51*i;
			tempY = 525+51*j;
			unitProfiles.put(unit, new UnitProfile(tempImage,tempX,tempY,new Rectangle(tempX,tempY,tempImage.getWidth(),tempImage.getHeight())));
			i++;
			if(i>14){
				i=0;
				j++;
			}
		}
	}
	
	private void generateAvatarOptions() {
		boonMap.put(0, "Robust");
		boonMap.put(1, "Strong");
		boonMap.put(2, "Clever");
		boonMap.put(3, "Deft");
		boonMap.put(4, "Quick");
		boonMap.put(5, "Lucky");
		boonMap.put(6, "Sturdy");
		boonMap.put(7, "Calm");
		baneMap.put(0, "Sickly");
		baneMap.put(1, "Weak");
		baneMap.put(2, "Dull");
		baneMap.put(3, "Clumsy");
		baneMap.put(4, "Slow");
		baneMap.put(5, "Unlucky");
		baneMap.put(6, "Fragile");
		baneMap.put(7, "Excitable");
		talentMap.put(0, "Cavalier");
		talentMap.put(1, "Knight");
		talentMap.put(2, "Fighter");
		talentMap.put(3, "Mercenary");
		talentMap.put(4, "Outlaw");
		talentMap.put(5, "Samurai");
		talentMap.put(6, "Oni Savage");
		talentMap.put(7, "Lancer");
		talentMap.put(8, "Diviner");
		talentMap.put(9, "Monk");
		talentMap.put(10, "Priestess");
		talentMap.put(11, "Sky Knight");
		talentMap.put(12, "Archer");
		talentMap.put(13, "Dragon");
		talentMap.put(14, "Ninja");
		talentMap.put(15, "Apothecary");
		talentMap.put(16, "Mage");
		talentMap.put(17, "Troubadour");
		talentToClass.put("Cavalier", "Cavalier");
		talentToClass.put("Knight", "Knight");
		talentToClass.put("Fighter", "Fighter");
		talentToClass.put("Mercenary", "Mercenary");
		talentToClass.put("Outlaw", "Outlaw");
		talentToClass.put("Samurai", "Samurai");
		talentToClass.put("Oni Savage", "Oni Savage");
		talentToClass.put("Lancer", "Spear Fighter");
		talentToClass.put("Diviner", "Diviner");
		talentToClass.put("Monk", "Monk");
		talentToClass.put("Priestess", "Shrine Maiden");
		talentToClass.put("Sky Knight", "Sky Knight");
		talentToClass.put("Archer", "Archer");
		talentToClass.put("Dragon", "Wyvern Rider");
		talentToClass.put("Ninja", "Ninja");
		talentToClass.put("Apothecary", "Apothecary");
		talentToClass.put("Mage", "Dark Mage");
		talentToClass.put("Troubadour", "Troubadour");
	}

	private HashMap<String, String> setParentWithChild() {
		HashMap<String,String> ptc = new HashMap<String,String>();
		ptc.put("AvatarM", "KanaF");
		ptc.put("AvatarF", "KanaM");
		ptc.put("Azura", "Shigure");
		ptc.put("Jakob", "Dwyer");
		ptc.put("Silas", "Sophie");
		ptc.put("Kaze", "Midori");
		ptc.put("Ryoma", "Shiro");
		ptc.put("Takumi", "Kiragi");
		ptc.put("Saizo", "Asugi");
		ptc.put("Kaden", "Selkie");
		ptc.put("Hinata", "Hisame");
		ptc.put("Azama", "Mitama");
		ptc.put("Subaki", "Caeldori");
		ptc.put("Hayato", "Rhajat");
		ptc.put("Xander", "Siegbert");
		ptc.put("Leo", "Forrest");
		ptc.put("Benny", "Ignatius");
		ptc.put("Keaton", "Velouria");
		ptc.put("Arthur","Percy");
		ptc.put("Odin", "Ophelia");
		ptc.put("Laslow", "Soleil");
		ptc.put("Niles", "Nina");
		return ptc;
	}

	@Override
	public void update(GameContainer gc, int i) throws SlickException {
		if(Display.wasResized()){
			((AppGameContainer) gc).setDisplayMode(Display.getWidth(), Display.getHeight(), false);
//			gc.reinit();
			
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		//Left right buttons
		g.draw(new Rectangle(5,30,15,70));
		g.draw(new Rectangle(780,30,15,70));
		g.drawString("<",8,55);
		g.drawString(">",783,55);
		g.drawString("HP", 5, 110);
		g.drawString("STR", 5, 125);
		g.drawString("MAG", 5,140);
		g.drawString("SKL", 5, 155);
		g.drawString("SPD", 5, 170);
		g.drawString("LCK", 5, 185);
		g.drawString("DEF", 5, 200);
		g.drawString("RES", 5, 215);
		g.drawString("MOV", 5, 230);
		g.drawString("HP", 305, 110);
		g.drawString("STR", 305, 125);
		g.drawString("MAG", 305,140);
		g.drawString("SKL", 305, 155);
		g.drawString("SPD", 305, 170);
		g.drawString("LCK", 305, 185);
		g.drawString("DEF", 305, 200);
		g.drawString("RES", 305, 215);
		g.drawString("MOV", 305, 230);
		g.drawString("HP", 605, 110);
		g.drawString("STR", 605, 125);
		g.drawString("MAG", 605,140);
		g.drawString("SKL", 605, 155);
		g.drawString("SPD", 605, 170);
		g.drawString("LCK", 605, 185);
		g.drawString("DEF", 605, 200);
		g.drawString("RES", 605, 215);
		g.drawString("MOV", 605, 230);
		
		g.draw(new Rectangle(127,115,95,20));
		g.drawString("Growth", 145, 115);
		g.draw(new Rectangle(127,145,95,20));
		g.drawString("Max stats", 135, 145);
		g.draw(new Rectangle(127,175,95,20));
		g.drawString("Pairup",145,175);
		g.draw(new Rectangle(127,205,95,20));
		g.drawString("Max+Pairup", 130, 205);
		
		g.draw(new Rectangle(427,115,95,20));
		g.drawString("Growth", 445, 115);
		g.draw(new Rectangle(427,145,95,20));
		g.drawString("Max stats", 435, 145);
		g.draw(new Rectangle(427,175,95,20));
		g.drawString("Pairup",445,175);
		g.draw(new Rectangle(427,205,95,20));
		g.drawString("Max+Pairup", 430, 205);
		
		g.drawString("Boon: ",575,730);
		g.drawString("Bane: ",575,750);
		g.drawString("Talent: ",575,770);
		
		g.drawString(boonMap.get(avatarBoon),640,730);
		g.drawString(baneMap.get(avatarBane),640,750);
		g.drawString(avatarTalent,640,770);
		
		if(!unitCompatible.equals("")){
			if(firstGenList.containsKey(unitCompatible)){
				tempUnit = firstGenList.get(unitCompatible);
			} else if(childList.containsKey(unitCompatible)){
				tempUnit = childList.get(unitCompatible);
			}
		}
		
		for(String unit : unitProfiles.keySet()){
			UnitProfile profile = unitProfiles.get(unit);
			int x = profile.getCurrentX();
			int y = profile.getCurrentY();
			profile.getImage().draw(x, y,1);
			
			if(!unitCompatible.equals("")){
				if(tempUnit.getSupportSet().contains(unit)){
					profile.getImage().setAlpha(1f);
				} else {
					profile.getImage().setAlpha(0.5f);
				}
			} else {
				profile.getImage().setAlpha(1f);
			}
		}
		FirstGenCharacter tempConstUnit = null, tempVarUnit = null;
		int k;
		int hasConst, hasVar, isConstChild, isVarChild;
		String pairing = new String();
		for(int i = 0; i<NUM_PAIRUPS;i++){
			g.draw(pairupBoxes[i].getRectangle(startingPoint));
			//draw data boxes under each pairup box
			tempConstUnit = null;
			tempVarUnit = null;
			pairing = "";
			hasConst = 0;
			hasVar = 0;
			isConstChild=0;
			isVarChild = 0;
			if(firstGenList.containsKey(pairupBoxes[i].getConstPar())){
				tempConstUnit = firstGenList.get(pairupBoxes[i].getConstPar());
				hasConst = 1;
			} else if(childList.containsKey(pairupBoxes[i].getConstPar())){
				tempConstUnit = childList.get(pairupBoxes[i].getConstPar());
				hasConst = 1;
				isConstChild = 1;
			}
			if(firstGenList.containsKey(pairupBoxes[i].getVarPar())){
				tempVarUnit = firstGenList.get(pairupBoxes[i].getVarPar());
				hasVar=1;
			} else if(childList.containsKey(pairupBoxes[i].getVarPar())){
				tempVarUnit = childList.get(pairupBoxes[i].getVarPar());
				hasVar=1;
				isVarChild = 1;
			}
			
			if(hasConst==1){
				k=-1;
				HashSet<String> newSkillSet = updateSkills(generateFullClassSet(tempConstUnit.getClassSet()).toArray(new String[generateFullClassSet(tempConstUnit.getClassSet()).size()]));
				//Is the unit a child? inherit from both parents
				if(isConstChild==1){
					String[] tempSkills = newSkillSet.toArray(new String[newSkillSet.size()+2]);
					int foundPar = 0;
					for (int x = 0; x<NUM_PAIRUPS;x++){
						if(pairupBoxes[x].getConstPar().equals(((ChildCharacter) tempConstUnit).getConstParent()) && pairupBoxes[x].getVarPar().equals(((ChildCharacter) tempConstUnit).getVarParent()) && !pairupBoxes[x].getVarPar().equals("")){
							tempSkills[tempSkills.length - 1] = pairupBoxes[x].getConstParSkillPool()[pairupBoxes[x].getConstParSkillsChoices()[4]];
							tempSkills[tempSkills.length - 2] = pairupBoxes[x].getVarParSkillPool()[pairupBoxes[x].getVarParSkillsChoices()[4]];
							foundPar = 1;
							if(tempSkills[tempSkills.length - 1].equals("Inspiring Song")){
								tempSkills[tempSkills.length - 1] = tempSkills[0];
							}
							if(tempSkills[tempSkills.length - 2].equals("Inspiring Song")){
								tempSkills[tempSkills.length - 2] = tempSkills[0];
							}
						} else if(pairupBoxes[x].getConstPar().equals(((ChildCharacter) tempConstUnit).getVarParent()) && pairupBoxes[x].getVarPar().equals(((ChildCharacter) tempConstUnit).getConstParent()) && !pairupBoxes[x].getVarPar().equals("")){
							tempSkills[tempSkills.length - 1] = pairupBoxes[x].getConstParSkillPool()[pairupBoxes[x].getConstParSkillsChoices()[4]];
							tempSkills[tempSkills.length - 2] = pairupBoxes[x].getVarParSkillPool()[pairupBoxes[x].getVarParSkillsChoices()[4]];
							foundPar = 1;
							if(tempSkills[tempSkills.length - 1].equals("Inspiring Song")){
								tempSkills[tempSkills.length - 1] = tempSkills[0];
							}
							if(tempSkills[tempSkills.length - 2].equals("Inspiring Song")){
								tempSkills[tempSkills.length - 2] = tempSkills[0];
							}
						}
					}
					if(foundPar == 1){
						pairupBoxes[i].setConstParSkillPool(tempSkills);
					} else {
						tempSkills[tempSkills.length - 1] = "";//cause of error
						tempSkills[tempSkills.length - 2] = "";//cause of error
						pairupBoxes[i].setConstParSkillsChoices(new int[]{0,0,0,0,0});
						pairupBoxes[i].setVarParSkillsChoices(new int[]{0,0,0,0,0});
						pairupBoxes[i].setConstParSkillPool(tempSkills);
					}
					

				} else {
					pairupBoxes[i].setConstParSkillPool(newSkillSet.toArray(new String[newSkillSet.size()]));
				}
				for(String unitClass : generateFullClassSet(tempConstUnit.getClassSet())){
					if(k==-1){
						g.drawString("none", pairupBoxes[i].getX()+20, pairupBoxes[i].getY()+215);
						k++;
						pairupBoxes[i].setConstClass("none");
					}
					if(pairupBoxes[i].getConstClassClicked()==k){
						g.drawString(">",pairupBoxes[i].getX()+10,pairupBoxes[i].getY()+230+15*k);
						pairupBoxes[i].setConstClass(unitClass);
					}
					g.drawString(unitClass, pairupBoxes[i].getX()+20, pairupBoxes[i].getY()+230+15*k);
					k++;
				}
				pairing = pairing + tempConstUnit.getName();
				if(pairupBoxes[i].getConstParSkillPool().length!=0){
					//Skills
					g.drawString(pairupBoxes[i].getConstParSkillPool()[pairupBoxes[i].getConstParSkillsChoices()[0]],pairupBoxes[i].getX()+20,445);
					g.drawString(pairupBoxes[i].getConstParSkillPool()[pairupBoxes[i].getConstParSkillsChoices()[1]],pairupBoxes[i].getX()+20,460);
					g.drawString(pairupBoxes[i].getConstParSkillPool()[pairupBoxes[i].getConstParSkillsChoices()[2]],pairupBoxes[i].getX()+20,475);
					g.drawString(pairupBoxes[i].getConstParSkillPool()[pairupBoxes[i].getConstParSkillsChoices()[3]],pairupBoxes[i].getX()+20,490);
					g.drawString(pairupBoxes[i].getConstParSkillPool()[pairupBoxes[i].getConstParSkillsChoices()[4]],pairupBoxes[i].getX()+20,505);
				}
				if(statView==0){
					for(int j=0;j<tempConstUnit.getGrowthRates().length;j++){
						if(!pairupBoxes[i].getConstClass().equals("none") && !pairupBoxes[i].getConstClass().isEmpty()){
							g.drawString(String.valueOf(tempConstUnit.getGrowthRates()[j] + classList.get(pairupBoxes[i].getConstClass()).getGrowthRates()[j]),pairupBoxes[i].getX()+20, pairupBoxes[i].getY()+80+15*j);
						} else {
							g.drawString(String.valueOf(tempConstUnit.getGrowthRates()[j]),pairupBoxes[i].getX()+20, pairupBoxes[i].getY()+80+15*j);
						}
					}
				} else if (statView==1){
					if(!pairupBoxes[i].getConstClass().equals("none") && !pairupBoxes[i].getConstClass().isEmpty()){
						for(int j=0;j<tempConstUnit.getStatModifiers().length+2;j++){
							if(j!=0 && j!=8){
								g.drawString(String.valueOf(tempConstUnit.getStatModifiers()[j-1] + classList.get(pairupBoxes[i].getConstClass()).getMaximumStats()[j]),pairupBoxes[i].getX()+20, pairupBoxes[i].getY()+80+15*j);
							} else {
								g.drawString(String.valueOf(classList.get(pairupBoxes[i].getConstClass()).getMaximumStats()[j]),pairupBoxes[i].getX()+20, pairupBoxes[i].getY()+80+15*j);
							}
						}
					} else {
						for(int j=0;j<tempConstUnit.getStatModifiers().length;j++){
							g.drawString(String.valueOf(tempConstUnit.getStatModifiers()[j]),pairupBoxes[i].getX()+20, pairupBoxes[i].getY()+95+15*j);
						}
					}
				} else if (statView==2){
					int[] totalPair = new int[]{0,0,0,0,0,0,0,0};
					for(int j=0;j<tempConstUnit.getPairupStats().length;j++){
						totalPair[0] = totalPair[0] + tempConstUnit.getPairupStats()[j][0];
						totalPair[1] = totalPair[1] + tempConstUnit.getPairupStats()[j][1];
						totalPair[2] = totalPair[2] + tempConstUnit.getPairupStats()[j][2];
						totalPair[3] = totalPair[3] + tempConstUnit.getPairupStats()[j][3];
						totalPair[4] = totalPair[4] + tempConstUnit.getPairupStats()[j][4];
						totalPair[5] = totalPair[5] + tempConstUnit.getPairupStats()[j][5];
						totalPair[6] = totalPair[6] + tempConstUnit.getPairupStats()[j][6];
					}
					if(!pairupBoxes[i].getConstClass().equals("none") && !pairupBoxes[i].getConstClass().isEmpty()){
						for(int j=0;j<totalPair.length;j++){
							g.drawString(String.valueOf(totalPair[j]+classList.get(pairupBoxes[i].getConstClass()).getPairupStats()[j]),pairupBoxes[i].getX()+20, pairupBoxes[i].getY()+95+15*j);
						}
					} else {
						for(int j=0;j<totalPair.length;j++){
							g.drawString(String.valueOf(totalPair[j]),pairupBoxes[i].getX()+20, pairupBoxes[i].getY()+95+15*j);
						}
					} 
				} else {
					if(hasVar==1){
						int[] maxStats = new int[]{0,0,0,0,0,0,0,0,0};
						int[] totalPair = new int[]{0,0,0,0,0,0,0,0};
						for(int j=0;j<tempVarUnit.getPairupStats().length;j++){
							totalPair[0] = totalPair[0] + tempVarUnit.getPairupStats()[j][0];
							totalPair[1] = totalPair[1] + tempVarUnit.getPairupStats()[j][1];
							totalPair[2] = totalPair[2] + tempVarUnit.getPairupStats()[j][2];
							totalPair[3] = totalPair[3] + tempVarUnit.getPairupStats()[j][3];
							totalPair[4] = totalPair[4] + tempVarUnit.getPairupStats()[j][4];
							totalPair[5] = totalPair[5] + tempVarUnit.getPairupStats()[j][5];
							totalPair[6] = totalPair[6] + tempVarUnit.getPairupStats()[j][6];
						}
						if(!pairupBoxes[i].getConstClass().equals("none") && !pairupBoxes[i].getConstClass().isEmpty() && !pairupBoxes[i].getVarClass().equals("none") && !pairupBoxes[i].getVarClass().isEmpty()){
							int[] constParStatModifiers = tempConstUnit.getStatModifiers();
							int[] constParClassMaxStats = classList.get(pairupBoxes[i].getConstClass()).getMaximumStats();
							int[] varClassPairup = classList.get(pairupBoxes[i].getVarClass()).getPairupStats();
							int[] varUnitPairup = totalPair;
							if(tempConstUnit.getSupportSet().contains(tempVarUnit.getName())){
								maxStats[0] = constParClassMaxStats[0];
								maxStats[1] = constParClassMaxStats[1] + constParStatModifiers[0] + varClassPairup[0] + varUnitPairup[0];
								maxStats[2] = constParClassMaxStats[2] + constParStatModifiers[1] + varClassPairup[1] + varUnitPairup[1];
								maxStats[3] = constParClassMaxStats[3] + constParStatModifiers[2] + varClassPairup[2] + varUnitPairup[2];
								maxStats[4] = constParClassMaxStats[4] + constParStatModifiers[3] + varClassPairup[3] + varUnitPairup[3];
								maxStats[5] = constParClassMaxStats[5] + constParStatModifiers[4] + varClassPairup[4] + varUnitPairup[4];
								maxStats[6] = constParClassMaxStats[6] + constParStatModifiers[5] + varClassPairup[5] + varUnitPairup[5];
								maxStats[7] = constParClassMaxStats[7] + constParStatModifiers[6] + varClassPairup[6] + varUnitPairup[6];
								maxStats[8] = constParClassMaxStats[8] + varClassPairup[7] + varUnitPairup[7];
							} else {
								maxStats[0] = constParClassMaxStats[0];
								maxStats[1] = constParClassMaxStats[1] + constParStatModifiers[0] + varClassPairup[0];
								maxStats[2] = constParClassMaxStats[2] + constParStatModifiers[1] + varClassPairup[1];
								maxStats[3] = constParClassMaxStats[3] + constParStatModifiers[2] + varClassPairup[2];
								maxStats[4] = constParClassMaxStats[4] + constParStatModifiers[3] + varClassPairup[3];
								maxStats[5] = constParClassMaxStats[5] + constParStatModifiers[4] + varClassPairup[4];
								maxStats[6] = constParClassMaxStats[6] + constParStatModifiers[5] + varClassPairup[5];
								maxStats[7] = constParClassMaxStats[7] + constParStatModifiers[6] + varClassPairup[6];
								maxStats[8] = constParClassMaxStats[8] + varClassPairup[7];
							}
						} 
						for(int j = 0;j<maxStats.length;j++){
							g.drawString(String.valueOf(maxStats[j]),pairupBoxes[i].getX()+20, pairupBoxes[i].getY()+80+15*j);
						}
					}
				}

			}
			if(hasVar==1){
				k=-1;
				HashSet<String> newSkillSet = updateSkills(generateFullClassSet(tempVarUnit.getClassSet()).toArray(new String[generateFullClassSet(tempVarUnit.getClassSet()).size()]));
				//Is the unit a child? inherit from both parents
				if(isVarChild==1){
					String[] tempSkills = newSkillSet.toArray(new String[newSkillSet.size()+2]);
					int foundPar = 0;
					for (int x = 0; x<NUM_PAIRUPS;x++){
						if(pairupBoxes[x].getConstPar().equals(((ChildCharacter) tempVarUnit).getConstParent()) && pairupBoxes[x].getVarPar().equals(((ChildCharacter) tempVarUnit).getVarParent()) && !pairupBoxes[x].getVarPar().equals("")){
							tempSkills[tempSkills.length - 1] = pairupBoxes[x].getConstParSkillPool()[pairupBoxes[x].getConstParSkillsChoices()[4]];
							tempSkills[tempSkills.length - 2] = pairupBoxes[x].getVarParSkillPool()[pairupBoxes[x].getVarParSkillsChoices()[4]];
							foundPar =1;
						} 
					}
					if(foundPar == 1){
						pairupBoxes[i].setVarParSkillPool(tempSkills);
					} else {
						tempSkills[tempSkills.length - 1] = "";//cause for error
						tempSkills[tempSkills.length - 2] = "";//cause for error
						pairupBoxes[i].setConstParSkillsChoices(new int[]{0,0,0,0,0});
						pairupBoxes[i].setVarParSkillsChoices(new int[]{0,0,0,0,0});
						pairupBoxes[i].setVarParSkillPool(tempSkills);
					}
					
				} else {
					pairupBoxes[i].setVarParSkillPool(newSkillSet.toArray(new String[newSkillSet.size()]));
				}
				for(String unitClass : generateFullClassSet(tempVarUnit.getClassSet())){
					if(k==-1){
						g.drawString("none", pairupBoxes[i].getX()+170, pairupBoxes[i].getY()+215);
						k++;
						pairupBoxes[i].setVarClass("none");
					}
					if(pairupBoxes[i].getVarClassClicked()==k){
						g.drawString(">",pairupBoxes[i].getX()+160,pairupBoxes[i].getY()+230+15*k);
						pairupBoxes[i].setVarClass(unitClass);
					}
					g.drawString(unitClass, pairupBoxes[i].getX()+170, pairupBoxes[i].getY()+230+15*k);
					k++;
				}
				pairing = pairing + " x " + tempVarUnit.getName();
				if(pairupBoxes[i].getVarParSkillPool().length!=0){
					//Skills
					g.drawString(pairupBoxes[i].getVarParSkillPool()[pairupBoxes[i].getVarParSkillsChoices()[0]],pairupBoxes[i].getX()+170,445);
					g.drawString(pairupBoxes[i].getVarParSkillPool()[pairupBoxes[i].getVarParSkillsChoices()[1]],pairupBoxes[i].getX()+170,460);
					g.drawString(pairupBoxes[i].getVarParSkillPool()[pairupBoxes[i].getVarParSkillsChoices()[2]],pairupBoxes[i].getX()+170,475);
					g.drawString(pairupBoxes[i].getVarParSkillPool()[pairupBoxes[i].getVarParSkillsChoices()[3]],pairupBoxes[i].getX()+170,490);
					g.drawString(pairupBoxes[i].getVarParSkillPool()[pairupBoxes[i].getVarParSkillsChoices()[4]],pairupBoxes[i].getX()+170,505);
				}
				if(statView==0){
					for(int j=0;j<tempVarUnit.getGrowthRates().length;j++){
						if(!pairupBoxes[i].getVarClass().equals("none") && !pairupBoxes[i].getVarClass().isEmpty()){
							g.drawString(String.valueOf(tempVarUnit.getGrowthRates()[j] + classList.get(pairupBoxes[i].getVarClass()).getGrowthRates()[j]),pairupBoxes[i].getX()+220, pairupBoxes[i].getY()+80+15*j);
						} else {
							g.drawString(String.valueOf(tempVarUnit.getGrowthRates()[j]),pairupBoxes[i].getX()+220, pairupBoxes[i].getY()+80+15*j);
						}
					}
				} else if (statView==1){
					if(!pairupBoxes[i].getVarClass().equals("none") && !pairupBoxes[i].getVarClass().isEmpty()){
						for(int j=0;j<tempVarUnit.getStatModifiers().length+2;j++){
							if(j!=0 && j!=8){
								g.drawString(String.valueOf(tempVarUnit.getStatModifiers()[j-1] + classList.get(pairupBoxes[i].getVarClass()).getMaximumStats()[j]),pairupBoxes[i].getX()+220, pairupBoxes[i].getY()+80+15*j);
							} else {
								g.drawString(String.valueOf(classList.get(pairupBoxes[i].getVarClass()).getMaximumStats()[j]),pairupBoxes[i].getX()+220, pairupBoxes[i].getY()+80+15*j);
							}
						}
					} else {
						for(int j=0;j<tempVarUnit.getStatModifiers().length;j++){
							g.drawString(String.valueOf(tempVarUnit.getStatModifiers()[j]),pairupBoxes[i].getX()+220, pairupBoxes[i].getY()+95+15*j);
						}
					}
				} else if (statView==2){
					int[] totalPair = new int[]{0,0,0,0,0,0,0,0};
					for(int j=0;j<tempVarUnit.getPairupStats().length;j++){
						totalPair[0] = totalPair[0] + tempVarUnit.getPairupStats()[j][0];
						totalPair[1] = totalPair[1] + tempVarUnit.getPairupStats()[j][1];
						totalPair[2] = totalPair[2] + tempVarUnit.getPairupStats()[j][2];
						totalPair[3] = totalPair[3] + tempVarUnit.getPairupStats()[j][3];
						totalPair[4] = totalPair[4] + tempVarUnit.getPairupStats()[j][4];
						totalPair[5] = totalPair[5] + tempVarUnit.getPairupStats()[j][5];
						totalPair[6] = totalPair[6] + tempVarUnit.getPairupStats()[j][6];
					}
					if(!pairupBoxes[i].getVarClass().equals("none") && !pairupBoxes[i].getVarClass().isEmpty()){
						for(int j=0;j<totalPair.length;j++){
							g.drawString(String.valueOf(totalPair[j]+classList.get(pairupBoxes[i].getVarClass()).getPairupStats()[j]),pairupBoxes[i].getX()+220, pairupBoxes[i].getY()+95+15*j);
						}
					} else {
						for(int j=0;j<totalPair.length;j++){
							g.drawString(String.valueOf(totalPair[j]),pairupBoxes[i].getX()+220, pairupBoxes[i].getY()+95+15*j);
						}
					} 
				} else {
					if(hasConst==1){
						int[] maxStats = new int[]{0,0,0,0,0,0,0,0,0};
						int[] totalPair = new int[]{0,0,0,0,0,0,0,0};
						for(int j=0;j<tempConstUnit.getPairupStats().length;j++){
							totalPair[0] = totalPair[0] + tempConstUnit.getPairupStats()[j][0];
							totalPair[1] = totalPair[1] + tempConstUnit.getPairupStats()[j][1];
							totalPair[2] = totalPair[2] + tempConstUnit.getPairupStats()[j][2];
							totalPair[3] = totalPair[3] + tempConstUnit.getPairupStats()[j][3];
							totalPair[4] = totalPair[4] + tempConstUnit.getPairupStats()[j][4];
							totalPair[5] = totalPair[5] + tempConstUnit.getPairupStats()[j][5];
							totalPair[6] = totalPair[6] + tempConstUnit.getPairupStats()[j][6];
						}
						if(!pairupBoxes[i].getConstClass().equals("none") && !pairupBoxes[i].getConstClass().isEmpty() && !pairupBoxes[i].getVarClass().equals("none") && !pairupBoxes[i].getVarClass().isEmpty()){
							int[] varParStatModifiers = tempVarUnit.getStatModifiers();
							int[] varParClassMaxStats = classList.get(pairupBoxes[i].getVarClass()).getMaximumStats();
							int[] constClassPairup = classList.get(pairupBoxes[i].getConstClass()).getPairupStats();
							int[] constUnitPairup = totalPair;
							if(tempVarUnit.getSupportSet().contains(tempConstUnit.getName())){
								maxStats[0] = varParClassMaxStats[0];
								maxStats[1] = varParClassMaxStats[1] + varParStatModifiers[0] + constClassPairup[0] + constUnitPairup[0];
								maxStats[2] = varParClassMaxStats[2] + varParStatModifiers[1] + constClassPairup[1] + constUnitPairup[1];
								maxStats[3] = varParClassMaxStats[3] + varParStatModifiers[2] + constClassPairup[2] + constUnitPairup[2];
								maxStats[4] = varParClassMaxStats[4] + varParStatModifiers[3] + constClassPairup[3] + constUnitPairup[3];
								maxStats[5] = varParClassMaxStats[5] + varParStatModifiers[4] + constClassPairup[4] + constUnitPairup[4];
								maxStats[6] = varParClassMaxStats[6] + varParStatModifiers[5] + constClassPairup[5] + constUnitPairup[5];
								maxStats[7] = varParClassMaxStats[7] + varParStatModifiers[6] + constClassPairup[6] + constUnitPairup[6];
								maxStats[8] = varParClassMaxStats[8] + constClassPairup[7] + constUnitPairup[7];
							} else {
								maxStats[0] = varParClassMaxStats[0];
								maxStats[1] = varParClassMaxStats[1] + varParStatModifiers[0] + constClassPairup[0];
								maxStats[2] = varParClassMaxStats[2] + varParStatModifiers[1] + constClassPairup[1];
								maxStats[3] = varParClassMaxStats[3] + varParStatModifiers[2] + constClassPairup[2];
								maxStats[4] = varParClassMaxStats[4] + varParStatModifiers[3] + constClassPairup[3];
								maxStats[5] = varParClassMaxStats[5] + varParStatModifiers[4] + constClassPairup[4];
								maxStats[6] = varParClassMaxStats[6] + varParStatModifiers[5] + constClassPairup[5];
								maxStats[7] = varParClassMaxStats[7] + varParStatModifiers[6] + constClassPairup[6];
								maxStats[8] = varParClassMaxStats[8] + constClassPairup[7];
							}
							
						}
						for(int j = 0;j<maxStats.length;j++){
							g.drawString(String.valueOf(maxStats[j]),pairupBoxes[i].getX()+220, pairupBoxes[i].getY()+80+15*j);
						}
					}
				}
	
			}
			
			if(pairing.equals("Ryoma x Scarlet") || pairing.equals("Jakob x Flora")){
				g.drawString("*cries*", pairupBoxes[i].getX(), pairupBoxes[i].getY()-20);
			} else {
				if(tempConstUnit != null && tempVarUnit != null){
					if(tempConstUnit.getSupportSet().contains(tempVarUnit.getName()) && tempVarUnit.getSupportSet().contains(tempConstUnit.getName())){
						g.drawString(pairing, pairupBoxes[i].getX(), pairupBoxes[i].getY()-20);
					} else {
						g.drawString(pairing + "(no S-support)", pairupBoxes[i].getX(), pairupBoxes[i].getY()-20);
					}
				}
			}
		}
//Draw the save and load box
		g.draw(new Rectangle(735,740,50,20));
		g.drawString("Save", 740, 740);
		g.draw(new Rectangle(735,765,50,20));
		g.drawString("Load", 740, 765);
	}
	
	@Override
	public void mousePressed(int button, int x, int y){
//		System.out.println("X = " + x + " || Y = " + y);
//If user pressed save
		if(x>735&&x<785&&y>740&&y<760){
			try {
				savePairings();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
//If user pressed load
		if(x>735&&x<785&&y>765&&y<785){
			loadPairings();
		}
		
		int activeProfile = 0;
		for(String unit : unitProfiles.keySet()){
			UnitProfile profile = unitProfiles.get(unit);
			int imgx = profile.getCurrentX();
			int imgy = profile.getCurrentY();
			if( x > imgx && x < imgx+profile.getImage().getWidth() && y > imgy && y < imgy+profile.getImage().getHeight()){
				profile.setIsDragging(1);
				activeProfile = 1;
				unitCompatible = unit;
			} 
		}
		if(activeProfile == 0 || button == 0){
			unitCompatible = "";
		}
		//detecting if mouse clicks string of class name y>230 and y<425, constpar x 50 to 160 , varpar 200 to 300
		for(int i = 0;i<pairupBoxes.length;i++){
			int mx = pairupBoxes[i].getX();
			int my = pairupBoxes[i].getY();
			//click on constpar
			if((x>mx+20) && (x<mx+150) && (y>my+215) && (y<my+410)){
				pairupBoxes[i].setConstClassClicked(-1+(y-(my+215))/15);
			} else if((x>mx+170) && (x<mx+300) && (y>my+215) && (y<my+410)){//click on varpar
				pairupBoxes[i].setVarClassClicked(-1+(y-(my+215))/15);
			}

			//Clicking to move to the next skill
			if((x>mx+20) && (x<mx+150) && (y>my+415) && (y<my+490)){
				if(pairupBoxes[i].getConstParSkillsChoices()[((y-(my+415))/15)]+1>=pairupBoxes[i].getConstParSkillPool().length){
					pairupBoxes[i].getConstParSkillsChoices()[((y-(my+415))/15)]=0;
				} else {
					pairupBoxes[i].getConstParSkillsChoices()[((y-(my+415))/15)]++;
				}
				
			} else if((x>mx+170) && (x<mx+300) && (y>my+415) && (y<my+490)){
				if(pairupBoxes[i].getVarParSkillsChoices()[((y-(my+415))/15)]+1>=pairupBoxes[i].getVarParSkillPool().length){
					pairupBoxes[i].getVarParSkillsChoices()[((y-(my+415))/15)]=0;
				} else {
					pairupBoxes[i].getVarParSkillsChoices()[((y-(my+415))/15)]++;
				}
			}

		}
		if(x>5&&x<20&&y>30&&y<100){
			if(startingPoint-1<0){
				startingPoint=0;
			} else {
				startingPoint--;
				for(String unit : unitProfiles.keySet()){
					int isPairedUp = 0;
					for(int i = 0; i<NUM_PAIRUPS; i++){
						if(pairups[i][0].equals(unit) || pairups[i][1].equals(unit)){
							isPairedUp=1;
						}
					}
					UnitProfile profile = unitProfiles.get(unit);
					if(isPairedUp==1){
						int tempx = profile.getCurrentX()+300;
						int tempy = profile.getCurrentY();
						profile.getImage().draw(tempx, tempy,1);
						profile.setCurrentX(tempx);
						profile.getProfileShape().setX(tempx);
					} 
				}
			}
		} else if(x>780&&x<795&&y>30&&y<100){
			if(startingPoint+2<NUM_PAIRUPS){
				startingPoint++;
				for(String unit : unitProfiles.keySet()){
					int isPairedUp = 0;
					for(int i = 0; i<NUM_PAIRUPS; i++){
						if(pairups[i][0].equals(unit) || pairups[i][1].equals(unit)){
							isPairedUp=1;
						}
					}
					UnitProfile profile = unitProfiles.get(unit);
					if(isPairedUp==1){
						int tempx = profile.getCurrentX()-300;
						int tempy = profile.getCurrentY();
						profile.getImage().draw(tempx, tempy,1);
						profile.setCurrentX(tempx);
						profile.getProfileShape().setX(tempx);
					} 
				}
			}
		}
		if((x>127 && x<222 && y>115 && y<135) || (x>427 && x<522 && y>115 && y<135)){
			statView = 0;
		} else if ((x>127 && x<222 && y>145 && y<165) || (x>427 && x<522 && y>145 && y<165)){
			statView = 1;
		} else if ((x>127 && x<222 && y>175 && y<195) || (x>427 && x<522 && y>175 && y<195)){
			statView = 2;
		} else if ((x>127 && x<222 && y>205 && y<225) || (x>427 && x<522 && y>205 && y<225)){
			statView = 3;
		}
		
		if(x>630 && x<710 && y>730 && y<749){
			avatarBoon++;
			if(avatarBoon==avatarBane){
				avatarBoon++;
			}
			if(avatarBoon>7){
				avatarBoon=0;
			}
			((AvatarUnit) firstGenList.get("AvatarM")).setBoon(avatarBoon);
			((AvatarUnit) firstGenList.get("AvatarF")).setBoon(avatarBoon);
		} else if(x>630 && x<710 && y>750 && y<769) {
			avatarBane++;
			if(avatarBoon==avatarBane){
				avatarBane++;
			}
			if(avatarBane>7){
				avatarBane=0;
			}
			((AvatarUnit) firstGenList.get("AvatarM")).setBane(avatarBane);
			((AvatarUnit) firstGenList.get("AvatarF")).setBane(avatarBane);
		} else if(x>630 && x<710 && y>770 && y<789) {
			int talentVal=0;
			for(int i = 0; i<talentMap.size();i++){
				if((talentMap.get(i)).equals(avatarTalent)){
					talentVal = i;
				}
			}
			talentVal++;
			if(talentVal>17){
				talentVal=0;
			}
			avatarTalent = (talentMap.get(talentVal));
			((AvatarUnit) firstGenList.get("AvatarM")).setTalent(avatarTalent);
			((AvatarUnit) firstGenList.get("AvatarF")).setTalent(avatarTalent);
			for(int i = 0;i<pairupBoxes.length;i++){
				if(pairupBoxes[i].getConstPar().equals("AvatarM") || pairupBoxes[i].getConstPar().equals("AvatarF") ||
						pairupBoxes[i].getVarPar().equals("AvatarM") || pairupBoxes[i].getVarPar().equals("AvatarF")){
					pairupBoxes[i].setConstParSkillsChoices(new int[]{0,0,0,0,0});
					pairupBoxes[i].setVarParSkillsChoices(new int[]{0,0,0,0,0});
				}
			}
		}
		determinePairings(pairups);
	}
	
	private void loadPairings() {
		// TODO Auto-generated method stub
		//lines 1-30: pairing x pairing
		//lines 31: avatar settings boon;bane;talent
		//lines 32+: const class; varclass; const skill 1;2;3;4;5 ; var skills 1;2;3;4;5
		String filename = "PairingPlannerPairings.txt";
		FileInputStream fis;
		String line = "";
		int pairupBoxCount = 0;
		
		try {
			fis = new FileInputStream(new File(filename));
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			line = br.readLine();
			
			while(!line.equals("") && !line.contains(";")){
				String[] pairs = line.split(" x ");
				pairupBoxes[pairupBoxCount].setConstPar(pairs[0]);
				pairupBoxes[pairupBoxCount].setVarPar(pairs[1]);
				pairups[pairupBoxCount][0]=pairs[0];
				pairups[pairupBoxCount][1]=pairs[1];
				
				UnitProfile constProfile = unitProfiles.get(pairs[0]);
				UnitProfile varProfile = unitProfiles.get(pairs[1]);
				
				constProfile.setCurrentX((int) (pairupBoxes[pairupBoxCount].getX()+20));
				constProfile.setCurrentY((int) (pairupBoxes[pairupBoxCount].getY()+10));
				constProfile.getProfileShape().setX(constProfile.getCurrentX());
				constProfile.getProfileShape().setY(constProfile.getCurrentY());
				
				varProfile.setCurrentX((int) (pairupBoxes[pairupBoxCount].getX()+220));
				varProfile.setCurrentY((int) (pairupBoxes[pairupBoxCount].getY()+10));
				varProfile.getProfileShape().setX(varProfile.getCurrentX());
				varProfile.getProfileShape().setY(varProfile.getCurrentY());
				
				pairupBoxCount++;
				line = br.readLine();
			}
			determinePairings(pairups);
			//this line contains the boon;bane;talent
			String[] avatarSettings = line.split(";");
			avatarBoon = Integer.parseInt(avatarSettings[0]);
			avatarBane = Integer.parseInt(avatarSettings[1]);
			avatarTalent = avatarSettings[2];
			
			pairupBoxCount=0;
			//lines until eof
			//lines 32+: const class; varclass; const skill 1;2;3;4;5 ; var skills 1;2;3;4;5
			line = br.readLine();
			while(line!=null&&line.contains(";")){
				String[] pairingInfo = line.split(";");
				pairupBoxes[pairupBoxCount].setConstClassClicked(Integer.parseInt(pairingInfo[0]));
				pairupBoxes[pairupBoxCount].setVarClassClicked(Integer.parseInt(pairingInfo[1]));

				pairupBoxes[pairupBoxCount].setConstParSkillsChoices(new int[]{Integer.parseInt(pairingInfo[2]),Integer.parseInt(pairingInfo[3]),Integer.parseInt(pairingInfo[4]),Integer.parseInt(pairingInfo[5]),Integer.parseInt(pairingInfo[6])});
				pairupBoxes[pairupBoxCount].setVarParSkillsChoices(new int[]{Integer.parseInt(pairingInfo[7]),Integer.parseInt(pairingInfo[8]),Integer.parseInt(pairingInfo[9]),Integer.parseInt(pairingInfo[10]),Integer.parseInt(pairingInfo[11])});
				pairupBoxCount++;
				line = br.readLine();		
			}
			
			fis.close();
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		((AvatarUnit) firstGenList.get("AvatarM")).setBoon(avatarBoon);
		((AvatarUnit) firstGenList.get("AvatarM")).setBane(avatarBane);
		((AvatarUnit) firstGenList.get("AvatarM")).setTalent(avatarTalent);
		((AvatarUnit) firstGenList.get("AvatarF")).setBoon(avatarBoon);
		((AvatarUnit) firstGenList.get("AvatarF")).setBane(avatarBane);
		((AvatarUnit) firstGenList.get("AvatarF")).setTalent(avatarTalent);
		//profile.setCurrentX((int) (pairupBoxes[tempInt].getX()+20));
		//profile.setCurrentY((int) (pairupBoxes[tempInt].getY()+10));
	}

	private void savePairings() throws IOException {
		
		String filename = "PairingPlannerPairings.txt";
		FileOutputStream outFile = new FileOutputStream(new File(filename));
		BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(outFile));
		for(int i = 0;i<pairupBoxes.length;i++){
			if(pairupBoxes[i].getConstPar() != "" && pairupBoxes[i].getVarPar() != ""){
				//System.out.println(pairupBoxes[i].getConstPar() + " x " + pairupBoxes[i].getVarPar());
				buffer.write(pairupBoxes[i].getConstPar() + " x " + pairupBoxes[i].getVarPar());
				buffer.newLine();
			}
		}
		buffer.write(avatarBoon+";");
		buffer.write(avatarBane+";");
		buffer.write(avatarTalent);
		buffer.newLine();
		
		for(int i = 0;i<pairupBoxes.length;i++){
			if(pairupBoxes[i].getConstPar() != "" && pairupBoxes[i].getVarPar() != ""){
				//System.out.println(pairupBoxes[i].getConstPar() + " x " + pairupBoxes[i].getVarPar());
				buffer.write(pairupBoxes[i].getConstClassClicked() + ";" + pairupBoxes[i].getVarClassClicked());
				
				for(int j = 0;j<pairupBoxes[i].getConstParSkillsChoices().length;j++){
					buffer.write(";" + pairupBoxes[i].getConstParSkillsChoices()[j]);
				}
				for(int j = 0;j<pairupBoxes[i].getVarParSkillsChoices().length;j++){
					buffer.write(";" + pairupBoxes[i].getVarParSkillsChoices()[j]);
				}
				buffer.newLine();
			}
		}
		
		buffer.close();
		outFile.close();
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy){
		for(String unit : unitProfiles.keySet()){
			UnitProfile profile = unitProfiles.get(unit);
			int imgx = profile.getCurrentX();
			int imgy = profile.getCurrentY();
			
			if(profile.getIsDragging()==1){
				if(imgx<0){
					imgx=0;
				}
				if (((imgx+profile.getImage().getWidth()))>WIDTH){
					imgx=800-profile.getImage().getWidth();
				}
				if(imgy<0){
					imgy=0;
				}
				if (((imgy+profile.getImage().getHeight()))>HEIGHT){
					imgy=800-profile.getImage().getHeight();
				}
				profile.setCurrentX(imgx+(newx-oldx));
				profile.setCurrentY(imgy+(newy-oldy));
				profile.getProfileShape().setX(imgx+(newx-oldx));
				profile.getProfileShape().setY(imgy+(newy-oldy));
			}
		}
	}
	
	
	@Override
	public void mouseReleased(int button, int x, int y){
		int intersectionExists=0;
		int currentSpot=0;
		int dropped = 0;
		String currentProfile = new String();
		for(String unit : unitProfiles.keySet()){
			UnitProfile profile = unitProfiles.get(unit);
			if(profile.getIsDragging() == 1){
				profile.setIsDragging(0);
				currentProfile = unit;
				dropped = 1;
			}
		}
		if(dropped == 1){
			UnitProfile profile = unitProfiles.get(currentProfile);
			for(int i=0;i<NUM_PAIRUPS;i++){
				int tempInt = i+startingPoint;
				if(tempInt>=NUM_PAIRUPS){
					tempInt = tempInt-NUM_PAIRUPS;
				}
				if(pairupBoxes[tempInt].getRectangle(startingPoint).intersects(profile.getProfileShape())){
					//Left for constPar, right for varPAr
					if(firstGenList.containsKey(currentProfile)){
						if(firstGenList.get(currentProfile).getSupportSet().contains("AvatarF") || currentProfile.equals("AvatarM")){
							if(!pairups[tempInt][0].isEmpty() && !pairups[tempInt][0].equals(currentProfile)){
								unitProfiles.get(pairups[tempInt][0]).setCurrentX(unitProfiles.get(pairups[tempInt][0]).getOriginalX());
								unitProfiles.get(pairups[tempInt][0]).setCurrentY(unitProfiles.get(pairups[tempInt][0]).getOriginalY());
								if(parentToChild.containsKey(pairups[tempInt][0])){
									childList.get(parentToChild.get(pairups[tempInt][0])).setVarParent("");
								}

							}
							if(pairups[tempInt][0].isEmpty() || !pairups[tempInt][0].equals(currentProfile)){
								profile.setCurrentX((int) (pairupBoxes[tempInt].getX()+20));
								profile.setCurrentY((int) (pairupBoxes[tempInt].getY()+10));
								pairupBoxes[tempInt].setConstPar(currentProfile);
								pairupBoxes[tempInt].setConstParSkillsChoices(new int[]{0,0,0,0,0});
								pairupBoxes[tempInt].setVarParSkillsChoices(new int[]{0,0,0,0,0});
								pairups[tempInt][0] = currentProfile;
								currentSpot = tempInt;
							} else if(pairups[tempInt][0].equals(currentProfile)){
								profile.setCurrentX((int) (pairupBoxes[tempInt].getX()+20));
								profile.setCurrentY((int) (pairupBoxes[tempInt].getY()+10));
								pairupBoxes[tempInt].setConstPar(currentProfile);
								pairups[tempInt][0] = currentProfile;
								currentSpot = tempInt;
							}
						} else {
							if(!pairups[tempInt][1].isEmpty() && !pairups[tempInt][1].equals(currentProfile)){
								unitProfiles.get(pairups[tempInt][1]).setCurrentX(unitProfiles.get(pairups[tempInt][1]).getOriginalX());
								unitProfiles.get(pairups[tempInt][1]).setCurrentY(unitProfiles.get(pairups[tempInt][1]).getOriginalY());				
							}
							if(pairups[tempInt][1].isEmpty() || !pairups[tempInt][1].equals(currentProfile)){
								profile.setCurrentX((int) (pairupBoxes[tempInt].getX()+220));
								profile.setCurrentY((int) (pairupBoxes[tempInt].getY()+10));
								pairupBoxes[tempInt].setVarPar(currentProfile);
								pairupBoxes[tempInt].setConstParSkillsChoices(new int[]{0,0,0,0,0});
								pairupBoxes[tempInt].setVarParSkillsChoices(new int[]{0,0,0,0,0});
								pairups[tempInt][1] = currentProfile;
								currentSpot = tempInt;
							}else if(pairups[tempInt][1].equals(currentProfile)){
								profile.setCurrentX((int) (pairupBoxes[tempInt].getX()+220));
								profile.setCurrentY((int) (pairupBoxes[tempInt].getY()+10));
								pairupBoxes[tempInt].setVarPar(currentProfile);
								pairups[tempInt][1] = currentProfile;
								currentSpot = tempInt;
							}
						}
					} else if(childList.containsKey(currentProfile)){
						if(childList.get(currentProfile).getSupportSet().contains("AvatarF") || currentProfile.equals("AvatarM") || currentProfile.equals("KanaM")){
							if(!pairups[tempInt][0].isEmpty() && !pairups[tempInt][0].equals(currentProfile)){
								unitProfiles.get(pairups[tempInt][0]).setCurrentX(unitProfiles.get(pairups[tempInt][0]).getOriginalX());
								unitProfiles.get(pairups[tempInt][0]).setCurrentY(unitProfiles.get(pairups[tempInt][0]).getOriginalY());
							}
							if(pairups[tempInt][0].isEmpty() || !pairups[tempInt][0].equals(currentProfile)){
								profile.setCurrentX((int) (pairupBoxes[tempInt].getX()+20));
								profile.setCurrentY((int) (pairupBoxes[tempInt].getY()+10));
								pairupBoxes[tempInt].setConstPar(currentProfile);
								pairups[tempInt][0] = currentProfile;
								currentSpot = tempInt;
							} else if(pairups[tempInt][0].equals(currentProfile)){
								profile.setCurrentX((int) (pairupBoxes[tempInt].getX()+20));
								profile.setCurrentY((int) (pairupBoxes[tempInt].getY()+10));
								pairupBoxes[tempInt].setConstPar(currentProfile);
								pairups[tempInt][0] = currentProfile;
								currentSpot = tempInt;
							}
						} else {
							if(!pairups[tempInt][1].isEmpty() && !pairups[tempInt][1].equals(currentProfile)){
								unitProfiles.get(pairups[tempInt][1]).setCurrentX(unitProfiles.get(pairups[tempInt][1]).getOriginalX());
								unitProfiles.get(pairups[tempInt][1]).setCurrentY(unitProfiles.get(pairups[tempInt][1]).getOriginalY());	
							}
							if(pairups[tempInt][1].isEmpty() || !pairups[tempInt][1].equals(currentProfile)){
								profile.setCurrentX((int) (pairupBoxes[tempInt].getX()+220));
								profile.setCurrentY((int) (pairupBoxes[tempInt].getY()+10));
								pairupBoxes[tempInt].setVarPar(currentProfile);
								pairups[tempInt][1] = currentProfile;
								currentSpot = tempInt;
							} else if(pairups[tempInt][1].equals(currentProfile)){
//								profile.setCurrentX((int) ((pairupBoxes[tempInt].getX()-150*i)+70));
								profile.setCurrentX((int) (pairupBoxes[tempInt].getX()+220));
								profile.setCurrentY((int) (pairupBoxes[tempInt].getY()+10));
								pairupBoxes[tempInt].setVarPar(currentProfile);
								pairups[tempInt][1] = currentProfile;
								currentSpot = tempInt;
							}
						}
					}
					intersectionExists = 1;
				}
			}
			if(intersectionExists==0){
				for(int i=0;i<NUM_PAIRUPS;i++){
					if(pairups[i][0].equals(currentProfile)){
						pairups[i][0]="";
						pairupBoxes[i].setConstPar("");
						pairupBoxes[i].setConstParSkillsChoices(new int[]{0,0,0,0,0});
						pairupBoxes[i].setVarParSkillsChoices(new int[]{0,0,0,0,0});
					} else if(pairups[i][1].equals(currentProfile)){
						pairups[i][1]="";
						pairupBoxes[i].setVarPar("");
						pairupBoxes[i].setConstParSkillsChoices(new int[]{0,0,0,0,0});
						pairupBoxes[i].setVarParSkillsChoices(new int[]{0,0,0,0,0});
					}
				}
				for(ChildCharacter child : childList.values()){
					if(child.getVarParent().equals(currentProfile)){
						child.setVarParent("");
						for (int ii = 0; ii<NUM_PAIRUPS;ii++){
							if(pairupBoxes[ii].getConstPar().equals(child.getConstParent()) && pairupBoxes[ii].getVarPar().equals(child.getVarParent())){
								pairupBoxes[ii].setConstParSkillsChoices(new int[]{0,0,0,0,0});
								pairupBoxes[ii].setVarParSkillsChoices(new int[]{0,0,0,0,0});
							} 
						}
					}
				}
				profile.setCurrentX(profile.getOriginalX());
				profile.setCurrentY(profile.getOriginalY());
			} else {
				for(int i=0;i<NUM_PAIRUPS;i++){
					if(pairups[i][0].equals(currentProfile) && i!= currentSpot){
						pairups[i][0]="";
						pairupBoxes[i].setConstPar("");
						pairupBoxes[i].setConstParSkillsChoices(new int[]{0,0,0,0,0});
						pairupBoxes[i].setVarParSkillsChoices(new int[]{0,0,0,0,0});
					} else if(pairups[i][1].equals(currentProfile) && i!= currentSpot){
						pairups[i][1]="";
						pairupBoxes[i].setVarPar("");
						pairupBoxes[i].setVarParSkillsChoices(new int[]{0,0,0,0,0});
						pairupBoxes[i].setConstParSkillsChoices(new int[]{0,0,0,0,0});
					}
				}
			}
			firstGenList = initializeCharacters();
			((AvatarUnit) firstGenList.get("AvatarM")).setBoon(avatarBoon);
			((AvatarUnit) firstGenList.get("AvatarM")).setBane(avatarBane);
			((AvatarUnit) firstGenList.get("AvatarM")).setTalent(avatarTalent);
			((AvatarUnit) firstGenList.get("AvatarF")).setBoon(avatarBoon);
			((AvatarUnit) firstGenList.get("AvatarF")).setBane(avatarBane);
			((AvatarUnit) firstGenList.get("AvatarF")).setTalent(avatarTalent);
			childList = initializeChildren();
			for (FirstGenCharacter unit : firstGenList.values()){
				unit.setSkillSet(updateSkills(unit.getClassSet()));
			}
			determinePairings(pairups);
		}
	}
	
	private void determinePairings(String[][] pairups) {
		for (int i = 0; i<pairups.length;i++){
			if((!pairups[i][0].isEmpty()||!pairups[i][1].isEmpty()) && (firstGenList.containsKey(pairups[i][0])||firstGenList.containsKey(pairups[i][1]))){
				pairup(pairups[i][0],pairups[i][1]);
				if(pairups[i][1].equals("Azura") || pairups[i][1].equals("AvatarF")){
					pairup(pairups[i][1],pairups[i][0]);
				}
			} 

		}
		for (int i = 0; i<pairups.length;i++){
			if((!pairups[i][0].isEmpty()||!pairups[i][1].isEmpty()) && (childList.containsKey(pairups[i][0])||childList.containsKey(pairups[i][1]))){
				pairup(pairups[i][0],pairups[i][1]);
				if(pairups[i][1].equals("Azura") || pairups[i][1].equals("AvatarF")){
					pairup(pairups[i][1],pairups[i][0]);
				}
			} 
		}
	}

	public static void main(String[] args) throws SlickException{
		AppGameContainer appgc = new AppGameContainer(new ScalableGame(new PairingPlanner("Fire Emblem Fates Pairing Planner"), 800, 800, true));
		appgc.setDisplayMode(WIDTH, HEIGHT, false);
		appgc.setShowFPS(false);
		Display.setResizable(true);
		appgc.start();
	}
	
	private HashSet<String> updateSkills(String[] classSet) {
		HashSet<String> skills = new HashSet<String>();
		for (int i = 0; i<classSet.length; i++){
			//Skills of basic class
			if(classList.containsKey(classSet[i])){
				skills.addAll(classList.get(classSet[i]).getSkillSet());
				//Skills of promoted class
				for (String promotedClass : classList.get(classSet[i]).getPromotions()){
					skills.addAll(classList.get(promotedClass).getSkillSet());
				}
			}
		}
		return skills;
	}

	private HashSet<String> generateFullClassSet(String[] classSet){
		HashSet<String> classes = new HashSet<String>();
		for (int i = 0; i<classSet.length; i++){
			if(classList.containsKey(classSet[i])){
				classes.add(classSet[i]);
				for (String promotedClass : classList.get(classSet[i]).getPromotions()){
					classes.add(promotedClass);
				}
			}
		}
		return classes;
	}
	
	private void pairup(String cp, String vp) {
		FirstGenCharacter constPar = new FirstGenCharacter();
		FirstGenCharacter varPar = new FirstGenCharacter();
		if(parentToChild.containsKey(cp)){
			constPar = firstGenList.get(cp);
			if(firstGenList.containsKey(vp)){
				varPar = firstGenList.get(vp);
			} else if(childList.containsKey(vp)){
				varPar = childList.get(vp);
			} else {
				ChildCharacter currentChild = childList.get(parentToChild.get(constPar.getName()));
				currentChild.setVarParent("");
			}
		} else if(childList.containsKey(cp)){
			constPar = childList.get(cp);
			if(childList.containsKey(vp)){
				varPar = childList.get(vp);
			} else if(firstGenList.containsKey(vp)){
				varPar = firstGenList.get(vp);
			}
		} 
		if(constPar.getSupportSet().contains(varPar.getName())){
			if(childList.containsKey(parentToChild.get(constPar.getName()))){
				ChildCharacter currentChild = childList.get(parentToChild.get(constPar.getName()));
				currentChild.setVarParent(varPar.getName());
			//child's growth rate
				double[] basegr = currentChild.getBaseGrowthRates();
				double[] vargr = varPar.getGrowthRates();
				double[] finalgr = new double[8];
				for(int i = 0;i<basegr.length;i++){
					finalgr[i] = (basegr[i] + vargr[i])/2;
				}
				currentChild.setGrowthRates(finalgr);
			//child's stat modifiers
				int[] constms = constPar.getStatModifiers();
				int[] varms = varPar.getStatModifiers();
				int[] finalms = new int[7];
				for (int i = 0;i<finalms.length;i++){
					if(firstGenList.containsKey(varPar.getName())){
						finalms[i]=constms[i]+varms[i]+1;
					} else{
						finalms[i]=constms[i]+varms[i];
					}
				}
				currentChild.setStatModifiers(finalms);
			//child's pairup stats
				int[][] constpu = constPar.getPairupStats();
				int[][] varpu = varPar.getPairupStats();
				int[][] finalpu = new int[4][7];
				for (int i=0;i<finalpu.length;i++){
					if(currentChild.getName().equals("Shigure")||currentChild.getName().equals("KanaM")){
						if(i==0 || i==2){
							finalpu[i][0] = varpu[i][0];
							finalpu[i][1] = varpu[i][1];
							finalpu[i][2] = varpu[i][2];
							finalpu[i][3] = varpu[i][3];
							finalpu[i][4] = varpu[i][4];
							finalpu[i][5] = varpu[i][5];
							finalpu[i][6] = varpu[i][6];
						} else {
							finalpu[i][0] = constpu[i][0];
							finalpu[i][1] = constpu[i][1];
							finalpu[i][2] = constpu[i][2];
							finalpu[i][3] = constpu[i][3];
							finalpu[i][4] = constpu[i][4];
							finalpu[i][5] = constpu[i][5];
							finalpu[i][6] = constpu[i][6];
						}
					}else {
						if(i==0 || i==2){
							finalpu[i][0] = constpu[i][0];
							finalpu[i][1] = constpu[i][1];
							finalpu[i][2] = constpu[i][2];
							finalpu[i][3] = constpu[i][3];
							finalpu[i][4] = constpu[i][4];
							finalpu[i][5] = constpu[i][5];
							finalpu[i][6] = constpu[i][6];
						} else {
							finalpu[i][0] = varpu[i][0];
							finalpu[i][1] = varpu[i][1];
							finalpu[i][2] = varpu[i][2];
							finalpu[i][3] = varpu[i][3];
							finalpu[i][4] = varpu[i][4];
							finalpu[i][5] = varpu[i][5];
							finalpu[i][6] = varpu[i][6];
						}
					}
				} 
				currentChild.setPairupStats(finalpu);
			//child's class set
				String[] childClassSet = new String[5];
				String[] constClassSet;
				String[] varClassSet;
				System.arraycopy(currentChild.getClassSet(), 0, childClassSet, 0, currentChild.getClassSet().length);
				if(cp.equals("Azura") || cp.equals("AvatarF")){
					varClassSet = constPar.getClassSet();
					constClassSet = varPar.getClassSet();
				} else {
					constClassSet = constPar.getClassSet();
					varClassSet = varPar.getClassSet();
				}
				String tempClass = new String();
				String tempClass2 = new String();
				tempClass = constClassSet[0];
				if(tempClass.equals("TroubadourM") && (currentChild.getSupportSet().contains("AvatarM") || currentChild.getName().equals("KanaF"))){
					tempClass = "TroubadourF";
				} else if (tempClass.equals("TroubadourF") && (currentChild.getSupportSet().contains("AvatarF") || currentChild.getName().equals("KanaM"))){
					tempClass = "TroubadourM";
				} else if(tempClass.equals("Monk") && (currentChild.getSupportSet().contains("AvatarM") || currentChild.getName().equals("KanaF"))){
					tempClass = "Shrine Maiden";
				} else if(tempClass.equals("Shrine Maiden") && (currentChild.getSupportSet().contains("AvatarF") || currentChild.getName().equals("KanaM"))){
					tempClass = "Monk";
				} else if(tempClass.equals("Nohr Prince") && (currentChild.getSupportSet().contains("AvatarM") || currentChild.getName().equals("KanaF"))){
					tempClass = "Nohr Princess";
				} else if(tempClass.equals("Nohr Princess") && (currentChild.getSupportSet().contains("AvatarF") || currentChild.getName().equals("KanaM"))){
					tempClass = "Nohr Prince";
				} 
				if (childClassSet[0].equals(tempClass)){
					childClassSet[1] = constClassSet[1];
				} else if(!constClassSet[0].equals("Songstress") ){
					childClassSet[1] = tempClass;
				} else{
					childClassSet[1] = classList.get(constClassSet[0]).getParallelClass();
				}
				if(childClassSet[1].equals("TroubadourM") && (currentChild.getSupportSet().contains("AvatarM") || currentChild.getName().equals("KanaF"))){
					childClassSet[1] = "TroubadourF";
				} else if (childClassSet[1].equals("TroubadourF") && (currentChild.getSupportSet().contains("AvatarF") || currentChild.getName().equals("KanaM"))){
					childClassSet[1] = "TroubadourM";
				} else if(childClassSet[1].equals("Monk") && (currentChild.getSupportSet().contains("AvatarM") || currentChild.getName().equals("KanaF"))){
					childClassSet[1] = "Shrine Maiden";
				} else if(childClassSet[1].equals("Shrine Maiden") && (currentChild.getSupportSet().contains("AvatarF") || currentChild.getName().equals("KanaM"))){
					childClassSet[1] = "Monk";
				} else if(childClassSet[1].equals("Nohr Prince") && (currentChild.getSupportSet().contains("AvatarM") || currentChild.getName().equals("KanaF"))){
					childClassSet[1] = "Nohr Princess";
				} else if(childClassSet[1].equals("Nohr Princess") && (currentChild.getSupportSet().contains("AvatarF")|| currentChild.getName().equals("KanaM"))){
					childClassSet[1] = "Nohr Prince";
				} 
				
				
				tempClass = varClassSet[0];
				tempClass2 = varClassSet[1];
				if(tempClass.equals("TroubadourM") && currentChild.getSupportSet().contains("AvatarM")){
					tempClass = "TroubadourF";
				} else if (tempClass.equals("TroubadourF") && currentChild.getSupportSet().contains("AvatarF")){
					tempClass = "TroubadourM";
				} else if(tempClass.equals("Monk") && currentChild.getSupportSet().contains("AvatarM")){
					tempClass = "Shrine Maiden";
				} else if(tempClass.equals("Shrine Maiden") && currentChild.getSupportSet().contains("AvatarF")){
					tempClass = "Monk";
				} else if(tempClass.equals("Nohr Prince") && ((currentChild.getSupportSet().contains("AvatarM")) || currentChild.getName().equals("KanaF"))){
					tempClass = "Nohr Princess";
				} else if(tempClass.equals("Nohr Princess") && ((currentChild.getSupportSet().contains("AvatarF")) || currentChild.getName().equals("KanaM"))){
					tempClass = "Nohr Prince";
				} 
				if(tempClass2.equals("TroubadourM") && currentChild.getSupportSet().contains("AvatarM")){
					tempClass2 = "TroubadourF";
				} else if (tempClass2.equals("TroubadourF") && currentChild.getSupportSet().contains("AvatarF")){
					tempClass2 = "TroubadourM";
				} else if(tempClass2.equals("Monk") && currentChild.getSupportSet().contains("AvatarM")){
					tempClass2 = "Shrine Maiden";
				} else if(tempClass2.equals("Shrine Maiden") && currentChild.getSupportSet().contains("AvatarF")){
					tempClass2 = "Monk";
				} else if(tempClass2.equals("Nohr Prince") && ((currentChild.getSupportSet().contains("AvatarM")) || currentChild.getName().equals("KanaF"))){
					tempClass2 = "Nohr Princess";
				} else if(tempClass2.equals("Nohr Princess") && ((currentChild.getSupportSet().contains("AvatarF")) || currentChild.getName().equals("KanaM"))){
					tempClass2 = "Nohr Prince";
				} 
				if(!childClassSet[0].equals(tempClass) && !childClassSet[1].equals(tempClass) && !tempClass.equals("Songstress")){
					childClassSet[2] = tempClass;
				} else if (!childClassSet[0].equals(tempClass2) && !childClassSet[1].equals(tempClass2)){
					childClassSet[2] = tempClass2;
				} else if (tempClass.equals("Songstress")){
					if(childClassSet[0].equals("TroubadourF") || childClassSet[1].equals("TroubadourF") ||
							childClassSet[0].equals("TroubadourM") || childClassSet[1].equals("TroubadourM")){
						childClassSet[2] = classList.get(tempClass2).getParallelClass();
					} else {
						childClassSet[2] = classList.get(tempClass).getParallelClass();
					}
				}else if  (tempClass.equals("Nohr Prince") || tempClass.equals("Nohr Princess")){
					if(childClassSet[0].equals(tempClass) || childClassSet[1].equals(tempClass)){
						if(childClassSet[0].equals(tempClass2) || childClassSet[1].equals(tempClass2)){
							childClassSet[2] = classList.get(tempClass2).getParallelClass();
						} else {
							childClassSet[2] = tempClass2;
						}
					} else {
						childClassSet[2] = tempClass;
					}
				
				} else {
					childClassSet[2] = classList.get(tempClass).getParallelClass();
				}
				if(childClassSet[2].equals("TroubadourM") && currentChild.getSupportSet().contains("AvatarM")){
					childClassSet[2] = "TroubadourF";
				} else if (childClassSet[2].equals("TroubadourF") && currentChild.getSupportSet().contains("AvatarF")){
					childClassSet[2] = "TroubadourM";
				} else if(childClassSet[2].equals("Monk") && currentChild.getSupportSet().contains("AvatarM")){
					childClassSet[2] = "Shrine Maiden";
				} else if(childClassSet[2].equals("Shrine Maiden") && currentChild.getSupportSet().contains("AvatarF")){
					childClassSet[2] = "Monk";
				} else if(childClassSet[2].equals("Nohr Prince") && ((currentChild.getSupportSet().contains("AvatarM")) || currentChild.getName().equals("KanaF"))){
					childClassSet[2] = "Nohr Princess";
				} else if(childClassSet[2].equals("Nohr Princess") && ((currentChild.getSupportSet().contains("AvatarF")) || currentChild.getName().equals("KanaM"))){
					childClassSet[2] = "Nohr Prince";
				} 
				currentChild.setClassSet(childClassSet);
				currentChild.setSkillSet(updateSkills(currentChild.getClassSet()));
			}
			
		//varpar partner seal class
			String[] constClassSet = constPar.getClassSet();
			String[] varClassSet = varPar.getClassSet();
			String tempClass = new String();
			String tempClass2 = new String();
			
			tempClass = constClassSet[0];
			if(constPar.getName().equals("Shigure")){
				tempClass2 = "TroubadourM";
			} else if (constPar.getName().equals("KanaM")){
				tempClass2 = firstGenList.get("AvatarF").getClassSet()[1];
			} else {
				tempClass2 = constClassSet[1];
			}
			if(tempClass.equals("TroubadourM") && (varPar.getSupportSet().contains("AvatarM") || varPar.getName().equals("AvatarF"))){
				tempClass = "TroubadourF";
			} else if (tempClass.equals("TroubadourF") && (varPar.getSupportSet().contains("AvatarF") || varPar.getName().equals("AvatarM"))){
				tempClass = "TroubadourM";
			} else if(tempClass.equals("Monk") && (varPar.getSupportSet().contains("AvatarM") || varPar.getName().equals("AvatarF"))){
				tempClass = "Shrine Maiden";
			} else if(tempClass.equals("Shrine Maiden") && (varPar.getSupportSet().contains("AvatarF") || varPar.getName().equals("AvatarM"))){
				tempClass = "Monk";
			} else if(tempClass.equals("Nohr Prince") && (varPar.getSupportSet().contains("AvatarM")|| varPar.getName().equals("AvatarF"))){
				tempClass = "Nohr Princess";
			} else if(tempClass.equals("Nohr Princess") && (varPar.getSupportSet().contains("AvatarF")|| varPar.getName().equals("AvatarM"))){
				tempClass = "Nohr Prince";
			} 
			if(tempClass2.equals("TroubadourM") && (varPar.getSupportSet().contains("AvatarM")|| varPar.getName().equals("AvatarF"))){
				tempClass2 = "TroubadourF";
			} else if (tempClass.equals("TroubadourF") && (varPar.getSupportSet().contains("AvatarF")|| varPar.getName().equals("AvatarM"))){
				tempClass2 = "TroubadourM";
			} else if(tempClass2.equals("Monk") && (varPar.getSupportSet().contains("AvatarM")|| varPar.getName().equals("AvatarF"))){
				tempClass2 = "Shrine Maiden";
			} else if(tempClass2.equals("Shrine Maiden") && (varPar.getSupportSet().contains("AvatarF")|| varPar.getName().equals("AvatarM"))){
				tempClass2 = "Monk";
			} else if(tempClass2.equals("Nohr Prince") && (varPar.getSupportSet().contains("AvatarM")|| varPar.getName().equals("AvatarF"))){
				tempClass2 = "Nohr Princess";
			} else if(tempClass2.equals("Nohr Princess") && (varPar.getSupportSet().contains("AvatarF")|| varPar.getName().equals("AvatarM"))){
				tempClass2 = "Nohr Prince";
			} 
			int i = 0;
			if(!varClassSet[0].equals(tempClass) && !tempClass.equals("Nohr Prince") && !tempClass.equals("Nohr Princess")
					&& !tempClass.equals("Songstress") && !tempClass.equals("Kitsune") && !tempClass.equals("Wolfskin")
					&& !tempClass.equals("Villager")){
				while(i<3 && !varClassSet[i].equals(tempClass)){
					i++;
				}
				varClassSet[i] = tempClass;
			} else if(!varClassSet[0].equals(tempClass2)){
				while(i<3 && !varClassSet[i].equals(tempClass2)){
					i++;
				}
				varClassSet[i] = tempClass2;
			} else if(constPar.getName().equals("AvatarM") || constPar.getName().equals("AvatarF")|| varPar.getName().equals("KanaF")|| constPar.getName().equals("KanaM")){
				while(i<3){
					i++;
				}
				varClassSet[i] = classList.get(tempClass2).getParallelClass();
			} else {
				while(i<3){
					i++;
				}
				if(varPar.getSupportSet().contains("AvatarF") && classList.get(tempClass).getParallelClass().equals("TroubadourF")){
					varClassSet[i] = "TroubadourM";
				} else {
					varClassSet[i] = classList.get(tempClass).getParallelClass();
				}
			}
			varPar.setClassSet(varClassSet);
			
		//constpar partner seal class	
			tempClass = varClassSet[0];
			tempClass2 = varClassSet[1];
			if(tempClass.equals("TroubadourM") && (constPar.getSupportSet().contains("AvatarM")|| constPar.getName().equals("AvatarF"))){
				tempClass = "TroubadourF";
			} else if (tempClass.equals("TroubadourF") && (constPar.getSupportSet().contains("AvatarF")|| constPar.getName().equals("AvatarM"))){
				tempClass = "TroubadourM";
			} else if(tempClass.equals("Monk") && (constPar.getSupportSet().contains("AvatarM")|| constPar.getName().equals("AvatarF"))){
				tempClass = "Shrine Maiden";
			} else if(tempClass.equals("Shrine Maiden") && (constPar.getSupportSet().contains("AvatarF")|| constPar.getName().equals("AvatarM"))){
				tempClass = "Monk";
			} else if(tempClass.equals("Nohr Prince") && (constPar.getSupportSet().contains("AvatarM")|| constPar.getName().equals("AvatarF"))){
				tempClass = "Nohr Princess";
			} else if(tempClass.equals("Nohr Princess") && (constPar.getSupportSet().contains("AvatarF")|| constPar.getName().equals("AvatarM"))){
				tempClass = "Nohr Prince";
			} 
			if(tempClass2.equals("TroubadourM") && (constPar.getSupportSet().contains("AvatarM")|| constPar.getName().equals("AvatarF"))){
				tempClass2 = "TroubadourF";
			} else if (tempClass2.equals("TroubadourF") && (constPar.getSupportSet().contains("AvatarF")|| constPar.getName().equals("AvatarM"))){
				tempClass2 = "TroubadourM";
			} else if(tempClass2.equals("Monk") && (constPar.getSupportSet().contains("AvatarM")|| constPar.getName().equals("AvatarF"))){
				tempClass2 = "Shrine Maiden";
			} else if(tempClass2.equals("Shrine Maiden") && (constPar.getSupportSet().contains("AvatarF")|| constPar.getName().equals("AvatarM"))){
				tempClass2 = "Monk";
			} else if(tempClass2.equals("Nohr Prince") && (constPar.getSupportSet().contains("AvatarM")|| constPar.getName().equals("AvatarF"))){
				tempClass2 = "Nohr Princess";
			} else if(tempClass2.equals("Nohr Princess") && (constPar.getSupportSet().contains("AvatarF")|| constPar.getName().equals("AvatarM"))){
				tempClass2 = "Nohr Prince";
			} 
			i = 0;
			if(!constClassSet[0].equals(tempClass) && !tempClass.equals("Nohr Prince") && !tempClass.equals("Nohr Princess")
					&& !tempClass.equals("Songstress") && !tempClass.equals("Kitsune") && !tempClass.equals("Wolfskin")
					&& !tempClass.equals("Villager")){
				while(i<3 && !constClassSet[i].equals(tempClass)){
					i++;
				}
				constClassSet[i] = tempClass;
			} else if(!constClassSet[0].equals(tempClass2)){
				while(i<3 && !constClassSet[i].equals(tempClass2)){
					i++;
				}
				constClassSet[i] = tempClass2;
			} else if(varPar.getName().equals("AvatarM") || varPar.getName().equals("AvatarF")|| varPar.getName().equals("KanaF")|| varPar.getName().equals("KanaM")){
				while(i<3){
					i++;
				}
				constClassSet[i] = classList.get(tempClass2).getParallelClass();
			} else {
				while(i<3){
					i++;
				}
				if(constPar.getSupportSet().contains("AvatarF") && classList.get(tempClass).getParallelClass().equals("TroubadourF")){
					constClassSet[i] = "TroubadourM";
				} else {
					constClassSet[i] = classList.get(tempClass).getParallelClass();
				}
			}
			constPar.setClassSet(constClassSet);
			constPar.setSkillSet(updateSkills(constPar.getClassSet()));
			varPar.setSkillSet(updateSkills(varPar.getClassSet()));
		} 
	}
	
	
	private HashMap<String, ChildCharacter> initializeChildren() {
		HashMap<String, ChildCharacter> children = new HashMap<String,ChildCharacter>();
		children.put("KanaF", new ChildCharacter("KanaF","AvatarM",new double[] {30,35,30,40,45,45,25,25},
				new HashSet<String>(Arrays.asList("Shigure","Dwyer","Shiro","Kiragi","Asugi","Hisame","Siegbert",
						"Forrest","Ignatius","Percy")),"Nohr Princess" ));
		children.put("KanaM", new ChildCharacter("KanaM","AvatarF",new double[] {30,35,30,40,45,45,25,25},
				new HashSet<String>(Arrays.asList("Sophie","Midori","Selkie","Mitama","Caeldori","Rhajat","Velouria",
						"Ophelia","Soleil","Nina")),"Nohr Prince" ));
		children.put("Shigure", new ChildCharacter("Shigure","Azura",new double[] {35,45,5,45,35,25,35,25},
				new HashSet<String>(Arrays.asList("AvatarF","KanaF","Sophie","Midori","Selkie","Mitama","Caeldori",
						"Rhajat","Velouria","Ophelia","Soleil","Nina")),"Sky Knight" ));
		children.put("Dwyer", new ChildCharacter("Dwyer","Jakob",new double[] {45,45,30,20,30,30,30,35},
				new HashSet<String>(Arrays.asList("AvatarF","KanaF","Sophie","Midori","Selkie","Mitama","Caeldori",
						"Rhajat","Velouria","Ophelia","Soleil","Nina")),"TroubadourM" ));
		children.put("Sophie", new ChildCharacter("Sophie","Silas",new double[] {35,35,10,55,50,35,25,35},
				new HashSet<String>(Arrays.asList("AvatarM","KanaM","Shigure","Dwyer","Shiro","Kiragi","Asugi",
						"Hisame","Siegbert","Forrest","Ignatius","Percy")),"Cavalier" ));
		children.put("Midori", new ChildCharacter("Midori","Kaze",new double[] {45,35,5,55,35,50,30,20},
				new HashSet<String>(Arrays.asList("AvatarM","KanaM","Shigure","Dwyer","Shiro","Kiragi","Asugi",
						"Hisame","Siegbert","Forrest","Ignatius","Percy")),"Apothecary" ));
		children.put("Shiro", new ChildCharacter("Shiro","Ryoma",new double[] {50,50,0,40,35,35,45,30},
				new HashSet<String>(Arrays.asList("AvatarF","KanaF","Sophie","Midori","Selkie","Mitama","Caeldori",
						"Rhajat","Ophelia","Nina")),"Spear Fighter" ));
		children.put("Kiragi", new ChildCharacter("Kiragi","Takumi",new double[] {45,40,0,45,50,45,40,15},
				new HashSet<String>(Arrays.asList("AvatarF","KanaF","Sophie","Midori","Selkie","Mitama","Caeldori",
						"Rhajat","Velouria","Soleil")),"Archer" ));
		children.put("Asugi", new ChildCharacter("Asugi","Saizo",new double[] {40,45,50,55,45,50,30,20},
				new HashSet<String>(Arrays.asList("AvatarF","KanaF","Sophie","Midori","Selkie","Mitama","Caeldori",
						"Rhajat","Soleil","Nina")),"Ninja" ));
		children.put("Selkie", new ChildCharacter("Selkie","Kaden",new double[] {35,30,15,35,55,60,30,50},
				new HashSet<String>(Arrays.asList("AvatarM","KanaM","Shigure","Dwyer","Shiro","Kiragi","Asugi",
						"Hisame","Forrest","Ignatius")),"Kitsune" ));
		children.put("Hisame", new ChildCharacter("Hisame","Hinata",new double[] {50,40,0,40,40,25,30,20},
				new HashSet<String>(Arrays.asList("AvatarF","KanaF","Sophie","Midori","Selkie","Mitama","Caeldori",
						"Rhajat","Velouria","Ophelia")),"Samurai" ));
		children.put("Mitama", new ChildCharacter("Mitama","Azama",new double[] {45,40,35,45,50,50,30,20},
				new HashSet<String>(Arrays.asList("AvatarM","KanaM","Shigure","Dwyer","Shiro","Kiragi","Asugi",
						"Hisame","Siegbert","Percy")),"Shrine Maiden" ));
		children.put("Caeldori", new ChildCharacter("Caeldori","Subaki",new double[] {55,35,15,40,40,45,35,20},
				new HashSet<String>(Arrays.asList("AvatarM","KanaM","Shigure","Dwyer","Shiro","Kiragi","Asugi",
						"Hisame","Siegbert","Ignatius")),"Sky Knight" ));
		children.put("Rhajat", new ChildCharacter("Rhajat","Hayato",new double[] {40,15,60,10,50,30,25,35},
				new HashSet<String>(Arrays.asList("AvatarM","KanaM","Shigure","Dwyer","Shiro","Kiragi","Asugi",
						"Hisame","Forrest","Percy")),"Diviner" ));
		children.put("Siegbert", new ChildCharacter("Siegbert","Xander",new double[] {40,45,5,45,45,45,35,20},
				new HashSet<String>(Arrays.asList("AvatarF","KanaF","Sophie","Midori","Mitama","Caeldori","Velouria",
						"Ophelia","Soleil","Nina")),"Cavalier" ));
		children.put("Forrest", new ChildCharacter("Forrest","Leo",new double[] {55,15,65,20,35,25,25,55},
				new HashSet<String>(Arrays.asList("AvatarF","KanaF","Sophie","Midori","Selkie","Rhajat","Velouria",
						"Ophelia","Soleil","Nina")),"TroubadourM" ));
		children.put("Ignatius", new ChildCharacter("Ignatius","Benny",new double[] {40,50,0,40,30,55,45,35},
				new HashSet<String>(Arrays.asList("AvatarF","KanaF","Sophie","Midori","Selkie","Caeldori","Velouria",
						"Ophelia","Soleil","Nina")),"Knight" ));
		children.put("Velouria", new ChildCharacter("Velouria","Keaton",new double[] {50,50,0,40,40,35,45,30},
				new HashSet<String>(Arrays.asList("AvatarM","KanaM","Shigure","Dwyer","Kiragi","Hisame","Siegbert",
						"Forrest","Ignatius","Percy")),"Wolfskin" ));
		children.put("Percy", new ChildCharacter("Percy","Arthur",new double[] {30,30,5,45,40,75,55,15},
				new HashSet<String>(Arrays.asList("AvatarF","KanaF","Sophie","Midori","Mitama","Rhajat","Velouria",
						"Ophelia","Soleil","Nina")),"Wyvern Rider" ));
		children.put("Ophelia", new ChildCharacter("Ophelia","Odin",new double[] {45,15,45,40,45,65,20,30},
				new HashSet<String>(Arrays.asList("AvatarM","KanaM","Shigure","Dwyer","Shiro","Hisame","Siegbert",
						"Forrest","Ignatius","Percy")),"Dark Mage" ));
		children.put("Soleil", new ChildCharacter("Soleil","Laslow",new double[] {25,60,0,35,35,45,35,40},
				new HashSet<String>(Arrays.asList("AvatarM","KanaM","Shigure","Dwyer","Kiragi","Asugi","Siegbert",
						"Forrest","Ignatius","Percy")),"Mercenary" ));
		children.put("Nina", new ChildCharacter("Nina","Niles",new double[] {30,45,30,35,40,50,25,45},
				new HashSet<String>(Arrays.asList("AvatarM","KanaM","Shigure","Dwyer","Shiro","Asugi","Siegbert",
						"Forrest","Ignatius","Percy")),"Outlaw" ));
		return children;

	}

	
	private HashMap<String, FirstGenCharacter> initializeCharacters() {
		HashMap<String, FirstGenCharacter> unitList = new HashMap<String, FirstGenCharacter>();
		unitList.put("AvatarM", new AvatarUnit("AvatarM")); 
		unitList.put("AvatarF", new AvatarUnit("AvatarF")); 
		unitList.put("Gunter", new FirstGenCharacter("Gunter", 
				new double[] {15,5,0,5,0,15,5,5}, 
				new int[] {2,0,1,-2,0,2,-2}, 
				new int[][] {{0,0,0,0,0,1,0},{1,0,0,0,0,0,0},{0,0,1,0,0,0,0},{1,0,0,0,0,1,0}}, 
				new String[] {"Cavalier","Mercenary","Wyvern Rider","",""},
				new HashSet<String>(Arrays.asList("AvatarF"))));
		unitList.put("Felicia",  new FirstGenCharacter("Felicia", 
				new double[] {40,10,35,30,40,55,15,35},
				new int[] {-2,2,0,1,0,-1,1},
				new int[][] {{0,0,0,1,0,0,0},{0,0,0,0,0,0,1},{0,1,0,0,0,0,0},{0,0,0,1,0,0,1}},
				new String[] {"TroubadourF", "Mercenary","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Subaki","Silas","Saizo","Azama",
						"Hayato","Hinata","Takumi","Kaden","Ryoma","Arthur","Odin","Niles","Laslow",
						"Benny","Leo","Keaton","Xander"))));
		unitList.put("Jakob",  new FirstGenCharacter("Jakob", 
				new double[] {50,30,15,40,35,45,25,25},
				new int[] {2,-2,2,0,-1,0,-1},
				new int[][] {{1,0,0,0,0,0,0},{0,0,0,1,0,0,0},{0,0,1,0,0,0,0},{1,0,1,0,0,0,0}},
				new String[] {"TroubadourM", "Cavalier","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Rinkah","Sakura","Azura","Hana","Orochi",
						"Hinoka","Setsuna","Oboro","Kagero","Elise","Effie","Nyx","Camilla","Selena","Beruka",
						"Peri","Charlotte","Mozu")) ));
		unitList.put("Rinkah",  new FirstGenCharacter("Rinkah", 
				new double[] {20,25,15,50,45,35,45,20},
				new int[] {-1,0,-2,1,0,2,0},
				new int[][] {{0,0,0,0,0,1,0},{0,0,0,1,0,0,0},{1,0,0,0,0,0,0},{0,0,0,1,0,1,0}},
				new String[] {"Oni Savage", "Ninja","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Subaki","Silas","Saizo","Azama",
						"Hayato","Hinata","Takumi","Kaden","Ryoma","Benny","Keaton")) ));
		unitList.put("Kaze",  new FirstGenCharacter("Kaze", 
				new double[] {55,40,0,45,65,20,20,35},
				new int[] {-2,0,2,3,-2,-1,1},
				new int[][] {{0,0,0,1,0,0,0},{0,0,1,0,0,0,0},{0,0,0,0,0,0,1},{0,0,1,1,0,0,0}}, 
				new String[] {"Ninja", "Samurai","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Rinkah","Sakura","Azura","Hana","Orochi",
						"Hinoka","Setsuna","Oboro","Kagero","Elise","Effie","Nyx","Camilla","Selena","Beruka",
						"Peri","Charlotte","Mozu")) ));
		unitList.put("Sakura",  new FirstGenCharacter("Sakura", 
				new double[] {45,30,50,40,40,55,30,20},
				new int[] {0,2,-1,1,0,-1,0},
				new int[][] {{0,1,0,0,0,0,0},{0,0,0,1,0,0,0},{0,0,0,0,1,0,0},{0,0,0,1,0,0,1}},
				new String[] {"Shrine Maiden", "Sky Knight","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Subaki","Silas","Saizo","Azama",
						"Hayato","Hinata","Kaden","Leo","Xander")) ));
		unitList.put("Azura",  new FirstGenCharacter("Azura", 
				new double[] {25,50,25,60,60,40,15,35},
				new int[] {0,0,1,3,0,-3,0},
				new int[][] {{0,0,0,1,0,0,0},{0,0,1,0,0,0,0},{0,0,0,1,0,0,0},{0,0,0,1,1,0,0}},
				new String[] {"Songstress", "Sky Knight","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Subaki","Silas","Saizo","Azama",
						"Hayato","Hinata","Takumi","Kaden","Ryoma","Arthur","Odin","Niles","Laslow","Benny",
						"Leo","Keaton","Xander")) ));
		unitList.put("Hana",  new FirstGenCharacter("Hana", 
				new double[] {25,55,10,45,55,25,20,30},
				new int[] {1,0,1,2,-1,-3,1},
				new int[][] {{0,0,0,1,0,0,0},{1,0,0,0,0,0,0},{0,0,1,0,0,0,0},{1,0,0,1,0,0,0}},
				new String[] {"Samurai", "Shrine Maiden","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Subaki","Silas","Saizo","Azama",
						"Hayato","Hinata","Takumi","Kaden","Ryoma","Laslow","Keaton")) ));
		unitList.put("Subaki",  new FirstGenCharacter("Subaki", 
				new double[] {55,30,20,50,20,25,45,5},
				new int[] {-1,0,2,-2,-1,3,-1},
				new int[][] {{0,0,0,0,0,1,0},{0,0,1,0,0,0,0},{0,0,0,0,0,1,0},{0,0,1,0,0,0,1}},
				new String[] {"Sky Knight", "Samurai","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Rinkah","Sakura","Azura","Hana","Orochi",
						"Hinoka","Setsuna","Oboro","Kagero","Nyx","Selena","Mozu")) ));
		unitList.put("Silas",  new FirstGenCharacter("Silas", 
				new double[] {40,45,5,50,40,40,40,25},
				new int[] {1,0,2,0,-1,0,-1},
				new int[][] {{1,0,0,0,0,0,0},{0,0,1,0,0,0,0},{0,0,0,1,0,0,0},{1,0,0,0,0,1,0}},
				new String[] {"Cavalier", "Mercenary","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Rinkah","Sakura","Azura","Hana","Orochi",
						"Hinoka","Setsuna","Oboro","Kagero","Elise","Effie","Nyx","Camilla","Selena","Beruka",
						"Peri","Charlotte","Mozu")) ));
		unitList.put("Saizo",  new FirstGenCharacter("Saizo", 
				new double[] {40,50,45,60,30,55,45,10},
				new int[] {1,0,3,-2,0,1,-2},
				new int[][] {{0,0,0,0,1,0,0},{0,0,0,1,0,0,0},{0,0,1,0,0,0,0},{1,0,0,0,1,0,0}},
				new String[] {"Ninja", "Samurai","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Rinkah","Sakura","Azura","Hana","Orochi",
						"Hinoka","Setsuna","Oboro","Kagero","Beruka","Charlotte","Mozu")) ));
		unitList.put("Orochi",  new FirstGenCharacter("Orochi", 
				new double[] {35,5,65,50,15,35,25,45},
				new int[] {0,3,2,-2,-1,-2,1},
				new int[][] {{0,1,0,0,0,0,0},{0,0,1,0,0,0,0},{0,0,0,0,0,0,1},{0,1,0,0,0,0,1}},
				new String[] {"Diviner", "Apothecary","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Subaki","Silas","Saizo","Azama",
						"Hayato","Hinata","Takumi","Kaden","Ryoma","Odin","Laslow")) ));
		unitList.put("Hinoka",  new FirstGenCharacter("Hinoka", 
				new double[] {45,45,15,40,45,40,35,40},
				new int[] {1,-1,-1,1,0,-1,2},
				new int[][] {{0,0,0,0,0,0,1},{0,0,0,0,1,0,0},{1,0,0,0,0,0,0},{0,0,0,1,0,0,1}},
				new String[] {"Sky Knight", "Spear Fighter","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Subaki","Silas","Saizo","Azama",
						"Hayato","Hinata","Kaden","Leo","Xander")) ));
		unitList.put("Azama",  new FirstGenCharacter("Azama", 
				new double[] {55,50,20,40,45,40,40,20},
				new int[] {2,-3,0,1,0,1,0},
				new int[][] {{1,0,0,0,0,0,0},{0,0,0,1,0,0,0},{0,0,0,0,0,1,0},{1,0,0,0,1,0,0}},
				new String[] {"Monk", "Apothecary","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Rinkah","Sakura","Azura","Hana","Orochi",
						"Hinoka","Setsuna","Oboro","Kagero","Effie","Beruka","Mozu")) ));
		unitList.put("Setsuna",  new FirstGenCharacter("Setsuna", 
				new double[] {30,20,0,30,60,30,15,40},
				new int[] {0,0,1,3,-1,-1,-1},
				new int[][] {{0,0,0,1,0,0,0},{0,0,0,1,0,0,0},{0,0,0,0,0,0,1},{0,0,1,1,0,0,0}},
				new String[] {"Archer", "Ninja","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Subaki","Silas","Saizo","Azama",
						"Hayato","Hinata","Takumi","Kaden","Ryoma","Arthur","Niles")) ));
		unitList.put("Hayato",  new FirstGenCharacter("Hayato", 
				new double[] {50,30,40,30,45,60,40,20},
				new int[] {0,1,-1,2,1,-1,-1},
				new int[][] {{0,0,0,1,0,0,0},{0,0,0,0,1,0,0},{0,1,0,0,0,0,0},{0,0,0,1,1,0,0}},
				new String[] {"Diviner", "Oni Savage","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Rinkah","Sakura","Azura","Hana","Orochi",
						"Hinoka","Setsuna","Oboro","Kagero","Effie","Nyx","Mozu")) ));
		unitList.put("Oboro",  new FirstGenCharacter("Oboro", 
				new double[] {30,40,20,40,40,40,40,30},
				new int[] {1,-1,1,1,-1,1,-1},
				new int[][] {{1,0,0,0,0,0,0},{0,0,0,0,0,1,0},{0,0,0,1,0,0,0},{0,0,1,0,1,0,0}},
				new String[] {"Spear Fighter", "Apothecary","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Subaki","Silas","Saizo","Azama",
						"Hayato","Hinata","Takumi","Kden","Ryoma","Niles","Benny")) ));
		unitList.put("Hinata",  new FirstGenCharacter("Hinata", 
				new double[] {55,35,0,25,15,45,45,15},
				new int[] {1,0,-1,-2,0,2,0},
				new int[][] {{0,0,0,0,0,1,0},{1,0,0,0,0,0,0},{0,0,0,0,0,1,0},{1,0,0,0,1,0,0}},
				new String[] {"Samurai", "Oni Savage","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Rinkah","Sakura","Azura","Hana","Orochi",
						"Hinoka","Setsuna","Oboro","Kagero","Selena","Peri","Mozu")) ));
		unitList.put("Takumi",  new FirstGenCharacter("Takumi", 
				new double[] {50,35,0,60,40,45,35,20},
				new int[] {1,0,3,-2,1,0,-2},
				new int[][] {{0,0,1,0,0,0,0},{0,0,0,1,0,0,0},{1,0,0,0,0,0,0},{0,0,1,0,0,1,0}},
				new String[] {"Archer", "Spear Fighter","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Rinkah","Azura","Hana","Orochi",
						"Setsuna","Oboro","Kagero","Elise","Camilla","Mozu")) ));
		unitList.put("Kagero",  new FirstGenCharacter("Kagero", 
				new double[] {30,65,0,20,50,30,25,40},
				new int[] {3,0,-1,-1,0,-1,1},
				new int[][] {{1,0,0,0,0,0,0},{0,0,0,0,0,0,1},{1,0,0,0,0,0,0},{0,0,0,1,0,0,1}},
				new String[] {"Ninja", "Diviner","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Subaki","Silas","Saizo","Azama",
						"Hayato","Hinata","Takumi","Kaden","Ryoma","Arthur","Odin")) ));
		unitList.put("Reina",  new FirstGenCharacter("Reina", 
				new double[] {40,45,5,20,45,10,20,10},
				new int[] {2,0,0,2,-1,-2,-1},
				new int[][] {{0,0,0,1,0,0,0},{1,0,0,0,0,0,0},{0,0,0,1,0,0,0},{1,0,0,1,0,0,0}},
				new String[] {"Sky Knight", "Diviner", "Ninja","",""},
				new HashSet<String>(Arrays.asList("AvatarM")) ));
		unitList.put("Kaden",  new FirstGenCharacter("Kaden", 
				new double[] {45,40,10,25,45,50,35,40},
				new int[] {1,0,-3,2,1,-2,2},
				new int[][] {{0,0,0,1,0,0,0},{0,0,0,0,0,0,1},{0,0,0,0,1,0,0},{0,0,0,1,0,0,1}},
				new String[] {"Kitsune", "Diviner","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Rinkah","Sakura","Azura","Hana","Orochi",
						"Hinoka","Setsuna","Oboro","Kagero","Peri","Charlotte","Mozu")) ));
		unitList.put("Ryoma",  new FirstGenCharacter("Ryoma", 
				new double[] {50,45,0,50,45,40,35,25},
				new int[] {1,0,2,1,1,-2,-2},
				new int[][] {{0,0,0,1,0,0,0},{1,0,0,0,0,0,0},{0,0,1,0,0,0,0},{0,0,0,2,0,0,0}},
				new String[] {"Samurai", "Sky Knight","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Rinkah","Azura","Hana","Orochi","Setsuna",
						"Oboro","Kagero","Elise","Camilla","Mozu")) ));
		unitList.put("Scarlet",  new FirstGenCharacter("Scarlet", 
				new double[] {30,45,20,40,50,40,25,20},
				new int[] {2,0,0,1,-1,0,-2},
				new int[][] {{1,0,0,0,0,0,0},{0,0,0,1,0,0,0},{0,0,0,0,0,1,0},{1,0,1,0,0,0,0}},
				new String[] {"Wyvern Rider", "Outlaw", "Knight","",""},
				new HashSet<String>(Arrays.asList("AvatarM")) ));
		unitList.put("Shura",  new FirstGenCharacter("Shura", 
				new double[] {30,25,10,20,35,30,15,35},
				new int[] {-1,0,-1,3,-1,-2,2},
				new int[][] {{0,0,0,1,0,0,0},{0,0,0,0,0,0,1},{0,0,0,0,0,0,1},{0,0,0,1,0,0,1}},
				new String[] {"Outlaw", "Ninja", "Fighter","",""},
				new HashSet<String>(Arrays.asList("AvatarF")) ));
		unitList.put("Elise",  new FirstGenCharacter("Elise", 
				new double[] {30,5,65,25,55,70,15,40},
				new int[] {-1,3,-2,1,1,-3,1},
				new int[][] {{0,1,0,0,0,0,0},{0,1,0,0,0,0,0},{0,0,0,0,1,0,0},{0,1,0,1,0,0,0}},
				new String[] {"TroubadourF", "Wyvern Rider","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Silas","Takumi","Ryoma","Arthur",
						"Odin","Niles","Laslow","Benny","Keaton")) ));
		unitList.put("Arthur",  new FirstGenCharacter("Arthur", 
				new double[] {50,45,0,55,35,5,45,20},
				new int[] {1,0,3,0,-3,1,-1},
				new int[][] {{0,0,1,0,0,0,0},{1,0,0,0,0,0,0},{1,0,0,0,0,0,0},{0,0,2,0,0,0,0}},
				new String[] {"Fighter", "Cavalier","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Azura","Setsuna","Kagero","Elise","Effie",
						"Nyx","Camilla","Selena","Beruka","Peri","Charlotte","Mozu")) ));
		unitList.put("Effie",  new FirstGenCharacter("Effie", 
				new double[] {35,60,0,35,50,50,35,30},
				new int[] {3,0,-1,1,0,-1,-1},
				new int[][] {{1,0,0,0,0,0,0},{0,0,0,0,1,0,0},{0,0,0,1,0,0,0},{2,0,0,0,0,0,0}},
				new String[] {"Knight", "TroubadourF","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Silas","Azama","Hayato","Arthur",
						"Odin","Niles","Laslow","Benny","Leo","Keaton","Xander")) ));
		unitList.put("Odin",  new FirstGenCharacter("Odin", 
				new double[] {55,35,30,55,35,60,40,20},
				new int[] {0,1,1,-1,1,0,-1},
				new int[][] {{0,1,0,0,0,0,0},{1,0,0,0,0,0,0},{0,0,0,0,1,0,0},{0,1,1,0,0,0,0}},
				new String[] {"Dark Mage", "Samurai","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Azura","Orochi","Kagero","Elise","Effie",
						"Nyx","Camilla","Selena","Beruka","Peri","Charlotte","Mozu")) ));
		unitList.put("Niles",  new FirstGenCharacter("Niles", 
				new double[] {40,35,20,40,50,30,30,40},
				new int[] {-2,0,-1,3,0,0,1},
				new int[][] {{0,0,0,1,0,0,0},{0,0,0,1,0,0,0},{0,0,0,0,0,0,1},{0,1,0,0,0,0,1}},
				new String[] {"Outlaw", "Dark Mage","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Azura","Setsuna","Oboro","Elise","Effie",
						"Nyx","Camilla","Selena","Beruka","Peri","Charlotte","Mozu")) ));
		unitList.put("Nyx",  new FirstGenCharacter("Nyx", 
				new double[] {30,5,50,35,50,20,15,30},
				new int[] {0,3,-2,2,-1,-2,1},
				new int[][] {{0,1,0,0,0,0,0},{0,0,0,1,0,0,0},{0,1,0,0,0,0,0},{0,1,0,1,0,0,0}},
				new String[] {"Dark Mage", "Outlaw","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Subaki","Silas","Hayato","Arthur",
						"Odin","Niles","Laslow","Benny","Leo","Keaton","Xander")) ));
		unitList.put("Camilla",  new FirstGenCharacter("Camilla", 
				new double[] {40,50,25,50,55,25,35,45},
				new int[] {1,-1,1,1,-2,1,0},
				new int[][] {{0,0,0,1,0,0,0},{0,0,0,0,0,0,1},{1,0,0,0,0,0,0},{1,0,0,0,0,1,0}},
				new String[] {"Wyvern Rider", "Dark Mage","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Silas","Takumi","Ryoma","Arthur",
						"Odin","Niles","Laslow","Benny","Keaton")) ));
		unitList.put("Selena",  new FirstGenCharacter("Selena", 
				new double[] {40,30,5,25,45,30,45,30},
				new int[] {-1,0,-1,2,0,1,0},
				new int[][] {{0,0,0,1,0,0,0},{0,0,0,0,0,1,0},{0,0,1,0,0,0,0},{1,0,0,1,0,0,0}},
				new String[] {"Mercenary", "Sky Knight","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Subaki","Silas","Hinata","Arthur",
						"Odin","Niles","Laslow","Benny","Leo","Keaton","Xander")) ));
		unitList.put("Beruka",  new FirstGenCharacter("Beruka", 
				new double[] {45,30,10,55,30,45,40,25},
				new int[] {-1,0,2,-2,0,2,-1},
				new int[][] {{0,0,1,0,0,0,0},{0,0,0,0,0,1,0},{0,0,1,0,0,0,0},{0,0,0,0,1,1,0}},
				new String[] {"Wyvern Rider", "Fighter","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Silas","Saizo","Azama","Arthur",
						"Odin","Niles","Laslow","Benny","Leo","Keaton","Xander")) ));
		unitList.put("Laslow",  new FirstGenCharacter("Laslow", 
				new double[] {50,45,0,45,30,55,35,25},
				new int[] {1,0,2,-1,1,-1,-1},
				new int[][] {{0,0,1,0,0,0,0},{1,0,0,0,0,0,0},{0,0,0,0,1,0,0},{1,0,0,0,1,0,0}},
				new String[] {"Mercenary", "Ninja","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Azura","Hana","Orochi","Elise","Effie",
						"Nyx","Camilla","Selena","Beruka","Peri","Charlotte","Mozu")) ));
		unitList.put("Peri",  new FirstGenCharacter("Peri", 
				new double[] {30,50,5,30,50,35,25,45},
				new int[] {1,0,-1,1,0,-2,2},
				new int[][] {{1,0,0,0,0,0,0},{0,0,0,1,0,0,0},{0,0,0,0,0,0,1},{1,0,0,1,0,0,0}},
				new String[] {"Cavalier", "Dark Mage","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Silas","Hinata","Kaden","Arthur",
						"Odin","Niles","Laslow","Benny","Leo","Keaton","Xander")) ));
		unitList.put("Benny",  new FirstGenCharacter("Benny", 
				new double[] {50,40,0,50,10,35,55,45},
				new int[] {0,0,0,-3,0,3,1},
				new int[][] {{0,0,0,0,0,1,0},{0,0,1,0,0,0,0},{1,0,0,0,0,0,0},{1,0,0,0,0,1,0}},
				new String[] {"Knight", "Fighter","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Rinkah","Azura","Oboro","Elise","Effie",
						"Nyx","Camilla","Selena","Beruka","Peri","Charlotte","Mozu")) ));
		unitList.put("Charlotte",  new FirstGenCharacter("Charlotte", 
				new double[] {65,55,0,35,50,45,20,5},
				new int[] {3,0,0,2,0,-2,-2},
				new int[][] {{1,0,0,0,0,0,0},{1,0,0,0,0,0,0},{0,0,0,1,0,0,0},{1,0,0,1,0,0,0}},
				new String[] {"Fighter", "TroubadourF","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Silas","Saizo","Kaden","Arthur",
						"Odin","Niles","Laslow","Benny","Leo","Keaton","Xander")) ));
		unitList.put("Leo",  new FirstGenCharacter("Leo", 
				new double[] {45,25,55,35,45,45,30,45},
				new int[] {-2,2,0,-2,0,0,2},
				new int[][] {{0,1,0,0,0,0,0},{0,0,0,0,0,0,1},{0,0,0,1,0,0,0},{0,1,0,0,1,0,0}},
				new String[] {"Dark Mage", "TroubadourM","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Sakura","Azura","Hinoka","Effie","Nyx",
						"Selena","Beruka","Peri","Charlotte","Mozu")) ));
		unitList.put("Keaton",  new FirstGenCharacter("Keaton", 
				new double[] {60,60,0,20,35,30,50,25},
				new int[] {3,0,-2,-1,0,2,-1},
				new int[][] {{1,0,0,0,0,0,0},{1,0,0,0,0,0,0},{0,0,0,0,0,1,0},{1,0,0,0,0,1,0}},
				new String[] {"Wolfskin", "Fighter","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Rinkah","Azura","Hana","Elise","Effie",
						"Nyx","Camilla","Selena","Beruka","Peri","Charlotte","Mozu")) ));
		unitList.put("Xander",  new FirstGenCharacter("Xander", 
				new double[] {45,50,5,40,35,60,40,15},
				new int[] {2,-1,-1,-1,2,1,-2},
				new int[][] {{1,0,0,0,0,0,0},{0,0,0,0,1,0,0},{0,0,0,0,0,1,0},{1,0,0,0,1,0,0}},
				new String[] {"Cavalier", "Wyvern Rider","","",""},
				new HashSet<String>(Arrays.asList("AvatarF","Felicia","Sakura","Azura","Hinoka","Effie","Nyx",
						"Selena","Beruka","Peri","Charlotte","Mozu")) ));
		unitList.put("Izana",  new FirstGenCharacter("Izana", 
				new double[] {45,15,35,55,30,45,35,35},
				new int[] {0,1,1,-2,0,0,1},
				new int[][] {{0,0,1,0,0,0,0},{0,1,0,0,0,0,0},{0,0,0,0,0,1,0},{0,0,1,0,0,0,1}},
				new String[] {"Monk", "Samurai", "Apothecary","",""},
				new HashSet<String>(Arrays.asList("AvatarF")) ));
		unitList.put("Fuga",  new FirstGenCharacter("Fuga", 
				new double[] {20,20,0,15,5,20,10,10},
				new int[] {2,-1,1,0,-1,2,-2},
				new int[][] {{0,0,0,0,0,1,0},{1,0,0,0,0,0,0},{0,0,1,0,0,0,0},{1,0,0,0,0,1,0}},
				new String[] {"Samurai", "Oni Savage", "Monk","",""},
				new HashSet<String>(Arrays.asList("AvatarF")) ));
		unitList.put("Yukimura",  new FirstGenCharacter("Yukimura", 
				new double[] {25,25,5,40,15,30,25,30},
				new int[] {-1,0,3,-1,0,-1,0},
				new int[][] {{0,0,1,0,0,0,0},{0,0,1,0,0,0,0},{1,0,0,0,0,0,0},{0,0,1,1,0,0,0}},
				new String[] {"Apothecary", "Samurai", "Monk","",""},
				new HashSet<String>(Arrays.asList("AvatarF")) ));
		unitList.put("Flora",  new FirstGenCharacter("Flora", 
				new double[] {35,40,20,45,30,35,30,30},
				new int[] {1,-1,2,0,-1,1,-1},
				new int[][] {{0,0,1,0,0,0,0},{0,0,0,0,0,1,0},{0,0,0,0,0,0,1},{1,0,0,0,1,0,0}},
				new String[] {"TroubadourF", "Dark Mage", "Mercenary","",""},
				new HashSet<String>(Arrays.asList("AvatarM")) ));
		unitList.put("Mozu",  new FirstGenCharacter("Mozu", 
				new double[] {30,40,5,50,55,45,35,30},
				new int[] {0,0,1,1,1,0,-2},
				new int[][] {{0,0,1,0,0,0,0},{0,0,0,0,1,0,0},{0,0,0,1,0,0,0},{0,0,1,0,1,0,0}},
				new String[] {"Villager", "Archer","","",""},
				new HashSet<String>(Arrays.asList("AvatarM","Jakob","Kaze","Subaki","Silas","Saizo","Azama",
						"Hayato","Hinata","Takumi","Kaden","Ryoma","Arthur","Odin","Niles","Laslow","Benny",
						"Leo","Keaton","Xander")) ));
		unitList.put("Anna",  new FirstGenCharacter("Anna", 
				new double[] {35,30,55,30,40,70,20,45},
				new int[] {-1,1,0,-1,2,-2,2},
				new int[][] {{0,0,0,0,1,0,0},{0,1,0,0,0,0,0},{0,0,0,0,1,0,0},{0,0,0,0,1,0,1}},
				new String[] {"Outlaw", "TroubadourF", "Apothecary","",""},
				new HashSet<String>(Arrays.asList("AvatarM")) ));
		return unitList;
	}

	
	private HashMap<String, UnitClass> initializeClasses() {
		HashMap<String, UnitClass> classes = new HashMap<String, UnitClass>();
		classes.put("Hoshido Noble", new UnitClass("Hoshido Noble",
				new double[] {15,15,10,10,10,10,15,0},
				new int[] {60,34,28,29,30,33,31,28,6},
				new int[] {2,0,1,1,2,2,0,0},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Dragon Ward","Hoshidan Unity")),"" ));
		classes.put("Samurai", new UnitClass("Samurai",
				new double[] {10,10,0,15,20,15,0,10},
				new int[] {40,20,16,23,25,24,18,20,5},
				new int[] {0,0,0,4,2,0,0,0},
				new HashSet<String>(Arrays.asList("Swordmaster","Master of Arms")),
				new HashSet<String>(Arrays.asList("Duelists Blow","Vantage")),"Mercenary" ));
		classes.put("Swordmaster", new UnitClass("Swordmaster",
				new double[] {10,10,5,15,20,15,0,10},
				new int[] {55,30,28,32,35,33,27,31,6},
				new int[] {0,0,0,5,3,0,0,0},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Astra","Swordfaire")),"" ));
		classes.put("Master of Arms", new UnitClass("Master of Arms",
				new double[] {20,15,0,10,10,10,10,0},
				new int[] {65,33,25,30,30,31,31,28,6},
				new int[] {2,0,2,2,0,2,0,0},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Seal Strength","Life and Death")),"" ));
		classes.put("Oni Savage", new UnitClass("Oni Savage",
				new double[] {20,20,10,0,10,0,20,0},
				new int[] {45,24,19,16,20,17,23,18,5},
				new int[] {4,0,0,0,0,2,0,0},
				new HashSet<String>(Arrays.asList("Oni Chieftain","Blacksmith")),
				new HashSet<String>(Arrays.asList("Seal Resistance","Shove")),"Fighter" ));
		classes.put("Oni Chieftain", new UnitClass("Oni Chieftain",
				new double[] {10,20,15,0,10,0,20,5},
				new int[] {60,34,28,25,30,25,36,31,6},
				new int[] {4,0,0,0,0,4,0,0},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Death Blow","Counter")),"" ));
		classes.put("Blacksmith", new UnitClass("Blacksmith",
				new double[] {20,15,0,15,10,5,15,0},
				new int[] {65,33,25,32,31,30,32,27,6},
				new int[] {3,0,2,0,0,3,0,0},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Salvage Blow","Lancebreaker")),"" ));
		classes.put("Spear Fighter", new UnitClass("Spear Fighter",
				new double[] {15,15,0,15,15,5,10,5},
				new int[] {40,22,15,23,22,21,22,21,5},
				new int[] {2,0,2,2,0,0,0,0},
				new HashSet<String>(Arrays.asList("Spear Master","Basara")),
				new HashSet<String>(Arrays.asList("Seal Defense","Swap")),"Knight" ));
		classes.put("Spear Master", new UnitClass("Spear Master",
				new double[] {15,15,0,15,15,5,10,5},
				new int[] {60,34,25,33,32,29,30,29,6},
				new int[] {3,0,3,2,0,0,0,0},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Seal Speed","Lancefaire")),"" ));
		classes.put("Basara", new UnitClass("Basara",
				new double[] {20,10,10,10,10,15,5,10},
				new int[] {65,31,30,30,31,35,30,32,6},
				new int[] {0,0,0,0,5,0,3,0},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Rend Heaven","Quixotic")),"" ));
		classes.put("Diviner", new UnitClass("Diviner",
				new double[] {0,5,15,10,15,5,0,10},
				new int[] {35,17,22,20,23,19,16,20,5},
				new int[] {0,3,0,3,0,0,0,0},
				new HashSet<String>(Arrays.asList("Basara","Onmyoji")),
				new HashSet<String>(Arrays.asList("Magic +2","Future Sight")),"Dark Mage" ));
		classes.put("Onmyoji", new UnitClass("Onmyoji",
				new double[] {0,0,20,10,15,0,0,15},
				new int[] {45,25,33,31,32,27,25,31,6},
				new int[] {0,4,0,4,0,0,0,0},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Rally Magic","Tomefaire")),"" ));
		classes.put("Monk", new UnitClass("Monk",
				new double[] {0,5,10,10,15,15,0,20},
				new int[] {35,18,21,20,22,23,17,24,5},
				new int[] {0,2,0,0,2,0,2,0},
				new HashSet<String>(Arrays.asList("Onmyoji","Great Master")),
				new HashSet<String>(Arrays.asList("Miracle","Rally Luck")),"" ));
		classes.put("Shrine Maiden", new UnitClass("Shrine Maiden",
				new double[] {0,5,10,10,15,15,0,20},
				new int[] {35,18,21,20,22,23,17,24,5},
				new int[] {0,2,0,0,2,0,2,0},
				new HashSet<String>(Arrays.asList("Onmyoji","Priestess")),
				new HashSet<String>(Arrays.asList("Miracle","Rally Luck")),"" ));
		classes.put("Great Master", new UnitClass("Great Master",
				new double[] {10,15,5,5,15,15,10,10},
				new int[] {55,32,30,31,33,32,28,32,6},
				new int[] {0,3,0,0,2,0,3,0},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Renewal","Countermagic")),"" ));
		classes.put("Priestess", new UnitClass("Priestess",
				new double[] {10,10,10,5,15,15,0,20},
				new int[] {50,29,32,30,33,34,26,34,6},
				new int[] {0,3,0,0,2,0,3,0},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Renewal","Countermagic")),"" ));
		classes.put("Sky Knight", new UnitClass("Sky Knight",
				new double[] {0,10,0,10,15,20,0,20},
				new int[] {35,19,16,21,23,25,18,25,7},
				new int[] {0,0,0,3,0,0,3,0},
				new HashSet<String>(Arrays.asList("Falcon Knight","Kinshi Knight")),
				new HashSet<String>(Arrays.asList("Darting Blow","Camaraderie")),"Wyvern Rider" ));
		classes.put("Falcon Knight", new UnitClass("Falcon Knight",
				new double[] {0,10,10,10,15,20,0,20},
				new int[] {55,28,27,30,34,35,27,35,8},
				new int[] {0,0,0,3,0,0,3,1},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Rally Speed","Warding Blow")),"" ));
		classes.put("Kinshi Knight", new UnitClass("Kinshi Knight",
				new double[] {0,5,0,15,15,15,0,15},
				new int[] {50,27,26,33,31,34,25,31,8},
				new int[] {0,0,2,2,2,0,0,1},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Air Superiority","Amaterasu")),"" ));
		classes.put("Archer", new UnitClass("Archer",
				new double[] {10,15,0,15,15,5,10,0},
				new int[] {40,21,15,23,21,20,20,17,5},
				new int[] {2,0,2,2,0,0,0,0},
				new HashSet<String>(Arrays.asList("Kinshi Knight","Sniper")),
				new HashSet<String>(Arrays.asList("Skill +2","Quick Draw")),"Outlaw" ));
		classes.put("Sniper", new UnitClass("Sniper",
				new double[] {10,15,0,20,15,5,10,0},
				new int[] {55,31,25,35,33,30,31,28,6},
				new int[] {2,0,3,3,0,0,0,0},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Certain Blow","Bowfaire")),"" ));
		classes.put("Ninja", new UnitClass("Ninja",
				new double[] {5,5,0,20,20,0,5,15},
				new int[] {35,17,15,25,25,18,19,20,5},
				new int[] {0,0,1,3,0,0,0,1},
				new HashSet<String>(Arrays.asList("Master Ninja","Mechanist")),
				new HashSet<String>(Arrays.asList("Locktouch","Poison Strike")),"Cavalier" ));
		classes.put("Master Ninja", new UnitClass("Master Ninja",
				new double[] {5,5,0,20,20,0,5,20},
				new int[] {55,27,25,35,35,28,26,34,6},
				new int[] {0,0,2,4,0,0,0,1},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Lethality","Shurikenfaire")),"" ));
		classes.put("Mechanist", new UnitClass("Mechanist",
				new double[] {10,10,0,15,10,5,5,15},
				new int[] {60,30,25,33,30,30,31,31,7},
				new int[] {2,0,2,0,0,2,2,0},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Golembane","Replicate")),"" ));
		classes.put("Apothecary", new UnitClass("Apothecary",
				new double[] {20,20,0,10,10,5,10,5},
				new int[] {45,24,15,19,19,21,23,20,5},
				new int[] {3,0,0,0,0,2,1,0},
				new HashSet<String>(Arrays.asList("Merchant","Mechanist")),
				new HashSet<String>(Arrays.asList("Potent Potion","Quick Salve")),"" ));
		classes.put("Merchant", new UnitClass("Merchant",
				new double[] {20,20,0,10,5,15,10,5},
				new int[] {65,33,25,29,28,32,33,30,6},
				new int[] {3,0,0,0,0,3,2,0},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Profiteer","Spendthrift")),"" ));
		classes.put("Kitsune", new UnitClass("Kitsune",
				new double[] {10,10,0,15,20,10,0,20},
				new int[] {40,20,18,23,24,24,18,23,5},
				new int[] {0,0,0,4,2,0,0,0},
				new HashSet<String>(Arrays.asList("Nine-tails")),
				new HashSet<String>(Arrays.asList("Evenhanded","Beastbane")),"Apothecary" ));
		classes.put("Nine-tails", new UnitClass("Nine-tails",
				new double[] {10,10,0,15,20,10,0,20},
				new int[] {55,29,29,33,34,33,27,34,6},
				new int[] {0,0,0,5,3,0,0,0},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Even Better","Grisly Wound")),"" ));
		classes.put("Songstress", new UnitClass("Songstress",
				new double[] {0,10,0,20,20,20,0,0},
				new int[] {45,28,27,31,31,35,27,28,5},
				new int[] {0,0,2,2,4,0,0,0},
				new HashSet<String>(),
				new HashSet<String>(Arrays.asList("Luck +4","Inspiring Song","Voice of Peace","Foreign Princess")),"TroubadourF" ));
		classes.put("Villager", new UnitClass("Villager",
				new double[] {10,10,0,10,10,20,10,0},
				new int[] {35,19,15,19,19,22,18,15,5},
				new int[] {0,0,3,0,3,0,0,0},
				new HashSet<String>(Arrays.asList("Merchant","Master of Arms")),
				new HashSet<String>(Arrays.asList("Aptitude","Underdog")),"Apothecary" ));
		classes.put("Nohr Prince", new UnitClass("Nohr Prince(ss)",
				new double[] {15,15,10,10,10,10,10,5},
				new int[] {40,23,17,19,21,22,21,19,5},
				new int[] {2,0,1,1,2,0,0,0},
				new HashSet<String>(Arrays.asList("Nohr Noble","Hoshido Noble")),
				new HashSet<String>(Arrays.asList("Nobility","Dragon Fang")),"" ));
		classes.put("Nohr Princess", new UnitClass("Nohr Prince(ss)",
				new double[] {15,15,10,10,10,10,10,5},
				new int[] {40,23,17,19,21,22,21,19,5},
				new int[] {2,0,1,1,2,0,0,0},
				new HashSet<String>(Arrays.asList("Nohr Noble","Hoshido Noble")),
				new HashSet<String>(Arrays.asList("Nobility","Dragon Fang")),"" ));
		classes.put("Nohr Noble", new UnitClass("Nohr Noble",
				new double[] {15,10,15,5,15,5,5,15},
				new int[] {60,32,31,28,32,27,29,32,6},
				new int[] {2,2,1,1,0,0,2,0},
				new HashSet<String>(Arrays.asList()),
				new HashSet<String>(Arrays.asList("Draconic Hex","Nohrian Trust")),"" ));
		classes.put("Cavalier", new UnitClass("Cavalier",
				new double[] {10,15,0,10,10,15,10,5},
				new int[] {40,22,15,21,20,24,22,21,7},
				new int[] {2,0,0,0,0,2,2,0},
				new HashSet<String>(Arrays.asList("Paladin","Great Knight")),
				new HashSet<String>(Arrays.asList("Elbow Room","Shelter")),"Ninja" ));
		classes.put("Paladin", new UnitClass("Paladin",
				new double[] {10,15,0,10,10,15,10,10},
				new int[] {60,31,26,30,30,32,32,32,8},
				new int[] {2,0,0,0,0,2,2,1},
				new HashSet<String>(Arrays.asList()),
				new HashSet<String>(Arrays.asList("Defender","Aegis")),"" ));
		classes.put("Great Knight", new UnitClass("Great Knight",
				new double[] {20,20,0,10,5,5,20,0},
				new int[] {65,35,25,29,27,28,37,28,7},
				new int[] {2,0,0,0,0,4,0,1},
				new HashSet<String>(Arrays.asList()),
				new HashSet<String>(Arrays.asList("Luna","Armored Blow")),"" ));
		classes.put("Knight", new UnitClass("Knight",
				new double[] {20,20,0,15,5,10,20,0},
				new int[] {45,24,15,22,17,22,26,18,4},
				new int[] {2,0,0,0,0,4,0,0},
				new HashSet<String>(Arrays.asList("Great Knight","General")),
				new HashSet<String>(Arrays.asList("Defense +2","Natural Cover")),"Spear Fighter" ));
		classes.put("General", new UnitClass("General",
				new double[] {25,20,0,15,0,10,20,5},
				new int[] {70,38,25,32,25,32,40,30,5},
				new int[] {3,0,0,0,0,5,0,0},
				new HashSet<String>(Arrays.asList()),
				new HashSet<String>(Arrays.asList("Wary Fighter","Pavise")),"" ));
		classes.put("Fighter", new UnitClass("Fighter",
				new double[] {20,20,0,15,15,5,5,0},
				new int[] {45,25,15,23,22,21,19,18,5},
				new int[] {4,0,0,2,0,0,0,0},
				new HashSet<String>(Arrays.asList("Berserker","Hero")),
				new HashSet<String>(Arrays.asList("HP +5","Gamble")),"Oni Savage" ));
		classes.put("Berserker", new UnitClass("Berserker",
				new double[] {30,25,0,15,15,0,0,0},
				new int[] {70,40,25,32,33,25,27,25,6},
				new int[] {5,0,0,3,0,0,0,0},
				new HashSet<String>(Arrays.asList()),
				new HashSet<String>(Arrays.asList("Rally Strength","Axefaire")),"" ));
		classes.put("Mercenary", new UnitClass("Mercenary",
				new double[] {10,15,0,20,15,5,10,5},
				new int[] {40,22,15,24,22,20,21,19,5},
				new int[] {0,0,2,3,0,1,0,0},
				new HashSet<String>(Arrays.asList("Hero","Bow Knight")),
				new HashSet<String>(Arrays.asList("Good Fortune","Strong Riposte")),"Samurai" ));
		classes.put("Hero", new UnitClass("Hero",
				new double[] {20,15,0,20,15,5,10,0},
				new int[] {60,32,25,35,32,31,30,27,6},
				new int[] {0,0,3,3,0,2,0,0},
				new HashSet<String>(Arrays.asList()),
				new HashSet<String>(Arrays.asList("Sol","Axebreaker")),"" ));
		classes.put("Bow Knight", new UnitClass("Bow Knight",
				new double[] {10,10,0,15,15,10,0,10},
				new int[] {55,29,25,32,33,30,27,32,8},
				new int[] {0,0,3,3,0,0,0,1},
				new HashSet<String>(Arrays.asList()),
				new HashSet<String>(Arrays.asList("Rally Skill","Shurikenbreaker")),"" ));
		classes.put("Outlaw", new UnitClass("Outlaw",
				new double[] {0,10,5,10,20,0,0,20},
				new int[] {35,19,18,20,24,18,17,22,5},
				new int[] {0,0,0,2,0,0,2,1},
				new HashSet<String>(Arrays.asList("Bow Knight","Adventurer")),
				new HashSet<String>(Arrays.asList("Locktouch","Movement +1")),"Archer" ));
		classes.put("Adventurer", new UnitClass("Adventurer",
				new double[] {0,5,15,5,20,0,0,20},
				new int[] {50,27,31,27,34,27,25,34,6},
				new int[] {0,0,0,4,0,0,2,1},
				new HashSet<String>(Arrays.asList()),
				new HashSet<String>(Arrays.asList("Lucky Seven","Pass")),"" ));
		classes.put("Wyvern Rider", new UnitClass("Wyvern Rider",
				new double[] {10,15,5,10,10,5,20,0},
				new int[] {40,22,17,21,20,19,24,15,7},
				new int[] {3,0,0,0,0,3,0,0},
				new HashSet<String>(Arrays.asList("Wyvern Lord", "Malig Knight")),
				new HashSet<String>(Arrays.asList("Strength +2","Lunge")),"Sky Knight" ));
		classes.put("Wyvern Lord", new UnitClass("Wyvern Lord",
				new double[] {10,15,0,15,10,5,20,0},
				new int[] {60,33,25,33,29,28,35,26,8},
				new int[] {3,0,0,0,0,3,0,1},
				new HashSet<String>(Arrays.asList()),
				new HashSet<String>(Arrays.asList("Rally Defense","Swordbreaker")),"" ));
		classes.put("Malig Knight", new UnitClass("Malig Knight",
				new double[] {0,15,15,10,5,0,10,15},
				new int[] {55,31,30,28,27,25,31,31,8},
				new int[] {0,2,0,0,0,2,2,1},
				new HashSet<String>(Arrays.asList()),
				new HashSet<String>(Arrays.asList("Savage Blow","Trample")),"" ));
		classes.put("Dark Mage", new UnitClass("Dark Mage",
				new double[] {0,10,20,0,10,0,5,10},
				new int[] {35,19,24,16,19,18,19,22,5},
				new int[] {0,3,0,0,0,0,3,0},
				new HashSet<String>(Arrays.asList("Sorcerer","Dark Knight")),
				new HashSet<String>(Arrays.asList("Heartseeker","Malefic Aura")),"Diviner" ));
		classes.put("Sorcerer", new UnitClass("Sorcerer",
				new double[] {0,0,25,0,10,0,5,15},
				new int[] {50,25,35,26,29,26,29,33,6},
				new int[] {0,5,0,0,0,0,3,0},
				new HashSet<String>(Arrays.asList()),
				new HashSet<String>(Arrays.asList("Vengeance","Bowbreaker")),"" ));
		classes.put("Dark Knight", new UnitClass("Dark Knight",
				new double[] {15,20,10,5,5,5,15,5},
				new int[] {55,32,31,28,27,31,34,30,8},
				new int[] {0,3,0,0,0,3,0,1},
				new HashSet<String>(Arrays.asList()),
				new HashSet<String>(Arrays.asList("Seal Magic","Lifetaker")),"" ));
		classes.put("TroubadourM", new UnitClass("TroubadourM",
				new double[] {0,0,10,20,10,15,0,15},
				new int[] {35,16,19,24,20,23,16,21,7},
				new int[] {0,2,0,0,2,0,2,0},
				new HashSet<String>(Arrays.asList("Strategist","Butler")),
				new HashSet<String>(Arrays.asList("Resistance +2","Gentilhomme")),"" ));
		classes.put("TroubadourF", new UnitClass("TroubadourF",
				new double[] {0,0,10,20,10,15,0,15},
				new int[] {35,16,19,24,20,23,16,21,7},
				new int[] {0,2,0,0,2,0,2,0},
				new HashSet<String>(Arrays.asList("Strategist","Maid")),
				new HashSet<String>(Arrays.asList("Resistance +2","Demoiselle")),"" ));
		classes.put("Strategist", new UnitClass("Strategist",
				new double[] {0,0,15,5,10,20,0,15},
				new int[] {45,25,33,28,31,33,25,32,8},
				new int[] {0,2,0,0,2,0,2,1},
				new HashSet<String>(Arrays.asList()),
				new HashSet<String>(Arrays.asList("Rally Resistance","Inspiration")),"" ));
		classes.put("Butler", new UnitClass("Butler",
				new double[] {0,10,10,15,15,10,5,10},
				new int[] {50,28,31,33,33,32,29,29,6},
				new int[] {0,2,0,3,3,0,0,0},
				new HashSet<String>(Arrays.asList()),
				new HashSet<String>(Arrays.asList("Live to Serve","Tomebreaker")),"" ));
		classes.put("Maid", new UnitClass("Maid",
				new double[] {0,10,10,15,15,10,5,10},
				new int[] {50,28,31,33,33,32,29,29,6},
				new int[] {0,2,0,3,3,0,0,0},
				new HashSet<String>(Arrays.asList()),
				new HashSet<String>(Arrays.asList("Live to Serve","Tomebreaker")),"" ));
		classes.put("Wolfskin", new UnitClass("Wolfskin",
				new double[] {20,20,0,5,15,5,10,0},
				new int[] {45,24,15,18,22,17,21,15,5},
				new int[] {3,0,0,3,0,0,0,0},
				new HashSet<String>(Arrays.asList("Wolfssegner")),
				new HashSet<String>(Arrays.asList("Odd Shaped","Beastbane")),"Outlaw" ));
		classes.put("Wolfssegner", new UnitClass("Wolfssegner",
				new double[] {20,20,0,5,15,5,10,0},
				new int[] {65,36,25,29,31,26,32,26,6},
				new int[] {4,0,0,4,0,0,0,0},
				new HashSet<String>(Arrays.asList()),
				new HashSet<String>(Arrays.asList("Better Odds","Grisly Wound")),"" ));
		return classes;
	}
}
