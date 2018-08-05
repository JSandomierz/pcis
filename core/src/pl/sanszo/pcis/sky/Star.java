package pl.sanszo.pcis.sky;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import pl.sanszo.pcis.Game;
import pl.sanszo.pcis.Utilities;

public class Star extends Actor {

    public final static int STARS_NUM = 3;
    public final static int STAR_WIDTH = 111;

    private int starType;
    private SkyActor skyActor;
    public Star(float startY, SkyActor skyActor) {
        this.skyActor = skyActor;
        reset(startY);
    }

    public void reset(float startY) {
        clearActions();
        starType = Utilities.randomBetween(0, STARS_NUM);
        setOrigin(STAR_WIDTH/2f, STAR_WIDTH/2f);
        setSize(STAR_WIDTH, STAR_WIDTH);
        setScale(Utilities.randomBetween(0.6f, 1f));
        setRotation(Utilities.randomBetween(0f, 360f));
        setPosition(Utilities.randomBetween(0, Game.WIDTH), Utilities.randomBetween(startY, startY+Game.HEIGHT));
        setColor(1f, 1f, 1f, Utilities.randomBetween(0.7f, 1f));
        float duration = Utilities.randomBetween(2f, 5f);
        addAction(Actions.forever(Actions.sequence(Actions.scaleBy(-0.4f, -0.4f, duration/2f), Actions.scaleBy(0.4f, 0.4f, duration/2f))));
    }

    @Override
    public void draw(Batch batch, float alpha){
        batch.setColor(1f,1f,1f, getColor().a);
        batch.draw (skyActor.getStarRegion(starType), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        batch.setColor(1f,1f,1f,1f);
    }
}
