package com.circlewave.squpz.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ShortArray;
import com.circlewave.squpz.Game;

public final class ShapeRenderer extends com.badlogic.gdx.graphics.glutils.ShapeRenderer {

    /**
     * Draws a rectangle with rounded corners of the given radius.
     */

    private final EarClippingTriangulator triangulator;
    private final Matrix4 matrix;

    public ShapeRenderer(final Matrix4 matrix) {
        super();
        setAutoShapeType(true);
        this.matrix = matrix;
        this.triangulator = new EarClippingTriangulator();
    }

    public final void roundRect(final float x, final float y, final float width, final float height, final float radius) {
        // Central rectangle
        super.rect(x, y + radius, width, height - 2 * radius);

        // Four side rectangles, in clockwise order
        super.rect(x + radius, y, width - 2 * radius, radius); // down
        // super.rect(x + width - radius, y + radius, radius, height - 2*radius); // lift
        super.rect(x + radius, y + height - radius, width - 2 * radius, radius); // up
        // super.rect(x, y + radius, radius, height - 2*radius); // right

        // arches, in clockwise order
        super.arc(x + radius, y + radius, radius, 180f, 90f); // down-lift
        super.arc(x + width - radius, y + radius, radius, 270f, 90f); // down-right
        super.arc(x + width - radius, y + height - radius, radius, 0f, 90f); // up-right
        super.arc(x + radius, y + height - radius, radius, 90f, 90f); // up left
    }

    /**
     * Draws a rectangle with top rounded corners of the given radius.
     */
    public final void topRoundedRect(final float x, final float y, final float width, final float height, final float radius) {
        // Central rectangle
        super.rect(x, y, width, height - radius);

        // Four side rectangles, in clockwise order
        super.rect(x + radius, y + height - radius, width - 2 * radius, radius); // up

        // arches, in clockwise order
        super.arc(x + width - radius, y + height - radius, radius, 0f, 90f); // up-right
        super.arc(x + radius, y + height - radius, radius, 90f, 90f); // up left
    }

    public final void polygon(final Polygon polygon){
        if(polygon != null) {
            final ShortArray triangleOrder = triangulator.computeTriangles(polygon.getTransformedVertices());
            for (int index = 0; index < triangleOrder.size / 3; index++) {
                triangle(polygon.getVector2TransformedVertex(triangleOrder.get(index * 3)),
                        polygon.getVector2TransformedVertex(triangleOrder.get(index * 3 + 1)),
                        polygon.getVector2TransformedVertex(triangleOrder.get(index * 3 + 2)));
            }
        }
    }

    private void triangle(final Vector2 point1, final Vector2 point2, final Vector2 point3){
        triangle(point1.x, point1.y, point2.x, point2.y, point3.x, point3.y);
    }

    public final void polygon(final Polygon polygon, final float lineWidth){
        final Array<Vector2> points = polygon.getVector2DTransformedVertices();
        for(int i = 0, j = 1; i < points.size; i++, j = (j > points.size - 2) ?  0 : j + 1){
            rectLine(points.get(i), points.get(j), lineWidth);
        }
    }

    public final void dashedPolygon(final  Polygon polygon, final int segments, final float lineWidth,
                                    final boolean roundedDashes){
        final Array<Vector2> points = polygon.getVector2DTransformedVertices();
        for(int i = 0, j = 1; i < points.size; i++, j = (j > points.size - 2) ?  0 : j + 1){
            dashedLine(points.get(i), points.get(j), segments, lineWidth, roundedDashes);
        }
    }

    /**
     * Normal ShapeRenderer draws a Wedge not a arc. Segments determine how many lines are drawn to approximate the arc
     * @param x
     * @param y
     * @param radius
     * @param start
     * @param degrees
     * @param segments
     * @param color
     */

    public void arcNotWedge (float x, float y, float radius, float start, float degrees, int segments, Color color) {
        if (segments <= 0) throw new IllegalArgumentException("segments must be > 0.");
        float colorBits = color.toFloatBits();
        float theta = (2 * MathUtils.PI * (degrees / 360.0f)) / segments;
        float cos = MathUtils.cos(theta);
        float sin = MathUtils.sin(theta);
        float cx = radius * MathUtils.cos(start * MathUtils.degreesToRadians);
        float cy = radius * MathUtils.sin(start * MathUtils.degreesToRadians);
        for (int i = 0; i < segments; i++) {
            getRenderer().color(colorBits);
            getRenderer().vertex(x + cx, y + cy, 0);
            float temp = cx;
            cx = cos * cx - sin * cy;
            cy = sin * temp + cos * cy;
            getRenderer().color(colorBits);
            getRenderer().vertex(x + cx, y + cy, 0);
        }
    }

