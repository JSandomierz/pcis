package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Jakub on 2018-01-09.
 */

public class MyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Gdx.app.debug("Phys", "begin contact");
        Body bodyb = contact.getFixtureB().getBody();
        Body bodya = contact.getFixtureA().getBody();
        //body.applyLinearImpulse(0f, 1f, body.getPosition().x, body.getPosition().y, true);
        if(bodyb.getUserData()!=null && bodya.getUserData()!=null){
            if(bodya.getUserData().getClass() == ObstacleActor.class){
                ObstacleActor obstacle = (ObstacleActor)bodya.getUserData();
                Gdx.app.debug("Phys", "Collision with obstacle: "+obstacle.label);
            }
            if(bodyb.getUserData().getClass() == ObstacleActor.class){
                ObstacleActor obstacle = (ObstacleActor)bodyb.getUserData();
                Gdx.app.debug("Phys", "Collision with obstacle: "+obstacle.label);
            }
            if(bodyb.getUserData().getClass() == PhysicsActor.class){
                PhysicsActor physicsActor = ((PhysicsActor)bodyb.getUserData());
                Gdx.app.debug("Phys", "begin contact body: "+physicsActor);
            }
            Polandball player = null;
            if(bodya.getUserData().getClass() == Polandball.class)
                player = (Polandball) bodya.getUserData();
            else if(bodyb.getUserData().getClass() == Polandball.class)
                player = (Polandball) bodyb.getUserData();

            if(player != null) {
                Gdx.app.log("PLAYER", "GAME OVER");
                player.kill();
            }

        }
    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.debug("Phys", "end contact: ");
        Body body = contact.getFixtureB().getBody();
        //body.applyLinearImpulse(0f, 1f, body.getPosition().x, body.getPosition().y, true);
        if(body.getUserData()!=null){
            if(body.getUserData().getClass() == PhysicsActor.class){
                PhysicsActor physicsActor = ((PhysicsActor)body.getUserData());
                Gdx.app.debug("Phys", "end contact body: "+physicsActor);
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Gdx.app.debug("Phys", "pre solve");
        Body body = contact.getFixtureB().getBody();
        //body.destroyFixture(body.getFixtureList().first());
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        //body.applyForceToCenter(0f, -300f, true);
        Gdx.app.debug("Phys", "post solve");
    }

}
