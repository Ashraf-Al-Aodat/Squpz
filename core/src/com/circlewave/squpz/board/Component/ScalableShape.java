package com.circlewave.squpz.board.Component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.circlewave.squpz.utils.ShapeGestureListener;
import com.circlewave.squpz.utils.ShapeRenderer;

public class ScalableShape extends Shape{

    private final float scaleLimit;

    private boolean scaleMode;

    public ScalableShape(final Array<Vector2> points, final float snappedValue, final Color color) {
        super(points, snappedValue, color);
        this.scaleLimit = 1;
        addListener(new ShapeGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                tapEvent(count, scaleLimit);
            }
        });
    }

    @Override
    public void drawBorder(final ShapeRenderer shapeRenderer, final float parentAlpha) {
        shapeRenderer.setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, getColor().a * parentAlpha);
        shapeRenderer.dottedPolygon(getPolygon(), getWidth() / 10, getSnappedValue() / 10 );
    }

    private void tapEvent(final int count, final float scaleLimit) {
        if(getTouchable().equals(Touchable.enabled) && count == 2) {
            final float scaleAmount = 1;
            if (!scaleMode) {
                addAction(Actions.sequence(Actions.scaleBy(scaleAmount, scaleAmount, 0.1f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                if(checkScaleSpace()) {
                                    addAction(Actions.sequence(Actions.repeat(2,
                                            Actions.parallel(Actions.sequence(Actions.color(Color.WHITE,0.1f),
                                                    Actions.color(getColor(), 0.1f)))),
                                            Actions.scaleBy(-scaleAmount, -scaleAmount, 0.1f)));
                                } else {
                                    if (Math.round(getScaleX() * 10) > (scaleLimit * 10)) scaleMode = true;
                                }
                            }
                })));
            } else {
                addAction(Actions.sequence(Actions.scaleBy(-scaleAmount, -scaleAmount, 0.1f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                if (Math.round(getScaleX() * 10) == 10) scaleMode = false;
                            }
                        })));
            }
        }
        setTouchable(Touchable.disabled);
    }

    private boolean checkScaleSpace(){
        return (getX() + getPolygon().getBoundingRectangle().width > getParent().getWidth() ||
                getY() + getPolygon().getBoundingRectangle().height > getParent().getHeight());
    }
}
