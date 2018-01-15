package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Created by szostakm on 15.01.18.
 */

public class Gameover extends FadeInOutSprite {
    private BitmapFont font = Game.content.getFont();
    private GlyphLayout layout = new GlyphLayout();
    private boolean newRecord = false;
    public Gameover(float fadeInDuration, float fadeOutDuration, float y) {
        super(Game.content.getTexture("taptotryagain"), fadeInDuration, fadeOutDuration, y);
    }

    public void show(boolean newRecord) {
        this.newRecord = newRecord;
        super.show();
    }

    @Override
    public void draw(Batch batch, float alpha) {
        super.draw(batch, alpha);

        font.setColor(1f, 1f, 1f, getColor().a);
        String str = "Highscore: " + String.valueOf(Game.prefs.getInteger("highscore"));
        layout.setText(font, str);
        font.draw(batch, str, Game.WIDTH/2f - layout.width/2f, getY() + sprite.getHeight() + 150f);
        layout.setText(font, "NEW RECORD!");
        if(newRecord) {
            font.setColor(1f, 0f, 0f, getColor().a);
            font.draw(batch, "NEW RECORD!", Game.WIDTH / 2f - layout.width / 2f, getY() + sprite.getHeight() + 300f);
        }
        font.setColor(1f, 1f, 1f, 1f);
    }
}
