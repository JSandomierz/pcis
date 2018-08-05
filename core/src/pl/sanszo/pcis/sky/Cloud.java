package pl.sanszo.pcis.sky;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import pl.sanszo.pcis.Game;
import pl.sanszo.pcis.Utilities;


public class Cloud extends Actor {

    private final static float MOVEMENT_DURATION = 20f;
    public final static int CLOUD_WIDTH = 337;
    public final static int CLOUDS_NUM = 5;

    private int cloudType;
    private boolean rightToLeft;
    private SkyActor skyActor;

    public Cloud(float y, SkyActor skyActor) {
        this.skyActor = skyActor;
        reset(y);
    }

    public void reset(float y) {
        clear();
        cloudType = Utilities.randomBetween(0, CLOUDS_NUM);
        rightToLeft = (Math.random() > 0.5);
        setY(y);
        setX(Utilities.randomBetween(0, Game.WIDTH));
        changeMovement.run();
    }

    @Override
    public void draw(Batch batch, float alpha){
        batch.draw(skyActor.getCloudRegion(cloudType), getX(), getY());
    }

    private Runnable changeMovement = new Runnable() {
        @Override
        public void run() {
            rightToLeft = !rightToLeft;
            cloudType = Utilities.randomBetween(0, CLOUDS_NUM);
            float duration_scale = (getX()+CLOUD_WIDTH)/(Game.WIDTH+CLOUD_WIDTH);
            Action action;
            if(rightToLeft)
                action = Actions.moveTo(Game.WIDTH, getY(), MOVEMENT_DURATION*(1f-duration_scale));
            else
                action = Actions.moveTo(-CLOUD_WIDTH, getY(), MOVEMENT_DURATION*duration_scale);

            addAction(Actions.sequence(action, Actions.run(changeMovement)));

        }
    };
}
