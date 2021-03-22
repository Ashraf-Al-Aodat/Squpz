package com.circlewave.squpz.board.Component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.circlewave.squpz.Game;
import com.circlewave.squpz.utils.*;

public class NormalShape extends Shape {


    public NormalShape(final Array<Vector2> points, final float snappedValue, final Color color, ShapeRenderer shapeRenderer) {
        super(points, snappedValue, color);
        addListener(new ShapeGestureListener());
    }


    @Override
    public void drawBorder(final ShapeRenderer shapeRenderer, final float parentAlpha) {
        shapeRenderer.setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, getColor().a * parentAlpha);
        shapeRenderer.polygon(getPolygon(), getSnappedValue() / 20);
    }

    public TextureRegion createTexture(final float snapValue, final ShapeRenderer shapeRenderer){
        final FrameBuffer buffer = new FrameBuffer(Pixmap.Format.RGBA8888,
                Math.round(getPolygon().getBoundingRectangle().width / snapValue) * 128,
                Math.round(getPolygon().getBoundingRectangle().height / snapValue) * 128, false);

        final Matrix4 matrix = new Matrix4();

        matrix.setToOrtho2D(0, 0, buffer.getWidth(), buffer.getHeight());
        shapeRenderer.setProjectionMatrix(matrix);

        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);


        buffer.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.BLACK);
        final int repeated = 10;
        final float pad = ((float) buffer.getWidth()) / repeated;
        final float lineWidth = ((float) buffer.getWidth()) / (repeated * 2);
        for(int i = 0; i <= repeated; i++) {
            for (int j = 0; j < repeated * (buffer.getHeight() / snapValue); j++) {
                //shapeRenderer.rect(pad * i - lineWidth / 2, pad * j - lineWidth / 2, lineWidth, lineWidth);
                //shapeRenderer.rectLine(pad * i, pad * j, pad * (i + 1), pad * (j + 1), lineWidth);
                //shapeRenderer.rectLine(pad * i, pad * (j + 1), pad * (i + 1), pad * j, lineWidth);
                shapeRenderer.circle(pad * i, pad * j, lineWidth / 2.5f);
            }
        }




        shapeRenderer.end();
        buffer.end();
        buffer.dispose();

        shapeRenderer.setToDefaultMatrix();

        Gdx.gl.glDisable(GL30.GL_BLEND);

        return new TextureRegion(buffer.getColorBufferTexture());
    }
}
