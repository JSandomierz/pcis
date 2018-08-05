package pl.sanszo.pcis.sky;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;

import pl.sanszo.pcis.FollowingCamera;
import pl.sanszo.pcis.Game;
import pl.sanszo.pcis.Utilities;


public class SkyActor extends Group {
    final static int ONE_PART_HIGH = 10000;
    final static private Color[] skyColors = new Color[]{
            new Color(0xd5e5ffff),
            new Color(0x70a9ffff),
            new Color(0x2266ccff),
            new Color(0x00255eff),
            new Color(0x000000ff)
    };



    final static private float HIGH_WHERE_CLOUDS_START = 800f;
    final static private float HIGH_WHERE_CLOUDS_END = 25000f;
    final static private float DISTANCE_BETWEEN_CLOUDS = 400f;
    private final  TextureRegion[] cloudsRegions = new TextureRegion[Cloud.CLOUDS_NUM];
    private Texture cloudsTexture = Game.content.getTexture("clouds");
    private Array<Cloud> clouds = new Array<>();


    public final static int NUMBER_OF_STARS = 15;
    private final  TextureRegion[] starsRegions = new TextureRegion[Cloud.CLOUDS_NUM];
    private Texture starsTexture = Game.content.getTexture("stars");
    private Array<Star> stars = new Array<>();

    private Sprite bgSprite = new Sprite(Game.content.getTexture("palace"));
    private FollowingCamera followingCamera;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public SkyActor(FollowingCamera followingCamera) {
        this.followingCamera = followingCamera;
        loadCloudsTexture();
        loadStarsTexture();
        resetClouds();
        for(int i=0; i<NUMBER_OF_STARS; ++i) {
            Star star = new Star(HIGH_WHERE_CLOUDS_END,this);
            addActor(star);
            stars.add(star);
        }

        for(float i = HIGH_WHERE_CLOUDS_START; i < HIGH_WHERE_CLOUDS_START+Game.HEIGHT; i+=DISTANCE_BETWEEN_CLOUDS) {
            Cloud cloud = new Cloud(i, this);
            clouds.add(cloud);
            addActor(cloud);
        }
    }

    private void loadCloudsTexture() {
        for(int i=0; i<Cloud.CLOUDS_NUM; ++i)
            cloudsRegions[i] = new TextureRegion(cloudsTexture, i*Cloud.CLOUD_WIDTH, 0, Cloud.CLOUD_WIDTH, cloudsTexture.getHeight());
    }

    private void resetClouds() {
        float i = HIGH_WHERE_CLOUDS_START;
        for(Cloud cloud : clouds) {
            cloud.reset(i);
            i+=DISTANCE_BETWEEN_CLOUDS;
        }
    }

    private void resetStars() {
        for(Star star : stars) {
            star.reset(HIGH_WHERE_CLOUDS_END);
        }
    }

    public void restart() {
        resetStars();
        resetClouds();
    }

    private void loadStarsTexture() {
        starsTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        for(int i=0; i<Star.STARS_NUM; ++i)
            starsRegions[i] = new TextureRegion(starsTexture, i*Star.STAR_WIDTH, 0, Star.STAR_WIDTH, starsTexture.getHeight());
    }

    public TextureRegion getCloudRegion(int id) {
        return cloudsRegions[id];
    }

    public TextureRegion getStarRegion(int id) {
        return starsRegions[id];
    }

    @Override
    public void draw(Batch batch, float alpha) {
        float bottomY = followingCamera.position.y - Game.HEIGHT / 2f;
        int actualPart = (int) (bottomY / ONE_PART_HIGH);
        if (actualPart < skyColors.length - 1) {
            batch.end();
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0, actualPart * ONE_PART_HIGH, Game.WIDTH, ONE_PART_HIGH, skyColors[actualPart], skyColors[actualPart], skyColors[actualPart + 1], skyColors[actualPart + 1]);
            if (actualPart < skyColors.length - 2) {
                shapeRenderer.rect(0, (actualPart + 1) * ONE_PART_HIGH, Game.WIDTH, ONE_PART_HIGH, skyColors[actualPart + 1], skyColors[actualPart + 1], skyColors[actualPart + 2], skyColors[actualPart + 2]);
            }
            shapeRenderer.end();
            batch.begin();
        }
        super.draw(batch, alpha);
        batch.draw(bgSprite, 0, 0);
    }

    @Override
    public void act(float delta) {
        updateClouds();
        updateStars();
        super.act(delta);
    }

    private void updateClouds() {
        float bottomY = followingCamera.position.y - Game.HEIGHT / 2f - cloudsTexture.getHeight();
        if(bottomY < HIGH_WHERE_CLOUDS_END) {
            for (Cloud cloud : clouds) {
                if (cloud.getY() < bottomY) {
                    cloud.reset(clouds.peek().getY() + cloudsTexture.getHeight() + DISTANCE_BETWEEN_CLOUDS);
                    clouds.removeValue(cloud, true);
                    clouds.add(cloud);
                } else break;
            }
        }

    }

    private void updateStars() {
        float bottomY = followingCamera.position.y - Game.HEIGHT / 2f - starsTexture.getHeight();
        float topY = followingCamera.position.y + Game.HEIGHT / 2f;

        for (Star star : stars) {
            if (star.getY() < bottomY) {
                star.reset(topY);
            }
        }
    }


}