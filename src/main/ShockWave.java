package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class ShockWave extends GameObject{

	private float alpha = 1;
	private float life;

	private Handler handler;
	private Color color;


	private Random r = new Random();


	public ShockWave(float x, float y, ID id, Color color, float life, Handler handler, int radius) {
		super(x, y, id);
		this.handler = handler;
		this.color = color;
		this.life = life;
		this.color = color;
		
		
		double i=1;

		while(true){
			//bottom right
			handler.addObject(new ShockWaveParticles(x,y,ID.ShockWave,handler,(float)Math.abs(2*Math.sqrt(2)*Math.cos(Math.PI/i)),
					(float)Math.abs(2*Math.sqrt(2)*Math.sin(Math.PI/i)),color,life, radius));
			//bottom left
			handler.addObject(new ShockWaveParticles(x,y,ID.ShockWave,handler,-(float)Math.abs(2*Math.sqrt(2)*Math.cos(Math.PI/i)),
					(float)Math.abs(2*Math.sqrt(2)*Math.sin(Math.PI/i)),color,life, radius));
			//top right
			handler.addObject(new ShockWaveParticles(x,y,ID.ShockWave,handler,(float)Math.abs(2*Math.sqrt(2)*Math.cos(Math.PI/i)),
					-(float)Math.abs(2*Math.sqrt(2)*Math.sin(Math.PI/i)),color,life, radius));
			//top left
			handler.addObject(new ShockWaveParticles(x,y,ID.ShockWave,handler,-(float)Math.abs(2*Math.sqrt(2)*Math.cos(Math.PI/i)),
					-(float)Math.abs(2*Math.sqrt(2)*Math.sin(Math.PI/i)),color,life, radius));
			
			
			
			if(i==1)
				i=2;
			else if (i==2)
				i=12/5.0;
			else if (i==12/5.0)
				i=3;
			else if (i==3)
				i=4;
			else if (i==4)
				i=6;
			else if (i==6)
				i=12;
			else if (i==12)
				break;

		}


	}

	@Override
	public void tick(){		
		if(alpha > life){
			alpha -= life - 0.001f;
		}else{
			handler.removeObject(this);
		}

	}


	@Override
	public void render(Graphics g){

	}



	@Override
	public Rectangle getBounds() {
		return null;
	}
}
