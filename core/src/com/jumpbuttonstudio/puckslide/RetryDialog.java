package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RetryDialog extends Dialog {
	ImageButton retry;
	Label scoreLabel, gameOverLabel, highScoreLabel;

	public RetryDialog(final GameScreen gameScreen, Skin skin) {
		super("", skin);

		Image panel = new Image(Textures.getTex("UI/panel.png"));

		background(panel.getDrawable());

		Image image = new Image(Textures.getTex("UI/retry_normal.png"));
		setSize(panel.getWidth(), panel.getHeight());

		ImageButtonStyle retryStyle = new ImageButtonStyle();
		retryStyle.up = image.getDrawable();
		retryStyle.down = new Image(Textures.getTex("UI/retry_pressed.png")).getDrawable();

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = new BitmapFont(Gdx.files.internal("Font/font.fnt"));
		labelStyle.fontColor = Color.WHITE;

		scoreLabel = new Label("Score: " + gameScreen.score, labelStyle);
		gameOverLabel = new Label("Game Over", labelStyle);
		highScoreLabel = new Label("High Score: " + Prefs.prefs.getInteger("highscore"), labelStyle);

		retry = new ImageButton(retryStyle);
		retry.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if (!gameScreen.inGameMode) {
					gameScreen.enterGameMode();
				}
			}
		});

		getContentTable().add(gameOverLabel);
		getContentTable().row();
		getContentTable().add(scoreLabel);
		getContentTable().row();
		getContentTable().add(highScoreLabel);
		getContentTable().row();
		getContentTable().add(retry);
	}

}
