package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;
import com.jumpbuttonstudio.puckslide.GroundTile.TileType;

public class GameScreen extends BaseScreen {
	ButtonsDialog dialog;
	RetryDialog retryDialog;
	Background background;
	Image logo, arrow, black;
	Flag flag;
	Label scoreLabel;
	PointsLabel pointsLabel;
	int score, lastMudTile, firstMudTile;
	float camX, lastSnowChange, snowChangeTime = 2f, startingX, lastFlagX;
	float fadeTime = 0.1f;
	Puck puck;
	Array<GroundTile> tiles, temp;
	InputMultiplexer multiplexer;
	boolean inGameMode, gameOver;
	PowerBar powerBar;
	ParticleEffect snow;
	GameContactListener listener;

	public GameScreen(PuckSlide game, boolean gameOver) {
		super(game);

		Prefs.init();

		this.gameOver = gameOver;

		listener = new GameContactListener();
		world.setContactListener(listener);

		black = new Image(Textures.getTex("black.png"));

		logo = new Image(Textures.getTex("logo.png"));
		logo.setPosition(Constants.WIDTH / 2 - logo.getWidth() / 2,
				Constants.HEIGHT - logo.getHeight() - 20);
		hudStage.addActor(logo);

		dialog = new ButtonsDialog(this, skin);
		dialog.show(hudStage);
		dialog.setY(Constants.HEIGHT - logo.getHeight() * 1.75f - dialog.getHeight());

		retryDialog = new RetryDialog(this, skin);
		hudStage.addActor(retryDialog);

		if (gameOver) {
			retryDialog.setVisible(true);
			retryDialog.addAction(Actions.sequence(Actions.moveTo(-retryDialog.getWidth(),
					Constants.HEIGHT / 2 - retryDialog.getHeight() / 2), Actions.moveTo(
					Constants.WIDTH / 2 - retryDialog.getWidth() / 2, Constants.HEIGHT / 2
							- retryDialog.getHeight() / 2, 0.4f, Interpolation.exp5Out)));
			dialog.setVisible(false);
		} else {
			retryDialog.setVisible(false);
			dialog.setVisible(true);

		}

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
		pointsLabel = new PointsLabel("", labelStyle);
		hudStage.addActor(scoreLabel);
		hudStage.addActor(pointsLabel);

		tiles = new Array<GroundTile>();
		temp = new Array<GroundTile>();

		enterMenuMode();

		if (gameOver) {
			logo.getActions().clear();
			logo.setVisible(false);
		} else {
		}
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
		dialog.setVisible(false);
		retryDialog.setVisible(false);
		logo.addAction(Actions.sequence(Actions.alpha(0, 0.3f), new VisibleAction(logo, false)));

		arrow = new Image(Textures.getTex("Object/arrow.png"));
		arrow.setSize(arrow.getWidth() * Constants.SCALE, arrow.getHeight() * Constants.SCALE);
		flag = new Flag();
		flag.setSize(flag.getWidth() * Constants.SCALE, flag.getHeight() * Constants.SCALE);
		stage.addActor(arrow);
		stage.addActor(flag);

		addTiles(Constants.SCLWIDTH / 2 - 3.09f / 4, true);

		puck = new Puck(tiles.get(0).body.getPosition().x + 0.21f, 1.83f * 1.5f, world);

		stage.addActor(puck);

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

	public void addTile(TileType type) {
		if (temp.size >= 1) {
			startingX += temp.get(temp.size - 1).getWidth();
		}
		GroundTile tile = new GroundTile(startingX, type, world);
		tile.addAction(Actions.sequence(Actions.alpha(0),
				Actions.delay((temp.size - 1) * fadeTime), Actions.alpha(1, 0.3f)));
		temp.add(tile);

		stage.addActor(tile);
	}

	public void addTiles(float modX, boolean leftEdge) {
		startingX = modX;

		if (leftEdge) {
			addTile(GroundTile.TileType.ICE_LEFT);
		} else {
			addTile(GroundTile.TileType.ICE_FLAT);
		}

		for (int i = 0; i < MathUtils.random(2, 4); i++) {
			addTile(GroundTile.TileType.ICE_FLAT);
		}
		for (int i = 0; i < MathUtils.random(2, 4); i++) {
			addTile(GroundTile.TileType.MUD);
		}

		addTile(GroundTile.TileType.ICE_RIGHT);

		for (GroundTile t : temp) {
			tiles.add(t);
		}
		temp.clear();
		int end = 0, start = 0;
		for (int i = 0; i < tiles.size; i++) {
			if (tiles.get(i).type == GroundTile.TileType.MUD) {
				end = i;
			}
		}
		for (int i = end; i > 0; i--) {
			if (tiles.get(i).type == GroundTile.TileType.MUD) {
				start = i;
			} else {
				start = i;
				break;
			}
		}

		camX = tiles.get(start).body.getPosition().x + tiles.get(start).getWidth();

		lastFlagX = flag.getX();
		flag.setPosition(
				tiles.get(end).body.getPosition().x
						+ tiles.get(end).getWidth()
						/ 2
						+ MathUtils.random(0.1f, (tiles.size - 1 - end)
								* tiles.get(tiles.size - 1).getWidth()), tiles.get(end).getHeight());

	}

	public float lastTileX() {
		return tiles.get(tiles.size - 2).getX() + tiles.get(tiles.size - 2).getWidth();
	}

	public void gameOver() {
		if (!gameOver) {
			gameOver = true;
			if (score > Prefs.prefs.getInteger("highscore")) {
				Prefs.prefs.putInteger("highscore", score);
				Prefs.prefs.flush();
			}
			game.setScreen(new GameScreen(game, true));
		}
	}

	public void enterMenuMode() {
		inGameMode = false;
		scoreLabel.addAction(Actions.sequence(Actions.alpha(0, 0.3f), new VisibleAction(scoreLabel,
				true)));

		logo.addAction(Actions.sequence(Actions.alpha(1, 0.3f), new VisibleAction(logo, true)));

		if (powerBar != null) {
			powerBar.addAction(Actions.sequence(Actions.alpha(0, 0.3f), new VisibleAction(powerBar,
					true)));
		}

		gameOver = false;
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		if (Gdx.input.isKeyPressed(Keys.Q)) {
			addTiles(lastTileX(), false);
		}

		if (snow != null) {
			snow.update(delta);
			stage.getBatch().begin();
			snow.draw(stage.getBatch());
			stage.getBatch().end();
			snow.setPosition(0, Constants.SCLHEIGHT);

			if (lastSnowChange > snowChangeTime) {
				lastSnowChange = 0;
				snow.getEmitters().get(0).getEmission().setHigh(MathUtils.random(0, 275));
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

		if (Gdx.input.isKeyPressed(Keys.A)) {
			puck.body.setLinearVelocity(-1, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			puck.body.setLinearVelocity(1, 0);
		}

// if (puck != null) {
// Vector3 position = new Vector3(puck.getX() + puck.getWidth() / 2
// - pointsLabel.getWidth() / 2, puck.getY() + puck.getHeight() * 3, 0);
// camera.project(position);
// pointsLabel.setPosition(position.x, position.y);
// }
		if (inGameMode) {

			if (powerBar.launched) {
				pointsLabel.applied = false;
				for (int i = 0; i < tiles.size; i++) {
					if (tiles.get(i).type == GroundTile.TileType.MUD) {
						lastMudTile = i;
					}
				}
			}

			if (powerBar.check) {
				if (puck.getX() + 0.06f < tiles.get(lastMudTile).getX()
						+ tiles.get(lastMudTile).getWidth()) {
					gameOver();
				}
				if (puck.getY() < tiles.get(0).getY() + tiles.get(0).getHeight()) {
					gameOver();
				}

				float distance = Math.abs(puck.getX() + puck.getWidth() / 2 - lastFlagX);

				if (distance < 0.05f) {
					pointsLabel.setText("+4");
					score += 4;
				} else if (distance < 0.15f) {
					pointsLabel.setText("+3");
					score += 3;
				} else if (distance < 0.3f) {
					pointsLabel.setText("+2");
					score += 2;
				} else {
					pointsLabel.setText("+1");
					score += 1;
				}
				Vector3 position = new Vector3(puck.getX() + puck.getWidth() / 2
						- pointsLabel.getWidth() / 2, puck.getY() + puck.getHeight() * 3, 0);
				camera.project(position);
				pointsLabel.setPosition(position.x, position.y);

				if (!pointsLabel.applied) {
					pointsLabel.addAction(Actions.sequence(Actions.alpha(0),
							Actions.alpha(1, 0.2f), Actions.delay(0.2f), Actions.alpha(0, 0.2f)));
					pointsLabel.applied = true;
				}

				powerBar.check = false;

			}

		}

		camera.update();

		if (tiles.size > 0) {
			if (camera.position.x < camX) {
				camera.position.set(camera.position.x + 0.05f, camera.position.y, 0);
			} else {

			}
		}
		if (puck != null && puck.body.getLinearVelocity().x == 0) {
			arrow.setY(puck.getY() + puck.getHeight() + arrow.getHeight() / 2);
			arrow.addAction(Actions.moveTo(
					puck.getX() + puck.getWidth() / 2 - arrow.getWidth() / 2, arrow.getY(), 0.1f,
					Interpolation.linear));

		}

		scoreLabel.setText(String.valueOf(score));
	}
}
