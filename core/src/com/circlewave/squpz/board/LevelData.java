package com.circlewave.squpz.board;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.circlewave.squpz.board.Component.FlippableShape;
import com.circlewave.squpz.board.Component.IntersectionShape;
import com.circlewave.squpz.board.Component.NormalShape;
import com.circlewave.squpz.board.Component.Shape;
import com.circlewave.squpz.utils.ShapeRenderer;

public class LevelData {

    private int name;

    private Array<Color> shapeColors;
    private Array<Array<Vector2>> shapes;
    private Array<Integer> shapesType;
    private Array<Integer> flipDirections;

    private int resolution;

    private ShapeRenderer shapeRenderer;

    public LevelData() {}

    public LevelData(final ShapeRenderer shapeRenderer){
        this.shapeRenderer = shapeRenderer;
    }

    private Color getShapeColor(final int index) {
        return shapeColors.get(index);
    }

    private Shape getShape(final int index, final float snappedValue){
        switch (shapesType.get(index)){
            case 1:
                return new IntersectionShape(shapes.get(index), snappedValue, shapeColors.get(index));
            case 2:
                final Vector2 flipDirection = flipDirections.get(index) == 0 ? FlippableShape.H : FlippableShape.V;
                return new FlippableShape(shapes.get(index), snappedValue, shapeColors.get(index), flipDirection);
            default:
                return new NormalShape(shapes.get(index), snappedValue, shapeColors.get(index), shapeRenderer);
        }
    }

    public int getResolution() {
        return resolution;
    }

    public int getName() {
        return name;
    }
}

