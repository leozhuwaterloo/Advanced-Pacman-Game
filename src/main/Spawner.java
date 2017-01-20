package main;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;

public class Spawner {

	private Handler handler;
	private HUD hud;
	private Random r = new Random(0);

	private int scoreKeep = 0;

	public Spawner(Handler handler, HUD hud){
		this.handler = handler;
		this.hud = hud;
	}

	private Color color;

	public void tick(){
		scoreKeep++;

		if(scoreKeep >= 300){
			
			scoreKeep = 0;
			color = new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255));
			//handler.addObject(new WaveShowMap(0, 0, ID.ShockWave, handler, 20, 0, color, 0.002f));
			//handler.addObject(new WaveShowMap(Game.WIDTH, 0, ID.ShockWave, handler, -20, 0, color, 0.002f));




			/*
			if(hud.getLevel()==2){
				handler.addObject(new Wave(Game.WIDTH/2, 0, ID.ShockWave, handler, -10, 0, Color.green, 0.002f));
			} else if(hud.getLevel()==3){
				handler.addObject(new Wave(Game.WIDTH/2, 0, ID.ShockWave, handler, 10, 0, Color.green, 0.002f));
			}
			 */
			//else if(hud.getLevel()>=4){
			//handler.addObject(new ShockWave(Player.playerX+18,Player.playerY+15,ID.Trail,Color.white,0.1f,handler,100));
			//}

		}
	}
}





