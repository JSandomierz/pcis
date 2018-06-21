package pl.sanszo.pcis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Body;



public class MyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        //Gdx.app.debug("Phys", "begin contact");
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

            if(bodyb.getUserData() instanceof PhysicsActor && bodya.getUserData() instanceof Polandball){
                //Gdx.app.debug("Phys", "end contact body: "+physicsActor);
                ((PhysicsActor) bodyb.getUserData()).reactToBeginContact((PhysicsActor)(bodyb.getUserData()), (Polandball)(bodya.getUserData()) );
            }else if(bodya.getUserData() instanceof PhysicsActor && bodyb.getUserData() instanceof Polandball){
                //Gdx.app.debug("Phys", "end contact body: "+physicsActor);
                ((PhysicsActor) bodya.getUserData()).reactToBeginContact((PhysicsActor)(bodya.getUserData()), (Polandball)(bodyb.getUserData()) );
            }

            if(player != null) {
                if(bottom.label.equals("bottom")) {
                    player.kill();
                } else {
                    SoundManager.playSingle("bounce");
                    Gdx.input.vibrate(20);
                }
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
            }else if(bodya.getUserData().getClass() == ObstacleActor.class){
                ObstacleActor obstacle = (ObstacleActor)bodyb.getUserData();
                GameStage.isPlayerTouchingWall = false;
            }

            if(bodya.getUserData().getClass() == TrampolineActor.class || bodyb.getUserData().getClass() == TrampolineActor.class){
                GameStage.isPlayerTouchingPaddle = false;
            }

            if(bodyb.getUserData() instanceof PhysicsActor && bodya.getUserData() instanceof Polandball){
                //Gdx.app.debug("Phys", "end contact body: "+physicsActor);
                ((PhysicsActor) bodyb.getUserData()).reactToEndContact((PhysicsActor)(bodyb.getUserData()), (Polandball)(bodya.getUserData()) );
            }else if(bodya.getUserData() instanceof PhysicsActor && bodyb.getUserData() instanceof Polandball){
                //Gdx.app.debug("Phys", "end contact body: "+physicsActor);
                ((PhysicsActor) bodya.getUserData()).reactToEndContact((PhysicsActor)(bodya.getUserData()), (Polandball)(bodyb.getUserData()) );
            }

            if((bodya.getUserData() instanceof Polandball && bodyb.getUserData() instanceof TrampolineActor)
                    ||
                    ( bodyb.getUserData() instanceof Polandball && bodya.getUserData() instanceof TrampolineActor)) {
                SoundManager.playSingle("jump");
                Gdx.input.vibrate(50);
            }
        }


    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
