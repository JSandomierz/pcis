package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;



public interface ActorAction<T,K> {
    public void commenceOperation(T me, K him);
}
