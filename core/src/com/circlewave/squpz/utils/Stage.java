package com.circlewave.squpz.utils;

public class Stage extends com.badlogic.gdx.scenes.scene2d.Stage {

    private final ShapeRenderer shapeRenderer;

    public Stage(){
        super();
        this.shapeRenderer = new ShapeRenderer(getViewport().getCamera().combined);
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    @Override
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
    }
}