    public final void dottedPolygon(final Polygon polygon, final float dotDist, final float size){
        final Array<Vector2> points = polygon.getVector2DTransformedVertices();
        float length = 0;
        final Vector2 mid = new Vector2();
        for(int i = 0, j = 1; i < points.size; i++, j = (j > points.size - 2) ?  0 : j + 1){
            mid.set(points.get(i)).sub(points.get(j));
            length += mid.len();
            Game.print("" +i + "  " + mid.len());
        }
        length /= 20;
        for(int i = 0, j = 1; i < points.size; i++, j = (j > points.size - 2) ?  0 : j + 1){
            length = dottedLine(points.get(i), points.get(j), length, size);

        }
    }

    /**
     * Draws dotted line.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param dotDist is the distance between individual dots eg, 5
     */
    public final float dottedLine(final Vector2 point1, final Vector2 point2, final float length, final float size) {
        final Vector2 temp = new Vector2(point1).sub(point2);
        final float angle =  temp.angle() + 180;
        final float localLength = temp.len();
        temp.set(point1);
        Game.print("LL: "+ localLength + " L: "+ length);
        if(localLength >= length && length > 0) {
            while ((Math.round(temp.dst(point2) * 100) / 100f) >= (Math.round(length * 100) / 100f)) {
                Game.print(": " + ((Math.round(temp.dst(point2) * 10) / 10f)) + "  " + (length));
                temp.set((float) (temp.x + (length * Math.cos(Math.toRadians(angle)))),
                        (float) (temp.y + (length * Math.sin(Math.toRadians(angle)))));
                circle(temp, size);
            }
        }
        Game.print("NEW L: " + (length - localLength));
        return length - localLength;
    }

    private void circle(final Vector2 point, final float radius){
        circle(point.x, point.y, radius);
    }

    /**
     * Draws dashed Line
     * @param point1  the start point
     * @param point2 the end point
     * @param segments the number of dashes
     * @param lineWidth the size of the line
     * @param roundedDashes make the dash rounded
     */
    public void dashedLine(final Vector2 point1, final Vector2 point2, final int segments, final float lineWidth,
                           final boolean roundedDashes){
        final Vector2 temp1 = new Vector2(point1);
        final Vector2 temp2 = new Vector2(point2);
        final Vector2 mid = new Vector2(temp2).sub(temp1);
        mid.scl(100f / segments / 200);
        temp2.set(new Vector2(temp1).add(mid));
        mid.scl(2);

        for(int segment = 0; segment < segments; segment++){
            rectLine(temp1, temp2, lineWidth);
            if(roundedDashes){
                circle(temp1.x, temp1.y,lineWidth / 2);
                circle(temp2.x, temp2.y,lineWidth / 2);
            }
            temp1.add(mid);
            temp2.add(mid);
        }
    }

    public Vector2 abs(final Vector2 vector2){
        return vector2.set(Math.abs(vector2.x) , Math.abs(vector2.y));
    }

    /**
     * Draws dotted arc
     * @param x
     * @param y
     * @param radius
     * @param start start angle of arc
     * @param degrees degrees of the arc
     * @param dotsInPi how many dots will there be in 90 degrees
     */
    public void dottedArcNotWedge (float x, float y, float radius, float start, float degrees, int dotsInPi) {
        if (dotsInPi <= 0) throw new IllegalArgumentException("dotsInPi must be > 0.");
        float dots=degrees*dotsInPi/90.0f;
        float theta = (2 * MathUtils.PI * (degrees / 360.0f)) / dots;
        float cos = MathUtils.cos(theta);
        float sin = MathUtils.sin(theta);
        float cx = radius * MathUtils.cos(start * MathUtils.degreesToRadians);
        float cy = radius * MathUtils.sin(start * MathUtils.degreesToRadians);
        for (int i = 0; i < dots; i++) {
            super.point(x + cx, y + cy, 0);
            float temp = cx;
            cx = cos * cx - sin * cy;
            cy = sin * temp + cos * cy;
            super.point(x + cx, y + cy, 0);
        }
    }

    public void setToDefaultMatrix() {
        setProjectionMatrix(matrix);
    }
}
