package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.sky.Fan;
import com.mygdx.game.sky.SkyActor;

import java.util.Vector;

import static com.badlogic.gdx.Gdx.input;


/**
 * Created by Jakub on 2018-01-09.
 */

public class GameStage extends Stage {
    Texture img;
   // List<PhysicsActor> obstacleList = new ArrayList<>();
    private Polandball player;
    private TrampolineActor trampolineActor;

    Vector2 touchBegin, touchEnd;
    boolean touchingScreen;
    float deltaSum = 0f;

    static boolean isPlayerTouchingWall = false, isPlayerTouchingPaddle = false;
    static boolean isPlayerTouchingSensorPaddle = false;

    public World world;
    private FollowingCamera camera;
    private Camera b2dCamera;
    private SkyActor skyActor;
    private ObstacleActor leftBorder, rightBorder, bottomSensor;
    private FadeInOutSprite logo, tapToStart, tapToTryAgain;
    private Array<Fan> fanList = new Array<>();
    private Vector<PhysicsActor> stuffVector = new Vector<>();
    private Vector<PhysicsActor> thingsToMoveUp = new Vector<>();
    private String[] powerupTypes = {"boostup", "boostdown", "boosthorizontal"};

    private enum Mode {
        START,
        PLAY,
        TRY_AGAIN
    }
    private Mode currentMode = Mode.START;

    public GameStage(Viewport viewport, SpriteBatch batch, FollowingCamera camera , Camera b2dCamera) {
        super(viewport, batch);
        touchBegin = new Vector2();
        touchEnd = new Vector2();
        touchingScreen = false;

        world = new World(new Vector2(0, -1.5f),true);
        world.setContactListener(new MyContactListener());

        this.camera = camera;
        this.b2dCamera = b2dCamera;

        skyActor = new SkyActor(camera);
        addActor(skyActor);

        float w = Game.WIDTH;
        float h = Game.HEIGHT;
        bottomSensor = new ObstacleActor(world, new Vector2(0,20), new Vector2(w, 10), "bottom", true, 1.2f);
        leftBorder = new ObstacleActor(world, new Vector2(0,0), new Vector2(10, h), "left", false, 0.7f);
        rightBorder = new ObstacleActor(world, new Vector2(w-10,0), new Vector2(10, h), "right", false, 0.7f);

        player = new Polandball(world, new Vector2(Game.WIDTH/2f-60f, 40f));
        addActor(player);
        camera.setPlayer(player);

        trampolineActor = new TrampolineActor(world, new Vector2(300,300), new Vector2(400,400), "elo", BodyDef.BodyType.KinematicBody, "trampoline");
        addActor(trampolineActor);

        for(int i=0;i<3;i++){//powerups
            PhysicsActor someBonus = new PhysicsActor(world, new Vector2(Game.WIDTH/2, (float)Math.random()*Game.HEIGHT*2+Game.HEIGHT), "boostup", BodyDef.BodyType.KinematicBody, "boostup", false, true);
            someBonus.setBeginContactAction(new ActorAction<PhysicsActor, Polandball>() {
                @Override
                public void commenceOperation(PhysicsActor me, Polandball him) {
                    switch(me.label){
                        case "boostup":
                            him.getBody().applyLinearImpulse(new Vector2((float)(Math.random()*2.0-1.0),1.2f), him.getBody().getPosition(), true);
                            break;
                        case "boostdown":
                            him.getBody().applyLinearImpulse(new Vector2((float)(Math.random()*2.0-1.0),-1.2f), him.getBody().getPosition(), true);
                            break;
                        case "boosthorizontal":
                            him.getBody().applyLinearImpulse(new Vector2((float)(Math.random()*8.0-4.0),0.5f), him.getBody().getPosition(), true);
                            break;
                    }
                }
            });
            someBonus.setEndContactAction(new ActorAction<PhysicsActor, Polandball>() {
                @Override
                public void commenceOperation(PhysicsActor me, Polandball him) {
                    thingsToMoveUp.add(me);
                }
            });
            addActor(someBonus);
            stuffVector.add(someBonus);
        }

        for(int i=0;i<3;i++){//enemies
            PhysicsActor someBonus = new PhysicsActor(world, new Vector2((float)Math.random()*Game.WIDTH/4+Game.WIDTH/2, (float)Math.random()*Game.HEIGHT*2+Game.HEIGHT), "britishball", BodyDef.BodyType.KinematicBody, "enemy", false, true);
            someBonus.setBeginContactAction(new ActorAction<PhysicsActor, Polandball>() {
                @Override
                public void commenceOperation(PhysicsActor me, Polandball him) {
                    him.kill();
                }
            });
            addActor(someBonus);
            stuffVector.add(someBonus);
        }

        logo = new FadeInOutSprite(Game.content.getTexture("logo"), 0.8f, 0.3f, 800f);
        addActor(logo);

        tapToStart = new FadeInOutSprite(Game.content.getTexture("taptostart"), 0.9f, 0.3f, 500f);
        addActor(tapToStart);

        logo.show();
        tapToStart.show();
    }


    public void restart() {
        camera.restart();
        player.restart();
        for(Fan fan : fanList) {
            fan.remove(world);
            fan.addAction(Actions.removeActor());
        }
        fanList.clear();
        trampolineActor.restart();
        Fan.restartSpawner();
    }

