package pl.sanszo.pcis;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class Cannon extends Actor {
    private Sprite backLayer = new Sprite(Game.content.getTexture("cannonback"));
    private Actor backActor = new Actor() {
        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(base, Game.WIDTH/2f - base.getWidth()/2f, Cannon.this.getY()-30f);
            batch.draw(backLayer, Cannon.this.getX(), Cannon.this.getY(), Cannon.this.getOriginX(), Cannon.this.getOriginY(), Cannon.this.getWidth(), Cannon.this.getHeight(), 1f, 1f, Cannon.this.getRotation());
        }

    };
    private Sprite topLayer = new Sprite(Game.content.getTexture("cannontop"));
    private Actor topActor = new Actor() {
        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(topLayer, Cannon.this.getX(), Cannon.this.getY(), Cannon.this.getOriginX(), Cannon.this.getOriginY(), Cannon.this.getWidth(), Cannon.this.getHeight(), 1f, 1f, Cannon.this.getRotation());
        }
    };

    private Sprite base = new Sprite(Game.content.getTexture("cannonbase"));


    public Cannon() {
        setPosition(Game.WIDTH/2f - backLayer.getWidth()/2f, 40f);
        setSize(backLayer.getWidth(), backLayer.getHeight());
        setOrigin(backLayer.getOriginX(), 55f);
        setScale(backLayer.getScaleX(), backLayer.getScaleY());
        setRotation(-90f);
    }

    public void restart() {
        setRotation(-90f);
    }



    public void runBeforeAddingPlayer(Stage stage) {
        stage.addActor(backActor);
    }

    public void runAfterAddingPlayer(Stage stage) {
        stage.addActor(topActor);
    }

    public void updateTextures() {
        backLayer.setTexture(Game.content.getTexture("cannonback"));
        topLayer.setTexture(Game.content.getTexture("cannontop"));
        base.setTexture(Game.content.getTexture("cannonbase"));
    }

}
