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
            if(bodya.getUserData() instanceof  TrampolineActor || bodyb.getUserData() instanceof  TrampolineActor){
                GameStage.isPlayerTouchingPaddle = true;
                //body is trampoline and is sensor, for a and b
                if((bodya.getFixtureList().get(0).isSensor() && bodya.getUserData() instanceof TrampolineActor) || bodyb.getFixtureList().get(0).isSensor() && bodyb.getUserData() instanceof TrampolineActor){
                    GameStage.isPlayerTouchingSensorPaddle = true;
                    //Gdx.app.debug("SENSOR", "paddle touch");
                }
            }
            if(bodya.getUserData().getClass() == ObstacleActor.class){
                ObstacleActor obstacle = (ObstacleActor)bodya.getUserData();
                GameStage.isPlayerTouchingWall = true;
                //Gdx.app.debug("Phys", "Collision with obstacle: "+obstacle.label);
            }
            if(bodyb.getUserData().getClass() == ObstacleActor.class){
                ObstacleActor obstacle = (ObstacleActor)bodyb.getUserData();
                GameStage.isPlayerTouchingWall = true;
                //Gdx.app.debug("Phys", "Collision with obstacle: "+obstacle.label);
            }
            if(bodyb.getUserData().getClass() == PhysicsActor.class){
                PhysicsActor physicsActor = ((PhysicsActor)bodyb.getUserData());
                //Gdx.app.debug("Phys", "begin contact body: "+physicsActor);
            }
            Polandball player = null;
            ObstacleActor bottom = null;

            if(bodya.getUserData().getClass() == Polandball.class
                    && bodyb.getUserData() instanceof  ObstacleActor) {
                player = (Polandball) bodya.getUserData();
                bottom = (ObstacleActor) bodyb.getUserData();
            } else if(bodyb.getUserData().getClass() == Polandball.class
                    && bodya.getUserData() instanceof  ObstacleActor) {
                player = (Polandball) bodyb.getUserData();
                bottom = (ObstacleActor) bodya.getUserData();
            }

            if(player != null && bottom.label.equals("bottom")) {
                player.kill();
            }

        }
    }

    @Override
    public void endContact(Contact contact) {
        //Gdx.app.debug("Phys", "end contact: ");
        Body bodyb = contact.getFixtureB().getBody();
        Body bodya = contact.getFixtureA().getBody();
        //body.applyLinearImpulse(0f, 1f, body.getPosition().x, body.getPosition().y, true);
        if(bodyb.getUserData()!=null && bodya.getUserData()!=null){
            if(bodya.getUserData().getClass() == ObstacleActor.class){
                ObstacleActor obstacle = (ObstacleActor)bodya.getUserData();
                GameStage.isPlayerTouchingWall = false;
                //Gdx.app.debug("Phys", "end collision with obstacle: "+obstacle.label);
            }
            if(bodya.getUserData().getClass() == TrampolineActor.class || bodyb.getUserData().getClass() == TrampolineActor.class){
                GameStage.isPlayerTouchingPaddle = false;
            }
            if(bodyb.getUserData().getClass() == ObstacleActor.class){
                ObstacleActor obstacle = (ObstacleActor)bodyb.getUserData();
                GameStage.isPlayerTouchingWall = false;
                //Gdx.app.debug("Phys", "end collision with obstacle: "+obstacle.label);
            }
            if(bodyb.getUserData().getClass() == PhysicsActor.class){
                PhysicsActor physicsActor = ((PhysicsActor)bodyb.getUserData());
                //Gdx.app.debug("Phys", "end contact body: "+physicsActor);
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        //Gdx.app.debug("Phys", "pre solve");
        //Body body = contact.getFixtureB().getBody();
        //body.destroyFixture(body.getFixtureList().first());
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        //body.applyForceToCenter(0f, -300f, true);
        //Gdx.app.debug("Phys", "post solve");
    }

}
