package com.circlewave.squpz.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.circlewave.squpz.Game;
import com.circlewave.squpz.board.Component.Shape;

public class ShapeGestureListener extends ActorGestureListener {

    private final Vector2 start;

    public ShapeGestureListener(){
        this.start = new Vector2();
    }

    @Override
    public void touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
        super.touchDown(event, x, y, pointer, button);
        getTouchDownTarget().toFront();
        start.set(x * getTouchDownTarget().getScaleX(),y * getTouchDownTarget().getScaleY());
    }

    @Override
    public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
        super.touchUp(event, x, y, pointer, button);
        if(getTouchDownTarget().getTouchable().equals(Touchable.enabled)) {
            final Vector2 coordinates = getTouchDownTarget().localToParentCoordinates(new Vector2(x, y));
            coordinates.set(coordinates.x - start.x, coordinates.y - start.y);
            coordinates.set(((Shape) getTouchDownTarget()).getSnapSize(coordinates));
            getTouchDownTarget().addAction(Actions.moveTo(coordinates.x, coordinates.y, 0.1f));
        }
        getTouchDownTarget().setTouchable(Touchable.enabled);
    }

    @Override
    public void tap(final InputEvent event, final float x, final float y, final int count, final int button) {
        super.tap(event, x, y, count, button);
    }

    @Override
    public boolean longPress(final Actor actor, final float x, final float y) {
        getTouchDownTarget().setTouchable(Touchable.disabled);
        getTouchDownTarget().toBack();
        return super.longPress(actor, x, y);
    }

    @Override
    public void pan(final InputEvent event, final float x, final float y, final float deltaX, final float deltaY) {
        super.pan(event, x, y, deltaX, deltaY);
        final Vector2 coordinates = getTouchDownTarget().localToParentCoordinates(new Vector2(x, y));
        coordinates.set(coordinates.x - start.x, coordinates.y - start.y);
        getTouchDownTarget().addAction(Actions.moveTo(coordinates.x, coordinates.y));
    }

    public Vector2 getStart() {
        return start;
    }
}
