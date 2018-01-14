package com.mygdx.game.sky;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Game;

/**
 * Created by szostak on 1/13/18.
 */

public class Fan extends Actor {
    private final static int FRAMES_NUM = 3;
    private final static float INTERVAL = 0.1f;
    private static int FRAME_WIDTH;
    private static Texture texture = Game.content.getTexture("fan");
    private static TextureRegion[] frames = new TextureRegion[FRAMES_NUM];
    {
        FRAME_WIDTH = texture.getWidth()/FRAMES_NUM;
        for(int i=0; i<FRAMES_NUM; ++i)
            frames[i] = new TextureRegion(texture, FRAME_WIDTH*i, 0, FRAME_WIDTH, texture.getHeight());
    }
    private int currentFrame = 0;
    private float time = 0f;

    public Fan(float y, boolean right) {
        setY(y);
        if(right)
            setX(0);
        else
            setX(Game.WIDTH-FRAME_WIDTH);
    }

    public void update(float delta) {
        time += delta;
        if(time > INTERVAL) {
            currentFrame++;
            if(currentFrame >= FRAMES_NUM) currentFrame = 0;
            time = 0f;
        }
    }

    @Override
    public void draw(Batch batch, float alpha){
        batch.draw(frames[currentFrame], getX(), getY());
    }
}
