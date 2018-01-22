package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.sky.Cloud;
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
    private Cannon cannon;
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
    private FadeInOutSprite logo, tapToStart;
    private Gameover gameover;
    private Array<Fan> fanList = new Array<>();
    private Stage hud;
    private ScoreText scoreText;
    private Vector<PhysicsActor> stuffVector = new Vector<>();
    private Vector<PhysicsActor> thingsToMoveUp = new Vector<>();
    private String[] powerupTypes = {"boostup", "boostdown", "boosthorizontal"};
    private String[] enemyTypes = {"british", "russia"};

    private enum Mode {
        START,
        PLAY,
        TRY_AGAIN
    }
    private Mode currentMode = Mode.START;

    public GameStage(Viewport viewport, SpriteBatch batch, FollowingCamera camera , Camera b2dCamera, Stage hud) {
        super(viewport, batch);

        this.hud = hud;

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


        cannon = new Cannon();
        player = new Polandball(world, new Vector2(Game.WIDTH/2f-60f, 40f));
        player.setActive(false);
        cannon.runBeforeAddingPlayer(this);
        addActor(player);
        cannon.runAfterAddingPlayer(this);
        addActor(cannon);
        player.moveBy(500, 0);


        camera.setPlayer(player);
        scoreText = new ScoreText(player);
        this.hud.addActor(scoreText);

        trampolineActor = new TrampolineActor(world, new Vector2(300,300), new Vector2(400,400), "elo", BodyDef.BodyType.KinematicBody, "trampoline");
        addActor(trampolineActor);


        for(int i=0;i<2;i++){//powerups
            PhysicsActor someBonus = new PhysicsActor(world, new Vector2(Game.WIDTH/2, (float)(Math.random()*Game.HEIGHT*3.0+Game.HEIGHT*1.5)), "boostup", BodyDef.BodyType.KinematicBody, "boostup", false, 0f, true);
            someBonus.setBeginContactAction(new ActorAction<PhysicsActor, Polandball>() {
                @Override
                public void commenceOperation(PhysicsActor me, Polandball him) {
                    SoundManager.playSingle("powerup");
                    Gdx.input.vibrate(40);

                    switch(me.label){
                        case "boostup":
                            him.getBody().applyLinearImpulse(new Vector2((float)(Math.random()*-0.5),0.9f), him.getBody().getPosition(), true);
                            break;
                        case "boostdown":
                            him.getBody().applyLinearImpulse(new Vector2((float)(Math.random()-0.5),-0.5f), him.getBody().getPosition(), true);
                            break;
                        case "boosthorizontal":
                            him.getBody().applyLinearImpulse(new Vector2((float)(Math.random()*3.0-1.5),0.3f), him.getBody().getPosition(), true);
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

        for(int i=0;i<2;i++){//enemies
            PhysicsActor someBonus = new PhysicsActor(world, new Vector2((float)(Math.random()*Game.WIDTH/4.0+Game.WIDTH/2.0), (float)(Math.random()*Game.HEIGHT*4.0+Game.HEIGHT*1.5)), "enemy_british", BodyDef.BodyType.KinematicBody, "enemy_british", true, 50f, true);
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
        this.hud.addActor(logo);

        tapToStart = new FadeInOutSprite(Game.content.getTexture("taptostart"), 0.9f, 0.3f, 500f);
        this.hud.addActor(tapToStart);

        gameover = new Gameover(0.9f, 0.3f, 500f);
        this.hud.addActor(gameover);

        logo.show();
        tapToStart.show();

        SoundManager.playBackgroundMusic();
    }

    public void resetPhysicalActors(){
        for(PhysicsActor element: stuffVector){
            if(element.label.matches("boost.*")){
                element.body.setTransform( (float)(Math.random()*(double)(Game.WIDTH - element.getWidth()))/100f, (float)(Math.random()*4.0*Game.HEIGHT+Game.HEIGHT*2.0)/100f, 0f );
            }
            if(element.label.equals("enemy")){
                element.body.setTransform( (float)(Math.random()*(double)(Game.WIDTH - element.getWidth()))/100f, (float)(Math.random()*3.0*Game.HEIGHT+Game.HEIGHT*2.0)/100f, 0f );
            }
        }
    }

    public void restart() {
        camera.restart();
        player.restart();
        touchingScreen = false;
        cannon.restart();
        for(Fan fan : fanList) {
            fan.remove(world);
            fan.addAction(Actions.removeActor());
        }
        fanList.clear();
        trampolineActor.restart(world);
        Fan.restartSpawner();
        resetPhysicalActors();
    }

    private boolean saveHighscore() {
        int currentHighscore = Game.prefs.getInteger("highscore", 0);
        if(player.getScore() > currentHighscore) {
            Game.prefs.putInteger("highscore", player.getScore());
            Game.prefs.flush();
            return true;
        }
        return false;
    }

    @Override
    public void act(float delta){
        super.act(delta);
        if(!player.isLive() && currentMode == Mode.PLAY) {
            SoundManager.playSingle("gameover");
            Gdx.input.vibrate(new long[] {0, 100, 50, 100}, -1);
            currentMode = Mode.TRY_AGAIN;
            gameover.show(saveHighscore());
        }
        leftBorder.setY(camera.position.y);
        rightBorder.setY(camera.position.y);
        bottomSensor.setY(camera.position.y - Game.HEIGHT/2f);

        preWorldStep();//setup sensor
        world.step(delta * 2f, 6, 2);
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
            if(element.label.matches("boost.*")){
                element.body.setTransform( (float)(Math.random()*(double)(Game.WIDTH - element.getWidth()))/100f, element.body.getPosition().y+(float)(Math.random()*(3.0+player.body.getLinearVelocity().y/2)*Game.HEIGHT+Game.HEIGHT)/100f, 0f );
                String nextPowerUp = powerupTypes[(int)(Math.random()*10.0)%powerupTypes.length];
                element.label = nextPowerUp;
                element.setSpriteTexture( nextPowerUp );
            }
            if(element.label.matches("enemy.*")){
                String nextEnemy = "enemy_"+enemyTypes[(int)(Math.random()*10.0)%enemyTypes.length];
                element.label = nextEnemy;
                Gdx.app.debug("ENEMY",nextEnemy);
                element.setSpriteTexture(nextEnemy);
                if(element.label.equals("enemy_russia")){
                    if(Math.random()>0.4999){
                        element.body.setTransform( (element.getWidth()/2f)/100f, element.body.getPosition().y+(float)(Math.random()*3.0*Game.HEIGHT+Game.HEIGHT)/100f, 0f );
                    }else{
                        element.body.setTransform( (Game.WIDTH - element.getWidth()/2f)/100f, element.body.getPosition().y+(float)(Math.random()*3.0*Game.HEIGHT+Game.HEIGHT)/100f, 0f );
                    }
                }
                if(element.label.equals("enemy_british")){
                    element.body.setTransform( (float)(Math.random()*(double)((Game.WIDTH - element.getWidth())/2.0) + (Game.WIDTH - element.getWidth())/2.0)/100f, element.body.getPosition().y+(float)(Math.random()*3.0*Game.HEIGHT+Game.HEIGHT)/100f, 0f );
                }
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
            trampolineActor.buildTrampoline(world, touchBegin, touchEnd, true, 0f);
            //deltaSum = 0f;
        }
    }

    public void postWorldStep(){
        if(!this.isPlayerTouchingSensorPaddle && touchingScreen){
            trampolineActor.buildTrampoline(world, touchBegin, touchEnd, false, player.body.getLinearVelocity().y);
        }

        if((!touchingScreen) && isPlayerTouchingPaddle && isPlayerTouchingWall && player.body.getLinearVelocity().len()<1f){
            //Gdx.app.debug("CLENCH", "fix?");
            player.body.applyLinearImpulse(0.5f, 0.5f, player.body.getPosition().x, player.body.getPosition().y, true);
        }
    }

    private void startGame() {
        player.addAction(Actions.rotateBy(360, 0.8f));
        player.addAction(Actions.sequence(Actions.moveBy(-480f, 0, 0.8f), Actions.run(new Runnable() {
            @Override
            public void run() {
                SoundManager.playSingle("reload");

                cannon.addAction(Actions.sequence(Actions.rotateTo(0, 1), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        SoundManager.playSingle("shot");
                        Gdx.input.vibrate(60);
                        player.start();
                    }
                })));
            }
        })));
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        boolean actionsFinished = logo.getActions().size == 0 && tapToStart.getActions().size == 0 && gameover.getActions().size == 0;
        if(actionsFinished) {
            if ((currentMode == Mode.START || currentMode == Mode.TRY_AGAIN)) {
                if (currentMode == Mode.TRY_AGAIN) {
                    gameover.hide();
                    restart();
                }
                currentMode = Mode.PLAY;
                logo.hide();
                tapToStart.hide();
                startGame();

            } else {
                Vector2 stageCoords = screenToStageCoordinates(new Vector2(screenX, screenY));
                Actor hittedActor = hit(stageCoords.x, stageCoords.y, true);
                if (Input.Buttons.LEFT == button && stageCoords.dst(player.getX(), player.getY()) > player.body.getFixtureList().get(0).getShape().getRadius()) {
                    touchingScreen = true;
                    //SoundManager.playSingle("click");
                    touchBegin = stageCoords;
                    touchEnd = new Vector2(stageCoords);
                }
            }
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if(currentMode == Mode.PLAY) {
            Vector2 stageCoords = screenToStageCoordinates(new Vector2(screenX, screenY));
            Actor hittedActor = hit(stageCoords.x, stageCoords.y, true);
            if(touchingScreen) SoundManager.playSingle("click");
            if(Input.Buttons.RIGHT == button){
                player.body.setTransform(stageCoords.x/100.f, stageCoords.y/100.f, player.body.getAngle());
                player.body.setActive(true);
                player.body.setAwake(true);
            }
        }
        touchingScreen = false;
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown (int keyCode) {
        if(keyCode == Input.Keys.PAGE_DOWN){
        }
        if(keyCode == Input.Keys.LEFT){
            player.body.applyLinearImpulse(new Vector2(-0.1f, 0f), player.body.getPosition(), true);
        }
        if(keyCode == Input.Keys.RIGHT){
            player.body.applyLinearImpulse(new Vector2(0.1f, 0f), player.body.getPosition(), true);
        }
        if(keyCode == Input.Keys.UP){
            player.body.applyLinearImpulse(new Vector2(0f, 0.1f), player.body.getPosition(), true);
        }
        if(keyCode == Input.Keys.DOWN){
            player.body.applyLinearImpulse(new Vector2(0f, -0.1f), player.body.getPosition(), true);
        }
        return super.keyDown(keyCode);
    }

    public void reloadContent(){
        Fan.reloadFans();
        Cloud.reloadClouds();
        skyActor.updateTexture();
        logo.updateTexture(Game.content.getTexture("logo"));
        tapToStart.updateTexture(Game.content.getTexture("taptostart"));
        gameover.updateTextures();
        scoreText.updateTextures();
        cannon.updateTextures();
    }

    @Override
    public void dispose(){
        img.dispose();
    }

}
