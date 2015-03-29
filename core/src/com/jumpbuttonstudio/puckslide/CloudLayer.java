package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Array;

public class CloudLayer extends Actor {
	Array<Image> clouds;
	Array<Image> pool;
	float nextCloud = 0, offsetY, speed;
	OrthographicCamera camera;
	Color shade;
	float x, y;
	int tileCount = 0;

	public CloudLayer(OrthographicCamera camera, float offsetX, float offsetY, float colorMag) {
		clouds = new Array<Image>();
		pool = new Array<Image>();

		colorMag = 1 - colorMag;

		shade = new Color((139 + colorMag * (255 - 139f)) / 255f,
				(146 + colorMag * (255 - 146f)) / 255f, 203 / 255f + colorMag * 52 / 255f, 1f);

		this.camera = camera;
		nextCloud = offsetX;
		this.offsetY = offsetY;
		this.x = offsetX;
		this.y = offsetY;

		speed = MathUtils.random(-0.01f, 0.01f);

		for (int i = 0; i < 3; i++) {

			Image cloud = new Image(Textures.getTex("Background/clouds.png"));
			cloud.setSize(cloud.getWidth() / 100f, cloud.getHeight() / 100f);

			if (speed < 0) {
				cloud.setPosition(offsetX, offsetY - 0.26f);
			} else {
				cloud.setPosition(camera.position.x + camera.viewportWidth / 2 - offsetX,
						offsetY - 0.26f);
			}
			if (i == 0) {
				clouds.add(cloud);
			}
			pool.add(cloud);
		}

	}

	@Override
	public void act(float delta) {
		super.act(delta);

		float leftBoundX = camera.position.x - camera.viewportWidth / 2;
		float rightBoundX = camera.position.x + camera.viewportWidth / 2;

		for (int i = 0; i < clouds.size; i++) {
			clouds.get(i).act(delta);
			clouds.get(i).setX(clouds.get(i).getX() + speed);

// if (clouds.get(i).getX() + clouds.get(i).getWidth() <= 0) {
// clouds.removeIndex(i);
// }
// if (clouds.get(i).getX() <= leftBoundX && speed < 0) {
// clouds.removeIndex(i);
// }
		}

		if (rightBoundX > clouds.get(0).getX() + clouds.get(0).getWidth()) {
			Image cloud = new Image(Textures.getTex("Background/clouds.png"));
			cloud.setSize(cloud.getWidth() / 100f, cloud.getHeight() / 100f);
			cloud.setPosition(clouds.get(0).getX() + clouds.get(0).getWidth(), offsetY - 0.26f);
			clouds.insert(0, cloud);
		}

		if (speed > 0 && clouds.get(clouds.size - 1).getX() > leftBoundX) {
			if (clouds.size < 3) {
				Image cloud = new Image(Textures.getTex("Background/clouds.png"));
				cloud.setSize(cloud.getWidth() / 100f, cloud.getHeight() / 100f);
				cloud.setPosition(clouds.get(clouds.size - 1).getX()
						- clouds.get(clouds.size - 1).getWidth(), offsetY - 0.26f);
				clouds.add(cloud);
			} else {
				clouds.insert(0, clouds.get(clouds.size - 1));
				clouds.get(0)
						.setPosition(
								clouds.get(clouds.size - 1).getX()
										- clouds.get(clouds.size - 1).getWidth(), offsetY - 0.26f);
				clouds.removeIndex(clouds.size - 1);
			}
		}

		if (speed < 0
				&& clouds.get(clouds.size - 1).getX() + clouds.get(0).getWidth() < rightBoundX) {
			if(clouds.size < 3){
			Image cloud = new Image(Textures.getTex("Background/clouds.png"));
			cloud.setSize(cloud.getWidth() / 100f, cloud.getHeight() / 100f);
			cloud.setPosition(clouds.get(clouds.size - 1).getX() + clouds.get(0).getWidth(),
					offsetY - 0.26f);
			} else {
				clouds.insert(clouds.size - 1, clouds.get(0));
				clouds.get(clouds.size - 1)
						.setPosition(
								clouds.get(0).getX()
										+ clouds.get(0).getWidth(), offsetY - 0.26f);
				clouds.removeIndex(0);
			}
		}

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		for (Image i : clouds) {
			i.draw(batch, parentAlpha);
		}

	}
}
