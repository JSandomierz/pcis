package com.mygdx.game.sky;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Game;
import com.mygdx.game.Polandball;

/**
 * Created by szostak on 1/13/18.
 */

public class Fan extends Actor {
    private final static int FRAMES_NUM = 3;
    private final static float INTERVAL = 0.1f;
    private final static float WIND_FORCE = 0.5f;
    private static int FRAME_WIDTH;
    private static Texture texture = Game.content.getTexture("fan");
    private static TextureRegion[] frames = new TextureRegion[FRAMES_NUM];
    private static TextureRegion[] flippedFrames = new TextureRegion[FRAMES_NUM];

    static {
        FRAME_WIDTH = texture.getWidth()/FRAMES_NUM;
        for(int i=0; i<FRAMES_NUM; ++i) {
            frames[i] = new TextureRegion(texture, FRAME_WIDTH * i, 0, FRAME_WIDTH, texture.getHeight());
            flippedFrames[i] = new TextureRegion(texture, FRAME_WIDTH * i, 0, FRAME_WIDTH, texture.getHeight());
            flippedFrames[i].flip(true, false);
        }
        restartSpawner();
    }
    private int currentFrame = 0;
    private float time = 0f;
    private boolean flipped;
    private Body body;

    public Fan(World world, float y, boolean right) {
        flipped = !right;
        setY(y);
        if(right)
            setX(0);
        else
            setX(Game.WIDTH-FRAME_WIDTH);

        float offsetX;
        if(right) offsetX = 90f;
        else offsetX = 220f;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((getX() + offsetX) / Game.PPM,
                (getY() + 90f)  / Game.PPM);
        body = world.createBody(bodyDef);
        body.setUserData(this);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50f / Game.PPM, 100f / Game.PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 0.5f;
        body.createFixture(fixtureDef);
    }

    public void update(float delta, Polandball player) {
        time += delta;
        if(time > INTERVAL) {
            currentFrame++;
            if(currentFrame >= FRAMES_NUM) currentFrame = 0;
            time = 0f;
            if(player.getY() > getY() && player.getY() < getY() + 190f) {
                float force;
                if (flipped)
                    force = -WIND_FORCE * player.getX() / Game.WIDTH;
                else
                    force = WIND_FORCE * (1f - player.getX() / Game.WIDTH);
                
                player.getBody().applyLinearImpulse(force, 0, player.getBody().getPosition().x, player.getBody().getPosition().y, true);
            }
        }
    }

    public static float lastY;
    public static float next;

    public static void restartSpawner() {
        lastY = 0f;
        next = (float) (1800f+Math.random()*1800f);
    }

    public static Fan spawnFan(World world, float y) {
        if(y - lastY > next) {
            lastY = y;
            next = (float) (1800f+Math.random()*1800f);
            return new Fan(world, y, Math.random() > 0.5);
        }
        return null;
    }

    public void remove(World world) {
        world.destroyBody(body);
    }

    @Override
    public void draw(Batch batch, float alpha){
        if(flipped)
            batch.draw(flippedFrames[currentFrame], getX(), getY());
        else
            batch.draw(frames[currentFrame], getX(), getY());
    }
}
