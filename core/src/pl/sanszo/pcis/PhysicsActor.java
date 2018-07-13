package pl.sanszo.pcis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;


public class PhysicsActor extends Actor{
    private static final float PIXELS_TO_METERS = 100f;
    Sprite sprite;
    private String textureName;
    Body body;
    public String label;
    boolean active = true;
    private ActorAction<PhysicsActor, Polandball> beginContactAction, endContactAction;

    public PhysicsActor(World world, Vector2 position, String textureName, BodyDef.BodyType bodyType, String label, boolean isRound, float radius, boolean isSensor){
        prepareBody(world, position, textureName, bodyType, label);
        Shape shape;
        this.setWidth(sprite.getWidth());
        this.setHeight(sprite.getHeight());
        if(isRound){
            shape = new CircleShape();
            shape.setRadius(radius/PIXELS_TO_METERS);
        }else{
            shape = new PolygonShape();
            ((PolygonShape)shape).setAsBox(sprite.getWidth()/2 / PIXELS_TO_METERS, sprite.getHeight()
                    /2 / PIXELS_TO_METERS);
        }
        createFixture(shape, isSensor);
    }

    public PhysicsActor(World world, Vector2 position, String textureName, BodyDef.BodyType bodyType, String label, Vector2[] vertices, boolean isSensor){
        prepareBody(world, position, textureName, bodyType, label);
        createFixture(vertices, isSensor);
    }

    public void setSpriteTexture(String textureName){
        //Gdx.app.debug("Texture",textureName+"; "+Game.content.getTexture(textureName).toString());
        this.textureName = textureName;
        sprite.setTexture( Game.content.getTexture(textureName) );
    }

    public void reactToBeginContact(PhysicsActor me, Polandball him){
        if(beginContactAction!=null) beginContactAction.commenceOperation(me, him);
    }

    public void reactToEndContact(PhysicsActor me, Polandball him){
        if(endContactAction!=null) endContactAction.commenceOperation(me, him);
    }

    public void setBeginContactAction(ActorAction action){
        this.beginContactAction = action;
    }
    public void setEndContactAction(ActorAction action){
        this.endContactAction = action;
    }

    private void prepareBody(World world, Vector2 position, String textureName, BodyDef.BodyType bodyType, String label){
        this.textureName = textureName;
        beginContactAction = new ActorAction<PhysicsActor, Polandball>() {
            @Override
            public void commenceOperation(PhysicsActor me, Polandball him) {
                //nothing to do here, empty action
                Gdx.app.debug("ACTORACTION", "pxm: "+me.PIXELS_TO_METERS);
            }
        };
        this.label = label;
        sprite = new Sprite(Game.content.getTexture(textureName));
        setSize(sprite.getWidth(), sprite.getHeight());
        setOrigin(Align.center);
        //setPosition((-sprite.getWidth()/2)+position.x,-sprite.getHeight()/2 +position.y);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set((position.x + sprite.getWidth()/2f) / Game.PPM,
                (position.y + sprite.getHeight()/2f) / Game.PPM);

        body = world.createBody(bodyDef);
        body.setUserData(this);
        setPosition((body.getPosition().x * Game.PPM) - sprite.getWidth() / 2,
                (body.getPosition().y * Game.PPM) - sprite.getHeight() / 2);

    }

    public void createFixture(Shape shape, boolean isSensor){
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 0.5f;
        fixtureDef.isSensor = isSensor;
        //fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
        //fixtureDef.filter.maskBits = WORLD_ENTITY;
        body.createFixture(fixtureDef);
    }

    public void createFixture(Vector2[] vertices, boolean isSensor){
        for(Vector2 v: vertices){
            v.x /= PIXELS_TO_METERS;
            v.y /= PIXELS_TO_METERS;
        }
        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 0.5f;
        fixtureDef.isSensor = isSensor;
        //fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
        //fixtureDef.filter.maskBits = WORLD_ENTITY;
        body.createFixture(fixtureDef);
    }

    @Override
    public void draw(Batch batch, float alpha){
        if(active) {
            setPosition((body.getPosition().x * Game.PPM) - sprite.getWidth() / 2,
                    (body.getPosition().y * Game.PPM) - sprite.getHeight() / 2);

        setRotation((float)Math.toDegrees(body.getAngle()));
        }
        //sprite.setTexture(Game.content.getTexture(textureName));
        batch.draw(sprite, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),1f,1f,getRotation());
    }

    @Override
    public void setY(float y) {
        body.setTransform(body.getPosition().x, (y+sprite.getHeight()/2f)/Game.PPM, body.getAngle());
    }

    @Override
    public void setX(float x) {
        body.setTransform((x+sprite.getWidth()/2f)/Game.PPM, body.getPosition().y, body.getAngle());
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
