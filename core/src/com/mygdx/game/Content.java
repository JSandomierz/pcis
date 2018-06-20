package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;



public class Content {
    private HashMap<String, String> fileNames = new HashMap<>();
    private HashMap<String, SoundExt> sounds = new HashMap<>();
    private HashMap<String, TextureRegion> textureRegions = new HashMap<>();
    private String fontFileName;
    private String musicFileName;
    private AssetManager assetManager = new AssetManager();
    private Texture texture;

    public class SoundExt {
        public String path;
        public int duration;
        public long playTime;
        public long id;
        public SoundExt(String path, int duration) {
            this.path = path;
            this.duration = duration;
        }
        public Sound getSound() {
            return assetManager.get(path);
        }
    }

    public void renewAssetManager(){
        assetManager = new AssetManager();
        Texture.setAssetManager(assetManager);
    }

    public Content() {
        Texture.setAssetManager(assetManager);
    }

    public void loadTexture(String name, String path) {
        fileNames.put(name, path);
        assetManager.load(path, Texture.class);
    }

    public void loadSound(String name, int duration, String path) {
        sounds.put(name, new SoundExt(path, duration));
        assetManager.load(path, Sound.class);
    }

    public void loadSound(String name, String path) {
        loadSound(name, 0, path);
    }

    public SoundExt getSound(String name) {
        return sounds.get(name);
    }

    public void loadFont(String path) {
        fontFileName = path;
        assetManager.load(path, BitmapFont.class);
    }

    public BitmapFont getFont() {
        if(fontFileName==null) return null;
        return assetManager.get(fontFileName);
    }

    public void loadMusic(String path) {
        musicFileName = path;
        assetManager.load(path, Music.class);
    }

    public Music getMusic() {
        return Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
    }

    public Texture getTexture(String name) {
        return assetManager.get(fileNames.get(name));
    }

    public void waitForLoad() {
        while(!assetManager.update()) {
            //Gdx.app.log("LOADING ASSETS", String.valueOf(assetManager.getProgress()));
        };
    }

    public void dispose() {
        assetManager.dispose();
    }

}
