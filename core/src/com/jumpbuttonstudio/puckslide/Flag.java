package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class Flag extends Actor {
	Animation anim;
	float stateTime;

	public Flag() {
		Array<TextureRegion> sprites = new Array<TextureRegion>();

		TextureRegion[][] regions = TextureRegion.split(Textures.getTex("Object/flagFrames.png"),
				64, 64);

		for (int i = 0; i < regions[0].length; i++) {
			sprites.add(regions[0][i]);
		}

		anim = new Animation(1 / 3f, sprites, PlayMode.LOOP);

	}

	@Override
	public void act(float delta) {
		super.act(delta);

		stateTime += delta;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		batch.draw(anim.getKeyFrame(stateTime), getX(), getY() - 0.17f, 0, 0, 64, 64, Constants.SCALE,
				Constants.SCALE, 0);

	}

}
