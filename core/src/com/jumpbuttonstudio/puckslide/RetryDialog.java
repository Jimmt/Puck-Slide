package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RetryDialog extends Dialog {
	ImageButton retry;

	public RetryDialog(final GameScreen gameScreen, Skin skin) {
		super("", skin);
		
		background(new Image(Textures.getTex("blank.png")).getDrawable());

		Image image = new Image(Textures.getTex("UI/retry_normal.png"));
		setSize(image.getWidth(), image.getHeight());

		ImageButtonStyle retryStyle = new ImageButtonStyle();
		retryStyle.up = image.getDrawable();
		retryStyle.down = new Image(Textures.getTex("UI/retry_pressed.png")).getDrawable();

		retry = new ImageButton(retryStyle);
		retry.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if (!gameScreen.inGameMode) {
					gameScreen.enterGameMode();
				}
			}
		});

		getContentTable().add(retry);
	}

}
