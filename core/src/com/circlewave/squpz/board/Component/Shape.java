package com.circlewave.squpz.board.Component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.circlewave.squpz.utils.Polygon;
import com.circlewave.squpz.utils.ShapeRenderer;
import com.circlewave.squpz.utils.Stage;

public abstract class Shape extends Actor {

    private final float snappedValue;
    private final Polygon polygon;

    public Shape(final Array<Vector2> points, final float snappedValue, final Color color){
        this.snappedValue = snappedValue;
        this.polygon = new Polygon(calculatePoints(points));
        setBounds(snappedValue / 2, snappedValue / 2,
                polygon.getBoundingRectangle().width, polygon.getBoundingRectangle().height);
        setColor(color);
    }

    public Vector2 getGridUnits(){
        return new Vector2(polygon.getBoundingRectangle().width / snappedValue,
                polygon.getBoundingRectangle().height / snappedValue);
    }

    @Override
    public Actor hit(final float x, final float y, final boolean touchable) {
        if (touchable && this.getTouchable() != Touchable.enabled) return null;
        if (!isVisible()) return null;
        return (polygon.contains(localToStageCoordinates(new Vector2(x, y)))) ? this : null;
    }

    private Array<Vector2> calculatePoints(final Array<Vector2> points){
        final Array<Vector2> gridPoints = new Array<>();
        for(Vector2 point : points){
            gridPoints.add(gridToScreenCoordinate(point));
        }
        return gridPoints;
    }

    private Vector2 gridToScreenCoordinate(final Vector2 point){
        return new Vector2(point.x * snappedValue, point.y * snappedValue);
    }

    public Vector2 getSnapSize(final Vector2 coordinate){
        final float size = getParent().getWidth();
        final Vector2 position = new Vector2();

        position.set((float) Math.floor(coordinate.x / snappedValue) * snappedValue + snappedValue / 2,
                (float) Math.floor(coordinate.y / snappedValue) * snappedValue + snappedValue / 2);

        if(coordinate.x < snappedValue / 2) {
            position.x = snappedValue / 2;
        }

        if(coordinate.x + polygon.getBoundingRectangle().width > size){
            position.x = size - snappedValue / 2 - polygon.getBoundingRectangle().width;
        }

        if(coordinate.y < snappedValue / 2) {
            position.y = snappedValue / 2;
        }

        if(coordinate.y + polygon.getBoundingRectangle().height > size){
            position.y = size - snappedValue / 2 - polygon.getBoundingRectangle().height;
        }

        return position;
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        if (getStage() != null) {
            super.draw(batch, parentAlpha);
            batch.end();

            final ShapeRenderer shapeRenderer = ((Stage) getStage()).getShapeRenderer();
            shapeRenderer.begin();

            Gdx.gl.glEnable(GL30.GL_BLEND);
            Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

            drawBorder(shapeRenderer, parentAlpha);

            shapeRenderer.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
            shapeRenderer.polygon(polygon);

            //shapeRenderer.setColor(Color.GRAY.r, Color.GRAY.g, Color.GRAY.b, getColor().a * parentAlpha);
            //shapeRenderer.circle(getX() + getOriginX(), getY() + getOriginY(), Gdx.graphics.getWidth() / 100f);

            //shapeRenderer.set(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line);
            //shapeRenderer.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, getColor().a * parentAlpha);
            //shapeRenderer.polygon(polygon.getTransformedVertices());

            //shapeRenderer.setColor(Color.BROWN);
            //shapeRenderer.rect(getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

            shapeRenderer.end();

            Gdx.gl.glDisable(GL30.GL_BLEND);

            batch.begin();
        }
    }


    public abstract void drawBorder(final ShapeRenderer shapeRenderer, final float parentAlpha);

    @Override
    public void act(final float delta) {
        super.act(delta);
        if(polygon != null)
        updatePolygon();
    }

    public void updatePolygon(){
        polygon.setScale(getScaleX(), getScaleY());
        polygon.setRotation(getRotation());
        polygon.setPosition(localToStageCoordinates(new Vector2()));
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public float getSnappedValue() {
        return snappedValue;
    }

    public TextureRegion createTexture(final Polygon polygon, final Color color, final com.circlewave.squpz.utils.ShapeRenderer shapeRenderer){
        final com.circlewave.squpz.utils.FrameBuffer buffer = new com.circlewave.squpz.utils.FrameBuffer(Pixmap.Format.RGBA8888, Math.round(polygon.getBoundingRectangle().width),
                Math.round(polygon.getBoundingRectangle().height), false);
        final Matrix4 matrix = new Matrix4();

        matrix.setToOrtho2D(0, 0, buffer.getWidth(), buffer.getHeight());
        shapeRenderer.setProjectionMatrix(matrix);

        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);


        buffer.begin();
        shapeRenderer.begin(com.circlewave.squpz.utils.ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(color);
        shapeRenderer.polygon(polygon);

        shapeRenderer.end();
        buffer.end();
        buffer.dispose();

        shapeRenderer.setToDefaultMatrix();

        Gdx.gl.glDisable(GL30.GL_BLEND);

        final TextureRegion textureRegion = new TextureRegion(buffer.getColorBufferTexture());
        textureRegion.flip(false, true);
        return textureRegion;
    }

            /*
            for(Vector2 point : polygon.getVector2DTransformedVertices()) {
                stage.getShapeRenderer().circle(point.x + getX(), point.y + getY(), 2.5f);
            }

            stage.getShapeRenderer().setColor(Color.MAGENTA);
            Vector2 v2 = localToParentCoordinates(new Vector2(0,0));
            stage.getShapeRenderer().circle(v2.x, v2.y, 10);


            stage.getShapeRenderer().setColor(Color.BLACK);
            Vector2 v4 = localToStageCoordinates(new Vector2(0,0));
            stage.getShapeRenderer().circle(v4.x, v4.y, 5);

            stage.getShapeRenderer().setColor(Color.WHITE);
            Vector2 v5 = localToAscendantCoordinates(this, new Vector2(0,0));
            stage.getShapeRenderer().circle(v5.x, v5.y, 2);


            stage.getShapeRenderer().setColor(Color.RED);
            stage.getShapeRenderer().circle(polygon.getX(), polygon.getY(), 2);

             */

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Shape)) {
            return false;
        }
        final Shape otherShape = (Shape) other;
        return this.getColor().equals(((Shape) other).getColor()) && this.getPolygon().equals(otherShape.getPolygon());
    }
}
