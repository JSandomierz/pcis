package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by szostakm on 10.01.18.
 */

public class SkyActor extends Actor {
    Sprite bgSprite = new Sprite(Game.content.getTexture("palace"));
    final static int COLUMN_HIGH = 10000;
    final static int COLUMN_NUM = 4;
    private TextureRegion[] columns = new TextureRegion[COLUMN_NUM];


    public SkyActor() {
        Texture txd = Game.content.getTexture("skybg");
        for(int i=0; i<COLUMN_NUM; ++i) {
            columns[i] = new TextureRegion(txd, i, 0, 1, COLUMN_HIGH-1);
        }
    }



    @Override
    public void draw(Batch batch, float alpha){

        for(int i=0; i<Game.WIDTH; ++i)
            for(int j=0; j<COLUMN_NUM; ++j) {
                batch.draw(columns[j], i, j*(COLUMN_HIGH-1));
            }
        batch.draw(bgSprite, 0, 0);
    }
}
