package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;

public class GameScreen extends BaseScreen {
	ButtonsDialog dialog;
	Background background;
	Image logo, arrow, flag;
	Label scoreLabel;
	int score, lastMudTile;
	float totalWidth = 3.09f, camX, lastSnowChange, snowChangeTime = 2f;
	Puck puck;
	Array<GroundTile> tiles;
	InputMultiplexer multiplexer;
	boolean inGameMode, gameOver;
	PowerBar powerBar;
	ParticleEffect snow;
	GameContactListener listener;

	public GameScreen(PuckSlide game) {
		super(game);

		listener = new GameContactListener();
		world.setContactListener(listener);

		logo = new Image(Textures.getTex("logo.png"));
		logo.setPosition(Constants.WIDTH / 2 - logo.getWidth() / 2,
				Constants.HEIGHT - logo.getHeight() - 20);
		hudStage.addActor(logo);

		dialog = new ButtonsDialog(this, skin);
		dialog.show(hudStage);
		dialog.setY(Constants.HEIGHT - logo.getHeight() * 1.75f - dialog.getHeight());

		background = new Background(camera);
		stage.addActor(background);

		multiplexer = new InputMultiplexer(hudStage, stage);
		Gdx.input.setInputProcessor(multiplexer);

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = new BitmapFont(Gdx.files.internal("Font/font.fnt"));
		labelStyle.fontColor = Color.WHITE;

		scoreLabel = new Label("0", labelStyle);
		scoreLabel.setPosition(Constants.WIDTH / 2, Constants.HEIGHT - scoreLabel.getHeight());
		scoreLabel.addAction(Actions.alpha(0));
		hudStage.addActor(scoreLabel);

		tiles = new Array<GroundTile>();

		enterMenuMode();

	}

	class VisibleAction extends Action {
		Actor actor;
		boolean visible;

		public VisibleAction(Actor actor, boolean visible) {
			this.actor = actor;
			this.visible = visible;
		}

		@Override
		public boolean act(float delta) {
			actor.setVisible(visible);
			return true;
		}

	}

	public void enterGameMode() {
		inGameMode = true;
		scoreLabel.addAction(Actions.sequence(Actions.alpha(1, 0.3f), new VisibleAction(scoreLabel,
				true)));
		dialog.addAction(Actions.sequence(Actions.alpha(0, 0.3f), new VisibleAction(dialog, false)));
		logo.addAction(Actions.sequence(Actions.alpha(0, 0.3f), new VisibleAction(logo, false)));

		puck = new Puck(addTiles(Constants.SCLWIDTH / 2 - totalWidth / 4, true), 1.83f * 1.5f,
				world);
		stage.addActor(puck);

		arrow = new Image(Textures.getTex("Object/arrow.png"));
		arrow.setSize(arrow.getWidth() * Constants.SCALE, arrow.getHeight() * Constants.SCALE);
		flag = new Image(Textures.getTex("Object/flag.png"));
		flag.setSize(flag.getWidth() * Constants.SCALE, flag.getHeight() * Constants.SCALE);
		stage.addActor(arrow);
		stage.addActor(flag);

		arrow.setPosition(puck.getX() + puck.getWidth() / 2 - arrow.getWidth() / 2, puck.getY()
				+ puck.getHeight() + arrow.getHeight() / 2);

		powerBar = new PowerBar(puck, this);
		powerBar.setPosition(Constants.WIDTH / 2 - powerBar.getWidth() / 2, powerBar.getHeight());
		hudStage.addActor(powerBar);
		multiplexer.addProcessor(powerBar);

		setupSnow();
	}

	public void setupSnow() {
		snow = new ParticleEffect();
		snow.load(Gdx.files.internal("Effect/snow.p"), Gdx.files.internal("Effect/"));
		Utils.scaleEffect(snow);
		snow.start();
	}

