package com.circlewave.squpz.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.circlewave.squpz.board.Component.Shape;
import com.circlewave.squpz.utils.ShapeRenderer;
import com.circlewave.squpz.utils.Stage;

public final class Board extends Table {

    private final int resolution;

    public Board(final int resolution){
        this.resolution = resolution;
        final float size = Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 50f;
        setBounds(Gdx.graphics.getWidth() / 100f, Gdx.graphics.getWidth() / 100f, size, size);
        setColor(Color.LIGHT_GRAY);
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        if(getStage() != null) {
            batch.end();
            final ShapeRenderer shapeRenderer = ((Stage) getStage()).getShapeRenderer();
            shapeRenderer.begin();
            shapeRenderer.set(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.9f, 0.9f, 0.9f, getColor().a * parentAlpha);
            shapeRenderer.roundRect(getX() , getY(), getWidth(), getHeight(), getTileSize() / 10);
            shapeRenderer.setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, getColor().a * parentAlpha);

            for(int i = 0; i < resolution; i++){
                for(int j = 0; j < resolution; j++) {
                    final Vector2 point = localToStageCoordinates(new Vector2(getWidth() / resolution * i + getWidth() / resolution / 2,
                            getHeight() / resolution * j + getWidth() / resolution / 2));
                    shapeRenderer.circle(point.x, point.y, getWidth() / 128);
                }
            }
            shapeRenderer.end();
            batch.begin();
        }
        super.draw(batch, parentAlpha);
    }

    public Array<Shape> getShapes() {
        final Array<com.circlewave.squpz.board.Component.Shape> shapes = new Array<>();
        for(Actor actor : getChildren()){
            if(actor instanceof Shape){
                shapes.add(((Shape) actor));
            }
        }
        return shapes;
    }

    @Override
    public void addActor(final Actor actor) {
        boolean found = false;
            for(final Shape shape : getShapes()) {
                if (actor instanceof Shape && actor.equals(shape)) {
                    found = true;
                    break;
                }
            }
        if(!found) super.addActor(actor);
    }

    public float getTileSize(){
        return getWidth() / resolution;
    }
}
