package pl.sanszo.pcis.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

import pl.sanszo.pcis.Game;
import pl.sanszo.pcis.Polandball;


public class ScoreText extends HudActor {
    private BitmapFont font = Game.content.getFont();
    private Polandball polandball;
    public ScoreText(Polandball polandball) {
        this.polandball = polandball;
    }

    public void updateTextures(){
        font = Game.content.getFont();
    }

    @Override
    public void draw(Batch batch, float alpha){
        font.draw(batch, String.format("%06d", polandball.getScore()), 10f, Game.HEIGHT-10f);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }
}
