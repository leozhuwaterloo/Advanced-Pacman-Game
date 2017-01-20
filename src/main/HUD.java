package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;

public class HUD {

	public static int HEALTH = 800;
	public static int DISPLAYLENGTH = HEALTH;

	public static int SCORE = 0;
	private int fps = 0;
	private int object =  0;

	private int TIMEINSECOND = 0;

	private int redValue, greenValue = 255, blueValue = 255;
	private int redValueChange;
	private int greenValueChange;
	private int blueValueChange;
	private int changeSpeed = 1;


	private int healthBarRed = 0;
	private int healthBarGreen = 255;
	private int healthBarBlue = 0;

	private PelletGenerator pelletGenerator;
	
	private Random r = new Random();

	public void tick(){		
		HEALTH = (int)Game.clamp(HEALTH, 0, 800);


		if(redValue==255&&greenValue==0&&blueValue==0){
			redValueChange=0;
			greenValueChange=0;
			blueValueChange=changeSpeed;
		}else if(redValue==255&&greenValue==0&&blueValue==255){
			redValueChange=-changeSpeed;
			greenValueChange=0;
			blueValueChange=0;
		}else if(redValue==0&&greenValue==0&&blueValue==255){
			redValueChange=0;
			greenValueChange=changeSpeed;
			blueValueChange=0;
		}else if(redValue==0&&greenValue==255&&blueValue==255){
			redValueChange=0;
			greenValueChange=0;
			blueValueChange=-changeSpeed;
		}else if(redValue==0&&greenValue==255&&blueValue==0){
			redValueChange=changeSpeed;
			greenValueChange=0;
			blueValueChange=0;
		}else if(redValue==255&&greenValue==255&&blueValue==0){
			redValueChange=0;
			greenValueChange=-changeSpeed;
			blueValueChange=0;
		}

		redValue+=redValueChange;
		greenValue+=greenValueChange;
		blueValue+=blueValueChange;


		if(Ghost.EATABLE){
			healthBarBlue = 255;
			healthBarGreen = (int)(DISPLAYLENGTH/800.0*255.0);
			healthBarGreen = (int)Game.clamp(healthBarGreen, 0, 255);
			DISPLAYLENGTH-=1;
			healthBarRed = 0;
			if(DISPLAYLENGTH==0){
				Ghost.setNoEat();
				Game.pelletGenerator.generateLeft(r.nextInt(30+1)+20);
				Game.pelletGenerator.generatRight(r.nextInt(30+1)+20);
				Initialize.powerPelletGenerated = false;
			}
		}else{
			DISPLAYLENGTH = HEALTH;
			healthBarGreen = (int)(HEALTH/800.0*255.0);
			healthBarRed = 255 - healthBarGreen;
			healthBarGreen = (int)Game.clamp(healthBarGreen, 0, 255);
			healthBarRed = (int)Game.clamp(healthBarRed, 0, 255);
			healthBarBlue = 0;
		}
	}



	public void render(Graphics g){
		/*
		g.setColor(Color.gray);
		g.fillRect(15, 15, 200, 32);
		g.setColor(new Color(redValue,greenValue,0));
		g.fillRect(15, 15, HEALTH/10*2, 32);
		g.setColor(Color.white);
		g.drawRect(15, 15, 200, 32);

		 */


		g.setColor(Color.white);
		g.drawString("Fps: "+fps, 15,20+16*0);
		g.drawString("Ghost After: "+Ghost.chase.size(), 15,20+16*1);
		g.drawString("Objects: "+object, 15,20+16*2);
		g.drawString(1000000000/Game.ns+"", 15,20+16*3);
		
		g.setFont(new Font("Bernard MT Condensed", Font.BOLD, 64));
		g.setColor(new Color(redValue,greenValue,blueValue));
		g.drawString(convertTime(TIMEINSECOND), Game.WIDTH/2-79, 180);

		g.setFont(new Font("Bernard MT Condensed", Font.BOLD, 42));
		g.drawString("Score:",  Game.WIDTH/2-52 ,Game.HEIGHT-200);
		g.drawString(String.format("%07d",SCORE),  Game.WIDTH/2-79 ,Game.HEIGHT-150);
		g.setColor(Color.black);
		
		g.fillRect(Game.WIDTH/2-79, 220, 160, 20);
		g.setColor(new Color(healthBarRed,healthBarGreen,healthBarBlue));
		g.fillRect(Game.WIDTH/2-79, 220, DISPLAYLENGTH/10*2, 20);
		g.setColor(Color.white);
		g.drawRect(Game.WIDTH/2-79, 220, 160, 20);
		
		
		

	}

	private String convertTime(int seconds){
		int m = seconds/60;
		int s = seconds - m*60;
		return String.format("%02d:%02d",m,s);
	}


	public int getTIMEINSECOND() {
		return TIMEINSECOND;
	}

	public void setTIMEINSECOND(int tIMEINSECOND) {
		this.TIMEINSECOND = tIMEINSECOND;
	}
	public int getFps() {
		return fps;
	}

	public void setFps(int fps) {
		this.fps = fps;
	}

	public int getObject() {
		return object;
	}

	public void setObject(int object) {
		this.object = object;
	}




}






