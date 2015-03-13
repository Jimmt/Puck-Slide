package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

public class CloudLayer extends Actor {
	Array<Image> clouds;
	float nextCloud = 0, offsetY, speed;
	OrthographicCamera camera;
	Color shade;

	public CloudLayer(OrthographicCamera camera, float offsetX, float offsetY, float colorMag) {
		clouds = new Array<Image>();

		colorMag = 1 - colorMag;

		shade = new Color((139 + colorMag * (255 - 139f)) / 255f,
				(146 + colorMag * (255 - 146f)) / 255f, 203 / 255f + colorMag * 52 / 255f, 1f);

		this.camera = camera;
		nextCloud = offsetX;
		this.offsetY = offsetY;
		
		speed = MathUtils.random(-0.01f, 0.01f);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		for (int i = 0; i < clouds.size; i++) {
			clouds.get(i).act(delta);
//			clouds.get(i).setX(clouds.get(i).getX() + speed);

			if (clouds.get(i).getX() + clouds.get(i).getWidth() <= 0) {
				clouds.removeIndex(i);
			}
		}

		float boundX = camera.position.x + camera.viewportWidth / 2;

		if (boundX > nextCloud) {

			Image cloud = new Image(Textures.getTex("Background/clouds.png"));
			cloud.setSize(cloud.getWidth() / 100f, cloud.getHeight() / 100f);
			cloud.setPosition(nextCloud, offsetY - 0.26f);
			nextCloud += 6;
			clouds.add(cloud);
		}

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		for (Image i : clouds) {
			i.draw(batch, parentAlpha);
		}
	}
}
