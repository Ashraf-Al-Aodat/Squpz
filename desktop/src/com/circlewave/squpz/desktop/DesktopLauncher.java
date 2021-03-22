package com.circlewave.squpz.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.circlewave.squpz.Game;

public class DesktopLauncher {
	public static void main (final String[] arg) {
		new LwjglApplication(new Game(), getConfiguration());
	}

	private static LwjglApplicationConfiguration getConfiguration() {
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.samples = 4;
		config.height = 640;
		config.width = 360;
		config.depth = 15;
		config.resizable = false;
		config.useGL30 = true;
		return config;
	}
}
