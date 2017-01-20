package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Wave extends GameObject{

	public static ArrayList<Ghost> ghostLeft = new ArrayList<>();
	public static ArrayList<Ghost> ghostRight = new ArrayList<>();


	private Handler handler;
	private Color color;
	private float life;
	private Random r = new Random();
	public Wave(float x, float y, ID id, Handler handler, float velX, float velY, Color color, float life) {
		super(x, y, id);
		this.handler = handler;
		this.velX = velX;
		this.velY = velY;
		this.color = color;
		this.life = life;

		if(velX<0){
			Initialize.wallLeftType = r.nextInt(Initialize.TOTALKINDSOFWALLS);
			changeWall(0);
			handler.object.removeAll(ghostLeft);
			ghostLeft.clear();
			generateGhost(5,0);
			generateFruit(1,0);
			handler.object.removeAll(PelletGenerator.pelletsLeft);
			int tempNum = PelletGenerator.pelletsLeft.size();
			PelletGenerator.pelletsLeft.clear();
			Game.pelletGenerator.generateLeft(tempNum);
		}

		if(velX>0){
			Initialize.wallRightType = r.nextInt(Initialize.TOTALKINDSOFWALLS);
			changeWall(1);
			handler.object.removeAll(ghostRight);
			ghostRight.clear();
			generateGhost(5,1);
			generateFruit(1,1);
			handler.object.removeAll(PelletGenerator.pelletsRight);
			int tempNum = PelletGenerator.pelletsRight.size();
			PelletGenerator.pelletsRight.clear();
			Game.pelletGenerator.generatRight(tempNum);
		}
		
		
		
	}

	private void changeWall(int type){
		if(type==0){
			handler.object.removeAll(Game.leftWalls);
			Game.leftWalls.clear();
			for(Wall everyWall: Initialize.wallCoord.get(Initialize.wall[0][Initialize.wallLeftType])){
				handler.addObject(everyWall);
				Game.leftWalls.add(everyWall);
			}
		}else if(type==1){
			handler.object.removeAll(Game.rightWalls);
			Game.rightWalls.clear();
			for(Wall everyWall: Initialize.wallCoord.get(Initialize.wall[1][Initialize.wallRightType])){
				handler.addObject(everyWall);
				Game.rightWalls.add(everyWall);
			}
		}
	}

	private void generateFruit(int number, int place){
		for(int i =1; i<=number;i++){
			Fruit fruit;
			do{
				int row = r.nextInt(7)+5;
				int column;
				if(row%2==0){
					column =r.nextInt(7)*2+2;
				}else{
					column =r.nextInt(6)*2+3;
				}
				fruit = new Fruit(Game.WPixel*(row+14*place)+Game.initX+10,Game.HPixel*(column)+Game.initY+10,ID.Fruit, Color.red);
			}while(!checkObject(fruit,place));
			handler.addObject(fruit);
		}
	}


	private void generateGhost(int number, int place){
		for(int i =1; i<=r.nextInt(number)+3;i++){
			Ghost ghost;
			do{
				int row = r.nextInt(7)+5;
				int column;
				if(row%2==0){
					column =r.nextInt(7)*2+2;
				}else{
					column =r.nextInt(6)*2+3;
				}
				ghost = new Ghost(Game.WPixel*(row+14*place)+Game.initX+10,Game.HPixel*(column)+Game.initY+10,ID.GhostSleep, handler, generateColor());
			}while(!checkObject(ghost,place));
			if (place==0)
				ghostLeft.add(ghost);
			else if(place==1)
				ghostRight.add(ghost);
			handler.addObject(ghost);
		}
	}

	private boolean checkObject(GameObject object,int place){
		
		if(place==0){
			for(Wall everyWall: Game.leftWalls){
				if(everyWall.getBounds().intersects(object.getBounds()))
					return false;
			}
			for(Ghost everyGhost: ghostLeft){
				if(everyGhost.getBounds().intersects(object.getBounds()))
					return false;
			}
			for(Pellet everyPellet: PelletGenerator.pelletsLeft){
				if(everyPellet.getBounds().intersects(object.getBounds()))
					return false;
			}
		}else if(place==1){
			for(Wall everyWall: Game.rightWalls){
				if(everyWall.getBounds().intersects(object.getBounds()))
					return false;
			}
			for(Ghost everyGhost: ghostRight){
				if(everyGhost.getBounds().intersects(object.getBounds()))
					return false;
			}
			for(Pellet everyPellet: PelletGenerator.pelletsRight){
				if(everyPellet.getBounds().intersects(object.getBounds()))
					return false;
			}
		}
		return true;
	}


	@Override
	public Rectangle getBounds() {
		return null;
	}

	@Override
	public void tick() {
		x += velX;
		if(x<=0||x>=Game.WIDTH)
			handler.removeObject(this);
		handler.addObject(new WaveTrail(x-50,y,ID.Trail,color,100,Game.HEIGHT,0.2f,0.005f,handler));

	}

	@Override
	public void render(Graphics g) {
	}

	private Color generateColor(){
		int colorDetermin = r.nextInt(6);
		if(colorDetermin==0)
			return new Color(255,0,0);
		else if(colorDetermin==1)
			return new Color(0,0,255);
		else if(colorDetermin==2)
			return new Color(0,255,0);
		else if(colorDetermin==3)
			return new Color(255,255,0);
		else if(colorDetermin==4)
			return new Color(0,255,255);
		else
			return new Color(255,0,255);
	}
}
