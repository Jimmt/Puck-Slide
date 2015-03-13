package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GameScreen extends BaseScreen {
	ButtonsDialog dialog;
	Background background;
	Image logo;

	public GameScreen(PuckSlide game) {
		super(game);
		
		logo = new Image(Textures.getTex("logo.png"));
		logo.setPosition(Constants.WIDTH / 2 - logo.getWidth() / 2, Constants.HEIGHT - logo.getHeight() - 20);
		hudStage.addActor(logo);

		dialog = new ButtonsDialog(this, skin);
		dialog.show(hudStage);
		dialog.setY(Constants.HEIGHT - logo.getHeight() * 1.75f - dialog.getHeight());

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
