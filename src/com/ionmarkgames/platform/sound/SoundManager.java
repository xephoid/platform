package com.ionmarkgames.platform.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

	private static Music introMusic;
	private static Music levelMusic;
	private static Music victoryMusic;
	
	private static Sound jumpSound;
	private static Sound coinSound;
	private static Sound failSound;
	private static Sound powerUpSound;
	private static Sound stompSound;
	private static Sound treasureChestSound;

	
	public static void loadSounds() {
		//introMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/Gothama1aa.mp3"));
		levelMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/theme1.mp3"));
		//victoryMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/Gothama3win.mp3"));
		
		jumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/jump2.wav"));
		coinSound = Gdx.audio.newSound(Gdx.files.internal("sounds/coin5.wav"));
		failSound = Gdx.audio.newSound(Gdx.files.internal("sounds/victory_fanfare.mp3"));
		powerUpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laugh.mp3"));
		stompSound = Gdx.audio.newSound(Gdx.files.internal("sounds/thump.wav"));
		treasureChestSound = Gdx.audio.newSound(Gdx.files.internal("sounds/switch_click.wav"));
	}
	
	public static void playIntroMusic() {
		introMusic.setLooping(true);
		introMusic.play();
	}
	
	public static void stopIntroMusic() {
		introMusic.stop();
	}
	
	public static void playLevelMusic() {
		levelMusic.setLooping(true);
		levelMusic.play();
	}
	
	public static void stopLevelMusic() {
		levelMusic.pause();
	}
	
	public static void playVictoryMusic() {
		victoryMusic.setLooping(true);
		victoryMusic.play();
	}
	
	public static void stopVictoryMusic() {
		victoryMusic.stop();
	}	
	
	
	public static void playJumpSound() {
		jumpSound.play();
	}
	public static void playCoinSound() {
		coinSound.play();
	}	
	public static void playFailSound() {
		failSound.play();
	}	
	public static void playPowerUpSound() {
		powerUpSound.play();
	}	
	public static void playStompSound() {
		stompSound.play();
	}	
	public static void playTreasureChestSound() {
		treasureChestSound.play();
	}	

}
