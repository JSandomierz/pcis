package pl.sanszo.pcis;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import pl.sanszo.pcis.hud.Hud;

public class Game extends ApplicationAdapter{
	GameStage gameStage;
	Hud hud;
	public static Content content;
	public final static int WIDTH = 1080;
	public final static int HEIGHT = 1920;
	public final static float PPM = 100f;
	SpriteBatch batch;
	FollowingCamera camera;
	OrthographicCamera hudCamera;
	OrthographicCamera b2dCam;
	Viewport viewport;
	Box2DDebugRenderer debugRenderer;
	static public Preferences prefs;
	private InputMultiplexer inputMultiplexer;



	private void loadContent() {
		content = new Content();
		content.loadTexture("logo", "logo.png");
		content.loadTexture("taptostart", "taptostart.png");
		content.loadTexture("taptotryagain", "taptotryagain.png");
		content.loadTexture("palace", "palace.png");
		content.loadTexture("clouds", "clouds.png");
		content.loadTexture("stars", "stars.png");
		content.loadTexture("mute", "mute.png");
		content.loadTexture("fan", "fan.png");
		content.loadTexture("cannonback", "cannonback.png");
		content.loadTexture("cannontop", "cannontop.png");
		content.loadTexture("cannonbase", "cannonbase.png");
		content.loadTexture("polandball", "polandball.png");
		content.loadTexture("enemy_british", "britishball.png");
		content.loadTexture("enemy_russia", "russiaball.png");
		content.loadTexture("boostup", "boostup.png");
		content.loadTexture("boostdown", "boostdown.png");
		content.loadTexture("boosthorizontal", "boosthorizontal.png");
		content.loadTexture("paddle_beg2", "paddle_beg2.png");
		content.loadTexture("paddle_mid", "paddle_mid.png");
		content.loadSound("jump", 240, "boink.wav");
		content.loadSound("bounce", 185, "bounce.wav");
		content.loadSound("gameover",  "gameover.wav");
		content.loadSound("powerup",  "powerup.wav");
		content.loadSound("click", 65, "click.wav");
		content.loadSound("blow", 250, "blow.wav");
		content.loadSound("shot", "shot.wav");
		content.loadSound("reload", "cannonreload.wav");
		content.loadFont("font.fnt");
		content.loadBackgroundMusic("music.mp3");
		content.waitForLoad();
	}

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.app.debug("APP", "Start");
		prefs = Gdx.app.getPreferences("Poland can into Space");
		loadContent();
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		batch = new SpriteBatch();
		camera = new FollowingCamera();
		viewport = new StretchViewport(WIDTH, HEIGHT, camera);//new ExtendViewport(WIDTH, HEIGHT, camera);
		hudCamera = new OrthographicCamera(WIDTH, HEIGHT);

		debugRenderer = new Box2DDebugRenderer();

		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, WIDTH/100.f, HEIGHT/100.f);
		hud = new Hud(new StretchViewport(WIDTH, HEIGHT, hudCamera), batch);
		gameStage = new GameStage(viewport, batch, camera, b2dCam, hud);

		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(hud);
		inputMultiplexer.addProcessor(gameStage);

		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render () {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gameStage.act(Gdx.graphics.getDeltaTime());
		hud.act(Gdx.graphics.getDeltaTime());
		//b2dCam.position.y = camera.position.y/PPM;
		camera.update();
		//b2dCam.update();
		batch.setProjectionMatrix(camera.combined);
		gameStage.draw();
		batch.setProjectionMatrix(hudCamera.combined);
		hud.draw();
		//debugRenderer.render(gameStage.world, b2dCam.combined);
	}

	@Override
	public void dispose () {
		batch.dispose();
		content.dispose();
	}

	@Override
	public void resize (int width, int height) {
		// viewport must be updated for it to work properly
		viewport.update(width, height);
	}

	@Override
	public void resume () {
		Gdx.app.debug("CONTENT", "resume");
		content.waitForLoad();
	}

}
