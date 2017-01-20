package main;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Music{

	private static Clip BGMusicPlayer;
	
	public static void init(){
		try {
			AudioInputStream audioBG = AudioSystem.getAudioInputStream(new File("sounds//BGMusic.wav").getAbsoluteFile());
			BGMusicPlayer= AudioSystem.getClip();
			BGMusicPlayer.open(audioBG);
		} catch (UnsupportedAudioFileException | IOException |LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public static void playBGMusic(float sound){
		FloatControl gainControl = (FloatControl) BGMusicPlayer.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(sound);
		BGMusicPlayer.loop(Clip.LOOP_CONTINUOUSLY);
		BGMusicPlayer.start();
		
	}
}






