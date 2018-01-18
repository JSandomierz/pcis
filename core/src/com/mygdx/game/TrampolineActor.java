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
    public float maxWidth = 200f;
    private static final float PIXELS_TO_METERS = 100f;
    Body body;
    boolean isSensor = false;
    public String label;
    BodyDef bodyDef = new BodyDef();
    float height = 40, width;
    float dx,dy;
    Vector2 delta = new Vector2();
    private String textureNameBegin, textureNameEnd;

    public TrampolineActor(World world, Vector2 position1, Vector2 position2, String textureName, BodyDef.BodyType bodyType, String label) {
        textureNameBegin = "paddle_beg2";
        textureNameEnd = "paddle_mid";
        //buildTrampoline(World world, position1, position2, 0f);
    }

    public void buildTrampoline(World world, Vector2 position1, Vector2 position2, boolean isSensor, float playerSpeed){
        this.isSensor = isSensor;
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
            buildBody(world, position1, position2, isSensor, playerSpeed);
        }
    }

    public void buildBody(World world, Vector2 position1, Vector2 position2, boolean isSensor, float playerSpeed){
        if(body!=null){
            world.destroyBody(body);
        }
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        bodyDef.position.set(
                (-(getWidth()/2+getHeight()/2) + getX()) / PIXELS_TO_METERS,
                (-(getHeight()) + getY()) / PIXELS_TO_METERS);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth()/2 / PIXELS_TO_METERS, getHeight()
                /2 / PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        double speed = Math.abs(playerSpeed);
        if(speed>4){
            fixtureDef.restitution = 1.0f;
        }else if(speed>3) {
            fixtureDef.restitution = 1.4f;
        }else if(speed>2) {
            fixtureDef.restitution = 1.65f;
        }else if(speed>1) {
            fixtureDef.restitution = 1.9f;
        }else{
            fixtureDef.restitution = 2.3f;
        }

        //fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
        //fixtureDef.filter.maskBits = WORLD_ENTITY;
        fixtureDef.isSensor = isSensor;

        body.createFixture(fixtureDef);
        body.setUserData(this);

        dx = position2.x - position1.x;
        dy = position2.y - position1.y;
        delta.x = position2.x; delta.y = position2.y;
        delta = delta.sub(position1).limit(maxWidth);
        body.setTransform((position1.x+delta.x/2)/PIXELS_TO_METERS, (position1.y+delta.y/2)/PIXELS_TO_METERS, (float)Math.toRadians(this.getRotation()));
    }

    public void restart(World world){
        if(body != null){
            world.destroyBody(body);
            body = null;
        }
        setPosition(-200, -200);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        batch.draw(Game.content.getTexture(textureNameBegin), getX(), getY(), getOriginX(), getOriginY(), 40f, getHeight(), 1f, 1f, getRotation(), 0, 0, 40, 40, false, false);
        batch.draw(Game.content.getTexture(textureNameEnd), getX()+40f, getY(), getOriginX()-40f, getOriginY(), getWidth()-40f, getHeight(), 1f, 1f, getRotation(), 0, 0, 40, 40, false, false);
        batch.draw(Game.content.getTexture(textureNameBegin), getX()+getWidth(), getY(), getOriginX()-getWidth(), getOriginY(), 40f, getHeight(), 1f, 1f, getRotation(), 0, 0, 40, 40, true, false);
    }
}
