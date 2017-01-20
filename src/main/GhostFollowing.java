package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

public class GhostFollowing extends GameObject{

	public static ArrayList<GhostReal> realGhostChasing = new ArrayList<>();
	
	private Handler handler;
	private Color color;
	private float width, height;
	private int timer;
	private long time;
	private Image image;
	private int radius;
	private float currentX = x;
	private float currentY = y;
	private Ghost ghost;

	public GhostFollowing(float x, float y, ID id, Color color, Image image, int radius, float width, float height, long time, Handler handler,Ghost ghost) {
		super(x, y, id);
		this.handler = handler;
		this.color = color;
		this.width = width;
		this.height = height;
		this.timer = 0;
		this.time = time;
		this.image = image;
		this.radius = radius;
		this.ghost =ghost;
	}


	@Override
	public void tick() {
		timer++;
		if(timer==time){
			ghost.setDrawGhost(false);
			ghost.setX(x);
			ghost.setY(y);
			GhostReal delayedRealGhost = new GhostReal(x,y,ID.GhostChasing,color, image, radius, width, height,handler);
			realGhostChasing.add(delayedRealGhost);
			handler.addObject(delayedRealGhost);
			
			ghost.getGhostFollowing().remove(this);
			handler.removeObject(this);
		}
	}

	@Override
	public void render(Graphics g) {
		//g.fillRect((int)x,(int)y,10,10);
	}


	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)currentX, (int)currentY, (int)width, (int)height);
	}
	
	public void setTime(int time){
		this.time = time;
	}

}
