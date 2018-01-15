package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.HashMap;

/**
 * Created by szostakm on 10.01.18.
 */

public class Content {
    private HashMap<String, String> textureHashMap = new HashMap<>();
    private String fontFileName;
    private AssetManager assetManager = new AssetManager();

    public void loadTexture(String name, String path) {
        textureHashMap.put(name, path);
        assetManager.load(path, Texture.class);
    }

    public void loadFont(String path) {
        fontFileName = path;
        assetManager.load(path, BitmapFont.class);
    }

    public BitmapFont getFont() {
        if(fontFileName==null) return null;
        return assetManager.get(fontFileName);
    }

    public Texture getTexture(String name) {
        return assetManager.get(textureHashMap.get(name));
    }

    public void waitForLoad() {
        while(!assetManager.update());
    }

}
