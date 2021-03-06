package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class WaveTrail extends GameObject{

	private float alpha = 1;
	private float life;

	private Handler handler;
	private Color color;
	private float width, height;

	public WaveTrail(float x, float y, ID id, Color color, float width, float height, float alpha, float life, Handler handler) {
		super(x, y, id);
		this.handler = handler;
		this.color = color;
		this.width = width;
		this.height = height;
		this.alpha = alpha;
		this.life = life;
	}


	@Override
	public void tick() {
		if(alpha> life){
			alpha -= life - 0.001f;
		}else{
			handler.removeObject(this);
		}
	}

	@Override
	public void render(Graphics g) {
		if(Game.RENDERBUBBLE){
			Graphics2D g2d= (Graphics2D) g;
			g2d.setComposite(makeTransparent(alpha));
			g.setColor(color);
			g.fillRect((int)x, (int)y, (int)width, (int)height);
			g2d.setComposite(makeTransparent(1));
		}
	}

	private AlphaComposite makeTransparent(float alpha){
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type,alpha));
	}



	@Override
	public Rectangle getBounds() {
		return null;
	}

}
