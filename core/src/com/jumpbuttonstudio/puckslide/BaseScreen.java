package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class BaseScreen implements Screen {
	Stage stage, hudStage;
	OrthographicCamera camera;
	Skin skin;

	public BaseScreen(PuckSlide game) {
		StretchViewport viewport = new StretchViewport(Constants.SCLWIDTH, Constants.SCLHEIGHT);
		stage = new Stage(viewport);
		StretchViewport hudViewport = new StretchViewport(Constants.WIDTH, Constants.HEIGHT);
		hudStage = new Stage(hudViewport);

		camera = (OrthographicCamera) stage.getCamera();
		
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();

		hudStage.act(delta);
		hudStage.draw();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
