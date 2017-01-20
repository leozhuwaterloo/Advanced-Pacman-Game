package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game extends Canvas implements Runnable{

	public static ArrayList<Wall> leftWalls= new ArrayList<>();
	public static ArrayList<Wall> rightWalls= new ArrayList<>();
	public static ArrayList<Wall> borderWalls= new ArrayList<>();

	public static ArrayList<SmartGhost> smartGhost = new ArrayList<>();

	public static Toolkit tk = Toolkit.getDefaultToolkit();

	public final static int WIDTH = 1920;
	public final static int HEIGHT =  1080;


	public static boolean RENDERBUBBLE = false;

	public final static int WPixel = WIDTH/32;
	public final static int HPixel = HEIGHT/18;
	public final static int initX = WIDTH/64-5;
	public final static int initY = HEIGHT/36-5;

	private Thread thread;
	private boolean running = false;
	private boolean initRender = false;

	private Random r = new Random();
	private Handler handler;
	private HUD hud;
	private Spawner spawner;
	private Menu menu;
	public static PelletGenerator pelletGenerator;

	public enum STATE{
		Menu,
		Game
	};

	public STATE gameState = STATE.Game;

	public Game(){

		handler = new Handler();
		menu = new Menu(this, handler);
		this.addKeyListener(new KeyInput(handler));
		this.addMouseListener(menu);

		new Window(WIDTH, HEIGHT, "Let's Build a Game!", this);

		hud = new HUD();
		spawner = new Spawner(handler, hud);
		pelletGenerator = new PelletGenerator(handler);

		if(gameState == STATE.Game){
			Scanner input = null;
			try {
				input = new Scanner(new File("coords\\maze.txt"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}


			input.next();
			handler.addObject(new Player( WPixel*input.nextInt()+initX+10,  HPixel*input.nextInt()+initY+10,ID.Player,handler));
			input.next();
			while(input.hasNextInt()){
				handler.addObject(new Ghost(WPixel*input.nextInt()+initX+10,HPixel*input.nextInt()+initY+10,ID.GhostSleep, handler, new Color(input.nextInt(), input.nextInt(), input.nextInt())));
			}
			input.next();
			while(input.hasNextInt()){
				Wall wall = new Wall(Game.WPixel*input.nextInt()+ Game.initX, Game.HPixel*input.nextInt()+Game.initY,Game.WPixel*input.nextInt(),Game.HPixel*input.nextInt(), ID.Wall, Color.cyan);
				borderWalls.add(wall);
				handler.addObject(wall);
			}
			input.close();

			for(Wall everyWall: Initialize.wallCoord.get(Initialize.wall[0][Initialize.wallLeftType])){
				handler.addObject(everyWall);
				leftWalls.add(everyWall);
			}

			for(Wall everyWall: Initialize.wallCoord.get(Initialize.wall[1][Initialize.wallRightType])){
				handler.addObject(everyWall);
				rightWalls.add(everyWall);
			}

			handler.addObject(new Fruit(Game.WPixel*(15+14)+Game.initX+10,Game.HPixel*(8)+Game.initY+10,ID.Fruit, Color.red));

			pelletGenerator.generateLeft(r.nextInt(30+1)+20);
			pelletGenerator.generatRight(r.nextInt(30+1)+20);

			/*
			SmartGhost ghostRed = new SmartGhost(Game.WPixel*(15)+Game.initX+10,Game.HPixel*(7)+Game.initY+10,ID.GhostChasing, handler, Color.red);
			SmartGhost ghostPink = new SmartGhost(Game.WPixel*(16)+Game.initX+10,Game.HPixel*(7)+Game.initY+10,ID.GhostChasing, handler, Color.PINK);
			SmartGhost ghostCyan = new SmartGhost(Game.WPixel*(14)+Game.initX+10,Game.HPixel*(7)+Game.initY+10,ID.GhostChasing, handler, Color.CYAN);

			smartGhost.add(ghostRed);
			smartGhost.add(ghostPink);
			smartGhost.add(ghostCyan);

			handler.addObject(ghostRed);
			handler.addObject(ghostPink);
			handler.addObject(ghostCyan);
			*/
			
			handler.addObject(new Boss(Game.WPixel*(14)+Game.initX+10,Game.HPixel*(0)+Game.initY+10,ID.GhostChasing, handler, Color.CYAN));
		}
	}


	public synchronized void start(){
		thread = new Thread(this);
		thread.start();
		running = true;

	}

	public synchronized void stop(){
		try{
			thread.join();
			running = false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static double ns = 1000000000/60.0;

	@Override
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running){

			long now = System.nanoTime();
			delta += (now-lastTime) /ns;
			lastTime = now;
			while(delta>=1){
				tick();
				delta--;
			}
			if(running)
				render();
			frames++;


			if(System.currentTimeMillis() - timer >1000){
				timer+=1000;
				hud.setFps(frames);
				hud.setObject(handler.object.size());
				hud.setTIMEINSECOND(hud.getTIMEINSECOND()+1);
				//System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}

	private void tick(){
		handler.tick();
		if(gameState == STATE.Game){
			hud.tick();
			spawner.tick();
		}else if(gameState == STATE.Menu){
			menu.tick();
		}
	}

	public static boolean checkChasingGhost(){
		for(GhostReal ghost: GhostFollowing.realGhostChasing){
			if(Math.abs(ghost.getY()-Player.playerY)+Math.abs(ghost.getX()-Player.playerX)<=110){
				return true;
			}
		}
		return false;
	}

	private void render(){
		BufferStrategy bs= this.getBufferStrategy();
		if(bs==null){
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		//g.setColor(Color);
		//g.fillRect(0, 0, WIDTH, HEIGHT);
		//g.drawImage(Initialize.wallAnimated, 0, 0, null);
		RENDERBUBBLE = true;


		g.drawImage(Initialize.wall[0][Initialize.wallLeftType][1], 0, 0, null);
		g.drawImage(Initialize.wall[1][Initialize.wallRightType][1], Game.WIDTH/2, 0, null);

		if(checkChasingGhost()){
			if(!Ghost.EATABLE){
				if(Player.color == Color.yellow||Player.color == Color.cyan){
					Player.color = Color.red;
					ns = 1000000000/40.0;
				}
			}
		}else{
			if(Player.color == Color.red){
				Player.color = Color.yellow;
				ns = 1000000000/60.0;
			}
		}


		handler.render(g);	
		g.drawImage(Initialize.wall[0][Initialize.wallLeftType][0], 0, 0, null);
		g.drawImage(Initialize.wall[1][Initialize.wallRightType][0], Game.WIDTH/2, 0, null);

		RENDERBUBBLE = false;
		handler.render(g);



		if(gameState == STATE.Game){
			hud.render(g);
		}else if(gameState == STATE.Menu){
			menu.render(g);
		}

		g.dispose();
		bs.show();
	}


	public static float clamp(float var, float min, float max){
		if(var>=max)
			return var = max;
		else if(var<=min)
			return var = min;
		else
			return var;

	}


	public static void main(String[] args){
		//System.out.println("Heap Size = " + Runtime.getRuntime().totalMemory());
		Initialize.init();
		Music.init();
		Music.playBGMusic(-10.0f);
		new Game();
	}
}




