package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;



public class SoundManager {

    final private static float BACKGROUND_MUSIC_VOLUME = 0.1f;

    public static void playSingle(String soundName, float volume) {
        Content.SoundExt soundExt = Game.content.getSound(soundName);
        soundExt.getSound().setVolume(soundExt.id, volume);
        long now = System.currentTimeMillis();
        if(soundExt.duration == 0 || now - soundExt.playTime > soundExt.duration) {
            soundExt.id = soundExt.getSound().play();
            soundExt.playTime = now;
        }
    }

    public static void playSingle(String soundName) {
        playSingle(soundName, 1f);
    }
        public static void playBackgroundMusic() {
        Music music = Game.content.getMusic();
        music.setLooping(true);
        music.setVolume(BACKGROUND_MUSIC_VOLUME);
        music.play();
    }
}
