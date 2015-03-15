package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ButtonsDialog extends Dialog {
	ImageButton play;
	GameScreen gameScreen;

	public ButtonsDialog(final GameScreen gameScreen, Skin skin) {
		super("", skin);

		this.gameScreen = gameScreen;

		background(new Image(Textures.getTex("blank.png")).getDrawable());

		Image button = new Image(Textures.getTex("UI/play_normal.png"));

		setSize(button.getWidth(), button.getHeight());

		ImageButtonStyle style = new ImageButtonStyle();
		style.up = button.getDrawable();
		style.down = new Image(Textures.getTex("UI/play_pressed.png")).getDrawable();
		play = new ImageButton(style);
		play.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				gameScreen.enterGameMode();
			}
		});
		getContentTable().add(play);

	}

	@Override
	public void act(float delta) {
		super.act(delta);

		if (getActions().size == 0) {
			addAction(Actions.sequence(Actions.moveBy(0, 15, 0.3f), Actions.moveBy(0, -15, 0.3f)));
		}
	}

}
