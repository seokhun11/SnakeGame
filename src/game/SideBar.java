package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SideBar extends JPanel implements ActionListener {
    private final Game game;
    private Image heart;

    Timer gameLoop;

    public SideBar(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(100, 500));
        setBackground(Color.white);
        gameLoop = new Timer(100, this); // ActionListener
        gameLoop.start(); // ActionListener start
        loadImage();
    }

    private void loadImage() {
        ImageIcon imgIcon = new ImageIcon("resources/heart.png"); // 이미지 로드
        heart = imgIcon.getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.drawRect(10,10,80,480);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Score", 10, 100);
        g.drawString(Integer.toString(game.getScore()), 10, 130);
        g.drawString("Stage", 10, 200);
        g.drawString(Integer.toString(game.stage), 10, 230);
        if(game.opp != 0) {
            for (int i = 0; i < game.opp; i++){
                int x = 20;
                g.drawImage(heart,10+(i*x), 260,30,30,this);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        repaint();
    } // 목숨과 스코어, 스테이지 갱신

}