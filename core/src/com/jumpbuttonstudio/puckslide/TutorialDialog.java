package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class TutorialDialog extends Dialog {

	public TutorialDialog(Skin skin) {
		super("", skin);

		Image bg = new Image(Textures.getTex("blackAlpha.png"));
		bg.setSize(Constants.WIDTH, Constants.HEIGHT);

		background(bg.getDrawable());

		Image tutorialImage = new Image(Textures.getTex("instructions.png"));

		getContentTable().add(tutorialImage);

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		if (Gdx.input.isTouched()) {
			hide();
		}
	}

}
