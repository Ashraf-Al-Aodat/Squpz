package com.circlewave.squpz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.circlewave.squpz.utils.Assets;
import com.circlewave.squpz.utils.Scene;
import com.circlewave.squpz.services.AndroidAPI;
import com.circlewave.squpz.services.ServicesManager;
import de.golfgl.gdxgamesvcs.IGameServiceClient;

public class Game extends com.badlogic.gdx.Game {

	private final Assets assets;
	private final ServicesManager servicesManager;

	private Scene scene;

	private Player player;

	private int selectedLevel;

	public Game(final AndroidAPI androidAPI, final IGameServiceClient gameServiceClient, final PurchaseManager purchaseManager) {
		this.assets = new Assets();
		this.servicesManager = new ServicesManager(this, androidAPI, gameServiceClient, purchaseManager);
		this.selectedLevel = 1;
	}

	public Game(){
		this.assets = new Assets();
		this.servicesManager = new ServicesManager(this);
	}

	public static void print(final String string) {
		Gdx.app.log("Testing ", string);

	}

	@Override
	public void create() {
		assets.load();
		//servicesManager.initializeLevels();
		//servicesManager.initializePlayer();
		//servicesManager.initializePurchaseManager();
		//servicesManager.initializeAdvertisement();
		scene = new Scene(this);
	}

	@Override
	public void render() {
		assets.update();
		scene.render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(final int width, final int height) {
		scene.resize(width, height);
	}

	@Override
	public void pause() {
		scene.pause();
		servicesManager.pause();
	}

	@Override
	public void resume() {
		scene.resume();
		if (scene != null) scene.resume();
		if (!servicesManager.isSessionActive()) getServicesManager().resume();
	}

	@Override
	public void dispose() {
		assets.dispose();
		scene.dispose();
		servicesManager.dispose();
	}

	public Assets getAssets() {
		return assets;
	}

	public void addActorToScene(final Actor actor) {
		scene.addActor(actor);
	}

	public ServicesManager getServicesManager() {
		return servicesManager;
	}

	public int getLevelsNumber() {
		return Gdx.files.local("levels").list().length;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(final Player player) {
		this.player = player;
	}

	public int getSelectedLevel() {
		return selectedLevel;
	}

	public void setSelectedLevel(final int selectedLevel) {
		this.selectedLevel = selectedLevel;
	}
}
