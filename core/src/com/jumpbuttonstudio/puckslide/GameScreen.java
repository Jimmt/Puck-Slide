package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.jumpbuttonstudio.puckslide.GroundTile.TileType;

public class GameScreen extends BaseScreen {
	ButtonsDialog dialog;
	RetryDialog retryDialog;
	Background background;
	Image logo, arrow, transition;
	Array<Image> compliments;
	Flag flag;
	Label scoreLabel;
	PointsLabel pointsLabel;
	int score, lastMudTile, firstMudTile;
	int foursCombo, lastMultiple = 1;
	float camX, lastSnowChange, snowChangeTime = 2f, startingX, lastFlagX;
	float fadeTime = 0.1f, arrowX;
	float fade1, fade2, fade3;
	Puck puck;
	Array<GroundTile> tiles, temp;
	InputMultiplexer multiplexer;
	boolean inGameMode, gameOver;
	PowerBar powerBar;
	ParticleEffect snow;
	GameContactListener listener;
	ImageButton home, sound, gplay, achievements, leaderboards;
// ImageButton removeAds;
	ImageButton gplayStatus;

	public GameScreen(final PuckSlide game, int score, boolean gameOver) {
		super(game);

		Prefs.init();

		this.gameOver = gameOver;

		if (!gameOver) {
			PuckSlide.soundManager.musics.get("menu").setLooping(true);
			PuckSlide.soundManager.musics.get("ingame").setLooping(true);
			PuckSlide.soundManager.playMusic("menu", 0.2f);
		} else {
			

		}

		listener = new GameContactListener();
		world.setContactListener(listener);

		transition = new Image(Textures.getTex("transition.png"));
		transition.setSize(Constants.WIDTH, Constants.HEIGHT);
		hudStage.addActor(transition);
		transition.getColor().a = 0;

		compliments = new Array<Image>();
		for (int i = 0; i < 3; i++) {
			Image image = new Image(Textures.getTex("Compliments/0" + i + ".png"));
			compliments.add(image);
			popupStage.addActor(image);
			image.getColor().a = 0;
		}
		logo = new Image(Textures.getTex("Icon/logo.png"));
		logo.setPosition(Constants.WIDTH / 2 - logo.getWidth() / 2,
				Constants.HEIGHT - logo.getHeight() - 20);
		hudStage.addActor(logo);

		dialog = new ButtonsDialog(this, skin);
		dialog.show(hudStage);
		dialog.setY(Constants.HEIGHT - logo.getHeight() * 1.75f - dialog.getHeight());

		retryDialog = new RetryDialog(this, score, skin);
		hudStage.addActor(retryDialog);

		ImageButtonStyle achievementsStyle = new ImageButtonStyle();
		achievementsStyle.up = new Image(Textures.getTex("Icon/achievements.png")).getDrawable();
		ImageButtonStyle leaderboardsStyle = new ImageButtonStyle();
		leaderboardsStyle.up = new Image(Textures.getTex("Icon/leaderboards.png")).getDrawable();
		ImageButtonStyle gplayStyle = new ImageButtonStyle();
		gplayStyle.up = new Image(Textures.getTex("Icon/gplay_360.png")).getDrawable();
		ImageButtonStyle homeStyle = new ImageButtonStyle();
		homeStyle.up = new Image(Textures.getTex("Icon/home_360.png")).getDrawable();
		ImageButtonStyle soundStyle = new ImageButtonStyle();
		soundStyle.imageChecked = new Image(Textures.getTex("Icon/soundon_360.png")).getDrawable();
		soundStyle.up = new Image(Textures.getTex("Icon/soundoff_360.png")).getDrawable();
		ImageButtonStyle removeAdsStyle = new ImageButtonStyle();
		removeAdsStyle.up = new Image(Textures.getTex("Icon/removead_360.png")).getDrawable();

		float borderSize = 10f;
		sound = new ImageButton(soundStyle);
		sound.setChecked(Prefs.prefs.getBoolean("sound"));
		sound.setSize(sound.getWidth() * 0.5f, sound.getHeight() * 0.5f);
		sound.setPosition(Constants.WIDTH - sound.getWidth() - borderSize,
				Constants.HEIGHT - sound.getHeight() - borderSize);
		gplay = new ImageButton(gplayStyle);
		gplay.setSize(gplay.getWidth() * 0.5f, gplay.getHeight() * 0.5f);
		gplay.setPosition(sound.getX() - gplay.getWidth() - borderSize, sound.getY());
		home = new ImageButton(homeStyle);
		home.setSize(home.getWidth() * 0.5f, home.getHeight() * 0.5f);
		home.setPosition(gplay.getX() - home.getWidth() - borderSize, sound.getY());

		achievements = new ImageButton(achievementsStyle);
		achievements.setSize(achievements.getWidth() * 0.5f, achievements.getHeight() * 0.5f);
		achievements.setPosition(borderSize, sound.getY());
		leaderboards = new ImageButton(leaderboardsStyle);
		leaderboards.setSize(leaderboards.getWidth() * 0.5f, leaderboards.getHeight() * 0.5f);
		leaderboards.setPosition(achievements.getX() + achievements.getWidth() + borderSize,
				sound.getY());
// removeAds = new ImageButton(removeAdsStyle);
// removeAds.setSize(removeAds.getWidth() * 0.5f, removeAds.getHeight() * 0.5f);
// removeAds.setPosition(leaderboards.getX() + leaderboards.getWidth() +
// borderSize,
// sound.getY());

		fade1 = 0.1f;
		fade2 = 0.1f;
		fade3 = 0.1f;
		home.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				PuckSlide.soundManager.play("button", 0.2f);

				if (!dialog.isVisible()) {
					transition.toFront();
					transition.addAction(Actions.sequence(Actions.alpha(1, fade1),
							Actions.delay(fade2),
							Actions.parallel(Actions.alpha(0, fade3), new GameOverAction(0, false))));

				}
			}
		});
		sound.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Prefs.prefs.putBoolean("sound", sound.isChecked());
				Prefs.prefs.flush();
				PuckSlide.soundManager.setPlay(sound.isChecked());

				if (sound.isChecked()) {
					if (retryDialog.isVisible() || dialog.isVisible()) {
						PuckSlide.soundManager.playMusic("menu", 0.2f);
					} else {
						PuckSlide.soundManager.playMusic("ingame", 0.2f);
					}
				}
			}
		});

		gplay.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if (PuckSlide.services.getSignedIn()) {
					PuckSlide.services.signOut();
				} else {
					PuckSlide.services.signIn();
				}
			}
		});
		achievements.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if (PuckSlide.services.getSignedIn()) {
					PuckSlide.services.getAchievements();
				} else {
					PuckSlide.services.signIn();
				}
			}
		});
		leaderboards.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if (PuckSlide.services.getSignedIn()) {
					PuckSlide.services.getLeaderboards();
				} else {
					PuckSlide.services.signIn();
				}
			}
		});
