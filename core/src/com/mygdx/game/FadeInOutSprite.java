package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * Created by szostak on 1/12/18.
 */

public class FadeInOutSprite extends Actor {
    private Sprite sprite;
    private float fadeInDuration;
    private float fadeOutDuration;
    public FadeInOutSprite(Texture texture, float fadeInDuration, float fadeOutDuration, float y) {
        this.fadeInDuration = fadeInDuration;
        this.fadeOutDuration = fadeOutDuration;
        sprite = new Sprite(texture);
        setColor(1f,1f,1f,0f);
        setPosition(Game.WIDTH/2f - sprite.getWidth()/2f, y);
    }

    public void show() {
        addAction(Actions.fadeIn(fadeInDuration));
    }

    public void hide() {
        addAction(Actions.fadeOut(fadeOutDuration));
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(1f,1f,1f, getColor().a);
        batch.draw(sprite, getX(), getY());
        batch.setColor(1f,1f,1f,1f);
    }

}
