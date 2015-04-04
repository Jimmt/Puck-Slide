package com.jumpbuttonstudio.puckslide.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jumpbuttonstudio.puckslide.DesktopGoogleServices;
import com.jumpbuttonstudio.puckslide.PuckSlide;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 800;
		new LwjglApplication(new PuckSlide(new DesktopGoogleServices()), config);
	}
}
