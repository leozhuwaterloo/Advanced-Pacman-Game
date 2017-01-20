package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Boss extends GameObject{

	public static boolean EATABLE = false;
	private final int radius = 80; 

	private boolean activated = false;
	private boolean drawGhost = true;
	private boolean inLine = false;
	private long currentTime;

	private Handler handler;
	public static final int WIDTH = 170, HEIGHT = 170;
	private int SPEED = 4;
	private BufferedImage[][] faceRight = new BufferedImage[4][2];
	private Image sleepImage[] = Initialize.sleepImage;
	private Image[][] image = Initialize.faceRight;
	private int currentImageIndex = 0, currentMouthIndex = 0;
	private int yMove = 0, yIndex = -1, sleepIndex = 0;
	private int timer = 0;
	private int lastTime = 0;
	private  Color color;
	private int number;
	private Random r = new Random();
	private ArrayList<String> beforeDirection;
	private String random = "0";
	private float initX, initY;

	private ArrayList<GhostFollowing> ghostFollow = new ArrayList<GhostFollowing>();

	public static ArrayList<Boss> chase = new ArrayList<>();

	public Boss(float x, float y, ID id, Handler handler, Color color) {
		super(x, y, id);
		this.initX = x;
		this.initY = y;
		this.handler = handler;
		this.color = color;
		this.beforeDirection = new ArrayList<String>();

	}


	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x,(int)y,WIDTH,HEIGHT);
	}

	@Override
	public void tick() {


		if(x>=Game.WIDTH-WIDTH||x<=0){
			SPEED *= (-1);
			if(SPEED>0)
				SPEED++;
			else if(SPEED<0)
				SPEED--;
		}

		velX = SPEED;
		x+=velX;
		timer++;
		
		if(timer%7==0){
			if(currentMouthIndex==0){
				currentMouthIndex = 1;
			}else{
				currentMouthIndex = 0;
			}
		}
		
		if(timer-lastTime == (100-Math.abs(SPEED*3))){
			System.out.println(timer-lastTime);
			if(timer-lastTime<70){
				handler.removeObject(this);
			}
			handler.addObject(new BossWave(x+WIDTH/2,y+HEIGHT/2,ID.Trail,Color.cyan,0.1f,handler,2000));
			lastTime = timer;
		}

	}


	@Override
	public void render(Graphics g) {

		Graphics2D g2d  = (Graphics2D) g;
		g2d.setColor(color);
		if (Game.RENDERBUBBLE){
			g2d.setComposite(makeTransparent(0.5f));
			g2d.fillOval((int)x-radius/2, (int)y-radius/2, (int)WIDTH + radius, (int)HEIGHT+ radius);
			g2d.setComposite(makeTransparent(1));
		}else{
			g2d.setComposite(makeTransparent(0.8f));
			g2d.fillOval((int)x, (int)y+yMove, (int)WIDTH, (int)HEIGHT-7);
			g2d.setComposite(makeTransparent(1));
			g2d.drawImage(image[currentImageIndex][currentMouthIndex].getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH),(int)x, (int)y+yMove, null);
		}

	}


	private AlphaComposite makeTransparent(float alpha){
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type,alpha));
	}
}




