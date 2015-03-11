package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;

public class ButtonsDialog extends Dialog {
	ImageButton play;

	public ButtonsDialog(Skin skin) {
		super("", skin);

		background(new Image(Textures.getTex("blank.png")).getDrawable());

		Image button = new Image(Textures.getTex("UI/play_normal.png"));

		setSize(button.getWidth(), button.getHeight() + 30);

		ImageButtonStyle style = new ImageButtonStyle();
		style.up = button.getDrawable();
		style.down = new Image(Textures.getTex("UI/play_pressed.png")).getDrawable();
		play = new ImageButton(style);

		button(play);

	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		
		if(play.getActions().size == 0){
			play.addAction(Actions.sequence(Actions.moveBy(0, 15, 0.3f), Actions.moveBy(0, -15, 0.3f)));
		}
	}

}
