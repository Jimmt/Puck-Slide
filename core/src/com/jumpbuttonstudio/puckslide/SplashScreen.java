package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

public class SplashScreen extends BaseScreen {
	Image splashImage;

// SplashImage splash;
//
// public class SplashAction extends Action {
// Array<TextureRegion> regions;
// float frameTime = 0.5f;
// float totalTime = 0f;
// SplashImage image;
//
// public SplashAction(SplashImage image) {
// this.image = image;
//
// FileHandle dir = Gdx.files.internal("Splash/");
// if (Gdx.app.getType() == ApplicationType.Android) {
// dir = Gdx.files.internal("Splash/");
// } else {
// dir = Gdx.files.internal("./bin/Splash/");
// }
// FileHandle[] files = dir.list();
//
// regions = new Array<TextureRegion>();
//
// for (int i = 0; i < files.length; i += 4) {
// Texture tex = new Texture(files[i]);
// regions.add(new TextureRegion(tex));
//
// }
//
// }
//
// @Override
// public boolean act(float delta) {
// totalTime += delta;
//
// if (totalTime < regions.size * frameTime) {
// // image.setDrawable(images.get((int) (totalTime /
// frameTime)).getDrawable());
// image.setRegion(regions.get((int) (totalTime / frameTime)));
// return false;
// } else {
// return true;
// }
//
// }
// }

	public SplashScreen(final PuckSlide game) {
		super(game);

		Action switchScreenAction = new Action() {

			@Override
			public boolean act(float delta) {
				game.setScreen(new GameScreen(game, 0, false));
				return true;
			}

		};

		splashImage = new Image(Textures.getTex("splash.png"));
		splashImage.setSize(Constants.WIDTH, Constants.HEIGHT);
		hudStage.addActor(splashImage);
		splashImage.setColor(1, 1, 1, 0);
		splashImage.addAction(Actions.sequence(Actions.fadeIn(0.6f), Actions.delay(0.7f),
				Actions.fadeOut(0.3f), switchScreenAction));

// splash = new SplashImage(new TextureRegion(
// Textures.getTex("Splash/JBS-animatedlogo-v4-FULLRES-notimestamp.mov_00045.png")));
// splash.setSize(Constants.WIDTH, Constants.HEIGHT);
//
// SplashAction splashAction = new SplashAction(splash);
// splash.addAction(Actions.sequence(splashAction, Actions.delay(0.3f),
// switchScreenAction));
// hudStage.addActor(splash);

	}

	@Override
	public void render(float delta) {
		super.render(delta);

		hudStage.draw();
	}

}
