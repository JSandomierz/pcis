package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


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
    private SkyActor skyActor = new SkyActor();
    private ObstacleActor leftBorder, rightBorder, bottomSensor;
    private FadeInOutSprite logo, tapToStart, tapToTryAgain;

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

        addActor(skyActor);

        float w = Game.WIDTH;
        float h = Game.HEIGHT;
        bottomSensor = new ObstacleActor(world, new Vector2(0,20), new Vector2(w, 10), "bottom", true, 1.2f);
        leftBorder = new ObstacleActor(world, new Vector2(0,0), new Vector2(10, h), "left", false, 0.5f);
        rightBorder = new ObstacleActor(world, new Vector2(w-10,0), new Vector2(10, h), "right", false, 0.5f);

        player = new Polandball(world, new Vector2(Game.WIDTH/2f-60f, 40f));
        addActor(player);
        camera.setPlayer(player);

        logo = new FadeInOutSprite(Game.content.getTexture("logo"), 0.8f, 0.3f, 1000f);
        addActor(logo);

        tapToStart = new FadeInOutSprite(Game.content.getTexture("taptostart"), 0.9f, 0.3f, 700f);
        addActor(tapToStart);

        logo.show();
        tapToStart.show();
    }

    @Override
    public void act(float delta){
        super.act(delta);
        if(!player.isLive() && currentMode == Mode.PLAY) {
            currentMode = Mode.TRY_AGAIN;

        }
        leftBorder.setY(camera.position.y);
        rightBorder.setY(camera.position.y);
        bottomSensor.setY(camera.position.y - Game.HEIGHT/2f);
        world.step(delta*2f, 6, 2);
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

    private void updateCamera(PhysicsActor player) {
        float d;
        if(player.getY() > Game.HEIGHT*3/4f) {
            d = player.getY() -1/4f*Game.HEIGHT;

        } else {
            d = Game.HEIGHT*1/2f;
        }
        Gdx.app.log("CAM", "player: " + player.getY() + " cam: " + camera.position.y);
        if(d > camera.position.y) {
            camera.position.y = d;
            b2dCamera.position.y = camera.position.y / 100f;
        }
    }
    private  void restartCamera() {
        camera.position.y = Game.HEIGHT*1/2f;
        b2dCamera.position.y = camera.position.y / 100f;
    }
}
