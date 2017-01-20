package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class WaveShowMap extends GameObject{

	
	private Handler handler;
	private Color color;
	private float life;
	private Random r = new Random();
	public WaveShowMap(float x, float y, ID id, Handler handler, float velX, float velY, Color color, float life) {
		super(x, y, id);
		this.handler = handler;
		this.velX = velX;
		this.velY = velY;
		this.color = color;
		this.life = life;
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

}