    @Override
    public void act(float delta){
        super.act(delta);
        if(!player.isLive() && currentMode == Mode.PLAY) {
            currentMode = Mode.TRY_AGAIN;
            restart();
        }
        leftBorder.setY(camera.position.y);
        rightBorder.setY(camera.position.y);
        bottomSensor.setY(camera.position.y - Game.HEIGHT/2f);

        preWorldStep();//setup sensor
        world.step(delta*2f, 6, 2);
        postWorldStep();//transform sensor to solid paddle is sensor does not detect collision

        Fan newFan = Fan.spawnFan(world,camera.position.y + Game.HEIGHT/2f + 300f);
        if(newFan != null) {
            fanList.add(newFan);
            addActor(newFan);
        }
        for(Fan fan : fanList)
            fan.update(delta, player);


        for(PhysicsActor element: stuffVector){
            if(element.body.getPosition().y < bottomSensor.body.getPosition().y) thingsToMoveUp.add(element);
        }
        for(PhysicsActor element: thingsToMoveUp){
            Vector2 currentPosition = new Vector2();

            if(element.label.matches("boost.*")){
                element.body.setTransform( (float)(Math.random()*(double)(Game.WIDTH - element.getWidth()))/100f, element.body.getPosition().y+(float)(Math.random()*4.0*Game.HEIGHT+Game.HEIGHT)/100f, 0f );
                String nextPowerUp = powerupTypes[(int)(Math.random()*10.0)%powerupTypes.length];
                element.label = nextPowerUp;
                element.setSpriteTexture( nextPowerUp );
            }
            if(element.label.equals("enemy")){
                element.body.setTransform( (float)(Math.random()*(double)(Game.WIDTH - element.getWidth()))/100f, element.body.getPosition().y+(float)(Math.random()*3.0*Game.HEIGHT+Game.HEIGHT)/100f, 0f );
            }
        }
        thingsToMoveUp.clear();
        //Gdx.app.debug("MyTag", "my debug message");
    }

    public void preWorldStep(){
        this.isPlayerTouchingSensorPaddle = false;
        if( touchingScreen ){
            touchEnd.set(input.getX(), input.getY());
            touchEnd = screenToStageCoordinates(touchEnd);
            trampolineActor.buildTrampoline(world, touchBegin, touchEnd, true);
            //deltaSum = 0f;
        }
    }

    public void postWorldStep(){
        if(!this.isPlayerTouchingSensorPaddle && touchingScreen){
            trampolineActor.buildTrampoline(world, touchBegin, touchEnd, false);
        }

        if((!touchingScreen) && isPlayerTouchingPaddle && isPlayerTouchingWall && player.body.getLinearVelocity().len()<1f){
            //Gdx.app.debug("CLENCH", "fix?");
            player.body.applyLinearImpulse(0.5f, 0.5f, player.body.getPosition().x, player.body.getPosition().y, true);
        }
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        if(currentMode == Mode.START || currentMode == Mode.TRY_AGAIN) {
            currentMode = Mode.PLAY;
            logo.hide();
            tapToStart.hide();
            player.start();
        } else {
            Vector2 stageCoords = screenToStageCoordinates(new Vector2(screenX, screenY));
            Actor hittedActor = hit(stageCoords.x, stageCoords.y, true);
            if( Input.Buttons.LEFT ==  button && stageCoords.dst(player.getX(), player.getY()) > player.body.getFixtureList().get(0).getShape().getRadius()){
                touchingScreen = true;
                touchBegin = stageCoords;
                touchEnd = new Vector2(stageCoords);
            }
            Gdx.app.debug("TOUCH", "down, pos: "+stageCoords.toString());
        }
//        Vector2 stageCoords = screenToStageCoordinates(new Vector2(screenX, screenY));
//        Actor hittedActor = hit(stageCoords.x, stageCoords.y, true);
//        Gdx.app.debug("TOUCH", "down, pos: "+stageCoords.toString());

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if(currentMode == Mode.PLAY) {
            Vector2 stageCoords = screenToStageCoordinates(new Vector2(screenX, screenY));
            Actor hittedActor = hit(stageCoords.x, stageCoords.y, true);
            Gdx.app.debug("TOUCH", "  up, pos: "+stageCoords.toString());
            touchingScreen = false;
            if(Input.Buttons.RIGHT == button){
                player.body.setTransform(stageCoords.x/100.f, stageCoords.y/100.f, player.body.getAngle());
                player.body.setActive(true);
                player.body.setAwake(true);
            }
        }
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown (int keyCode) {
        if(keyCode == Input.Keys.PAGE_DOWN){
        }
        if(keyCode == Input.Keys.LEFT){
            player.body.applyLinearImpulse(new Vector2(-1f, 0f), player.body.getPosition(), true);
        }
        if(keyCode == Input.Keys.RIGHT){
            player.body.applyLinearImpulse(new Vector2(1f, 0f), player.body.getPosition(), true);
        }
        if(keyCode == Input.Keys.UP){
            player.body.applyLinearImpulse(new Vector2(0f, 1f), player.body.getPosition(), true);
        }
        if(keyCode == Input.Keys.DOWN){
            player.body.applyLinearImpulse(new Vector2(0f, -1f), player.body.getPosition(), true);
        }
        return super.keyDown(keyCode);
    }

    @Override
    public void dispose(){
        img.dispose();
    }

}
