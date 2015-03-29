package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Flag extends Actor {
	Animation anim;
	float stateTime;

	public Flag() {

		anim = new Animation(1 / 3f, null, PlayMode.LOOP);

	}

	@Override
	public void act(float delta) {
		super.act(delta);

		stateTime += delta;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		batch.draw(anim.getKeyFrame(stateTime), getX(), getY());
	}

}
