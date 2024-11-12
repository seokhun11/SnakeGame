package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class App extends JFrame  {
    private JFrame mainFrame;

    public static void main(String[] args) {
        new App();
    }

    public App() {
        mainFrame = new JFrame("Game Choice");
        mainFrame.setSize(600, 600);


        JButton snakeGameButton = new JButton("Snake Game");
        JButton memoryGameButton = new JButton("Memory Game");


        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(snakeGameButton);
        buttonPanel.add(memoryGameButton);

        mainFrame.add(buttonPanel);
        snakeGameButton.addActionListener(new ActionListener() {   // 버튼 누를 시 snakeGame 실행
            @Override
            public void actionPerformed(ActionEvent e) {
                launchSnakeGame();
            }
        });

        memoryGameButton.addActionListener(new ActionListener() {   // 버튼 누를 시 memoryGame 실행
            @Override
            public void actionPerformed(ActionEvent e) {
                launchMemoryGame();
            }
        });

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainFrame.requestFocus();
        mainFrame.setVisible(true);

    }

    private void launchSnakeGame() {               // snakeGame 실행 메서드
        JFrame snakeFrame = new JFrame("Snake Game");
        Game game = new Game();
        Panel panel = new Panel();
        SideBar title = new SideBar(game);

        snakeFrame.setSize(600, 600);
        snakeFrame.setLayout(new BorderLayout());
        snakeFrame.add(panel, BorderLayout.NORTH);
        snakeFrame.add(game, BorderLayout.CENTER);
        snakeFrame.add(title, BorderLayout.EAST);
        snakeFrame.setLocationRelativeTo(null);
        snakeFrame.setResizable(false);
        snakeFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        snakeFrame.pack();
        game.requestFocus();
        snakeFrame.setVisible(true);

    }
    private void launchMemoryGame() {                  // memoryGame 실행 메서드
        JFrame numberFrame = new JFrame("Memory Game");

        TextPanel textPanel = new TextPanel();
        MemoryGame memoryGame = new MemoryGame(textPanel);

        numberFrame.setSize(600, 600);
        numberFrame.setLayout(new BorderLayout());
        numberFrame.add(memoryGame, BorderLayout.CENTER);
        numberFrame.add(textPanel, BorderLayout.NORTH);
        numberFrame.setLocationRelativeTo(null);
        numberFrame.setResizable(false);
        numberFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        numberFrame.pack();
        memoryGame.requestFocus();
        numberFrame.setVisible(true);
    }
}