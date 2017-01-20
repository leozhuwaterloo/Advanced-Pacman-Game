package main;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.UIManager;

public class PowerPellet extends GameObject{

	public static int WIDTH = 50, HEIGHT=50;
	private Color color;
	private int radius = 80;
	
	private int redValue, greenValue, blueValue;
	private int redValueChange;
	private int greenValueChange;
	private int blueValueChange;
	private int changeSpeed = 5;
	
	
	public PowerPellet(float x, float y, ID id, Color color) {
		super(x, y, id);
		this.redValue = color.getRed();
		this.greenValue = color.getGreen();
		this.blueValue = color.getBlue();
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x,(int)y,(int)WIDTH,(int)HEIGHT);
	}

	@Override
	public void tick() {
		if(redValue==255&&greenValue==0&&blueValue==0){
			redValueChange=0;
			greenValueChange=0;
			blueValueChange=changeSpeed;
		}else if(redValue==255&&greenValue==0&&blueValue==255){
			redValueChange=-changeSpeed;
			greenValueChange=0;
			blueValueChange=0;
		}else if(redValue==0&&greenValue==0&&blueValue==255){
			redValueChange=0;
			greenValueChange=changeSpeed;
			blueValueChange=0;
		}else if(redValue==0&&greenValue==255&&blueValue==255){
			redValueChange=0;
			greenValueChange=0;
			blueValueChange=-changeSpeed;
		}else if(redValue==0&&greenValue==255&&blueValue==0){
			redValueChange=changeSpeed;
			greenValueChange=0;
			blueValueChange=0;
		}else if(redValue==255&&greenValue==255&&blueValue==0){
			redValueChange=0;
			greenValueChange=-changeSpeed;
			blueValueChange=0;
		}

		redValue+=redValueChange;
		greenValue+=greenValueChange;
		blueValue+=blueValueChange;
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(redValue, greenValue,blueValue));
		if (Game.RENDERBUBBLE){
			g2d.setComposite(makeTransparent(0.5f));
			g2d.fillOval((int)x-radius/2-3, (int)y-radius/2, (int)WIDTH+radius, (int)HEIGHT+radius);
			g2d.setComposite(makeTransparent(1));
		}else{

			g2d.fillOval((int)x, (int)y, WIDTH, HEIGHT);
		}
	}

	private AlphaComposite makeTransparent(float alpha){
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type,alpha));
	}

}
