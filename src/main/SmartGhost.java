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

public class SmartGhost extends GameObject{

	public static boolean EATABLE = false;
	private final int radius = 80; 

	private boolean activated = false;
	private boolean drawGhost = true;
	private boolean inLine = false;
	private long currentTime;

	private Handler handler;
	public static final int WIDTH = 50, HEIGHT = 50;
	private final int SPEED = 4;
	private BufferedImage[][] faceRight = new BufferedImage[4][2];
	private Image sleepImage[] = Initialize.sleepImage;
	private Image[][] image = Initialize.image;
	private int currentImageIndex = 0, currentMouthIndex = 0;
	private int yMove = 0, yIndex = -1, sleepIndex = 0;
	private int timer = 0;
	private  Color color;
	private int number;
	private Random r = new Random();
	private ArrayList<String> beforeDirection;
	private String random = "0";
	private float initX, initY;

	private ArrayList<GhostFollowing> ghostFollow = new ArrayList<GhostFollowing>();

	public static ArrayList<SmartGhost> chase = new ArrayList<>();

	public SmartGhost(float x, float y, ID id, Handler handler, Color color) {
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

		if(velY<0){
			currentImageIndex = 0;
		}else if(velY>0){
			currentImageIndex = 2;
		}

		if(velX<0){
			currentImageIndex = 3;
		}else if(velX>0){
			currentImageIndex = 1;
		}

		timer++;
		if(timer%7==0){
			if(currentMouthIndex==0){
				currentMouthIndex = 1;
			}else{
				currentMouthIndex = 0;
			}
		}

		if(x>Game.WPixel*(13)+Game.initX+10&&x<Game.WPixel*(18)+Game.initY+10&&y>Game.HPixel*(5)+Game.initY+10&&y<Game.HPixel*(11)+Game.initY+10){
			velY = -SPEED;
			y+=velY;
		}else{

			ArrayList<String> possibleDirection  = new ArrayList<>();

			if(checkAndSee(0,-SPEED)){
				possibleDirection.add("0");
			}
			if(checkAndSee(0,SPEED)){
				possibleDirection.add("2");
			}
			if(checkAndSee(SPEED,0)){
				possibleDirection.add("1");
			}
			if(checkAndSee(-SPEED,0)){
				possibleDirection.add("3");
			}

			if(x>Player.playerX){
				//possibleDirection.remove("1");
				checkPossibleAndAdd(possibleDirection,"0",1);
				checkPossibleAndAdd(possibleDirection,"2",1);
				checkPossibleAndAdd(possibleDirection,"3",10);
			}else if(x<Player.playerX){
				//possibleDirection.remove("3");
				checkPossibleAndAdd(possibleDirection,"0",1);
				checkPossibleAndAdd(possibleDirection,"1",10);
				checkPossibleAndAdd(possibleDirection,"2",1);
			}else{
				checkPossibleAndAdd(possibleDirection,"0",2);
				checkPossibleAndAdd(possibleDirection,"2",2);
			}

			if(y>Player.playerY){
				//possibleDirection.remove("2");
				checkPossibleAndAdd(possibleDirection,"0",10);
				checkPossibleAndAdd(possibleDirection,"1",1);
				checkPossibleAndAdd(possibleDirection,"3",1); 
			}else if(y<Player.playerY){
				//possibleDirection.remove("0");
				checkPossibleAndAdd(possibleDirection,"1",1);
				checkPossibleAndAdd(possibleDirection,"2",10);
				checkPossibleAndAdd(possibleDirection,"3",1);
			}else{
				checkPossibleAndAdd(possibleDirection,"1",2);
				checkPossibleAndAdd(possibleDirection,"3",2);
			}



			if(!beforeDirection.equals(possibleDirection)&&possibleDirection.size()>4){
				String noDirection = Integer.toString((Integer.parseInt(random)+2)%4);
				do{
					random = possibleDirection.get(r.nextInt(possibleDirection.size()));
				}while(random.equals(noDirection));

				//System.out.println(possibleDirection);
				//System.out.println(random);


				if(random.equals("0")){
					velX=0;
					velY=-SPEED;
				}else if(random.equals("1")){
					velX=SPEED;
					velY=0;
				}else if(random.equals("2")){
					velX=0;
					velY=SPEED;
				}else if(random.equals("3")){
					velX=-SPEED;
					velY=0;
				}

				beforeDirection.clear();
				beforeDirection.addAll(possibleDirection);

			}

			if(checkAndSee((int)velX,(int)velY)){
				x+=velX;
				y+=velY;
			}
			
			while(!checkAndSee(0, 0)){
				x+=velX;
				y+=velY;
			}
			
		}
		
		

		if(x<=0)
			x+=Game.WIDTH;
		if(x>=Game.WIDTH-WIDTH)
			x-=Game.WIDTH;

		collision();

		if(velX!=0||velY!=0)
			handler.addObject(new TrailImage(x,y,ID.Trail,image[currentImageIndex][currentMouthIndex],0.1f,0.005f,handler));
	}


	private void checkPossibleAndAdd(ArrayList<String> possibleDirection, String string,int times){
		if(possibleDirection.contains(string)){
			for(int i =1; i<=times;i++)
				possibleDirection.add(string);
		}
	}


	private boolean checkAndSee(int velX, int velY){
		for(Wall everyWall: Game.leftWalls){
			if(new Rectangle((int)(x+velX),(int)(y+velY),WIDTH,HEIGHT).intersects(everyWall.getBounds())){
				return false;
			}
		}
		for(Wall everyWall: Game.rightWalls){
			if(new Rectangle((int)(x+velX),(int)(y+velY),WIDTH,HEIGHT).intersects(everyWall.getBounds())){
				return false;
			}
		}
		for(Wall everyWall: Game.borderWalls){
			if(new Rectangle((int)(x+velX),(int)(y+velY),WIDTH,HEIGHT).intersects(everyWall.getBounds())){
				return false;
			}
		}
		return true;
	}


	private void collision(){

		if(new Rectangle((int)x+10,(int)y+10,WIDTH-20,HEIGHT-20).intersects(new Rectangle((int)Player.playerX,(int)Player.playerY,Player.WIDTH,Player.HEIGHT))){
			if(EATABLE){
				handler.removeObject(this);
				chase.remove(this);
				handler.object.removeAll(ghostFollow);
				ghostFollow.clear();
				HUD.SCORE+=100;
			}else{
				HUD.HEALTH-=50;
				x =  initX;
				y = initY;
				handler.addObject(new ShockWave(Player.playerX+18,Player.playerY+15,ID.Trail,color,0.1f,handler,100));
			}
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
			g2d.drawImage(image[currentImageIndex][currentMouthIndex],(int)x, (int)y+yMove, null);
		}

	}


	private AlphaComposite makeTransparent(float alpha){
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type,alpha));
	}
}




