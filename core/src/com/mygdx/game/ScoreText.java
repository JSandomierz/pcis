package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;



public class ScoreText extends Actor {
    private BitmapFont font = Game.content.getFont();
    private Polandball polandball;
    public ScoreText(Polandball polandball) {
        this.polandball = polandball;
    }

    public void updateTextures(){
        font = Game.content.getFont();
    }

    @Override
    public void draw(Batch batch, float alpha){
        font.draw(batch, String.format("%06d", polandball.getScore()), 10f, Game.HEIGHT-10f);
    }
}
