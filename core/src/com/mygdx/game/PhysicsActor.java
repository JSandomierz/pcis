package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.List;

/**
 * Created by Jakub on 2018-01-09.
 */

public class PhysicsActor extends Actor {
    private static final float PIXELS_TO_METERS = 100f;
    Sprite sprite;
    Body body;
    public String label;

    public PhysicsActor(World world, Vector2 position, String textureName, BodyDef.BodyType bodyType, String label){
        prepareBody(world, position, textureName, bodyType, label);
        createFixture();
    }

    public PhysicsActor(World world, Vector2 position, String textureName, BodyDef.BodyType bodyType, String label, Vector2[] vertices){
        prepareBody(world, position, textureName, bodyType, label);
        createFixture(vertices);
    }

    private void prepareBody(World world, Vector2 position, String textureName, BodyDef.BodyType bodyType, String label){
        this.label = label;
        Texture texture = new Texture(textureName);
        sprite = new Sprite(texture);
        sprite.setPosition((-sprite.getWidth()/2)+position.x,-sprite.getHeight()/2 +position.y);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set((sprite.getX() + sprite.getWidth()/2) / PIXELS_TO_METERS,
                (sprite.getY() + sprite.getHeight()/2) / PIXELS_TO_METERS);

        body = world.createBody(bodyDef);
        body.setUserData(this);
    }

    public void createFixture(){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2 / PIXELS_TO_METERS, sprite.getHeight()
                /2 / PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 0.5f;
        //fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
        //fixtureDef.filter.maskBits = WORLD_ENTITY;
        body.createFixture(fixtureDef);
    }

    public void createFixture(Vector2[] vertices){
        for(Vector2 v: vertices){
            v.x /= PIXELS_TO_METERS;
            v.y /= PIXELS_TO_METERS;
        }
        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 0.5f;
        //fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
        //fixtureDef.filter.maskBits = WORLD_ENTITY;
        body.createFixture(fixtureDef);
    }

    @Override
    public void draw(Batch batch, float alpha){
        sprite.setPosition((body.getPosition().x * PIXELS_TO_METERS) - sprite.getWidth()/2 ,
                            (body.getPosition().y * PIXELS_TO_METERS) - sprite.getHeight()/2 );

        sprite.setRotation((float)Math.toDegrees(body.getAngle()));
        batch.draw(sprite, sprite.getX(), sprite.getY(),sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(),1f,1f,sprite.getRotation());
    }
}
