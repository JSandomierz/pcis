package pl.sanszo.pcis.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import pl.sanszo.pcis.Game;
import pl.sanszo.pcis.SoundManager;

public class MuteButton extends HudActor {

    private final static int ICON_WIDTH = 101;
    private final static int PADDING = 15;
    private final static float FADING_DURATION = 0.25f;

    @Override
    public void show() {
        addAction(Actions.fadeIn(FADING_DURATION));

    }

    @Override
    public void hide() {
        addAction(Actions.fadeOut(FADING_DURATION));

    }

    private Texture iconTexture;
    private TextureRegion[] icon = new TextureRegion[2];
    private boolean muted = false;
    boolean touched = false;
    public MuteButton() {
        muted = Game.prefs.getBoolean("mute", false);
        playOrPause();
        iconTexture = Game.content.getTexture("mute");
        icon[0] = new TextureRegion(iconTexture, 0, 0, ICON_WIDTH, iconTexture.getHeight());
        icon[1] = new TextureRegion(iconTexture, ICON_WIDTH, 0, ICON_WIDTH, iconTexture.getHeight());
        setWidth(ICON_WIDTH+2*PADDING);
        setHeight(iconTexture.getHeight()+2*PADDING);
        setPosition(Game.WIDTH-ICON_WIDTH-10-PADDING, Game.HEIGHT-16-iconTexture.getHeight()-PADDING);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!touched) touched = true;
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(touched) {
                    toggleMute();
                    touched = false;
                }
            }
        });
    }

    @Override
    public void draw(Batch batch, float alpha){
        batch.setColor(1f,1f,1f, getColor().a);
        batch.draw(icon[muted ?1:0], getX()+PADDING, getY()+PADDING);
        batch.setColor(1f,1f,1f,1f);

    }

    private void toggleMute() {
        muted = !muted;
        Game.prefs.putBoolean("mute", muted);
        Game.prefs.flush();
        playOrPause();
    }

    private void playOrPause() {
        if(muted) SoundManager.pauseBackgroundMusic();
        else SoundManager.playBackgroundMusic();
    }

}
