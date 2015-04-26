package com.jumpbuttonstudio.puckslide.android;

import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.jumpbuttonstudio.puckslide.IGoogleServices;

public class AndroidServices implements GameHelperListener, ActionResolver, IGoogleServices {
	public AndroidLauncher launcher;
	public GameHelper gameHelper;
	public int REQUEST_ACHIEVEMENTS = 1001; // arbitrary

	public AndroidServices(GameHelper _gameHelper, AndroidLauncher _launcher) {
		launcher = _launcher;
		gameHelper = _gameHelper;
	}

	@Override
	public void signIn() {

		loginGPGS();
	}

	@Override
	public boolean getSignedInGPGS() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void loginGPGS() {
		try {
			launcher.runOnUiThread(new Runnable() {
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {
		}
	}

	@Override
	public void submitScoreGPGS(int score) {
		Games.Leaderboards.submitScore(gameHelper.getApiClient(),
				launcher.getString(R.string.leaderboard_id), score);
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
	}

	@Override
	public void getLeaderboardGPGS() {
		launcher.startActivityForResult(
				Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
						launcher.getString(R.string.leaderboard_id)), 0);
	}

	@Override
	public void getAchievementsGPGS() {
		launcher.startActivityForResult(
				Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()),
				REQUEST_ACHIEVEMENTS);
	}

	@Override
	public void onSignInFailed() {
	}

	@Override
	public void onSignInSucceeded() {
	}

	@Override
	public void submitHighscore(int score) {
		submitScoreGPGS(score);

	}

	@Override
	public void getLeaderboards() {
		getLeaderboardGPGS();

	}

	@Override
	public boolean getSignedIn() {
		return getSignedInGPGS();
	}

	@Override
	public void unlockAchievement(String id) {
		unlockAchievementGPGS(id);

	}

	@Override
	public void getAchievements() {
		getAchievementsGPGS();

	}

	@Override
	public void showOrLoadInterstitial() {
		try {
			launcher.runOnUiThread(new Runnable() {
				public void run() {
					if (launcher.interstitialAd.isLoaded()) {
						launcher.interstitialAd.show();
// Toast.makeText(launcher.getApplicationContext(), "Showing Interstitial",
// Toast.LENGTH_SHORT).show();
					} else {
						AdRequest interstitialRequest = new AdRequest.Builder().build();
						launcher.interstitialAd.loadAd(interstitialRequest);
// Toast.makeText(launcher.getApplicationContext(), "Loading Interstitial",
// Toast.LENGTH_SHORT).show();
					}
				}
			});
		} catch (Exception e) {
		}
	}

}
