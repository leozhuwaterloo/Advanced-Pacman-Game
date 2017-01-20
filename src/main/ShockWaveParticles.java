package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class ShockWaveParticles extends GameObject{
	
	private float orginX;
	private float orginY;
	private float radius;
	
	private Handler handler;
	private Color color;
	private float life;
	private Random r = new Random();
	
	public ShockWaveParticles(float x, float y, ID id, Handler handler, float velX, float velY, Color color, float life, int radius) {
		super(x, y, id);
		this.orginX = x;
		this.orginY = y;
		this.radius = radius;
		this.handler = handler;
		this.velX = velX;
		this.velY = velY;
		this.color = color;
		this.life = life;
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x,(int)y,16,16);
	}

	@Override
	public void tick() {
		x += velX;
		y += velY;
		
		if(Math.pow(x-orginX, 2)+Math.pow(y-orginY, 2)>=Math.pow(radius, 2))
			handler.removeObject(this);
			
		handler.addObject(new Trail(x,y,ID.Trail,color,16,16,1,life,handler));
	}

	@Override
	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect((int)x, (int)y, 16, 16);
	}

	
	
	
}
