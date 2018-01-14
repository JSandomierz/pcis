package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Jakub on 2018-01-12.
 */

public class TrampolineActor extends Actor {
    public float maxWidth = 400f;
    private static final float PIXELS_TO_METERS = 100f;
    Body body;
    public String label;
    Texture bodyMiddle, bodyBegin;
    float height = 40, width;

    public TrampolineActor(World world, Vector2 position1, Vector2 position2, String textureName, BodyDef.BodyType bodyType, String label) {
        bodyBegin = new Texture(Gdx.files.internal("paddle_beg2.png"));
        bodyMiddle =  new Texture(Gdx.files.internal("paddle_mid.png"));
        //buildTrampoline(World world, position1, position2, 0f);
    }

    public void buildTrampoline(World world, Vector2 position1, Vector2 position2){
        width = position1.dst(position2);
        if( width > height/2f) {
            if (width < height) {
                width = height;
            }
            if( width > maxWidth ){
                width = maxWidth;
            }
            this.setPosition(position1.x-height/2f, position1.y-height/2f);
            this.setWidth(width);
            this.setHeight(height);
            this.setOrigin(height/2, height/2);
            this.setRotation(90f + (float) Math.toDegrees(Math.atan2((double) (position1.x - position2.x), (double) (position2.y - position1.y))));
            //buildBody(world, position1, position2);
        }
    }

    public void buildBody(World world, Vector2 position1, Vector2 position2){
        if(body!=null){
            world.destroyBody(body);
        }
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        bodyDef.position.set(
                (-(getWidth()/2+20) + getX()) / PIXELS_TO_METERS,
                (-(getHeight()/2+20) + getY()) / PIXELS_TO_METERS);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth()/2 / PIXELS_TO_METERS, getHeight()
                /2 / PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 1.9f;
        //fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
        //fixtureDef.filter.maskBits = WORLD_ENTITY;
        fixtureDef.isSensor = false;

        body.createFixture(fixtureDef);
        body.setUserData(this);
        float dx,dy;
        dx = position2.x - position1.x;
        dy = position2.y - position1.y;
        Vector2 delta = new Vector2(position2);
        delta = delta.sub(position1).limit(maxWidth);
        Gdx.app.debug("TRAMP","delta: "+delta.toString());
        body.setTransform((position1.x+delta.x/2)/PIXELS_TO_METERS, (position1.y+delta.y/2)/PIXELS_TO_METERS, (float)Math.toRadians(this.getRotation()));
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        //batch.draw(bodyMiddle, getX()+40f, getY(), getOriginX()-40f, getOriginY(), getWidth()-40f, getHeight(), 1f, 1f, 0, 0, 0, 40, 40, false, false);
        //batch.draw(bodyBegin, getX(), getY(), getOriginX(), getOriginY(), 40f, getHeight(), 1f, 1f, 0, 0, 0, 40, 40, false, false);
        //batch.draw(bodyBegin, getX()+getWidth(), getY(), getOriginX()-getWidth(), getOriginY(), 40f, getHeight(), 1f, 1f, 0, 0, 0, 40, 40, true, false);

        batch.draw(bodyBegin, getX(), getY(), getOriginX(), getOriginY(), 40f, getHeight(), 1f, 1f, getRotation(), 0, 0, 40, 40, false, false);
        batch.draw(bodyMiddle, getX()+40f, getY(), getOriginX()-40f, getOriginY(), getWidth()-40f, getHeight(), 1f, 1f, getRotation(), 0, 0, 40, 40, false, false);
        batch.draw(bodyBegin, getX()+getWidth(), getY(), getOriginX()-getWidth(), getOriginY(), 40f, getHeight(), 1f, 1f, getRotation(), 0, 0, 40, 40, true, false);
    }
}
