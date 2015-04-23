package com.jumpbuttonstudio.puckslide.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.jumpbuttonstudio.puckslide.PuckSlide;

public class AndroidLauncher extends AndroidApplication {
	private GameHelper helper;
	private final static int REQUEST_CODE_UNUSED = 1001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		helper = new GameHelper(this, GameHelper.CLIENT_GAMES);
// helper.enableDebugLog(false);
//
// GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
// @Override
// public void onSignInSucceeded() {
// Gdx.app.log("AndroidLauncher", "Sign in succeeded");
// }
//
// @Override
// public void onSignInFailed() {
// Gdx.app.log("AndroidLauncher", "Sign in failed");
// }
// };
//
// helper.setup(gameHelperListener);

		AndroidServices services = new AndroidServices(helper, this);
		helper.setup(services);
		helper.enableDebugLog(true);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		initialize(new PuckSlide(services), config);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onPause() {
		// android.os.Process.killProcess(android.os.Process.myPid());
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		helper.onActivityResult(requestCode, resultCode, data);
	}

}