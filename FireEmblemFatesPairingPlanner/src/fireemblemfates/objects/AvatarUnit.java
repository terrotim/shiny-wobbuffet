package fireemblemfates.objects;

import java.util.Arrays;
import java.util.HashSet;

public class AvatarUnit extends FirstGenCharacter{
	int boon;
	int bane;
	double[] baseGrowthRates = new double[8];
	double[][] growthBoonModifiers = new double[][] {{15,0,0,0,0,0,5,5},{0,15,0,5,0,0,5,0},{0,0,20,0,5,0,0,5},{0,5,0,25,0,0,5,0},
			{0,0,0,5,15,5,0,0},{0,5,5,0,0,25,0,0},{0,0,0,0,0,5,10,5},{0,0,5,0,5,0,0,10}};
	double[][] growthBaneModifiers = new double[][] {{-10,0,0,0,0,0,-5,-5},{0,-10,0,-5,0,0,-5,0},{0,0,-15,0,-5,0,0,-5},{0,-5,0,-20,0,0,-5,0},
			{0,0,0,-5,-10,-5,0,0},{0,-5,-5,0,0,-20,0,0},{0,0,0,0,0,-5,-10,-5},{0,0,-5,0,-5,0,0,-10}};
	int[][] maxStatBoonModifiers = new int[][] {{1,1,0,0,2,2,2},{4,0,2,0,0,2,0},{0,4,0,2,0,0,2},{2,0,4,0,0,2,0},
			{0,0,2,4,2,0,0},{2,2,0,0,4,0,0},{0,0,0,0,2,4,2},{0,2,0,2,0,0,4}};
	int[][] maxStatBaneModifiers = new int[][] {{-1,-1,0,0,-1,-1,-1},{-3,0,-1,0,0,-1,0},{0,-3,0,-1,0,0,-1},{-1,0,-3,0,0,-1,0},
			{0,0,-1,-3,-1,0,0},{-1,-1,0,0,-3,0,0},{0,0,0,0,-1,-3,-1},{0,-1,0,-1,0,0,-3}};
	String talent = new String();
	
	
	public AvatarUnit(String name){
		this.setName(name);
		//HP,STR0,MAG1,SKL2,SPD3,LCK4,DEF5,RES6
		this.setBaseGrowthRates(new double[] {45,45,30,40,45,45,35,25});
		updateGrowthRates();
		updateStatModifiers();
		updatePairup();
		updateClasses();
		if(name.equals("AvatarM")){
			this.setClassSet(new String[] {"Nohr Prince",getTalentToClass(),"","",""});
			this.setSupportSet(new HashSet<String>(Arrays.asList("Felicia","Rinkah","Sakura","Azura","Hana","Orochi","Hinoka",
					"Setsuna","Oboro","Kagero","Reina","Scarlet","Elise","Effie","Nyx","Camilla","Selena","Beruka","Peri",
					"Charlotte","Flora","Mozu","Sophie","Midori","Selkie","Mitama","Caeldori","Rhajat","Velouria","Ophelia",
					"Soleil","Nina","Anna")));
		} else if(name.equals("AvatarF")){
			this.setClassSet(new String[] {"Nohr Princess",getTalentToClass(),"","",""});
			this.setSupportSet(new HashSet<String>(Arrays.asList("Gunter","Jakob","Kaze","Subaki","Silas","Saizo","Azama","Hayato",
					"Hinata","Takumi","Kaden","Ryoma","Shura","Arthur","Odin","Niles","Laslow","Benny","Leo","Keaton","Xander",
					"Izana","Fuga","Yukimura","Shigure","Dwyer","Shiro","Kiragi","Asugi","Hisame","Siegbert","Forrest","Ignatius",
					"Percy")));
		}
	}
	
	private void updateClasses() {
		if(name.equals("AvatarM")){
			this.setClassSet(new String[] {"Nohr Prince",getTalentToClass(),"","",""});
		}else if(name.equals("AvatarF")){
				this.setClassSet(new String[] {"Nohr Princess",getTalentToClass(),"","",""});
		}
	}

