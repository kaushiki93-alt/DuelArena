
package duelarena;

import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

public class Arena {

    private final String name;
    private final String imagePath;
    private Image backgroundImage;

    public Arena(String name, String imageFile) {

        this.name = name;
        this.imagePath = "/arenas/" + imageFile;

        URL imageURL = getClass().getResource(imagePath);

        if (imageURL != null) {

            backgroundImage = new ImageIcon(imageURL).getImage();

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

    public void draw(
            Graphics g,
            int x,
            int y,
            int width,
            int height) {

        if (backgroundImage != null) {

            g.drawImage(
                    backgroundImage,
                    x,
                    y,
                    width,
                    height,
                    null
            );

        } else {

            g.setColor(java.awt.Color.DARK_GRAY);
            g.fillRect(x, y, width, height);
        }
    }
}