// removeAds.addListener(new ClickListener() {
// public void clicked(InputEvent event, float x, float y) {
// PuckSlide.services.removeAds();
// }
// });

		hudStage.addActor(home);
		hudStage.addActor(sound);
		hudStage.addActor(gplay);
		hudStage.addActor(leaderboards);
		hudStage.addActor(achievements);
// hudStage.addActor(removeAds);

		ImageButtonStyle ibstyle = new ImageButtonStyle();
		ibstyle.checked = new Image(Textures.getTex("UI/check.png")).getDrawable();
		ibstyle.up = new Image(Textures.getTex("UI/x.png")).getDrawable();
		gplayStatus = new ImageButton(ibstyle);
		gplayStatus.setPosition(gplay.getX() + gplay.getWidth() / 2 - gplayStatus.getWidth() / 2,
				gplay.getY() - gplayStatus.getHeight() * 1.25f);
		hudStage.addActor(gplayStatus);

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
		popupStage.addActor(pointsLabel);

		tiles = new Array<GroundTile>();
		temp = new Array<GroundTile>();

		enterMenuMode();

		if (gameOver) {
			logo.getActions().clear();
			logo.setVisible(false);
		} else {
		}

		if (gameOver) {
			home.setVisible(true);
		} else {
			home.setVisible(puck != null);
		}
		if (!PuckSlide.triedSignIn) {
			PuckSlide.triedSignIn = true;
			PuckSlide.services.signIn();
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

	public void enterMenuMode() {
		inGameMode = false;
		scoreLabel.addAction(Actions.sequence(Actions.alpha(0, 0.3f), new VisibleAction(scoreLabel,
				true)));

		float x = logo.getX();
		float y = logo.getY();
		logo.addAction(Actions.sequence(Actions.alpha(1), Actions.moveTo(x, y + 100),
				Actions.moveTo(x, y, 0.5f, Interpolation.pow5Out), new VisibleAction(logo, true)));
		dialog.play.addAction(Actions.sequence(Actions.alpha(0), Actions.alpha(1, 0.3f)));
		if (powerBar != null) {
			powerBar.addAction(Actions.sequence(Actions.alpha(0, 0.5f), new VisibleAction(powerBar,
					true)));
		}

		gameOver = false;
	}

	public void enterGameMode() {
		if (!PuckSlide.soundManager.musics.get("ingame").isPlaying()) {
			PuckSlide.soundManager.playMusic("ingame", 0.15f);
		}
		home.setVisible(true);
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

		puck = new Puck(tiles.get(0).body.getPosition().x + 0.21f, 1.9f, world);

		stage.addActor(puck);

		arrow.setPosition(puck.getX() + puck.getWidth() / 2 - arrow.getWidth() / 2, puck.getY()
				+ puck.getHeight() + arrow.getHeight() / 2);
		arrowX = puck.getX() + puck.getWidth() / 2 - arrow.getWidth() / 2;
		powerBar = new PowerBar(puck, this);
		powerBar.setPosition(Constants.WIDTH / 2 - powerBar.getWidth() / 2,
				powerBar.getHeight() / 2f);
		hudStage.addActor(powerBar);
		multiplexer.addProcessor(powerBar);

		setupSnow();

		if (!PuckSlide.showedTutorial) {
			TutorialDialog tutDialog = new TutorialDialog(skin);
			tutDialog.show(hudStage);
			tutDialog.setFillParent(true);
			PuckSlide.showedTutorial = true;
		}
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
								* tiles.get(tiles.size - 1).getWidth()) - 0.12f, tiles.get(end)
						.getHeight());

	}

	public float lastTileX() {
		return tiles.get(tiles.size - 2).getX() + tiles.get(tiles.size - 2).getWidth();
	}

	class GameOverAction extends Action {
		int score;
		boolean gameOver;

		public GameOverAction(int score, boolean gameOver) {
			this.score = score;
			this.gameOver = gameOver;
		}

		@Override
		public boolean act(float delta) {
			game.setScreen(new GameScreen(game, score, gameOver));
			return true;
		}

	}

	public void gameOver() {
		if (!gameOver) {
			Prefs.prefs.putInteger("deaths", Prefs.prefs.getInteger("deaths") + 1);
			Prefs.prefs.flush();
			if (PuckSlide.services.getSignedIn()) {
				if (score > 0) {
					PuckSlide.services.unlockAchievement(Constants.POINTS_0);
				}
				if (score >= 20) {
					PuckSlide.services.unlockAchievement(Constants.POINTS_20);
				}
				if (score >= 100) {
					PuckSlide.services.unlockAchievement(Constants.POINTS_100);
				}

				if (Prefs.prefs.getInteger("deaths") >= 10) {
					PuckSlide.services.unlockAchievement(Constants.FAIL_10);
				}
				if (Prefs.prefs.getInteger("deaths") >= 100) {
					PuckSlide.services.unlockAchievement(Constants.FAIL_100);
				}
			}

			PuckSlide.soundManager.play("lose");
			gameOver = true;
			if (PuckSlide.services.getSignedIn()) {
				PuckSlide.services.submitHighscore(score);
			}
			if (score > Prefs.prefs.getInteger("highscore")) {
				Prefs.prefs.putInteger("highscore", score);
				Prefs.prefs.flush();
			}
			transition.toFront();
			transition.addAction(Actions.sequence(Actions.alpha(1, fade1), Actions.delay(fade2),
					Actions.parallel(Actions.alpha(0, fade3), new GameOverAction(score, true))));

		}
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		camera.update();
		popupStage.getCamera().position.set(camera.position);

		gplayStatus.setChecked(PuckSlide.services.getSignedIn());

		if (snow != null) {
			snow.update(delta);
			stage.getBatch().begin();
			snow.draw(stage.getBatch());
			stage.getBatch().end();
			snow.setPosition(camera.position.x - camera.viewportWidth / 2, Constants.SCLHEIGHT);

			if (lastSnowChange > snowChangeTime) {
				lastSnowChange = 0;
// snow.getEmitters().get(0).getEmission().setHigh(MathUtils.random(0, 0));
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

			if (puck.getY() < tiles.get(0).getY() + tiles.get(0).getHeight() - 1.0f) {
				gameOver();
			}

			if (powerBar.check) {
				arrowX = puck.getX() + puck.getWidth() / 2 - arrow.getWidth() / 2;
				arrow.getActions().clear();
				arrow.addAction(Actions.moveTo(arrowX, arrow.getY(), 0.3f));

				if (puck.getX() + 0.06f < tiles.get(lastMudTile).getX()
						+ tiles.get(lastMudTile).getWidth()) {
					gameOver();
				}

				float distance = Math.abs(puck.getX() + puck.getWidth() / 2 - lastFlagX);

				if (distance < 0.15f) {
					pointsLabel.setText("+4");
					foursCombo++;
					score += 4;
				} else if (distance < 0.3f) {
					pointsLabel.setText("+3");
					foursCombo++;
					score += 3;
				} else if (distance < 0.45f) {
					pointsLabel.setText("+2");
					foursCombo = 0;
					score += 2;
				} else {
					pointsLabel.setText("+1");
					foursCombo = 0;
					score += 1;
				}

				if (!gameOver) {
					PuckSlide.soundManager.play("success", 0.2f);
				}

// Vector3 position = new Vector3(puck.getX() + puck.getWidth() / 2
// - pointsLabel.getWidth() / 2, puck.getY() + puck.getHeight() * 3, 0);
// viewport.project(position);

				float x = puck.getX() + puck.getWidth() / 2 - pointsLabel.getWidth() / 2;
				float y = puck.getY() + puck.getHeight() * 3;
				pointsLabel.setPosition(x, y);

				for (int i = 0; i < compliments.size; i++) {
					compliments.get(i).setPosition(
							x + puck.getWidth() / 2 - compliments.get(i).getWidth() / 2,
							y + pointsLabel.getHeight() * 1.5f);
				}

				if (!pointsLabel.applied) {
					pointsLabel.applied = true;
					pointsLabel.addAction(Actions.sequence(Actions.alpha(0),
							Actions.alpha(1, 0.2f), Actions.delay(0.2f), Actions.alpha(0, 0.2f)));

					if (foursCombo == 5) {
						compliments.get(2).addAction(
								Actions.sequence(Actions.alpha(0), Actions.alpha(1, 0.2f),
										Actions.delay(0.2f), Actions.alpha(0, 0.2f)));
						
					} else 
					if (foursCombo == 3) {

						compliments.get(0).addAction(
								Actions.sequence(Actions.alpha(0), Actions.alpha(1, 0.2f),
										Actions.delay(0.2f), Actions.alpha(0, 0.2f)));
						
					} else 
					if (score > lastMultiple * 20) {
						compliments.get(1).addAction(
								Actions.sequence(Actions.alpha(0), Actions.alpha(1, 0.2f),
										Actions.delay(0.2f), Actions.alpha(0, 0.2f)));
						lastMultiple++;
						
					}
				}

				powerBar.check = false;

			}

		}

		if (tiles.size > 0) {
			if (camera.position.x < camX) {
				camera.position.set(camera.position.x + 0.05f, camera.position.y, 0);
			} else {

			}
		}

		if (arrow != null && arrow.getActions().size == 0) {

			arrow.addAction(Actions.sequence(
					Actions.moveTo(arrow.getX(), puck.getY() + puck.getHeight() + arrow.getHeight()
							/ 2 + 0.3f, 0.5f),
					Actions.moveTo(arrow.getX(), puck.getY() + puck.getHeight() + arrow.getHeight()
							/ 2, 0.5f)));
		}

// if (puck != null && puck.body.getLinearVelocity().x == 0) {
// // arrow.setY(puck.getY() + puck.getHeight() + arrow.getHeight() / 2);
// arrow.addAction(Actions.moveTo(
// puck.getX() + puck.getWidth() / 2 - arrow.getWidth() / 2, arrow.getY(), 0.2f,
// Interpolation.linear));
//
// }

		scoreLabel.setText(String.valueOf(score));
	}
}
