package com.circlewave.squpz.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.circlewave.squpz.Game;
import com.circlewave.squpz.board.Board;
import com.circlewave.squpz.board.Component.*;
import com.circlewave.squpz.board.Component.Shape;
import com.circlewave.squpz.utils.Stage;

public class PlayScene extends Group {

    private final int resolution;
    private final Table table;

    PlayScene(final Game game) {
        this.resolution = 7;
        this.table = new Table();
        setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void initialize(){
        Board board = new Board(resolution);
        addActor(board);

        final Array<Vector2> v = new Array<>();
        v.add(new Vector2(0, 0));
        v.add(new Vector2(2, 0));
        v.add(new Vector2(2, 2));
        v.add(new Vector2(1, 3));
        v.add(new Vector2(0, 2));

        final Array<Vector2> v1 = new Array<>();
        v1.add(new Vector2(0, 0));
        v1.add(new Vector2(1, 0));
        v1.add(new Vector2(1, 3));

        final Array<Vector2> v2 = new Array<>();
        v2.add(new Vector2(0, 0));
        v2.add(new Vector2(3, 0));
        v2.add(new Vector2(3, 3));
        v2.add(new Vector2(0, 3));

        final Array<Vector2> v3 = new Array<>();
        v3.add(new Vector2(0, 0));
        v3.add(new Vector2(0, 3));
        v3.add(new Vector2(3, 3));
        v3.add(new Vector2(3, 2));
        v3.add(new Vector2(1, 2));
        v3.add(new Vector2(1, 0));

        final Shape shape1 = new IntersectionShape(v2, board.getTileSize(), Color.RED);
        final Shape shape2 = new FlippableShape(v3, board.getTileSize(), Color.RED, FlippableShape.V);
        final Shape shape3 = new NormalShape(v1, board.getTileSize(), Color.BLUE,
                ((Stage) getStage()).getShapeRenderer());
        final Shape shape4 = new ScalableShape(v1, board.getTileSize(), Color.GREEN);


        board.addActor(shape1);
        board.addActor(shape2);
        board.addActor(shape3);
        board.addActor(shape4);

    }

    private void add(final Shape shape){
        final float pad = table.getWidth() / 3 - shape.getPolygon().getBoundingRectangle().width;
        table.add(shape).padLeft(pad / 2).padRight(pad / 2);
        shape.getListeners().clear();
    }

    private ScrollPane buildScrollPane(final Table table){
        final ScrollPane pane = new ScrollPane(table);
        pane.setBounds(table.getX(), table.getY(), table.getWidth(), table.getHeight());
        pane.setScrollingDisabled(false, true);
        return pane;
    }
}
