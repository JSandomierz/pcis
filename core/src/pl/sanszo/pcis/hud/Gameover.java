package pl.sanszo.pcis.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import pl.sanszo.pcis.FadeInOutSprite;
import pl.sanszo.pcis.Game;


public class Gameover extends FadeInOutSprite {
    private BitmapFont font = Game.content.getFont();
    private GlyphLayout layout = new GlyphLayout();
    private boolean newRecord = false;
    private float newRecordWidth;
    private String highScoreString = "";
    private float highScoreStringWidth;
    public Gameover(float fadeInDuration, float fadeOutDuration, float y) {
        super(Game.content.getTexture("taptotryagain"), fadeInDuration, fadeOutDuration, y);
        layout.setText(font, "NEW RECORD!");
        newRecordWidth = layout.width;
    }

    public void updateTextures(){
        super.updateTexture(Game.content.getTexture("taptotryagain"));
        font = Game.content.getFont();
    }

    public void update(boolean newRecord) {
        this.newRecord = newRecord;
        highScoreString = "Highscore: " + String.valueOf(Game.prefs.getInteger("highscore"));
        layout.setText(font, highScoreString);
        highScoreStringWidth = layout.width;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        super.draw(batch, alpha);

        font.setColor(1f, 1f, 1f, getColor().a);
        font.draw(batch, highScoreString, Game.WIDTH/2f - highScoreStringWidth/2f, getY() + sprite.getHeight() + 150f);
        if(newRecord) {
            font.setColor(1f, 0f, 0f, getColor().a);
            font.draw(batch, "NEW RECORD!", Game.WIDTH / 2f - newRecordWidth / 2f, getY() + sprite.getHeight() + 300f);
        }
        font.setColor(1f, 1f, 1f, 1f);
    }
}
