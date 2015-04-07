package com.jumpbuttonstudio.puckslide.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.jumpbuttonstudio.puckslide.IGoogleServices;
import com.jumpbuttonstudio.puckslide.PuckSlide;

public class AndroidLauncher extends AndroidApplication implements IGoogleServices {
	private GameHelper helper;
	private final static int REQUEST_CODE_UNUSED = 1001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		helper = new GameHelper(this, GameHelper.CLIENT_GAMES);
//		helper.enableDebugLog(false);
//
//		GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
//			@Override
//			public void onSignInSucceeded() {
//				Gdx.app.log("MainActivity", "Sign in succeeded");
//			}
//
//			@Override
//			public void onSignInFailed() {
//				Gdx.app.log("MainActivity", "Sign in failed");
//			}
//		};
//
//		helper.setup(gameHelperListener);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new PuckSlide(this), config);
	}

	@Override
	protected void onStart() {
		super.onStart();
//		helper.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
//		helper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		helper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void signIn() {
		try {
			runOnUiThread(new Runnable() {
				// @Override
				public void run() {
					helper.beginUserInitiatedSignIn();
				}
			});
		} catch (Exception e) {
			Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void signOut() {
		try {
			runOnUiThread(new Runnable() {
				// @Override
				public void run() {
					helper.signOut();
				}
			});
		} catch (Exception e) {
			Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void rateGame() {
		// Replace the end of the URL with the package of your game
		String str = "https://play.google.com/store/apps/details?id=com.jumpbuttonstudio.puckslide.android";
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
	}

	@Override
	public void submitScore(long score) {
		if (isSignedIn() == true) {
			Games.Leaderboards.submitScore(helper.getApiClient(),
					getString(R.string.leaderboard_id), score);
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(helper.getApiClient(),
					getString(R.string.leaderboard_id)), REQUEST_CODE_UNUSED);
		} else {
			// Maybe sign in here then redirect to submitting score?
		}
	}

	@Override
	public void showScores() {
		if (isSignedIn() == true)
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(helper.getApiClient(),
					getString(R.string.leaderboard_id)), REQUEST_CODE_UNUSED);
		else {
			// Maybe sign in here then redirect to showing scores?
		}
	}

	@Override
	public boolean isSignedIn() {
		return helper.isSignedIn();
	}
}
