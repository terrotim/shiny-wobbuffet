package simpleslickgame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;

public class RobotGA extends BasicGame {
	static int windowWidth = 1000;
	static int windowHeight = 1000;
	
	Bot player;
	Team redTeam;
	Team blueTeam;
	ArrayList<Rectangle> obstacles;
	ArrayList<Bullet> bullets;
	MapLayout layout;
	String mapname="mapB";
	int redScore;
	int blueScore;
	int populationSize = 100;
	ArrayList<String> redPopulation = new ArrayList<String>();
	ArrayList<Integer> redFitnessPopulation = new ArrayList<Integer>();
	ArrayList<String> bluePopulation = new ArrayList<String>();
	ArrayList<Integer> blueFitnessPopulation = new ArrayList<Integer>();
	ArrayList<String> redWinners = new ArrayList<String>();
	ArrayList<String> blueWinners = new ArrayList<String>();
	String[] bestTeams = new String[2];
	String bestRedTeam = new String();
	int bestRedTeamScore=-10000;
	String bestBlueTeam = new String();
	int bestBlueTeamScore=-10000;
	Random randomGenerator = new Random();
	int DNAstringlength = 1800;
	
	int delta;
	int sendinbestred = 0;
	int sendinbestblue = 0;
	int roundTime = 0;
	int roundTimeLimit = 60000;
	int populationNum=0;
	int generationNum=0;
	int timer=5;
	int firststart = 1;
	
	public RobotGA(){
		super("GA Bot Teamwork?");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		//player = new Bot(1000-25, 25, 10, 90, "white");
		layout = new MapLayout(mapname);
		if(firststart ==1){
		bestTeams = layout.getBestTeams();
		obstacles = layout.getObstacles();
		bestRedTeam = bestTeams[0];
		bestBlueTeam = bestTeams[1];
		redPopulation = layout.getRedTeam();
		bluePopulation = layout.getBlueTeam();
		firststart = 0;
		}
		redScore = 0;
		blueScore = 0;
		redTeam = new Team();
		blueTeam = new Team("blue", windowWidth, windowHeight);
		String tempdna;
		
		if(redPopulation.size() <= populationNum) {
			tempdna = initializeDNA();
			redTeam.setDNA(tempdna);
			redPopulation.add(tempdna);
		} else if(redPopulation.get(populationNum).length() != DNAstringlength) {
			tempdna = initializeDNA();
			redTeam.setDNA(tempdna);
			redPopulation.set(populationNum, tempdna);
		}
		else {
			redTeam.setDNA(redPopulation.get(populationNum));
		}
		if(sendinbestred == 1 && bestRedTeam.length()==DNAstringlength){
			System.out.println("Sending in best red team");
			redTeam.setDNA(bestRedTeam);
			sendinbestred = 0;
		} 
		if(bluePopulation.size() <= populationNum ){
			tempdna = initializeDNA();
			blueTeam.setDNA(tempdna);
			bluePopulation.add(tempdna);
		} else if (bluePopulation.get(populationNum).length() != DNAstringlength){
			tempdna = initializeDNA();
			blueTeam.setDNA(tempdna);
			bluePopulation.set(populationNum, tempdna);
		} else{
			blueTeam.setDNA(bluePopulation.get(populationNum));
		}
		if(sendinbestblue == 1 && bestBlueTeam.length()==DNAstringlength){
			System.out.println("Sending in best blue team");
			blueTeam.setDNA(bestBlueTeam);
			sendinbestblue = 0;
		} 
		

		bullets = new ArrayList<Bullet>();
	}

	private String initializeDNA() {
		String temp = new String();
		for(int i = 0; i<DNAstringlength;i++){
			temp = temp + randomGenerator.nextInt(3);
		}
		return temp;
	}

