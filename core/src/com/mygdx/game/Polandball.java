package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by szostak on 1/12/18.
 */

public class Polandball extends PhysicsActor {
    private Vector2 startPos;
    private boolean live = true;
    public Polandball(World world, Vector2 position) {
        super(world, position, "polandball.png", BodyDef.BodyType.DynamicBody, "player", true);
        startPos = new Vector2(position);
        body.setActive(false);
    }

    public void restart() {
        body.setActive(false);
        setX(startPos.x);
        setY(startPos.y);
        setPosition(startPos.x, startPos.y);
        live = true;
    }

    public void start() {
        body.setActive(true);
        body.applyLinearImpulse(new Vector2(0, 1f), body.getPosition(), true);
    }

    public void kill() {
        live = false;
    }

    public boolean isLive() {
        return live;
    }
}
