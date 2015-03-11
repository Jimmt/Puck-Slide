package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

public class Background extends Actor {
	OrthographicCamera camera;

	Array<Image> clouds;
	Array<Image> backClouds;
	Array<Image> mountains;
	Array<Image> trees;
	Array<Array<Image>> arrays;
	int tileClouds = 600;
	int nextCloud = 0;

	public Background(OrthographicCamera camera) {
		clouds = new Array<Image>();
		backClouds = new Array<Image>();
		mountains = new Array<Image>();
		trees = new Array<Image>();
		arrays = new Array<Array<Image>>();
		arrays.add(clouds);
		arrays.add(backClouds);
		arrays.add(mountains);
		arrays.add(trees);

// Image cloud = new Image(Textures.getTex("Background/clouds.png"));
// cloud.setSize(cloud.getWidth() / 100f, cloud.getHeight() / 100f);
// clouds.add(cloud);
// Image back = new Image(Textures.getTex("Background/cloudsback.png"));
// back.setY(Constants.HEIGHT - back.getHeight());
// backClouds.add(back);
// Image mount = new Image(Textures.getTex("Background/Montfront.png"));
// mount.setY(Constants.HEIGHT - back.getHeight() - mount.getHeight() / 2);
// mountains.add(mount);
// trees.add(new Image(Textures.getTex("Background/tree.png")));

		this.camera = camera;
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		float boundX = camera.position.x + camera.viewportWidth / 2;

		if (boundX > nextCloud) {

			Image cloud = new Image(Textures.getTex("Background/clouds.png"));
			cloud.setSize(cloud.getWidth() / 100f, cloud.getHeight() / 100f);
			cloud.setX(nextCloud);
			nextCloud += 6;
			clouds.add(cloud);
		}

		for (int i = 0; i < arrays.size; i++) {
			for (int j = 0; j < arrays.get(i).size; j++) {
				arrays.get(i).get(j).act(delta);
			}
		}

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		for (int i = 0; i < arrays.size; i++) {
			for (int j = 0; j < arrays.get(i).size; j++) {
				arrays.get(i).get(j).draw(batch, parentAlpha);
			}
		}
	}
}