	@Override
	public void update(GameContainer gc, int i) throws SlickException {
		Input input = gc.getInput();
		delta++;
		roundTime++;
		if(delta > timer){
			if(input.isKeyPressed(Input.KEY_ENTER)){
				System.out.println("Generation number: " + generationNum);
				System.out.println(bestRedTeam);
				System.out.println(bestRedTeamScore);
				System.out.println();
				System.out.println(bestBlueTeam);
				System.out.println(bestBlueTeamScore);
				System.out.println("----------");
				try {
					layout.saveBestTeams(bestRedTeam, bestBlueTeam);
					layout.saveAllTeams(redPopulation, bluePopulation);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(input.isKeyPressed(Input.KEY_B)){
				sendinbestred = 1;
			}
			if(input.isKeyPressed(Input.KEY_N)){
				sendinbestblue = 1;
			}
			
			
			//movePlayer(player, i, input);
			moveTeam(redTeam, i);
			moveTeam(blueTeam, i);
			moveBullets(i);
			//player.reduceCooldown();
			redTeam.reduceCooldown();
			blueTeam.reduceCooldown();
			//player.setSights();
			redTeam.setSights();
			blueTeam.setSights();

			//updateSights(player);
			updateSights(redTeam);
			updateSights(blueTeam);
			
			delta = 0;
		} 
		if(roundTime>roundTimeLimit){
			roundTime=0;
			System.out.println("Red Score " + populationNum + ": " + redScore);
			System.out.println("Blue Score: " + populationNum + ": " + blueScore);
			if(redScore>bestRedTeamScore){
				bestRedTeamScore = redScore;
				bestRedTeam = redTeam.getDNA();
			}
			if(blueScore>bestBlueTeamScore){
				bestBlueTeamScore = blueScore;
				bestBlueTeam = blueTeam.getDNA();
			}
			redFitnessPopulation.add(redScore);
			blueFitnessPopulation.add(blueScore);
			populationNum++;
			if(populationNum >= populationSize ){
				redWinners = getRedTournamentWinners(redPopulation);
				redPopulation = getNextGen(redWinners);
				blueWinners = getBlueTournamentWinners(bluePopulation);
				bluePopulation = getNextGen(blueWinners);
				
				populationNum=0;
				generationNum++;
				redFitnessPopulation.clear();
				blueFitnessPopulation.clear();
			}
			
			init(gc);
		}
	}

	private ArrayList<String> getRedTournamentWinners(ArrayList<String> population) {
		int challenge;
		ArrayList<String> w = new ArrayList<String>();
		for (int i = 0; i<population.size();){
			challenge = randomGenerator.nextInt(population.size());
			if(challenge != i){
				if(redFitnessPopulation.get(i) < redFitnessPopulation.get(challenge) ){
					w.add(population.get(challenge));
				} else {
					w.add(population.get(i));
				}
				i++;
			}
		}
		return w;
	}
	
	private ArrayList<String> getBlueTournamentWinners(ArrayList<String> population) {
		int challenge;
		ArrayList<String> w = new ArrayList<String>();
		for (int i = 0; i<population.size();){
			challenge = randomGenerator.nextInt(population.size());
			if(challenge != i){
				if(blueFitnessPopulation.get(i) < blueFitnessPopulation.get(challenge) ){
					w.add(population.get(challenge));
				} else {
					w.add(population.get(i));
				}
				i++;
			}
		}
		return w;
	}
	
	private ArrayList<String> getNextGen(ArrayList<String> w) {
		ArrayList<String> newPop = new ArrayList<String>();
		String child1 = new String();
		String child2 = new String();
		int crossoverPoint;
		int temp, temp2;
		while(w.size() > 0){
			temp = randomGenerator.nextInt(w.size());
			temp2 = randomGenerator.nextInt(w.size());
			while(temp == temp2){
				temp2 = randomGenerator.nextInt(w.size());
			}
			crossoverPoint = randomGenerator.nextInt(DNAstringlength);
			child1 = crossover(w.get(temp), w.get(temp2), crossoverPoint);
			child1 = chanceMutation(child1);
			child2 = crossover(w.get(temp2), w.get(temp), crossoverPoint);
			child2 = chanceMutation(child2);
			w.remove(temp);
			if(temp<temp2){
				w.remove(temp2-1);
			} else{
				w.remove(temp2);
			}
			newPop.add(child1);
			newPop.add(child2);
		}
		return newPop;
	}
	
	private String chanceMutation(String child) {
		int mutation = randomGenerator.nextInt(100);
		if(mutation < 5){
			char c[] = child.toCharArray();
			int pos = randomGenerator.nextInt(DNAstringlength);
			int mut= randomGenerator.nextInt(3);
			c[pos] = Integer.toString(mut).charAt(0);

			return new String(c);
		}
		return child;
	}

	private String crossover(String parent1, String parent2, int crossoverPoint) {
			String parent1DNA1 = new String();
			String parent2DNA2 = new String();
			parent1DNA1 = parent1.substring(0, crossoverPoint);
			parent2DNA2 = parent2.substring(crossoverPoint, DNAstringlength);
			return parent1DNA1+parent2DNA2;
	}

	private void updateSights(Team team) {
		for(int i =0; i<team.getBots().length; i++){
			updateSights(team.getBots()[i]);
		}
	}

	private void updateSights(Bot b) {
		b.setLeftSightSpotted(0);
		b.setRightSightSpotted(0);
		int collision, collision2, collision3, collision4, collision5, collision6;
		collision = testSightObstCollision(b, 0, obstacles);
		collision2 = testSightObstCollision(b, 1, obstacles);
		//collision3 = testSightBotCollision(b, b.getLeftSight(), player);
		//collision4 = testSightBotCollision(b, b.getRightSight(), player);
		collision5 = testSightBotCollision(b, b.getLeftSight(), redTeam, blueTeam);
		collision6 = testSightBotCollision(b, b.getRightSight(), redTeam, blueTeam);
		b.setLeftSightSpotted(collision+collision5);
		b.setRightSightSpotted(collision2 +collision6);

	}

	private int testSightBotCollision(Bot b, Polygon sight, Team rt,Team bt) {
		int seen = 0;
		for(int i = 0; i<rt.getBots().length; i++){
				if(sight.intersects(rt.getBots()[i].getHitBox())){
					if(b.getTeam().equals("red")){
						seen = seen + 10;
					} else {
						seen = seen + 100;
					}
				}
		}
		for(int i = 0; i<bt.getBots().length; i++){
				if(sight.intersects(bt.getBots()[i].getHitBox())){
					if(b.getTeam().equals("blue")){
						seen = seen + 10;
					} else {
						seen = seen + 100;
					}
				}
		}
		return seen;
	}

	private int testSightObstCollision(Bot b, int sight, ArrayList<Rectangle> rects) {
		int seen = 0;
		for(int i = 0; i < rects.size(); i++){
			if (sight == 0){
				if(b.getLeftSight().intersects(rects.get(i))){
					while(b.getLeftSight().intersects(rects.get(i))&&b.getLeftScope()>-13){
						b.zoomOutLeft();
						b.setSights();
					}
						seen = seen+1;
				}
			} else {
				if(b.getRightSight().intersects(rects.get(i))){
					while(b.getRightSight().intersects(rects.get(i))&&b.getRightScope()>-13){
						b.zoomOutRight();
						b.setSights();
					}
						seen = seen+1;
				}
			}
		}
		return seen;
	}

	private boolean testObstCollision(Bot b, ArrayList<Rectangle> rects) {
		for(int i = 0; i < rects.size(); i++){
			if(b.getHitBox().intersects(rects.get(i))){
				return true;
			}
		}
		return false;
	}
	
	private boolean testBotCollision(Bot b, Team rt, Team bt, int botnum) {
		for(int i = 0; i<rt.getBots().length; i++){
			
			if(b.getHitBox().intersects(rt.getBots()[i].getHitBox())){
				if((!b.getTeam().equals(rt.getTeam())) || (botnum!=i)){
				return true;
				}
			}
			if(b.getHitBox().intersects(bt.getBots()[i].getHitBox())){
				if((!b.getTeam().equals(bt.getTeam())) || (botnum!=i)){
				return true;
				}
			}
			
		}
		return false;
	}
	
	private boolean testBulletBotCollision(Bullet b, Team rt,Team bt) {
		for(int i = 0; i<rt.getBots().length; i++){
			if(b.getHitBox().intersects(rt.getBots()[i].getHitBox())){
				if(b.getFirer().equals("red")){
					redScore = redScore -10;
				} else {
					blueScore = blueScore + 100;
					redScore = redScore -25;
				}
				return true;
			}
			if(b.getHitBox().intersects(bt.getBots()[i].getHitBox())){
				if(b.getFirer().equals("blue")){
					blueScore = blueScore -10;
				}else {
					redScore = redScore + 100;
					blueScore = blueScore -25;
				}
				return true;
			}
		}
		return false;
	}

	private boolean testBulletObstCollision(Bullet b, ArrayList<Rectangle> rects) {
		for(int i = 0; i < rects.size(); i++){
			if(b.getHitBox().intersects(rects.get(i))){
				return true;
			}
		}
		return false;
	}

	private void moveBullets(int vel) {
		boolean collision, collision2;
		for(int i = 0; i<bullets.size(); ){
			
			if(bullets.get(i).getTravelTime()>0){
				Bullet tempBullet = bullets.get(i);
				tempBullet.getHitBox().setCenterX((float)(tempBullet.getHitBox().getCenterX()+(2*vel * Math.cos(Math.toRadians(tempBullet.getAngle())))));
				tempBullet.getHitBox().setCenterY((float)(tempBullet.getHitBox().getCenterY()-(2*vel * Math.sin(Math.toRadians(tempBullet.getAngle())))));
				tempBullet.setTravelTime(tempBullet.getTravelTime()-1);
				bullets.set(i, tempBullet);
				collision = testBulletObstCollision(tempBullet, obstacles);
				collision2 = testBulletBotCollision(tempBullet, redTeam, blueTeam);
				if(collision || collision2){
					bullets.remove(i);
				} else {
					i++;
				}
				
			} else {
				bullets.remove(i);
			}
		}
	}

	private void moveTeam(Team team, int i) {
		for (int j = 0; j<team.getBots().length; j++){
			movePlayer(team.getBots()[j], i, team.getDNA(), j);
		}
	}


	private void movePlayer(Bot bot, int i, String dna, int botnum) {
		int lss = bot.getLeftSightSpotted();
		int rss = bot.getRightSightSpotted();
		int leftHandEnemies, leftHandFriends, leftHandObstacles;
		int rightHandEnemies, rightHandFriends, rightHandObstacles;
		int decision = 0;
		leftHandEnemies = (lss%1000)/100;
		leftHandFriends = (lss%100)/10;
		leftHandObstacles = (lss%10);
		rightHandEnemies = (rss%1000)/100;
		rightHandFriends = (rss%100)/10;
		rightHandObstacles = (rss%10);
		
		if(bot.getBulletCoolTime()>0){
			decision = decision + 0;
		} else {
			decision = decision + 1;
		}
		if(rightHandObstacles>0){
			decision = decision + 0;
		} else {
			decision = decision + 2;
		}
		if(leftHandObstacles>0){
			decision = decision + 0;
		} else {
			decision = decision + 4;
		}
		if(rightHandEnemies>rightHandFriends){
			decision = decision + 0;
		} else if (rightHandEnemies<rightHandFriends){
			decision = decision + 8;
		} else {
			decision = decision + 16;
		}
		if(leftHandEnemies>leftHandFriends){
			decision = decision + 0;
		} else if (leftHandEnemies<leftHandFriends){
			decision = decision + 24;
		} else {
			decision = decision + 48;
		}
//DNA index 0 to 359. decision 0to71
		decision = decision*5;
		decision = decision+(botnum*360);
		performAction(bot, i, dna, decision, botnum);
	}

	private void performAction(Bot bot, int i, String dna, int decision, int botnum) {
		int choice = Integer.parseInt(dna.substring(decision, decision+5));
		int scopeChoice = (choice/10000);
		int turnChoice = (choice%10000)/1000;
		int moveChoice = (choice%1000)/100;
		int strafeChoice = (choice%100)/10;
		int shootChoice = (choice%10);
		if(scopeChoice==1){
			zoomIn(bot,i,botnum);
		} else if(scopeChoice==2){
			zoomOut(bot,i,botnum);
		}
		if(turnChoice==1){
			turnLeft(bot,i, botnum);
		} else if (turnChoice==2){
			turnRight(bot,i, botnum);
		} 
		if(moveChoice==1){
			moveForward(bot,i, botnum);
		} else if (moveChoice==2){
			moveBackward(bot,i, botnum);
		}
		if(strafeChoice==1){
			strafeLeft(bot,i, botnum);
		}else if(strafeChoice==2){
			strafeRight(bot,i, botnum);
		}
		if(shootChoice>0){
			shoot(bot,i, botnum);
		}
	}

//	private void movePlayer(Bot p, int i, Input input) {
//		if(input.isKeyDown(Input.KEY_W)){
//			moveForward(p,i);
//		}
//		if(input.isKeyDown(Input.KEY_A)){
//			strafeLeft(p,i);
//		}
//		if(input.isKeyDown(Input.KEY_S)){
//			moveBackward(p,i);
//		}
//		if(input.isKeyDown(Input.KEY_D)){
//			strafeRight(p,i);
//		}
//		if(input.isKeyDown(Input.KEY_Q)){
//			turnLeft(p,i);
//		}
//		if(input.isKeyDown(Input.KEY_E)){
//			turnRight(p,i);
//		}
//		if(input.isKeyDown(Input.KEY_SPACE)){
//			shoot(p,i);
//		}
//
//	}

	private void shoot(Bot p, int i, int botnum) {
		if(p.getBulletsLeft() > 0){
			if(p.getBulletCoolTime()==0){
				//p.setBulletsLeft(p.getBulletsLeft()-1);
				p.cooldown();
				float x = p.getHitBox().getCenterX();
				float y =  p.getHitBox().getCenterY();
				float bx = (float) (x + p.getHitBox().getRadius()*Math.cos(Math.toRadians(p.getAngle())));
				float by = (float) (y + p.getHitBox().getRadius()*-Math.sin(Math.toRadians(p.getAngle())));
				bullets.add(new Bullet(bx, by, p.getAngle(), p.getTeam()));
			}
		}
	}

	private void zoomIn(Bot p, int i, int botnum) {
		p.zoomInRight();
		p.zoomInLeft();
	}
	
	private void zoomOut(Bot p, int i, int botnum) {
		p.zoomOutRight();
		p.zoomOutLeft();
	}
	
	private void turnRight(Bot p, int i, int botnum) {
		p.setAngle(p.getAngle()-i);
		if(p.getHitBox().getMinX() < 0){
			p.getHitBox().setCenterX(p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMaxX() > windowWidth){
			p.getHitBox().setCenterX(windowWidth - p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMinY() < 0){
			p.getHitBox().setCenterY(p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMaxY() > windowHeight){
			p.getHitBox().setCenterY(windowHeight - p.getHitBox().getRadius());
		}
	}

	private void turnLeft(Bot p, int i, int botnum) {
		p.setAngle(p.getAngle()+i);
		if(p.getHitBox().getMinX() < 0){
			p.getHitBox().setCenterX(p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMaxX() > windowWidth){
			p.getHitBox().setCenterX(windowWidth - p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMinY() < 0){
			p.getHitBox().setCenterY(p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMaxY() > windowHeight){
			p.getHitBox().setCenterY(windowHeight - p.getHitBox().getRadius());
		}
	}

	private void strafeRight(Bot p, int i, int botnum) {
		boolean collision;
		p.getHitBox().setCenterX((float) (p.getHitBox().getCenterX()+(i * Math.sin(Math.toRadians(p.getAngle())))));
		p.getHitBox().setCenterY((float) (p.getHitBox().getCenterY()+(i * Math.cos(Math.toRadians(p.getAngle())))));
		collision = (testObstCollision(p, obstacles) || testBotCollision(p, redTeam, blueTeam, botnum));
		if(collision){
			p.getHitBox().setCenterX((float) (p.getHitBox().getCenterX()-(i * Math.sin(Math.toRadians(p.getAngle())))));
			p.getHitBox().setCenterY((float) (p.getHitBox().getCenterY()-(i * Math.cos(Math.toRadians(p.getAngle())))));
		}
		if(p.getHitBox().getMinX() < 0){
			p.getHitBox().setCenterX(p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMaxX() > windowWidth){
			p.getHitBox().setCenterX(windowWidth - p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMinY() < 0){
			p.getHitBox().setCenterY(p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMaxY() > windowHeight){
			p.getHitBox().setCenterY(windowHeight - p.getHitBox().getRadius());
		}
	}

	private void moveBackward(Bot p, int i, int botnum) {
		boolean collision;
		p.getHitBox().setCenterX((float) (p.getHitBox().getCenterX()-(i * Math.cos(Math.toRadians(p.getAngle())))));
		p.getHitBox().setCenterY((float) (p.getHitBox().getCenterY()+(i * Math.sin(Math.toRadians(p.getAngle())))));
		collision = (testObstCollision(p, obstacles) || testBotCollision(p, redTeam, blueTeam, botnum));
		if(collision){
			p.getHitBox().setCenterX((float) (p.getHitBox().getCenterX()+(i * Math.cos(Math.toRadians(p.getAngle())))));
			p.getHitBox().setCenterY((float) (p.getHitBox().getCenterY()-(i * Math.sin(Math.toRadians(p.getAngle())))));
		}
		if(p.getHitBox().getMinX() < 0){
			p.getHitBox().setCenterX(p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMaxX() > windowWidth){
			p.getHitBox().setCenterX(windowWidth - p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMinY() < 0){
			p.getHitBox().setCenterY(p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMaxY() > windowHeight){
			p.getHitBox().setCenterY(windowHeight - p.getHitBox().getRadius());
		}
	}

	private void strafeLeft(Bot p, int i, int botnum) {
		boolean collision;
		p.getHitBox().setCenterX((float) (p.getHitBox().getCenterX()-(i * Math.sin(Math.toRadians(p.getAngle())))));
		p.getHitBox().setCenterY((float) (p.getHitBox().getCenterY()-(i * Math.cos(Math.toRadians(p.getAngle())))));
		collision = (testObstCollision(p, obstacles) || testBotCollision(p, redTeam, blueTeam, botnum));
		if(collision){
			p.getHitBox().setCenterX((float) (p.getHitBox().getCenterX()+(i * Math.sin(Math.toRadians(p.getAngle())))));
			p.getHitBox().setCenterY((float) (p.getHitBox().getCenterY()+(i * Math.cos(Math.toRadians(p.getAngle())))));
		}
		if(p.getHitBox().getMinX() < 0){
			p.getHitBox().setCenterX(p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMaxX() > windowWidth){
			p.getHitBox().setCenterX(windowWidth - p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMinY() < 0){
			p.getHitBox().setCenterY(p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMaxY() > windowHeight){
			p.getHitBox().setCenterY(windowHeight - p.getHitBox().getRadius());
		}
	}

	private void moveForward(Bot p, int i, int botnum) {
		boolean collision;
		p.getHitBox().setCenterX((float) (p.getHitBox().getCenterX()+(i * Math.cos(Math.toRadians(p.getAngle())))));
		p.getHitBox().setCenterY((float) (p.getHitBox().getCenterY()-(i * Math.sin(Math.toRadians(p.getAngle())))));
		collision = (testObstCollision(p, obstacles) || testBotCollision(p, redTeam, blueTeam, botnum) );
		if(collision){
			p.getHitBox().setCenterX((float) (p.getHitBox().getCenterX()-(i * Math.cos(Math.toRadians(p.getAngle())))));
			p.getHitBox().setCenterY((float) (p.getHitBox().getCenterY()+(i * Math.sin(Math.toRadians(p.getAngle())))));
		}
		if(p.getHitBox().getMinX() < 0){
			p.getHitBox().setCenterX(p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMaxX() > windowWidth){
			p.getHitBox().setCenterX(windowWidth - p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMinY() < 0){
			p.getHitBox().setCenterY(p.getHitBox().getRadius());
		}
		if(p.getHitBox().getMaxY() > windowHeight){
			p.getHitBox().setCenterY(windowHeight - p.getHitBox().getRadius());
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{

		for(int i = 0; i<redTeam.getBots().length; i++){
			g.setColor(Color.red);
			g.fill(redTeam.getBots()[i].getHitBox());
			g.draw(redTeam.getBots()[i].getLeftSight());
			g.draw(redTeam.getBots()[i].getRightSight());
			if(redTeam.getBots()[i].getLeftSightSpotted()>0){
				g.draw(redTeam.getBots()[i].getLeftSight());
			} else {
				g.draw(redTeam.getBots()[i].getLeftSight());
			}
			if(redTeam.getBots()[i].getRightSightSpotted()>0){
				g.draw(redTeam.getBots()[i].getRightSight());
			}else {
				g.draw(redTeam.getBots()[i].getRightSight());
			}
		}
		for(int i = 0; i<blueTeam.getBots().length; i++){
			g.setColor(Color.blue);
			g.fill(blueTeam.getBots()[i].getHitBox());
			g.draw(blueTeam.getBots()[i].getLeftSight());
			g.draw(blueTeam.getBots()[i].getRightSight());
			if(blueTeam.getBots()[i].getLeftSightSpotted()>0){
				g.draw(blueTeam.getBots()[i].getLeftSight());
			} else {
				g.draw(blueTeam.getBots()[i].getLeftSight());
			}
			if(blueTeam.getBots()[i].getRightSightSpotted()>0){
				g.draw(blueTeam.getBots()[i].getRightSight());
			}else {
				g.draw(blueTeam.getBots()[i].getRightSight());
			}
		}
		for(int i = 0; i<obstacles.size(); i++){
			g.setColor(Color.white);
			g.fill(obstacles.get(i));
		}
		
//		g.fill(player.getHitBox());
//		if(player.getLeftSightSpotted()>0){
//			g.fill(player.getLeftSight());
//		} else {
//			g.draw(player.getLeftSight());
//		}
//		if(player.getRightSightSpotted()>0){
//			g.fill(player.getRightSight());
//		}else {
//			g.draw(player.getRightSight());
//		}
		
		for(int i=0;i<bullets.size();i++){
			g.setColor(Color.green);
			g.fill(bullets.get(i).getHitBox());
		}
	}

	public static void main(String[] args) throws SlickException{
		AppGameContainer game = new AppGameContainer(new RobotGA());
		game.setDisplayMode(windowWidth, windowHeight, false);
		game.setAlwaysRender(true);
		game.setUpdateOnlyWhenVisible(false);
		//game.setShowFPS(false);
		game.start();
	}
}