package main;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class KeyInput extends KeyAdapter{

	private Handler handler;
	public static int wantedDirection=1;

	public Random r = new Random();

	public KeyInput(Handler handler){
		this.handler = handler;
	}

	public void keyPressed(KeyEvent e){
		int key  = e.getKeyCode();
		int location = e.getKeyLocation();

		if(key==KeyEvent.VK_W){
			wantedDirection=0;
		}
		if(key==KeyEvent.VK_D){
			wantedDirection=1;
		}
		if(key==KeyEvent.VK_S){
			wantedDirection=2;
		}
		if(key==KeyEvent.VK_A){
			wantedDirection=3;
		}


		if(key==KeyEvent.VK_UP){
			wantedDirection=0;
		}
		if(key==KeyEvent.VK_RIGHT){
			wantedDirection=1;
		}
		if(key==KeyEvent.VK_DOWN){
			wantedDirection=2;
		}
		if(key==KeyEvent.VK_LEFT){
			wantedDirection=3;
		}



		if(key==KeyEvent.VK_ESCAPE)
			System.exit(0);

		if(key==KeyEvent.VK_SHIFT&&location==KeyEvent.KEY_LOCATION_LEFT){
			handler.addObject(new Wave(Game.WIDTH/2, 0, ID.ShockWave, handler, -20, 0, Color.GREEN, 0.002f));
		}

		if(key==KeyEvent.VK_SHIFT&&location==KeyEvent.KEY_LOCATION_RIGHT){
			handler.addObject(new Wave(Game.WIDTH/2, 0, ID.ShockWave, handler, 20, 0, Color.GREEN, 0.002f));
		}

		if(key==KeyEvent.VK_CONTROL&&location==KeyEvent.KEY_LOCATION_LEFT){
			for(int i=1;i<=44;i++)
				handler.addObject(new Ghost(Game.WPixel*(5)+Game.initX+10,Game.HPixel*(13)+Game.initY+10,ID.GhostSleep, handler,Color.cyan));
		}
		if(key==KeyEvent.VK_CONTROL&&location==KeyEvent.KEY_LOCATION_RIGHT){
			if(Ghost.EATABLE){
				Ghost.setNoEat();
			}else{
				Ghost.setToEat();
			}
		}


	}



}








