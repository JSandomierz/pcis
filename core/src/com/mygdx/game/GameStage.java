package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.logging.Level;
import java.util.logging.Logger;

import sun.rmi.runtime.Log;

/**
 * Created by Jakub on 2018-01-09.
 */

public class GameStage extends Stage {
    Texture img;
    PhysicsActor actor;
    World world;

    public GameStage(Viewport viewport, SpriteBatch batch) {
        super(viewport, batch);

        world = new World(new Vector2(0, -1f),true);
        world.setContactListener(new MyContactListener());

        //actor = new PhysicsActor(world, new Vector2(Gdx.graphics.getWidth()/2, 100));
        actor = new PhysicsActor(world, new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2), "badlogic.jpg", BodyDef.BodyType.DynamicBody);
        addActor(actor);
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
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown (int keyCode) {
        if(keyCode == Input.Keys.PAGE_DOWN){
        }
        return super.keyDown(keyCode);
    }

    @Override
    public void dispose(){
        img.dispose();
    }
}
