package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;

public class GameScreen extends BaseScreen {
	ButtonsDialog dialog;
	Background background;

	public GameScreen(PuckSlide game) {
		super(game);

		dialog = new ButtonsDialog(skin);
		dialog.show(hudStage);

		background = new Background(camera);
		stage.addActor(background);

		Gdx.input.setInputProcessor(new InputMultiplexer(hudStage, stage));
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		camera.update();
		if (Gdx.input.isKeyPressed(Keys.D)) {

			camera.position.set(camera.position.x + 0.1f, camera.position.y, camera.position.z);
		}

	}

}
