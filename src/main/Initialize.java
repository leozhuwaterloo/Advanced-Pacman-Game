package main;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Initialize {

	public static final int TOTALKINDSOFWALLS = 4;
	public static Image fruit;
	public static Image sleepImage[] = new Image[2];
	public static Image[][] image = new Image[4][2];

	public static int wallLeftType = 3;
	public static int wallRightType = 3;
	
	public static BufferedImage[][] faceRight = new BufferedImage[4][2];
	
	public static Image[][][] wall = new Image[2][TOTALKINDSOFWALLS][2];

	public static HashMap<Image[], ArrayList<Wall>> wallCoord = new HashMap<Image[], ArrayList<Wall>>();
	public static boolean powerPelletGenerated = false;

	public static void init(){

		
		try {
			
			fruit = ImageIO.read(new File("images\\Cherry.png")).getScaledInstance(Fruit.WIDTH-5, Fruit.HEIGHT, Image.SCALE_SMOOTH);
			
			faceRight[0][0] = ImageIO.read(new File("images\\GhostUp.png"));
			faceRight[1][0] = ImageIO.read(new File("images\\GhostRight.png"));
			faceRight[2][0] = ImageIO.read(new File("images\\GhostDown.png"));
			faceRight[3][0] = ImageIO.read(new File("images\\GhostLeft.png"));


			faceRight[0][1] = ImageIO.read(new File("images\\GhostUp2.png"));
			faceRight[1][1] = ImageIO.read(new File("images\\GhostRight2.png"));
			faceRight[2][1] = ImageIO.read(new File("images\\GhostDown2.png"));
			faceRight[3][1] = ImageIO.read(new File("images\\GhostLeft2.png"));

			sleepImage[0] = ImageIO.read(new File("images\\GhostSleep.png")).getScaledInstance(Ghost.WIDTH,Ghost.HEIGHT, Image.SCALE_SMOOTH);
			sleepImage[1] = ImageIO.read(new File("images\\GhostSleep2.png")).getScaledInstance(Ghost.WIDTH,Ghost.HEIGHT, Image.SCALE_SMOOTH);

			for(int i= 0; i<4; i++){
				image[i][0] = faceRight[i][0].getScaledInstance(Ghost.WIDTH,Ghost.HEIGHT, Image.SCALE_SMOOTH);
				image[i][1] = faceRight[i][1].getScaledInstance(Ghost.WIDTH,Ghost.HEIGHT, Image.SCALE_SMOOTH);
			}
			

			wall[0][0][0] = ImageIO.read(new File("walls\\WallEmptyLeft0.png"));
			wall[0][0][1] = ImageIO.read(new File("walls\\WallLeft0.png"));
			wallCoord.put(wall[0][0],readFile("coords\\wallLeft0.txt"));

			wall[0][1][0] = ImageIO.read(new File("walls\\WallEmptyLeft1.png"));
			wall[0][1][1] = ImageIO.read(new File("walls\\WallLeft1.png"));
			wallCoord.put(wall[0][1],readFile("coords\\wallLeft1.txt"));
			
			wall[0][2][0] = ImageIO.read(new File("walls\\WallEmptyLeft2.png"));
			wall[0][2][1] = ImageIO.read(new File("walls\\WallLeft2.png"));
			wallCoord.put(wall[0][2],readFile("coords\\wallLeft2.txt"));
			
			wall[0][3][0] = ImageIO.read(new File("walls\\WallEmptyLeft3.png"));
			wall[0][3][1] = ImageIO.read(new File("walls\\WallLeft3.png"));
			wallCoord.put(wall[0][3],readFile("coords\\wallLeft3.txt"));
			
			
			wall[1][0][0] = ImageIO.read(new File("walls\\WallEmptyRight0.png"));
			wall[1][0][1] = ImageIO.read(new File("walls\\WallRight0.png"));
			wallCoord.put(wall[1][0],readFile("coords\\wallRight0.txt"));
			
			wall[1][1][0] = ImageIO.read(new File("walls\\WallEmptyRight1.png"));
			wall[1][1][1] = ImageIO.read(new File("walls\\WallRight1.png"));
			wallCoord.put(wall[1][1],readFile("coords\\wallRight1.txt"));
			
			wall[1][2][0] = ImageIO.read(new File("walls\\WallEmptyRight2.png"));
			wall[1][2][1] = ImageIO.read(new File("walls\\WallRight2.png"));
			wallCoord.put(wall[1][2],readFile("coords\\wallRight2.txt"));
			
			wall[1][3][0] = ImageIO.read(new File("walls\\WallEmptyRight3.png"));
			wall[1][3][1] = ImageIO.read(new File("walls\\WallRight3.png"));
			wallCoord.put(wall[1][3],readFile("coords\\wallRight3.txt"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static ArrayList<Wall> readFile(String fileName){
		Scanner input = null;
		try {
			input = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ArrayList<Wall> wallsList = new ArrayList<>(); 
		while(input.hasNextInt()){
			Wall wall = new Wall(Game.WPixel*input.nextInt()+ Game.initX, Game.HPixel*input.nextInt()+Game.initY,Game.WPixel*input.nextInt(),Game.HPixel*input.nextInt(), ID.Wall, Color.cyan);
			wallsList.add(wall);
		}
		input.close();
		
		return wallsList;
	}
}