	private void updatePairup() {
		int b=0, x=0, y=0;

		switch(getBoon()){
			case 0:
				b = 5;
				x = 6;
				y = 4;
				break;
			case 1:
				b = 0;
				x = 2;
				y = 5;
				break;
			case 2:
				b = 1;
				x = 3;
				y = 6;
				break;
			case 3:
				b = 2;
				x = 5;
				y = 0;
				break;
			case 4:
				b = 3;
				x = 4;
				y = 2;
				break;
			case 5:
				b = 4;
				x = 0;
				y = 1;
				break;
			case 6:
				b = 5;
				x = 6;
				y = 4;
				break;
			case 7:
				b = 6;
				x = 1;
				y = 3;
				break;
			}
		int[][] basePairup;
		
		switch(getBane()){
			case 0:
				basePairup = new int[][] {{1,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,1,0,0,0},{0,0,0,0,0,0,0}};
				basePairup[1][b]++;
				basePairup[3][x]++;
				basePairup[3][b]++;
				this.setPairupStats(basePairup);
				break;
			case 1:
				basePairup = new int[][] {{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,1,0,0,0},{0,0,0,0,0,0,1}};
				basePairup[0][b]++;
				basePairup[1][y]++;
				basePairup[3][b]++;
				this.setPairupStats(basePairup);
				break;
			case 2:
				basePairup = new int[][] {{1,0,0,0,0,0,0},{0,0,0,0,0,1,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0}};
				basePairup[2][x]++;
				basePairup[3][y]++;
				basePairup[3][b]++;
				this.setPairupStats(basePairup);
				break;
			case 3:
				basePairup = new int[][] {{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,1,0,0,0},{0,0,0,0,0,0,1}};
				basePairup[0][y]++;
				basePairup[1][x]++;
				basePairup[3][b]++;
				this.setPairupStats(basePairup);
				break;
			case 4:
				basePairup = new int[][] {{1,0,0,0,0,0,0},{0,0,0,0,0,1,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,1}};
				basePairup[2][b]++;
				basePairup[3][b]++;
				this.setPairupStats(basePairup);
				break;
			case 5:
				basePairup = new int[][] {{0,0,0,0,0,0,0},{0,0,0,0,0,1,0},{0,0,0,1,0,0,0},{0,0,0,0,0,0,1}};
				basePairup[0][x]++;
				basePairup[3][b]++;
				this.setPairupStats(basePairup);
				break;
			case 6:
				basePairup = new int[][] {{1,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,1,0,0,0},{0,0,0,0,0,0,0}};
				basePairup[1][b]++;
				basePairup[3][x]++;
				basePairup[3][b]++;
				this.setPairupStats(basePairup);
				break;
			case 7:
				basePairup = new int[][] {{1,0,0,0,0,0,0},{0,0,0,0,0,1,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0}};
				basePairup[2][y]++;
				basePairup[3][b]++;
				basePairup[3][b]++;
				this.setPairupStats(basePairup);
				break;
		}
		
	}


	public void updateStatModifiers() {
		int[] fullModifiers = new int[7];
		int[] boonModifiers = new int[7];
		int[] baneModifiers = new int[7];
		boonModifiers = this.maxStatBoonModifiers[getBoon()];
		baneModifiers = this.maxStatBaneModifiers[getBane()];
		for (int i = 0; i<fullModifiers.length; i++){
			fullModifiers[i] = boonModifiers[i]+baneModifiers[i];
		}
		this.setStatModifiers(fullModifiers);
	}


	public void updateGrowthRates() {
		// TODO Auto-generated method stub
		double[] fullModifiers = new double[8];
		double[] boonModifiers = new double[8];
		double[] baneModifiers = new double[8];
		double[] baseGrowth = new double[8];
		baseGrowth = this.baseGrowthRates;
		boonModifiers = this.growthBoonModifiers[getBoon()];
		baneModifiers = this.growthBaneModifiers[getBane()];
		for (int i = 0; i<fullModifiers.length; i++){
			fullModifiers[i] = baseGrowth[i]+boonModifiers[i]+baneModifiers[i];
		}
		this.setGrowthRates(fullModifiers);
	}

	public String getTalentToClass() {
		String unitClass = this.getTalent();
		if (talent.equals("Monk") && getName().equals("AvatarF")){
			unitClass = "Shrine Maiden";
		} else if (talent.equals("Priestess") && getName().equals("AvatarM")){
			unitClass = "Monk";
		} else if (talent.equals("Priestess") && getName().equals("AvatarF")){
			unitClass = "Shrine Maiden";
		} else if (talent.equals("Troubadour") && getName().equals("AvatarF")){
			unitClass = "TroubadourF";
		}else if (talent.equals("Troubadour") && getName().equals("AvatarM")){
			unitClass = "TroubadourM";
		} else if (talent.equals("Lancer")){
			unitClass = "Spear Fighter";
		}else if (talent.equals("Mage")){
			unitClass = "Dark Mage";
		}else if (talent.equals("Dragon")){
			unitClass = "Wyvern Rider";
		}
		return unitClass;
	}

	public String getTalent() {
		return talent;
	}


	public void setTalent(String talent) {
		this.talent = talent;
		updateClasses();
	}


	public int getBoon() {
		return boon;
	}

	public void setBoon(int boon) {
		this.boon = boon;
		updateGrowthRates();
		updateStatModifiers();
		updatePairup();
	}

	public int getBane() {
		return bane;
	}

	public void setBane(int bane) {
		this.bane = bane;
		updateGrowthRates();
		updateStatModifiers();
		updatePairup();
	}

	public double[] getBaseGrowthRates() {
		return baseGrowthRates;
	}

	public void setBaseGrowthRates(double[] baseGrowthRates) {
		this.baseGrowthRates = baseGrowthRates;
	}

	public void updateClassSet() {
		this.classSet[1] = this.getTalentToClass();
	}
	
}
