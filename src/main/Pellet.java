package main;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.UIManager;

public class Pellet extends GameObject{

	public static int WIDTH = 20, HEIGHT=20;
	private Color color;
	private int radius = 100;


	public Pellet(float x, float y, ID id, Color color) {
		super(x, y, id);
		this.color = color;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x,(int)y,(int)WIDTH,(int)HEIGHT);
	}

	@Override
	public void tick() {

	}

	@Override
	public void render(Graphics g) {
		if (!Game.RENDERBUBBLE){

			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(color);

			g2d.fillOval((int)x-WIDTH/2+Game.WIDTH/32/2-5, (int)y-HEIGHT/2+Game.HEIGHT/18/2-5, WIDTH, HEIGHT);
		}
	}

	private AlphaComposite makeTransparent(float alpha){
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type,alpha));
	}

}
