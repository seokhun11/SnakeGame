package game;

import javax.swing.*;
import java.awt.*;

public class TextPanel extends JPanel {
    private JLabel stageLabel;
    private JLabel textLabel;
    private int stage;

    public TextPanel() {
        setPreferredSize(new Dimension(600, 100));
        setLayout(new GridLayout(2, 1));
        setBackground(Color.gray);

        stageLabel = new JLabel("Stage : 1",SwingConstants.CENTER);
        textLabel = new JLabel("",SwingConstants.CENTER);

        textLabel.setFont(new Font("Arial", Font.BOLD, 35));
        textLabel.setForeground(Color.BLUE);
        textLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        stageLabel.setFont(new Font("Arial", Font.BOLD, 35));
        stageLabel.setForeground(Color.BLUE);
        stageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(stageLabel);
        add(textLabel);
    }

    public void setText(String str) {
        textLabel.setText(str);
    }
    public void setstage(int stage){
        this.stage = stage;
        String scr = String.valueOf(stage);
        stageLabel.setText("Stage : " + scr);
    }
}

