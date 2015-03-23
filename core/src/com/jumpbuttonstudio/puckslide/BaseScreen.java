package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class BaseScreen implements Screen {
	Stage stage, hudStage;
	OrthographicCamera camera;
	Skin skin;
	World world;
	Box2DDebugRenderer renderer;

	public BaseScreen(PuckSlide game) {
		StretchViewport viewport = new StretchViewport(Constants.SCLWIDTH, Constants.SCLHEIGHT);
		stage = new Stage(viewport);
		StretchViewport hudViewport = new StretchViewport(Constants.WIDTH, Constants.HEIGHT);
		hudStage = new Stage(hudViewport);

		camera = (OrthographicCamera) stage.getCamera();

		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

		world = new World(new Vector2(0, -9.81f), false);
		
		renderer = new Box2DDebugRenderer();
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.step(1 / 60f, 3, 3);

		stage.act(delta);
		stage.draw();

		hudStage.act(delta);
		
		
//		renderer.render(world, camera.combined);
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
