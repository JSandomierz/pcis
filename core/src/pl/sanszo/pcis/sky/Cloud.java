package pl.sanszo.pcis.sky;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import pl.sanszo.pcis.Game;


public class Cloud extends Actor {
    private final static int CLOUD_WIDTH = 337;
    private final static int CLOUDS_NUM = 5;
    private final static float MOVEMENT_DURATION = 20f;
    private final TextureRegion[] cloudsRegions = new TextureRegion[CLOUDS_NUM];

    public void reloadClouds(){
        for(int i=0; i<CLOUDS_NUM; ++i)
            cloudsRegions[i] = new TextureRegion(Game.content.getTexture("clouds"), i*CLOUD_WIDTH, 0, CLOUD_WIDTH, Game.content.getTexture("clouds").getHeight());
    }

    private int cloudType;
    private boolean rightToLeft;

    public Cloud(float y) {
        reloadClouds();
        cloudType = (int)(Math.random()*CLOUDS_NUM);
        rightToLeft = (Math.random() > 0.5);
        setY(y);
        setX((float) (Math.random()*Game.WIDTH));
        changeMovement.run();
    }

    @Override
    public void draw(Batch batch, float alpha){
        batch.draw(cloudsRegions[cloudType], getX(), getY());
    }

    private Runnable changeMovement = new Runnable() {
        @Override
        public void run() {
            rightToLeft = !rightToLeft;
            cloudType = (int)(Math.random()*CLOUDS_NUM);
            float duration_scale = getX()/Game.WIDTH;
            Action action;
            if(rightToLeft)
                action = Actions.moveTo(Game.WIDTH, getY(), MOVEMENT_DURATION*(1-duration_scale));
            else
                action = Actions.moveTo(-CLOUD_WIDTH, getY(), MOVEMENT_DURATION*duration_scale);

            addAction(Actions.sequence(action, Actions.run(changeMovement)));

        }
    };
}
