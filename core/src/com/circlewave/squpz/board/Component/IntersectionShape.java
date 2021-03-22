package com.circlewave.squpz.board.Component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.circlewave.squpz.Game;
import com.circlewave.squpz.board.Board;
import com.circlewave.squpz.utils.*;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.OperatorExportToJson;
import com.esri.core.geometry.OperatorIntersection;

public class IntersectionShape extends Shape{

    public IntersectionShape(final Array<Vector2> points, final float snappedValue, final Color color) {
        super(points, snappedValue, color);
        setOrigin(Align.center);
        addListener(new ShapeGestureListener());
    }

    @Override
    public void drawBorder(final ShapeRenderer shapeRenderer, final float parentAlpha) {
        shapeRenderer.setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, getColor().a * parentAlpha);
        shapeRenderer.dashedPolygon(getPolygon(), 5, getSnappedValue() / 20, false);
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

            if (getParent() instanceof Board) {
                final Board board = ((Board) getParent());
                for (final Shape otherShape : board.getShapes()) {
                    if (getZIndex() > otherShape.getZIndex()) {
                        shapeRenderer.setColor(mixColors(otherShape.getColor()));
                        shapeRenderer.polygon(getIntersectionShape(otherShape));
                    }
                }
            }

            shapeRenderer.end();

            Gdx.gl.glDisable(GL30.GL_BLEND);

            batch.begin();
        }
    }

    public Array<Vector2> parseStringGeometryToVectorArray(final Geometry geometry) {
        final Array<Vector2> points = new Array<>();
        final String jsonString = OperatorExportToJson.local().execute(null, geometry);
        for (final String text : jsonString.replaceAll(":", "").replaceAll("\\{", "")
                .replaceAll("\\}", "").replaceAll("rings","")
                .replaceAll("]", "").replaceAll("\"spatialReference\"\"wkid\"4326", "")
                .replaceAll("\"", "").split("\\[")){
            if(!text.isEmpty() && text.length() > 2) {
                final String[] arrayString = text.split(",");
                points.add(new Vector2(Float.parseFloat(arrayString[0]), Float.parseFloat(arrayString[1])));
            }
        }
        return points;
    }

    public Polygon getIntersectionShape(final Shape shape) {
        if(!shape.equals(this)) {

            final com.esri.core.geometry.Polygon polygon = new com.esri.core.geometry.Polygon();
            for(int i = 0; i < getPolygon().getVector2DTransformedVertices().size; i++){
                Vector2 point = getPolygon().getVector2TransformedVertex(i);
                if(i == 0){
                    polygon.startPath(point.x, point.y);
                } else {
                    polygon.lineTo(point.x, point.y);
                }
            }

            final com.esri.core.geometry.Polygon otherPolygon = new com.esri.core.geometry.Polygon();
            for(int i = 0; i < shape.getPolygon().getVector2DTransformedVertices().size; i++){
                Vector2 point = shape.getPolygon().getVector2TransformedVertex(i);
                if(i == 0){
                    otherPolygon.startPath(point.x, point.y);
                } else {
                    otherPolygon.lineTo(point.x, point.y);
                }
            }

            final Array<Vector2> intersectionPoints =  parseStringGeometryToVectorArray(
                    OperatorIntersection.local().execute(polygon, otherPolygon,
                            null, null));

            if (intersectionPoints.size > 3) {
                return new Polygon(intersectionPoints);
            }
        }
        return null;
    }

    private Color mixColors(final Color color){
        final float r = color.r + getColor().r;
        final float g = color.g + getColor().g;
        final float b = color.b + getColor().b;
        final float a = Math.max(getColor().a, color.a);
        return new Color(r, g, b, a);
    }
}
