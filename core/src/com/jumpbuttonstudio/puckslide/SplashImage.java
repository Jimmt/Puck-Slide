package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class SplashImage extends Image {
	TextureRegion region;

	public SplashImage(TextureRegion region) {
		this.region = region;
	}

	public void setRegion(TextureRegion region) {
		this.region = region;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(region, getX(), getY(), getWidth(), getHeight());
	}

}
