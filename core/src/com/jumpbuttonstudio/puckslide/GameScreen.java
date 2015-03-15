package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;

public class GameScreen extends BaseScreen {
	ButtonsDialog dialog;
	Background background;
	Image logo;
	Label scoreLabel;
	int score;
	Array<GroundTile> tiles;

	public GameScreen(PuckSlide game) {
		super(game);

		logo = new Image(Textures.getTex("logo.png"));
		logo.setPosition(Constants.WIDTH / 2 - logo.getWidth() / 2,
				Constants.HEIGHT - logo.getHeight() - 20);
		hudStage.addActor(logo);

		dialog = new ButtonsDialog(this, skin);
		dialog.show(hudStage);
		dialog.setY(Constants.HEIGHT - logo.getHeight() * 1.75f - dialog.getHeight());

		background = new Background(camera);
		stage.addActor(background);

		Gdx.input.setInputProcessor(new InputMultiplexer(hudStage, stage));

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = new BitmapFont(Gdx.files.internal("Font/font.fnt"));
		labelStyle.fontColor = Color.WHITE;

		scoreLabel = new Label("0", labelStyle);
		scoreLabel.setPosition(Constants.WIDTH / 2, Constants.HEIGHT - scoreLabel.getHeight());
		scoreLabel.addAction(Actions.alpha(0));
		hudStage.addActor(scoreLabel);

		tiles = new Array<GroundTile>();

		enterMenuMode();
	}

	public void enterGameMode() {
		scoreLabel.addAction(Actions.alpha(1, 0.3f));
		dialog.addAction(Actions.alpha(0, 0.3f));
		logo.addAction(Actions.alpha(0, 0.3f));

		float totalWidth = 3.09f;
		float startingX = Constants.SCLWIDTH / 2 - totalWidth / 4;

		GroundTile leftTile = new GroundTile(startingX, GroundTile.TileType.ICE_LEFT, world);
		tiles.add(leftTile);
		startingX += 0.36f;

		for (int i = 0; i < 2; i++) {
			GroundTile iceTile = new GroundTile(startingX, GroundTile.TileType.ICE_FLAT, world);
			tiles.add(iceTile);
			startingX += iceTile.getWidth();
		}
		for (int i = 0; i < 3; i++) {
			GroundTile mudTile = new GroundTile(startingX, GroundTile.TileType.MUD, world);
			tiles.add(mudTile);

			if (i == 2) {
				startingX += 0.48f;
			} else {
				startingX += mudTile.getWidth();
			}
		}

		GroundTile rightTile = new GroundTile(startingX, GroundTile.TileType.ICE_RIGHT, world);
		tiles.add(rightTile);
		startingX += rightTile.getWidth();

		for (GroundTile t : tiles) {
			stage.addActor(t);
		}
	}

	public void enterMenuMode() {
		scoreLabel.addAction(Actions.alpha(0, 0.3f));
		dialog.addAction(Actions.alpha(1, 0.3f));
		logo.addAction(Actions.alpha(1, 0.3f));
	}

	@Override
	public void render(float delta) {
		super.render(delta);

// for (int i = 0; i < tiles.size; i++) {
//
// }

		camera.update();
		if (Gdx.input.isKeyPressed(Keys.D)) {
			camera.position.set(camera.position.x + 0.1f, camera.position.y, camera.position.z);
		}

		scoreLabel.setText(String.valueOf(score));

	}

}
