package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player extends GameObject{

	public static float playerX, playerY;
	private int radius = 80;

	private Handler handler;
	public static final int WIDTH = 50, HEIGHT = 50;
	public static int SPEED = 4;
	private BufferedImage[][] faceRight = new BufferedImage[4][2];

	private Image[][] image = new Image[4][2];
	private int currentImageIndex = 0, currentMouthIndex = 0;
	private int timer = 0;

	public Player(float x, float y, ID id, Handler handler) {
		super(x, y, id);
		this.handler = handler;
		playerX = x;
		playerY = y;

		try {
			faceRight[0][0] = ImageIO.read(new File("images\\pacManUp.png"));
			faceRight[1][0] = ImageIO.read(new File("images\\pacManRight.png"));
			faceRight[2][0] = ImageIO.read(new File("images\\pacManDown.png"));
			faceRight[3][0] = ImageIO.read(new File("images\\pacManLeft.png"));


			faceRight[0][1] = ImageIO.read(new File("images\\pacManUp2.png"));
			faceRight[1][1] = ImageIO.read(new File("images\\pacManRight2.png"));
			faceRight[2][1] = ImageIO.read(new File("images\\pacManDown2.png"));
			faceRight[3][1] = ImageIO.read(new File("images\\pacManLeft2.png"));

			for(int i= 0; i<4; i++){
				image[i][0] = faceRight[i][0].getScaledInstance(WIDTH,HEIGHT, Image.SCALE_SMOOTH);
				image[i][1] = faceRight[i][1].getScaledInstance(WIDTH,HEIGHT, Image.SCALE_SMOOTH);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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


		x += velX;
		y += velY;
		playerX = x;
		playerY = y;

		if(x<=0)
			x+=Game.WIDTH;
		if(x>=Game.WIDTH-WIDTH)
			x-=Game.WIDTH;

		y = Game.clamp(y, 0, Game.HEIGHT-HEIGHT);

		if(velX!=0||velY!=0)
			handler.addObject(new TrailImage(x,y,ID.Trail,image[currentImageIndex][currentMouthIndex],0.1f,0.005f,handler));

		collision();
	}

	private boolean checkAndSee(){
		for(Wall everyWall: Game.leftWalls){
			if(new Rectangle((int)(x+velX/SPEED),(int)(y+velY/SPEED),WIDTH,HEIGHT).intersects(everyWall.getBounds())){
				return false;
			}
		}
		for(Wall everyWall: Game.rightWalls){
			if(new Rectangle((int)(x+velX/SPEED),(int)(y+velY/SPEED),WIDTH,HEIGHT).intersects(everyWall.getBounds())){
				return false;
			}
		}
		for(Wall everyWall: Game.borderWalls){
			if(new Rectangle((int)(x+velX/SPEED),(int)(y+velY/SPEED),WIDTH,HEIGHT).intersects(everyWall.getBounds())){
				return false;
			}
		}
		return true;
	}




	private void collision(){
		for(int i =0; i< handler.object.size();i++){
			GameObject tempObject = handler.object.get(i);
			
			/*
			else if(tempObject.getId() == ID.Ghost){
				if(Ghost.chase.size()>=10){ 
					if(tempObject!=null&&tempObject.getBounds()!=null){
						if(getBounds().intersects(tempObject.getBounds())){
							HUD.HEALTH-=1;
						}
					}
				}

			}
			 */



			if(tempObject.getId() == ID.Wall){
				if(getBounds().intersects(tempObject.getBounds())){
					x-=velX;
					y-=velY;
					velX=0;
					velY=0;
				}
			}

			else if(tempObject.getId() == ID.Fruit){
				if(getBounds().intersects(tempObject.getBounds())){
					handler.removeObject(tempObject);
					HUD.SCORE+=200;
					HUD.HEALTH+=50;
					if(tempObject.getX()>Game.WIDTH/2){
						handler.addObject(new Wave(Game.WIDTH/2, 0, ID.ShockWave, handler, -20, 0, Color.GREEN, 0.002f));
					}else if(tempObject.getX()<Game.WIDTH/2){
						handler.addObject(new Wave(Game.WIDTH/2, 0, ID.ShockWave, handler, 20, 0, Color.GREEN, 0.002f));
					}
				}
			}

			else if(tempObject.getId() == ID.PowerPellet){
				if(getBounds().intersects(tempObject.getBounds())){
					handler.removeObject(tempObject);
					HUD.SCORE+=100;
					Ghost.setToEat();
				}
			}
			
			else if(tempObject.getId() == ID.Pellet){
				if(getBounds().intersects(tempObject.getBounds())){
					handler.removeObject(tempObject);
					HUD.SCORE+=50;
					PelletGenerator.pelletsLeft.remove(tempObject);
					PelletGenerator.pelletsRight.remove(tempObject);
					
					if(PelletGenerator.pelletsLeft.size()+PelletGenerator.pelletsRight.size()==0){
						if(!Initialize.powerPelletGenerated){
							handler.addObject(new PowerPellet(Game.WPixel*(15)+Game.initX+10,Game.HPixel*(11)+Game.initY+10,ID.PowerPellet, Color.red));
							Initialize.powerPelletGenerated = true;
						}
					}
				}
			}
		}
	}


	public static Color color = Color.YELLOW;
	@Override
	public void render(Graphics g) {
		Graphics2D g2d  = (Graphics2D) g;
		g2d.setColor(color);
		if (Game.RENDERBUBBLE){
			g2d.setComposite(makeTransparent(0.8f));
			g2d.fillOval((int)x-radius/2, (int)y-radius/2, WIDTH + radius, HEIGHT+ radius);
			g2d.setComposite(makeTransparent(1));
		}else{
			g2d.setComposite(makeTransparent(1));
			g.drawImage(image[currentImageIndex][currentMouthIndex],(int)x, (int)y, null);
		}

		//g.setColor(Color.red);
		//g2d.draw(getBounds());

		//g.fillRect((int)x, (int)y, 32, 32);
	}

	private AlphaComposite makeTransparent(float alpha){
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type,alpha));
	}

}




