package main;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public class TrailImage extends GameObject{

	private float alpha = 1;
	private float life;
	
	private Handler handler;
	private Image image;
	
	
	public TrailImage(float x, float y, ID id,Image image, float alpha, float life, Handler handler) {
		super(x, y, id);
		this.handler = handler;
		this.image = image;
		this.life = life;
		this.alpha = alpha;
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
		Graphics2D g2d= (Graphics2D) g;
		g2d.setComposite(makeTransparent(alpha));
		g.drawImage(image,(int)x, (int)y, null);
		g2d.setComposite(makeTransparent(1));
		
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
