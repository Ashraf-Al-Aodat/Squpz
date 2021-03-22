package com.circlewave.squpz;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.pay.android.googlebilling.PurchaseManagerGoogleBilling;
import com.circlewave.squpz.Game;
import com.circlewave.squpz.services.AndroidAPI;
import de.golfgl.gdxgamesvcs.GpgsClient;

public class AndroidLauncher extends AndroidApplication implements AndroidAPI {

	private GpgsClient gpgsClient;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gpgsClient = new GpgsClient().initialize(this, true);
		initialize(new Game(this, gpgsClient, new PurchaseManagerGoogleBilling(this)), getConfiguration());
	}

	private AndroidApplicationConfiguration getConfiguration() {
		final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.numSamples = 2;
		config.useImmersiveMode = true;
		config.useCompass = false;
		config.useRotationVectorSensor = false;
		config.useGyroscope = false;
		config.depth = 15;
		return config;
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (gpgsClient != null) {
			gpgsClient.onGpgsActivityResult(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean isConnected() {
		return false;
	}

	@Override
	public void share() {

	}

	@Override
	public void showInterstitial() {

	}
}
