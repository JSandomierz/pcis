package pl.sanszo.pcis.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import pl.sanszo.pcis.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=360;
		config.height=640;
		config.addIcon("icon128.png", Files.FileType.Internal);
		config.addIcon("icon32.png", Files.FileType.Internal);
		config.addIcon("icon16.png", Files.FileType.Internal);
		config.title = "Poland can into Space";
		new LwjglApplication(new Game(), config);
	}
}
