package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Game extends ApplicationAdapter{
	GameStage gameStage;
	SpriteBatch batch;
	OrthographicCamera camera;
	Viewport viewport;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(800, 600, camera);
		gameStage = new GameStage(viewport, batch);
		Gdx.input.setInputProcessor(gameStage);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//gameStage.draw();
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.

		batch.setProjectionMatrix(camera.combined);
		gameStage.act(Gdx.graphics.getDeltaTime());
		gameStage.draw();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
