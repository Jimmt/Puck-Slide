package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PuckSlide extends Game {
	public static SoundManager soundManager;
	public static IGoogleServices services;
	
	public PuckSlide(IGoogleServices services){
		PuckSlide.services = services;
	}

	@Override
	public void create() {

		soundManager = new SoundManager();
		soundManager.loadSound("button", Gdx.files.internal("SFX/Button.wav"));
		soundManager.loadSound("compliment", Gdx.files.internal("SFX/Compliment.wav"));
		soundManager.loadSound("lose", Gdx.files.internal("SFX/Lose.wav"));
		soundManager.loadSound("puckhit", Gdx.files.internal("SFX/Puckhit.wav"));
		soundManager.loadSound("success", Gdx.files.internal("SFX/Success.wav"));
		soundManager.loadSound("tiles", Gdx.files.internal("SFX/Tiles.wav"));

		soundManager.loadMusic("menu", Gdx.files.internal("SFX/Mainmenu.mp3"));
		soundManager.loadMusic("ingame", Gdx.files.internal("SFX/Ingame.mp3"));

		setScreen(new GameScreen(this, false));
	}

}
