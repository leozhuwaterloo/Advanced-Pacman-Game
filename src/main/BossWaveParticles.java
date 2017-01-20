package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class BossWaveParticles extends GameObject{

	private float orginX;
	private float orginY;
	private float radius;

	private Handler handler;
	private Color color;
	private float life;
	private Random r = new Random();
	private int timer;

	public BossWaveParticles(float x, float y, ID id, Handler handler, float velX, float velY, Color color, float life) {
		super(x, y, id);
		this.orginX = x;
		this.orginY = y;
		this.handler = handler;
		this.velX = velX;
		this.velY = velY;
		this.color = color;
		this.life = life;

		this.timer = 0;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x,(int)y,16,16);
	}

	@Override
	public void tick() {
		x += velX;
		y += velY;

		if(x<=0||x>=Game.WIDTH||y<=0||y>=Game.HEIGHT)
			handler.removeObject(this);

		handler.addObject(new Trail(x,y,ID.Trail,color,16,16,1,life,handler));

		timer++;

		//if(timer%200==0){
			//handler.addObject(new BossWave(x,y,ID.Trail,Color.cyan,0.1f,handler,2000));
		//}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect((int)x, (int)y, 16, 16);
	}




}
