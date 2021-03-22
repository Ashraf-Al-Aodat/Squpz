package com.circlewave.squpz.utils;

import com.badlogic.gdx.math.Vector2;

import java.util.Comparator;

public class PointClockWiseComparator implements Comparator<Vector2> {

    private final Vector2 center;
    public PointClockWiseComparator(final Vector2 center) {
        this.center = center;
    }
    @Override
    public int compare(final Vector2 point1, Vector2 point2) {
        double angle1 = Math.atan2(point1.y - center.y, point1.x - center.x);
        double angle2 = Math.atan2(point2.y - center.y, point2.x - center.x);

        //For counter-clockwise, just reverse the signs of the return values
        if(angle1 < angle2) return 1;
        else if (angle2 < angle1) return -1;
        return 0;
    }
}
