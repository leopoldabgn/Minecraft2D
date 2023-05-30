package launcher;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import gui.Window;

public class App {

    private static String RESOURCES_FOLDER = "src/main/java/resources/";

    public static void main( String[] args ){
        new Window(600, 600);
    }

    public static String getResourcesFolder() {
        return RESOURCES_FOLDER;
    }

	public static Image getImage(final String pathAndFileName) throws IOException {
		final URL url = Thread.currentThread().getContextClassLoader().getResource(pathAndFileName);
		return ImageIO.read(url);
	}

}
