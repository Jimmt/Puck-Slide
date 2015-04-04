package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class PowerBar extends Image implements InputProcessor {
	GameScreen gameScreen;
	boolean startTime, launched, check;
	float timeDown, timeMax = 1.0f;
	float launchTime, timeCap = 2f;
	float originalCamX, finalCamX;
	boolean set;
	float barX;
	Image barBG, bar;
	Puck puck;

	public PowerBar(Puck puck, GameScreen gameScreen) {
		super(Textures.getTex("UI/powerbar.png"));

		this.gameScreen = gameScreen;

		this.puck = puck;

		barBG = new Image(Textures.getTex("UI/bar.png"));
		bar = new Image(Textures.getTex("UI/movingbar.png"));

	}

	@Override
	public void act(float delta) {
		super.act(delta);

		barBG.act(delta);
		bar.act(delta);

		if (startTime) {
			check = false;

			barX = getX() + (timeDown % timeMax) * getWidth();
			if (timeDown < timeMax) {

				gameScreen.camera.zoom = 1 - (timeDown / timeMax) * 0.25f;
				if (!set) {
					set = true;
					originalCamX = gameScreen.camera.position.x;
				}

				finalCamX = gameScreen.camera.position.x;
			}
			gameScreen.camera.position.set(Math.max(
					gameScreen.puck.getX() + gameScreen.puck.getWidth(),
					gameScreen.camera.position.x - 0.06f), gameScreen.camera.position.y, 0);
			bar.addAction(Actions.alpha(1));
			timeDown += delta;
		} else {

			if (barX > getX() + 8) {

				puck.body.applyForceToCenter(4 + (timeDown % timeMax) * 6, 0, false);
				launched = true;
			}
			timeDown = 0;
			set = false;
			barX = getX() + 8;
			bar.addAction(Actions.alpha(0));
			gameScreen.camera.zoom = 1;
		}
		if (launched) {

			if (launchTime > timeCap) {
				launchTime = 0;
				gameScreen.addTiles(gameScreen.lastTileX(), false);
				launched = false;
				check = true;

			} else {
				launchTime += delta;
			}
		}

		puck.launched = launched;

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		barBG.setPosition(getX() + 8, getY() + 9);
		barBG.draw(batch, parentAlpha);
		bar.setY(getY() + 8);
		bar.setX(barX);
		bar.draw(batch, parentAlpha);

	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (!launched) {
			startTime = true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		startTime = false;
		PuckSlide.soundManager.play("puckhit");
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
