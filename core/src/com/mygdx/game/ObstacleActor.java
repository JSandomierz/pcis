package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Jakub on 2018-01-10.
 */

public class ObstacleActor extends Actor {
    private static final float PIXELS_TO_METERS = 100f;
    Body body;
    public String label;

    public ObstacleActor(World world, Vector2 position, Vector2 size, String label, boolean isSensor, float resituation){
        this.label = label;
        Gdx.app.debug("OBSTACLE", this.label);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        bodyDef.position.set(
                (size.x/2 + position.x) / PIXELS_TO_METERS,
                (size.y/2 + position.y) / PIXELS_TO_METERS);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x/2 / PIXELS_TO_METERS, size.y
                /2 / PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = resituation;
        //fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
        //fixtureDef.filter.maskBits = WORLD_ENTITY;
        fixtureDef.isSensor = isSensor;

        body.createFixture(fixtureDef);
        body.setUserData(this);
    }
}
