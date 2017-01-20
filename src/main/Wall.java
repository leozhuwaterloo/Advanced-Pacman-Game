package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.UIManager;

public class Wall extends GameObject{

	private float width, height;
	private Color color;

	public Wall(float x, float y, float width, float height, ID id, Color color) {
		super(x+10, y+10, id);

		this.width = width-10;
		this.height = height-10;
		this.color = color;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x-10,(int)y-10,(int)width+20,(int)height+20);
	}

	@Override
	public void tick() {

	}

	@Override
	public void render(Graphics g) {
		/*
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(10));
		g2d.drawRect((int)x, (int)y, (int)width, (int)height);
		g2d.setStroke(new BasicStroke(1));
		*/
	}

}
