package com.jumpbuttonstudio.puckslide;

import java.util.Collections;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;

public class Background extends Actor {
	OrthographicCamera camera;

	Array<Image> clouds;
	Array<Image> backClouds;
	Array<Image> mountains;
	Array<Image> backMountains;
	Array<Image> trees;
	Array<Image> fogs;
	Array<CloudLayer> cloudLayers;
	Array<Array<Image>> arrays;
	Image filler;
	float nextBackCloud = 0, nextFog = 0, nextMountain = -1.5f, nextBackMountain = 0f,
			nextTree = MathUtils.random(0, 3);
	float lastOffset;
	float cloudHeight = Textures.getTex("Background/cloudsback.png").getHeight() / 100f;
	float mountainHeight = Textures.getTex("Background/Montfront.png").getHeight() / 100f;

	public Background(OrthographicCamera camera) {
		this.camera = camera;

		clouds = new Array<Image>();
		backClouds = new Array<Image>();
		mountains = new Array<Image>();
		backMountains = new Array<Image>();
		trees = new Array<Image>();
		fogs = new Array<Image>();
		arrays = new Array<Array<Image>>();
		cloudLayers = new Array<CloudLayer>();
		arrays.add(backClouds);
		arrays.add(backMountains);
		arrays.add(mountains);
		arrays.add(trees);
		arrays.add(fogs);

		float distance = (Constants.SCLHEIGHT - cloudHeight - 0.35f) / (cloudHeight / 3 * 2);

		for (int i = (int) distance; i >= 0; i--) {
			CloudLayer cloudLayer = new CloudLayer(camera, -MathUtils.random(3), i == 0 ? 0 : (i
					* (cloudHeight / 3 * 2) + MathUtils.random(0.1f) - 0.05f) - 0.15f, i / distance);
			cloudLayers.add(cloudLayer);
		}

		filler = new Image(Textures.getTex("Background/fill.png"));
		filler.setSize(Constants.SCLWIDTH, Constants.SCLHEIGHT);

// CloudLayer cloudLayer = new CloudLayer(camera, 0, 0, 0);
// cloudLayers.add(cloudLayer);

	}

	@Override
	public void act(float delta) {
		super.act(delta);

		float boundX = camera.position.x + camera.viewportWidth / 2;

		filler.act(delta);

		if (boundX > nextFog) {

			Image fog = new Image(Textures.getTex("Background/fog.png"));
			fog.setSize(fog.getWidth() / 100f, fog.getHeight() / 100f);
			fog.setOrigin(fog.getWidth() / 2, fog.getHeight() / 2);
			fog.setPosition(nextFog, Constants.SCLHEIGHT - fog.getHeight() / 2);
			boolean flip = MathUtils.randomBoolean();
			if (flip) {
				fog.setScale(-1, 1);
			}

			nextFog += 11f;
			fogs.add(fog);
		}

		if (boundX > nextBackCloud) {

			Image cloud = new Image(Textures.getTex("Background/cloudsback.png"));
			cloud.setSize(cloud.getWidth() / 100f, cloud.getHeight() / 100f);
			cloud.setPosition(nextBackCloud, Constants.SCLHEIGHT - cloud.getHeight());
			nextBackCloud += 6;
			backClouds.add(cloud);
		}

		if (boundX > nextTree) {

			Image tree = new Image(Textures.getTex("Background/tree.png"));
			tree.setSize(tree.getWidth() / 100f, tree.getHeight() / 100f);
			tree.setPosition(nextTree, Constants.SCLHEIGHT - cloudHeight - mountainHeight / 4f);
			nextTree += MathUtils.random(2, 7);
			trees.add(tree);
		}

		if (boundX > nextMountain) {
			Image mountain = new Image(Textures.getTex("Background/Montfront.png"));
			mountain.setSize(mountain.getWidth() / 100f, mountain.getHeight() / 100f);
			mountain.setPosition(nextMountain,
					Constants.SCLHEIGHT - cloudHeight - mountain.getHeight() / 4f);
			lastOffset = MathUtils.random(4, 5);
			nextMountain += lastOffset;
			mountains.add(mountain);
		}

		if (boundX > nextBackMountain) {
			Image mountain = new Image(Textures.getTex("Background/Montback.png"));
			mountain.setSize(mountain.getWidth() / 100f, mountain.getHeight() / 100f);
			mountain.setPosition(nextBackMountain,
					Constants.SCLHEIGHT - cloudHeight - mountain.getHeight() / 4f);

			nextBackMountain += lastOffset + MathUtils.random(2) - 1f;
			backMountains.add(mountain);
		}

		for (int i = 0; i < arrays.size; i++) {
			for (int j = 0; j < arrays.get(i).size; j++) {
				arrays.get(i).get(j).act(delta);

				if (arrays.get(i).get(j).getX() + arrays.get(i).get(j).getWidth() <= 0) {
					arrays.get(i).removeIndex(j);
				}
			}
		}

		for (int i = 0; i < cloudLayers.size; i++) {
			cloudLayers.get(i).act(delta);

		}

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		filler.setX(camera.position.x - camera.viewportWidth / 2);
		filler.draw(batch, parentAlpha);

		for (int i = 0; i < arrays.size; i++) {
			for (int j = 0; j < arrays.get(i).size; j++) {
				arrays.get(i).get(j).draw(batch, parentAlpha);
			}
		}
		for (int i = 0; i < cloudLayers.size; i++) {
			cloudLayers.get(i).draw(batch, parentAlpha);
		}
	}
}
