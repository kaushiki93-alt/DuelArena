

package duelarena;

import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

public class Arena {

    private final String name;
    private final String imagePath;
    private Image backgroundImage;

    public Arena(String name, String imageFile) {
        this.name = name;
        this.imagePath = "resources/arenas/" + imageFile;

        File file = new File(imagePath);

        if (file.exists()) {
            backgroundImage = new ImageIcon(
                    file.getAbsolutePath()
            ).getImage();
        } else {
            System.out.println(
                    "Arena image not found: " + imagePath
            );
        }
    }

    public String getName() {
        return name;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public void draw(Graphics g, int x, int y,
                     int width, int height) {

        if (backgroundImage != null) {
            g.drawImage(
                    backgroundImage,
                    x, y, width, height,
                    null
            );
        } else {
            g.fillRect(x, y, width, height);
        }
    }
}
