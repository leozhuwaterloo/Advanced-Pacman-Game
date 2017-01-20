package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

public class GhostReal extends GameObject{

	private Handler handler;
	private float width, height;
	private Image image;
	private int radius;
	private Color color;
	private int timer;

	Random r = new Random();

	public GhostReal(float x, float y, ID id, Color color, Image image, int radius, float width, float height, Handler handler) {
		super(x, y, id);
		this.handler = handler;
		this.color = color;
		this.width = width;
		this.height = height;
		this.image = image;
		this.radius = radius;
	}


	@Override
	public void tick() {

		if(timer==3){
			handler.removeObject(this);
			GhostFollowing.realGhostChasing.remove(this);
		}
		timer++;
		
	}


	@Override
	public void render(Graphics g) {

		Graphics2D g2d  = (Graphics2D) g;
		g2d.setColor(color);
		if (Game.RENDERBUBBLE){
			g2d.setComposite(makeTransparent(0.2f));
			g2d.fillOval((int)x-radius/2, (int)y-radius/2, (int)width + radius, (int)height+ radius);
			g2d.setComposite(makeTransparent(1));
		}else{
			g2d.setComposite(makeTransparent(0.5f));
			g2d.fillOval((int)x, (int)y, (int)width, (int)height-7);
			g2d.setComposite(makeTransparent(1));
			g2d.drawImage(image,(int)x, (int)y, null);
		}
		/*
		Graphics2D g2d= (Graphics2D) g;
		g2d.setComposite(makeTransparent(alpha));
		g.setColor(color);
		g.fillRect((int)x, (int)y, (int)width, (int)height);
		g2d.setComposite(makeTransparent(1));
		 */
	}

	private AlphaComposite makeTransparent(float alpha){
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type,alpha));
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}

}
