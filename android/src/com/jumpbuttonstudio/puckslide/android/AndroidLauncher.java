package com.jumpbuttonstudio.puckslide.android;

import java.io.IOException;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.example.android.trivialdrivesample.util.IabHelper;
import com.example.android.trivialdrivesample.util.IabResult;
import com.example.android.trivialdrivesample.util.Purchase;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.jumpbuttonstudio.puckslide.Prefs;
import com.jumpbuttonstudio.puckslide.PuckSlide;

public class AndroidLauncher extends AndroidApplication implements GameHelperListener, IabInterface {
	private GameHelper helper;
	private static final String AD_UNIT_ID_BANNER = "ca-app-pub-8823077351295808/7430625172";
	private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-8823077351295808/1375588373"; 
	private String licenseKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5cTtUZ8EETuf6oCjA5ND8dA3Tv1j4ZzXP9T6k2VD31mfT7XFI5TTJtx3G4+vbetSR5ZzZGRwVEZ/DzJynvGzOtzfRwrQ18Y5yD+F00ilklLmnGG02YWZlG0HT1AN/01zjnnalQUtjRPludaR2y/BLKhKCahmrvikdefNwth/2Lwj+1gDtC/YDj0iHatnM6Aj4hs1/a4ngaIpaiUh6f8l0eCVpEowUjmGvD/oOqg7Cl6RcvnmVsYE8t6SuMllfRq86SvaZsQiL250RU8N3w8SHeAYrqFFc0afHQz6dT94OhRvD6oibQtHrFPquA76nxFeHRqScaRkRQY2m/yLGTgAjwIDAQAB";
	private AndroidServices services;
	private IabHelper mHelper;
	public InterstitialAd interstitialAd;
	protected AdView adView, admobView;
	protected View gameView;
	private RelativeLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		// compute your public key and store it in base64EncodedPublicKey
		mHelper = new IabHelper(this, licenseKey);

		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					Log.d("IAB", "Problem setting up In-app Billing: " + result);
				}
				// Hooray, IAB is fully set up!
				Log.d("IAB", "Billing Success: " + result);

// removeAds();
			}
		});

		helper = new GameHelper(this, GameHelper.CLIENT_GAMES);

		services = new AndroidServices(helper, this);
		helper.setup(services);
		helper.enableDebugLog(true);

// new Thread(new Runnable() {
// public void run() {
// getId();
// }
// }).start();

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

// initialize(new PuckSlide(services), config);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);

		admobView = createAdView();
		layout.addView(admobView);
		View gameView = createGameView(config);
		layout.addView(gameView);

		setContentView(layout);
		startAdvertising(admobView);

		interstitialAd = new InterstitialAd(this);

		interstitialAd.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
// Toast.makeText(getApplicationContext(), "Finished Loading Interstitial",
// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAdClosed() {
// Toast.makeText(getApplicationContext(), "Closed Interstitial",
// Toast.LENGTH_SHORT)
// .show();
			}
		});

	}

	public void getId() {
		Info adInfo = null;
		try {
			adInfo = AdvertisingIdClient.getAdvertisingIdInfo(this);

		} catch (IOException e) {
			// Unrecoverable error connecting to Google Play services (e.g.,
			// the old version of the service doe sn't support getting
// AdvertisingId).

		} catch (GooglePlayServicesNotAvailableException e) {
			// Google Play services is not available entirely.
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GooglePlayServicesRepairableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final String id = adInfo.getId();
		System.out.println(id);
	}

	private AdView createAdView() {
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(AD_UNIT_ID_BANNER);
		adView.setId(12345); // this is an arbitrary id, allows for relative
// positioning in createGameView()
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		adView.setLayoutParams(params);
		adView.setBackgroundColor(Color.BLACK);
		return adView;
	}

	private View createGameView(AndroidApplicationConfiguration cfg) {
		gameView = initializeForView(new PuckSlide(services), cfg);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.BELOW, adView.getId());
		gameView.setLayoutParams(params);
		return gameView;
	}

	private void startAdvertising(AdView adView) {
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}

	@Override
	public void onDestroy() {
		
		adView.destroy();
		super.onDestroy();
		if (mHelper != null)
			mHelper.dispose();
		mHelper = null;
		
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public void onPause() {
		// android.os.Process.killProcess(android.os.Process.myPid());
		adView.pause();
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		helper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void removeAds() {
		// Callback for when a purchase is finished
		IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
			public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
				if (purchase == null)
					return;
				Log.d("IAB", "Purchase finished: " + result + ", purchase: " + purchase);

				// if we were disposed of in the meantime, quit.
				if (mHelper == null)
					return;

				if (result.isFailure()) {
					// complain("Error purchasing: " + result);
					// setWaitScreen(false);
					return;
				}
// if (!verifyDeveloperPayload(purchase)) {
// //complain("Error purchasing. Authenticity verification failed.");
// //setWaitScreen(false);
// return;
// }

				Log.d("IAB", "Purchase successful.");

				if (purchase.getSku().equals(SKU_REMOVE_ADS)) {
					Log.d("IAB", "Purchase is premium upgrade. Congratulating user.");
					layout.removeView(admobView);
					Prefs.prefs.putBoolean("ads", false);

				}

			}
		};
		mHelper.flagEndAsync();
		mHelper.launchPurchaseFlow(this, SKU_REMOVE_ADS, RC_REQUEST, mPurchaseFinishedListener,
				"HANDLE_PAYLOADS");

	}

	@Override
	public void onResume() {
		adView.resume();
		super.onResume();
	}

	@Override
	public void onSignInFailed() {

	}

	@Override
	public void onSignInSucceeded() {

	}

}