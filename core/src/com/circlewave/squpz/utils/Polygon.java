package com.circlewave.squpz.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;

public class Polygon extends com.badlogic.gdx.math.Polygon {

    public Polygon(final Array<Vector2> vertices){
        setVertices(vertices);
    }

    public Polygon(){}

    public void setPosition(final Vector2 position) {
        setPosition(position.x, position.y);
    }

    public Array<Vector2> getVector2DVertices() {
        final float[] floatVertices = super.getVertices();
        final Array<Vector2> Vector2DVertices = new Array<>();
        for(int index = 0; index < floatVertices.length / 2; index =+2) {
            Vector2DVertices.add(new Vector2(floatVertices[index], floatVertices[index + 1]));
        }
        return Vector2DVertices;
    }

    public Array<Vector2> getVector2DTransformedVertices() {
        final Array<Vector2> vector2DVertices = new Array<>();
        for(int index = 0; index < getVertices().length / 2; index++) {
            vector2DVertices.add(getVector2TransformedVertex(index));
        }
        return vector2DVertices;
    }


    public void setVertices(Array<Vector2> vertices) {
        final float[] temp = new float[vertices.size * 2];
        for(int index = 0; index < vertices.size; index++){
            temp[index * 2] = vertices.get(index).x;
            temp[index * 2 + 1] = vertices.get(index).y;
        }
        super.setVertices(temp);
    }

    public Vector2 getVector2Vertex(final int index) {
        final float[] vertices = getVertices();

        if (index > vertices.length / 2) throw new IllegalArgumentException("index should be less then" +
                (vertices.length / 2) + ".");

        return new Vector2(vertices[index * 2], vertices[index * 2 + 1]);
    }

    public Vector2 getVector2TransformedVertex(final int index) {
        final float[] vertices = getTransformedVertices();

        if (index > vertices.length / 2) throw new IllegalArgumentException("index should be less then" +
                (vertices.length / 2) + ".");

        return new Vector2(vertices[index * 2], vertices[index * 2 + 1]);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Polygon)) {
            return false;
        }
        final Polygon otherPolygon = (Polygon) other;
        Array<Vector2> a = new Array<>();
        Array<Vector2> a1 = new Array<>();
        a.add(new Vector2());
        a1.add(new Vector2(0,0));
        //Game.print(">>>> " + getVector2DVertices().size);
        return Arrays.equals(getVertices(), otherPolygon.getVertices());
    }
}
