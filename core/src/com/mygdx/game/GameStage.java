package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sun.rmi.runtime.Log;

/**
 * Created by Jakub on 2018-01-09.
 */

public class GameStage extends Stage {
    Texture img;
   // List<PhysicsActor> obstacleList = new ArrayList<>();
    PhysicsActor player;
    public World world;

    public GameStage(Viewport viewport, SpriteBatch batch) {
        super(viewport, batch);

        world = new World(new Vector2(0, -1f),true);
        world.setContactListener(new MyContactListener());

        //actor = new PhysicsActor(world, new Vector2(Gdx.graphics.getWidth()/2, 100));

        //create window frame
        ObstacleActor obstacleActor;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        obstacleActor = new ObstacleActor(world, new Vector2(0,-h+10), new Vector2(w, h), "bottom", false, 1.2f);
        addActor(obstacleActor);
        obstacleActor = new ObstacleActor(world, new Vector2(-w+10,0), new Vector2(w, h), "left", false, 0.5f);
        addActor(obstacleActor);
        obstacleActor = new ObstacleActor(world, new Vector2(w-10,0), new Vector2(w, h), "right", false, 0.5f);
        addActor(obstacleActor);

        //Gdx.app.debug("");
        PhysicsActor actor;
        actor = new PhysicsActor(world, new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2), "badlogic.jpg", BodyDef.BodyType.DynamicBody, "player", true);
        addActor(actor);
        player = actor;

        actor = new PhysicsActor(world, new Vector2(20, 20), "badlogic.jpg", BodyDef.BodyType.DynamicBody, "box", false);
        addActor(actor);
        /*Vector2[] vertices;
        int divs = 6;
        vertices = new Vector2[divs];
        float r = 100f;
        for(int i=0;i<divs;i++){
            float x,y;
            x = r * (float)(Math.cos(Math.toRadians(360/divs*i+90)));
            y = r * (float)(Math.sin(Math.toRadians(360/divs*i+90)));
            Gdx.app.debug("TRIG", i+": "+x+", "+y);
            vertices[i] = new Vector2( x,y );
        }
        actor = new PhysicsActor(world, new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2), "badlogic.jpg", BodyDef.BodyType.DynamicBody, "player", vertices);
        */ // custom shape
    }

    @Override
    public void act(float delta){
        super.act(delta);
        world.step(delta*2f, 6, 2);
        //Gdx.app.debug("MyTag", "my debug message");
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        Vector2 stageCoords = screenToStageCoordinates(new Vector2(screenX, screenY));
        Actor hittedActor = hit(stageCoords.x, stageCoords.y, true);
        Gdx.app.debug("TOUCH", "down, pos: "+stageCoords.toString());
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        Vector2 stageCoords = screenToStageCoordinates(new Vector2(screenX, screenY));
        Actor hittedActor = hit(stageCoords.x, stageCoords.y, true);
        Gdx.app.debug("TOUCH", "  up, pos: "+stageCoords.toString());
        player.body.setTransform(stageCoords.x/100.f, stageCoords.y/100.f, player.body.getAngle());
        player.body.setActive(true);
        player.body.setAwake(true);
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
