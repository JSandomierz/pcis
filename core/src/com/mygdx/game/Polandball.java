package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by szostak on 1/12/18.
 */

public class Polandball extends PhysicsActor {
    private Vector2 startPos;
    private boolean live = true;
    private int maxHeight = 0;
    Filter filter = new Filter();
    public Polandball(World world, Vector2 position) {
        super(world, position, "polandball", BodyDef.BodyType.DynamicBody, "player", true, false);
        startPos = new Vector2(position);
        body.setAwake(false);
        body.setActive(false);
    }

    public Body getBody() {
        return body;
    }

    public void restart() {
        filter.maskBits = 0x1;
        body.getFixtureList().first().setFilterData(filter);
        maxHeight = 0;
        body.setAwake(false);
        body.setActive(false);

        setX(startPos.x);
        setY(startPos.y);
        setPosition(startPos.x, startPos.y);
        live = true;
    }

    public void start() {
        body.setAwake(true);
        body.setActive(true);
        body.applyLinearImpulse(new Vector2(0, 1f), body.getPosition(), true);
    }

    public void kill() {
        if(live) {
            live = false;
            filter.maskBits = 0x0;
            body.getFixtureList().first().setFilterData(filter);
            body.setLinearVelocity(0,0);
            //body.setAwake(false);
        }
    }

    public boolean isLive() {
        return live;
    }

    @Override
    public void act (float delta) {
        if(getY() > maxHeight) {
            maxHeight = (int) getY();
        }
    }

    public int getScore() {
        return maxHeight/100;
    }
}
