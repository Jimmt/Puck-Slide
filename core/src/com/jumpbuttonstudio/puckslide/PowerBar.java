package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class PowerBar extends Image implements InputProcessor {
	GameScreen gameScreen;
	boolean startTime, launched, check;
	float timeDown, timeMax = 1.0f;
	float launchTime, timeCap = 1f;
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
			barX = Math.min(getX() + (timeDown / timeMax) * getWidth(), getX() + getWidth() - 8);
			bar.addAction(Actions.alpha(1));
			timeDown += delta;
		} else {

			if (barX > getX() + 8) {

				puck.body.applyForceToCenter(6 + (timeDown / timeMax) * 8, 0, false);
				launched = true;
			}
			timeDown = 0;
			barX = getX() + 8;
			bar.addAction(Actions.alpha(0));
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
