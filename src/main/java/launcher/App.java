package launcher;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import gui.Window;

public class App {

    public static void main( String[] args ) {
        SwingUtilities.invokeLater(() -> {
            new Window(600, 600);
        });
    }

	public static Image getImage(final String pathAndFileName) throws IOException {
		final URL url = Thread.currentThread().getContextClassLoader().getResource(pathAndFileName);
		return ImageIO.read(url);
	}

}
