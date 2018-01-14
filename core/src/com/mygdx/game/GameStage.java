package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.sky.Fan;
import com.mygdx.game.sky.SkyActor;


/**
 * Created by Jakub on 2018-01-09.
 */

public class GameStage extends Stage {
    Texture img;
   // List<PhysicsActor> obstacleList = new ArrayList<>();
    private Polandball player;
    public World world;
    private FollowingCamera camera;
    private Camera b2dCamera;
    private SkyActor skyActor;
    private ObstacleActor leftBorder, rightBorder, bottomSensor;
    private FadeInOutSprite logo, tapToStart, tapToTryAgain;
    private Array<Fan> fanList = new Array<>();

    private enum Mode {
        START,
        PLAY,
        TRY_AGAIN
    }
    private Mode currentMode = Mode.START;

    public GameStage(Viewport viewport, SpriteBatch batch, FollowingCamera camera , Camera b2dCamera) {
        super(viewport, batch);
        world = new World(new Vector2(0, -1f),true);
        world.setContactListener(new MyContactListener());

        this.camera = camera;
        this.b2dCamera = b2dCamera;

        skyActor = new SkyActor(camera);
        addActor(skyActor);

        float w = Game.WIDTH;
        float h = Game.HEIGHT;
        bottomSensor = new ObstacleActor(world, new Vector2(0,20), new Vector2(w, 10), "bottom", true, 1.2f);
        leftBorder = new ObstacleActor(world, new Vector2(0,0), new Vector2(10, h), "left", false, 0.5f);
        rightBorder = new ObstacleActor(world, new Vector2(w-10,0), new Vector2(10, h), "right", false, 0.5f);

        player = new Polandball(world, new Vector2(Game.WIDTH/2f-60f, 40f));
        addActor(player);
        camera.setPlayer(player);

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
        world.step(delta*2f, 6, 2);
        Fan newFan = Fan.spawnFan(world,camera.position.y + Game.HEIGHT/2f + 300f);
        if(newFan != null) {
            fanList.add(newFan);
            addActor(newFan);
        }
        for(Fan fan : fanList)
            fan.update(delta, player);
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        if(currentMode == Mode.START || currentMode == Mode.TRY_AGAIN) {
            currentMode = Mode.PLAY;
            logo.hide();
            tapToStart.hide();
            player.start();
        }
//        Vector2 stageCoords = screenToStageCoordinates(new Vector2(screenX, screenY));
//        Actor hittedActor = hit(stageCoords.x, stageCoords.y, true);
//        Gdx.app.debug("TOUCH", "down, pos: "+stageCoords.toString());

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
//        Vector2 stageCoords = screenToStageCoordinates(new Vector2(screenX, screenY));
//        Actor hittedActor = hit(stageCoords.x, stageCoords.y, true);
//        Gdx.app.debug("TOUCH", "  up, pos: "+stageCoords.toString());
//        player.body.setTransform(stageCoords.x/100.f, stageCoords.y/100.f, player.body.getAngle());
//        player.body.setActive(true);
//        player.body.setAwake(true);
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
