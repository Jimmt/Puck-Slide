package com.jumpbuttonstudio.puckslide;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Prefs {
	public static Preferences prefs;

	public static void init() {
		prefs = Gdx.app.getPreferences("prefs");

		if (!prefs.contains("highscore")) {
			prefs.putInteger("highscore", 0);
		}

	}
}
