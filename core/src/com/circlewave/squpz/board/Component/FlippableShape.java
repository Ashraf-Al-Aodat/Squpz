package com.circlewave.squpz.board.Component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.circlewave.squpz.utils.ShapeGestureListener;
import com.circlewave.squpz.utils.ShapeRenderer;

public class FlippableShape extends Shape {

    public static Vector2 H = new Vector2(0, -2);
    public static Vector2 V = new Vector2(-2, 0);

    private final Vector2 direction;

    public FlippableShape(final Array<Vector2> points, final float snappedValue, final Color color, final Vector2 direction) {
        super(points, snappedValue, color);
        this.direction = new Vector2(direction);
        setOrigin(Align.center);
        addListener(new ShapeGestureListener(){

            @Override
            public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                touchUpEvent(getStart(), x, y);
            }

            @Override
            public void tap(final InputEvent event, final float x, final float y, final int count, final int button) {
                tapEvent(count);
            }

            @Override
            public void pan(final InputEvent event, final float x, final float y, final float deltaX, final float deltaY) {
                panEvent(getStart(), x, y);
            }
        });
    }

    protected void tapEvent(final int count) {
        if(getTouchable().equals(Touchable.enabled) && count == 2) {
            setTouchable(Touchable.disabled);
            addAction(Actions.sequence(Actions.scaleBy(
                    FlippableShape.this.direction.x, FlippableShape.this.direction.y, 0.1f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            FlippableShape.this.direction.set(FlippableShape.this.direction.x * -1,
                                    FlippableShape.this.direction.y * -1);
                        }
                    })));
        }
    }

    private void touchUpEvent(final Vector2 start, final float x, final float y){
        if(getTouchable().equals(Touchable.enabled)) {
            final Vector2 coordinates = localToParentCoordinates(new Vector2(x, y));
            applyFlipToTouchCoordinate(coordinates, start);
            coordinates.set(getSnapSize(coordinates));
            addAction(Actions.moveTo(coordinates.x, coordinates.y, 0.1f));
        }
        setTouchable(Touchable.enabled);
    }

    private void panEvent(final Vector2 start, final float x, final float y){
        final Vector2 coordinates = localToParentCoordinates(new Vector2(x, y));
        applyFlipToTouchCoordinate(coordinates, start);
        addAction(Actions.moveTo(coordinates.x, coordinates.y));
    }

    private void applyFlipToTouchCoordinate(final Vector2 coordinates, final Vector2 start){
        coordinates.set(coordinates.x - start.x, coordinates.y - start.y);
        if (getScaleX() < 0) {
            coordinates.x = coordinates.x - getPolygon().getBoundingRectangle().width;
        }
        if(getScaleY() < 0){
            coordinates.y = coordinates.y - getPolygon().getBoundingRectangle().height;
        }
    }

    @Override
    public void drawBorder(final ShapeRenderer shapeRenderer, final float parentAlpha) {

    }
}
