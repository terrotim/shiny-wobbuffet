package simpleslickgame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class MapLayout {

	private String mapname = new String();
	
	public MapLayout(String mapname) {
		setMapname("data/" + mapname);
	}

	public ArrayList<Rectangle> getObstacles() throws SlickException{
		ArrayList<Rectangle> obstacles = new ArrayList<Rectangle>();
		try {
			//System.out.println(new File(s).getAbsolutePath());
			FileInputStream fis = new FileInputStream(new File(getMapname()));
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			while(br.ready()){
				obstacles.add(generateObstacle(readNextValidLine(br)));
			}
			fis.close();
			br.close();
		} catch (FileNotFoundException e) {
			throw new SlickException("Could not find file");
		} catch (IOException e) {
			throw new SlickException("Could not load obstacles");
		}
		return obstacles;
	}

	private Rectangle generateObstacle(String[] coords) {
		int x = Integer.parseInt(coords[0]);
		int y = Integer.parseInt(coords[1]);
		int width = Integer.parseInt(coords[2]);
		int height = Integer.parseInt(coords[3]);
		return new Rectangle(x, y, width, height);
	}

	private String[] readNextValidLine(BufferedReader br) throws SlickException {
		String coords[] = null;
		boolean read = false;
		
		while(!read){
			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e) {
				throw new SlickException("Could not read map file", e);
			}
			if(!line.isEmpty()){
				read = true;
				coords = line.split(" ");
			}
		}
		return coords;
	}

	public void saveBestTeams(String rbt, String bbt) throws IOException {
		String tempmapname = getMapname()+"best";
		FileOutputStream fos = new FileOutputStream(new File(tempmapname));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		bw.write(rbt);
		bw.newLine();
		bw.write(bbt);
		bw.close();
		fos.close();
		
	}

	public void saveAllTeams(ArrayList<String> redPopulation,
			ArrayList<String> bluePopulation) throws IOException {
		String tempmapnamered = getMapname()+"red";
		String tempmapnameblue = getMapname()+"blue";
		FileOutputStream fosred = new FileOutputStream(new File(tempmapnamered));
		FileOutputStream fosblue = new FileOutputStream(new File(tempmapnameblue));
		BufferedWriter bwred = new BufferedWriter(new OutputStreamWriter(fosred));
		BufferedWriter bwblue = new BufferedWriter(new OutputStreamWriter(fosblue));
		for(int i = 0; i< redPopulation.size(); i++){
			bwred.write(redPopulation.get(i));
			bwred.newLine();
		}
		for(int i = 0; i< bluePopulation.size(); i++){
			bwblue.write(bluePopulation.get(i));
			bwblue.newLine();
		}
		bwred.close();
		bwblue.close();
		fosred.close();
		fosblue.close();
		
	}
	
	public ArrayList<String> getRedTeam() {
		String tempmapnamered = getMapname()+"red";
		FileInputStream fisred;
		ArrayList<String> redpop = new ArrayList<String>();
		try {
			fisred = new FileInputStream(new File(tempmapnamered));
			BufferedReader br = new BufferedReader(new InputStreamReader(fisred));
			for (int i = 0; i<100; i++){
				String line = br.readLine();
				if (line != null){
					redpop.add(line);
				} else {
					redpop.add("");
				}
				
			}
			fisred.close();
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return redpop;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return redpop;
	}

	public ArrayList<String> getBlueTeam() {
		String tempmapnameblue = getMapname()+"blue";
		FileInputStream fisblue;
		ArrayList<String> bluepop = new ArrayList<String>();
		try {
			fisblue = new FileInputStream(new File(tempmapnameblue));
			BufferedReader br = new BufferedReader(new InputStreamReader(fisblue));
			for (int i = 0; i<100; i++){
				String line = br.readLine();
				if (line != null){
					bluepop.add(line);
				} else {
					bluepop.add("");
				}
			}
			fisblue.close();
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return bluepop;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bluepop;
	}


	public String getMapname() {
		return mapname;
	}

	public void setMapname(String mapname) {
		this.mapname = mapname;
	}

	public String[] getBestTeams() {
		String tempmapname = getMapname()+"best";
		FileInputStream fis;
		String[] bestTeams = new String[2];
		try {
			fis = new FileInputStream(new File(tempmapname));
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			bestTeams[0]= br.readLine();
			bestTeams[1]=br.readLine();
			fis.close();
			br.close();
		} catch (FileNotFoundException e1) {
			bestTeams[0]="";
			bestTeams[1]="";
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Loaded Best Red Team: ");
		System.out.println(bestTeams[0]);
		System.out.println("Loaded Best Blue Team: ");
		System.out.println(bestTeams[1]);
		
		return bestTeams;
	}



	

}
