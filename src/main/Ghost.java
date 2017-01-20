package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Ghost extends GameObject{

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

	private int redValue, greenValue, blueValue;
	private int redValueChange;
	private int greenValueChange;
	private int blueValueChange;
	private int changeSpeed = 5;

	private ArrayList<GhostFollowing> ghostFollow = new ArrayList<GhostFollowing>();

	public static ArrayList<Ghost> chase = new ArrayList<>();

	public Ghost(float x, float y, ID id, Handler handler, Color color) {
		super(x, y, id);
		this.handler = handler;
		this.color = color;

		this.redValue = color.getRed();
		this.greenValue = color.getGreen();
		this.blueValue = color.getBlue();
	}
	
	public static void setNoEat(){
		Ghost.EATABLE = false;
		Player.color = Color.yellow;
	}
	
	public static void setToEat(){
		Ghost.EATABLE = true;
		Player.color = Color.cyan;
		HUD.DISPLAYLENGTH = 800;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x,(int)y,WIDTH,HEIGHT);
	}

	@Override
	public void tick() {

		if (activated){
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

			float tempVelY = velY;
			float tempVelX = velX;

			if(KeyInput.wantedDirection==0){
				velY=-SPEED;
				velX=0;
			}else if(KeyInput.wantedDirection==1){
				velX=SPEED;
				velY=0;
			}else if(KeyInput.wantedDirection==2){
				velY=SPEED;
				velX=0;
			}else if(KeyInput.wantedDirection==3){
				velX=-SPEED;
				velY=0;
			}

			if(!checkAndSee()){
				velY = tempVelY;
				velX = tempVelX;
			}

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

			GhostFollowing trail= new GhostFollowing(Player.playerX, Player.playerY, ID.GhostFake, new Color(redValue, greenValue, blueValue),image[currentImageIndex][currentMouthIndex], radius, WIDTH, HEIGHT, 60+checkGhost()*10, handler,this);
			ghostFollow.add(trail);
			handler.addObject(trail);
		}else{
			if(Math.abs(y-Player.playerY)+Math.abs(x-Player.playerX)<=radius){
				if(!EATABLE){
					if(chase.size()<=44){
						activated = true;
						chase.add(this);
						Wave.ghostLeft.remove(this);
						Wave.ghostRight.remove(this);
					}
				}
			}

			timer++;
			if(timer%7==0){
				if(timer%49==0){
					if(sleepIndex==0){
						sleepIndex = 1;
					}else{
						sleepIndex = 0;
					}
				}

				if(yMove==5){
					yIndex=-1;	
				}else if(yMove==-5){
					yIndex=1;
				}
				yMove+=yIndex;
			}
		}


		collision();
	}



	private void collision(){

		if(getBounds().intersects(new Rectangle((int)Player.playerX,(int)Player.playerY,Player.WIDTH,Player.HEIGHT))){
			if(EATABLE){
				handler.removeObject(this);
				chase.remove(this);
				handler.object.removeAll(ghostFollow);
				ghostFollow.clear();
				HUD.SCORE+=100;
			}else{
				HUD.HEALTH-=1;
			}
		}
	}

	private boolean checkAndSee(){
		for(Wall everyWall: Game.leftWalls){
			if(new Rectangle((int)(Player.playerX+velX/SPEED),(int)(Player.playerY+velY/SPEED),WIDTH,HEIGHT).intersects(everyWall.getBounds())){
				return false;
			}
		}
		for(Wall everyWall: Game.rightWalls){
			if(new Rectangle((int)(Player.playerX+velX/SPEED),(int)(Player.playerY+velY/SPEED),WIDTH,HEIGHT).intersects(everyWall.getBounds())){
				return false;
			}
		}
		for(Wall everyWall: Game.borderWalls){
			if(new Rectangle((int)(Player.playerX+velX/SPEED),(int)(Player.playerY+velY/SPEED),WIDTH,HEIGHT).intersects(everyWall.getBounds())){
				return false;
			}
		}
		return true;
	}

	public void setDrawGhost(boolean myBoolean){
		this.drawGhost = myBoolean;
	}

	public ArrayList<GhostFollowing> getGhostFollowing(){
		return ghostFollow;
	}

	@Override
	public void render(Graphics g) {
		if(drawGhost){

			Graphics2D g2d  = (Graphics2D) g;
			if(!activated){
				g2d.setColor(color);
				if (Game.RENDERBUBBLE){
					g2d.setComposite(makeTransparent(0.2f));
					g2d.fillOval((int)x-radius/2, (int)y-radius/2, (int)WIDTH + radius, (int)HEIGHT+ radius);
					g2d.setComposite(makeTransparent(1));
				}else{
					g2d.setComposite(makeTransparent(0.5f));
					g2d.fillOval((int)x, (int)y+yMove, (int)WIDTH, (int)HEIGHT-7);
					g2d.setComposite(makeTransparent(1));
					g2d.drawImage(sleepImage[sleepIndex],(int)x, (int)y+yMove, null);
				}
			}else{
				g2d.drawImage(image[currentImageIndex][currentMouthIndex],(int)x, (int)y+yMove, null);
			}
		}
	}

	public int checkGhost(){
		for(int i=0; i<chase.size();i++){
			if(chase.get(i)==this){
				return i;
			}
		}
		return -999;
	}

	private AlphaComposite makeTransparent(float alpha){
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type,alpha));
	}

	public int getNumber(){
		return number;
	}

	public boolean getActivated(){
		return activated;
	}
}




