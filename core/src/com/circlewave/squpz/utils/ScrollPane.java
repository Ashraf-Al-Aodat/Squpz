package com.circlewave.squpz.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.circlewave.squpz.Game;

public class ScrollPane extends com.badlogic.gdx.scenes.scene2d.ui.ScrollPane {

    public ScrollPane(final Table table){
        super(table);
    }

    public Table getTable(){
        return ((Table) getActor());
    }

    public Cell add(final Actor actor){
        float factor = 1;
        if(getHeight() > getWidth()) factor = -getWidth() / 100f;
        else factor = -getHeight() / 100f;
        //actor.addAction(Actions.scaleBy(factor, factor, 30));
        //actor.setDebug(true);

        if(actor.getWidth() > actor.getHeight()) factor = actor.getWidth() / actor.getHeight();
        else factor = actor.getHeight() / actor.getWidth();
        return getTable().add(actor).width(actor.getWidth() / factor).height(actor.getHeight() / factor);
    }
}