	public float addTiles(float modX, boolean leftEdge) {
		float startingX = modX;
		float fadeTime = 0.1f;

		if (leftEdge) {
			GroundTile leftTile = new GroundTile(startingX, GroundTile.TileType.ICE_LEFT, world);
			tiles.add(leftTile);
			leftTile.addAction(Actions.sequence(Actions.alpha(0), Actions.alpha(1, fadeTime)));
			startingX += 0.36f;
		} else {
			GroundTile leftTile = new GroundTile(startingX, GroundTile.TileType.ICE_FLAT, world);
			tiles.add(leftTile);
			leftTile.addAction(Actions.sequence(Actions.alpha(0), Actions.alpha(1, fadeTime)));
			startingX += 0.36f;
		}

		for (int i = 0; i < 2; i++) {
			GroundTile iceTile = new GroundTile(startingX, GroundTile.TileType.ICE_FLAT, world);
			tiles.add(iceTile);
			iceTile.addAction(Actions.sequence(Actions.alpha(0), Actions.delay((1 + i) * fadeTime),
					Actions.alpha(1, fadeTime)));
			startingX += iceTile.getWidth();
		}
		for (int i = 0; i < 3; i++) {
			GroundTile mudTile = new GroundTile(startingX, GroundTile.TileType.MUD, world);
			tiles.add(mudTile);
			mudTile.addAction(Actions.sequence(Actions.alpha(0), Actions.delay((3 + i) * fadeTime),
					Actions.alpha(1, fadeTime)));

			if (i == 2) {
				startingX += 0.48f;
			} else {
				startingX += mudTile.getWidth();
			}
		}

		GroundTile rightTile = new GroundTile(startingX, GroundTile.TileType.ICE_RIGHT, world);
		tiles.add(rightTile);
		rightTile.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(6 * fadeTime),
				Actions.alpha(1, fadeTime)));
		startingX += rightTile.getWidth();

		for (GroundTile t : tiles) {
			stage.addActor(t);
		}

		return Constants.SCLWIDTH / 2 - totalWidth / 4;
	}

	public float lastTileX() {
		return tiles.get(tiles.size - 1).getX() + tiles.get(tiles.size - 1).getWidth();
	}

	public void gameOver() {
		if (!gameOver) {
			gameOver = true;
			System.out.println("GAME OVER");
		}
	}

	public void enterMenuMode() {
		inGameMode = false;
		scoreLabel.addAction(Actions.sequence(Actions.alpha(0, 0.3f), new VisibleAction(scoreLabel,
				true)));
		dialog.addAction(Actions.sequence(Actions.alpha(1, 0.3f), new VisibleAction(dialog, true)));
		logo.addAction(Actions.sequence(Actions.alpha(1, 0.3f), new VisibleAction(logo, true)));
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		if (snow != null) {
			snow.update(delta);
			stage.getBatch().begin();
			snow.draw(stage.getBatch());
			stage.getBatch().end();
			snow.setPosition(0, Constants.SCLHEIGHT);

			if (lastSnowChange > snowChangeTime) {
				lastSnowChange = 0;
				snow.getEmitters().get(0).getEmission().setHigh(MathUtils.random(0, 300));
				snow.getEmitters()
						.get(0)
						.getWind()
						.setHigh(MathUtils.random(-150 * Constants.SCALE, -50 * Constants.SCALE),
								MathUtils.random(-300 * Constants.SCALE, -200 * Constants.SCALE));
				snowChangeTime = MathUtils.random(2, 5);
			} else {
				lastSnowChange += delta;
			}
		}

		hudStage.draw();

		if (inGameMode) {
			arrow.setY(puck.getY() + puck.getHeight() + arrow.getHeight() / 2);
			if (powerBar.launched) {
				for (int i = 0; i < tiles.size; i++) {

					if (tiles.get(i).type == GroundTile.TileType.MUD) {
						lastMudTile = i;
					}
				}
			}
			if (powerBar.check) {
				if (puck.getX() < tiles.get(lastMudTile).getX() + tiles.get(lastMudTile).getWidth()) {
					gameOver();
				}
				if (puck.getY() < tiles.get(0).getY() + tiles.get(0).getHeight()) {
					gameOver();
				}

			}

		}

		camera.update();

		if (tiles.size > 0 && powerBar.check) {
			camX = puck.body.getPosition().x;
			if (camera.position.x < camX) {
				camera.position.set(camera.position.x + 0.05f, camera.position.y, 0);
			} else {

			}
		}

		scoreLabel.setText(String.valueOf(score));

	}
}
