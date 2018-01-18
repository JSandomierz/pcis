package com.mygdx.game.sky;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.game.FollowingCamera;
import com.mygdx.game.Game;

/**
 * Created by szostakm on 10.01.18.
 */

public class SkyActor extends Group {
    final static int ONE_PART_HIGH = 10000;
    final static private Color[] skyColors = new Color[]{
            new Color(0xd5e5ffff),
            new Color(0x70a9ffff),
            new Color(0x2266ccff),
            new Color(0x00255eff),
            new Color(0x000000ff)
    };
    final static private float HIGH_WHERE_CLOUDS_START = 800f;
    final static private float HIGH_WHERE_CLOUDS_END = 15000f;
    final static private float DISTANCE_BETWEEN_CLOUDS = 300f;

    private Sprite bgSprite = new Sprite(Game.content.getTexture("palace"));
    private FollowingCamera followingCamera;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Cloud testClous = new Cloud(1600);

    public SkyActor(FollowingCamera followingCamera) {
        //Texture txd = Game.content.getTexture("skybg");
        this.followingCamera = followingCamera;

        for(float i = HIGH_WHERE_CLOUDS_START; i < HIGH_WHERE_CLOUDS_END; i+=DISTANCE_BETWEEN_CLOUDS) {
            addActor(new Cloud(i));
        }
    }

    public void updateTexture(){
        bgSprite.setTexture(Game.content.getTexture("palace"));
    }

    @Override
    public void draw(Batch batch, float alpha) {
        float bottomY = followingCamera.position.y - Game.HEIGHT / 2f;
        int actualPart = (int) (bottomY / ONE_PART_HIGH);
        if (actualPart < skyColors.length - 1) {
            batch.end();
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0, actualPart * ONE_PART_HIGH, Game.WIDTH, ONE_PART_HIGH, skyColors[actualPart], skyColors[actualPart], skyColors[actualPart + 1], skyColors[actualPart + 1]);
            if (actualPart < skyColors.length - 2) {
                shapeRenderer.rect(0, (actualPart + 1) * ONE_PART_HIGH, Game.WIDTH, ONE_PART_HIGH, skyColors[actualPart + 1], skyColors[actualPart + 1], skyColors[actualPart + 2], skyColors[actualPart + 2]);
            }
            shapeRenderer.end();
            batch.begin();
        }
        super.draw(batch, alpha);
        batch.draw(bgSprite, 0, 0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }


}