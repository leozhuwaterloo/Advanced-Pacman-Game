package main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class PelletGenerator{
	
	public static ArrayList<Pellet> pelletsLeft = new ArrayList<>();
	public static ArrayList<Pellet> pelletsRight = new ArrayList<>();
	
	private Handler handler;
	
	private Random r = new Random();
	
	public PelletGenerator(Handler handler){
		this.handler = handler;
	}
	
	public void generateLeft(int number){
		generatePellet(number,0);
	}
	
	public void generatRight(int number){
		generatePellet(number,1);
	}
	
	
	private void generatePellet(int number, int place){
		for(int i =1; i<=number;i++){
			Pellet pellet;
			do{
				int row = r.nextInt(9)+4;
				int column =r.nextInt(15)+1;
				pellet =new Pellet(Game.WPixel*(row+14*place)+Game.initX+10,Game.HPixel*(column)+Game.initY+10,ID.Pellet, Color.yellow);
			}while(!checkPellet(pellet,place));
			handler.addObject(pellet);
			if (place==0)
				pelletsLeft.add(pellet);
			else if(place==1)
				pelletsRight.add(pellet);
		}
	}


	private boolean checkPellet(Pellet pellet,int place){

		if(place==0){
			for(Wall everyWall: Game.leftWalls){
				if(everyWall.getBounds().intersects(pellet.getBounds()))
					return false;
			}
			for(Ghost everyGhost: Wave.ghostLeft){
				if(everyGhost.getBounds().intersects(pellet.getBounds()))
					return false;
			}
			for(Pellet everyPellet: PelletGenerator.pelletsLeft){
				if(everyPellet.getBounds().intersects(pellet.getBounds()))
					return false;
			}
		}else if(place==1){
			for(Wall everyWall: Game.rightWalls){
				if(everyWall.getBounds().intersects(pellet.getBounds()))
					return false;
			}
			for(Ghost everyGhost: Wave.ghostRight){
				if(everyGhost.getBounds().intersects(pellet.getBounds()))
					return false;
			}
			for(Pellet everyPellet: PelletGenerator.pelletsRight){
				if(everyPellet.getBounds().intersects(pellet.getBounds()))
					return false;
			}
		}
		return true;
	}

}
