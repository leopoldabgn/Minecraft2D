package launcher;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import gui.Window;
import model.Textures;

public class App {

    public static void main( String[] args ) {

        // On charge les textures (au minimum celles par default)
        new Textures();

        // On cree la fenetre
        SwingUtilities.invokeLater(() -> {
            new Window(900, 650);
        });
    }

    public static int[] getStringLength(Graphics2D g, Font font, String str) {
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D bounds = fm.getStringBounds(str, g);
        int width = (int)bounds.getWidth();
        int height = (int)bounds.getHeight();

        return new int[] {width, height};
    }

	public static Image getImage(final String pathAndFileName) throws IOException {
		final URL url = Thread.currentThread().getContextClassLoader().getResource(pathAndFileName);
        if(url == null)
            throw new IOException("path doesn't exist !");
		return ImageIO.read(url);
	}

    public void dispMemoryInfos() {
        Runtime runtime = Runtime.getRuntime();

        // Mémoire totale en octets
        long totalMemory = runtime.totalMemory();

        // Mémoire libre en octets
        long freeMemory = runtime.freeMemory();

        // Mémoire occupée en octets
        long usedMemory = totalMemory - freeMemory;

        // Conversion en méga-octets (Mo)
        long totalMemoryInMegabytes = totalMemory / (1024 * 1024);
        long usedMemoryInMegabytes = usedMemory / (1024 * 1024);

        System.out.println("Mémoire totale : " + totalMemoryInMegabytes + " Mo");
        System.out.println("Mémoire utilisée : " + usedMemoryInMegabytes + " Mo");
    }    

}
