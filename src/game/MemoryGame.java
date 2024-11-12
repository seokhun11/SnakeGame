package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class MemoryGame extends JPanel implements ActionListener, KeyListener {
    private final int gridSize = 4;
    private final int maxStage = 3;
    private final JPanel gridPanel;
    private boolean showNumbers = true;
    private final ArrayList<JLabel> labels;
    private int currentClick = 1;
    private int stage = 1;
    private boolean gameOver = false;
    private final Random random = new Random();
    private final int[] randomPositions = new int[5];
    private final Timer timer;

    TextPanel textPanel;

    SoundPlayer backgroundMusic = new SoundPlayer("resources/background_music.wav"); // 배경 음악 초기화


    public MemoryGame(TextPanel textPanel) {
        setPreferredSize(new Dimension(600, 500));
        setLayout(new BorderLayout());

        labels = new ArrayList<>();

        timer = new Timer(5000, this);

        this.textPanel = textPanel;

        gridPanel = new JPanel(new GridLayout(gridSize, gridSize));
        setting();
        add(gridPanel, BorderLayout.CENTER);
        addKeyListener(this);

        backgroundMusic.loop(); // 배경 음악 무한 반복 재생 시작

    }

    private void setting() {   // 초기 화면 세팅
        for (int i = 0; i < gridSize * gridSize; i++) {
            JLabel label = new JLabel();
            label.setOpaque(true);
            label.setBackground(Color.WHITE);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 30));
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (showNumbers || gameOver) return;

                    JLabel label1 = (JLabel) e.getSource();
                    JLabel label2 = labels.get(randomPositions[currentClick - 1]);
                    if (label1 == label2) {
                        label1.setBackground(Color.WHITE);
                        label1.setText(String.valueOf(currentClick));
                        currentClick++;
                        if (currentClick > stage + 2) {
                            if (stage == maxStage) {
                                stage++;
                                textPanel.setText("Success !");
                                labelsReset();
                            } else {
                                stage++;
                                textPanel.setstage(stage);
                                upStage();
                            }
                        }
                    } else {
                        gameOver = true;
                        textPanel.setText("Fail !");
                        labelsReset();
                        for (int i = 0; i < stage + 2; i++) {
                            JLabel label = labels.get(randomPositions[i]);
                            label.setBackground(Color.WHITE);
                        }
                        repaint();
                    }
                }
            });

            labels.add(label);
            gridPanel.add(label);
        }

        setNumber();

        timer.start();

        revalidate();
        repaint();
    }

    public void setNumber() {   // 랜덤 위치 지정
        for (int i = 0; i < stage + 2; i++) {
            int randomPos;
            do {
                randomPos = random.nextInt(gridSize * gridSize);
            } while (!labels.get(randomPos).getText().isEmpty());
            randomPositions[i] = randomPos;
            JLabel label = labels.get(randomPos);
            label.setText(String.valueOf(i + 1));
        }
    }

    public void labelsReset() {   // 레이블 빈칸으로 초기화
        for (int i = 0; i < gridSize * gridSize; i++) {
            JLabel label = labels.get(i);
            label.setText("");
        }
    }

    public void upStage() {  // 스테이지 증가
        currentClick = 1;
        labelsReset();
        setNumber();

        timer.restart();
    }

    private void resetGame() {   // 스테이지 리셋
        gameOver = false;
        currentClick = 1;
        stage = 1;
        labelsReset();
        setNumber();

        timer.restart();
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();
            backgroundMusic.stop();
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            textPanel.setstage(1);
            textPanel.setText("");
            resetGame();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.stop();

        showNumbers = false;

        labelsReset();

        for (int i = 0; i < stage + 2; i++) {
            JLabel label = labels.get(randomPositions[i]);
            label.setBackground(Color.GRAY);
        }
    }
}