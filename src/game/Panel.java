package game;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {

    private Image backgroundImage;

    public Panel() {
        setPreferredSize(new Dimension(600, 100));
        loadImage();
    }

    private void loadImage() {
        ImageIcon imgIcon = new ImageIcon("resources/snaketitle.jpg");
        backgroundImage = imgIcon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
            g.drawImage(backgroundImage, 22, 10, 555, 80, this);

    }
}