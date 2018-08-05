package pl.sanszo.pcis;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import pl.sanszo.pcis.hud.HudActor;


public class FadeInOutSprite extends HudActor {
    protected Sprite sprite;
    private float fadeInDuration;
    private float fadeOutDuration;
    public FadeInOutSprite(Texture texture, float fadeInDuration, float fadeOutDuration, float y) {
        this.fadeInDuration = fadeInDuration;
        this.fadeOutDuration = fadeOutDuration;
        sprite = new Sprite(texture);
        setColor(1f,1f,1f,0f);
        setPosition(Game.WIDTH/2f - sprite.getWidth()/2f, y);
    }

    @Override
    public void show() {
        addAction(Actions.fadeIn(fadeInDuration));
    }

    @Override
    public void hide() {
        addAction(Actions.fadeOut(fadeOutDuration));
    }

    public void updateTexture(Texture tex){
        sprite.setTexture(tex);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(1f,1f,1f, getColor().a);
        batch.draw(sprite, getX(), getY());
        batch.setColor(1f,1f,1f,1f);
    }

}
