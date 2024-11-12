package game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class Game extends JPanel implements ActionListener, KeyListener {
    private static class Tile {
        int x;
        int y;
        Tile(int x, int y) {   // 타일 위치 지정
            this.x = x;
            this.y = y;
        }

        Tile(){
        }
    }
    private int boardWidth = 500;
    private int boardHeight = 500;
    private final int tileSize = 25;   // 타일 크기
    int opp = 3;

    Tile snakeHead;  //Snake
    ArrayList<Tile> snakeBody;
    ArrayList<Tile> poisonTiles; // 여러 개의 포이즌을 저장할 리스트
    Tile poison; // Toxic
    Tile food;   // Point
    Tile hurdle; // Trap
    Random random; // Food와 Posion 랜덤 생성 위해 Random 객체 선언
    Timer gameLoop; // 지정된 시간마다 갱신을 위해 timer 객체 선언
    int velocityX; // Snake  X 방향
    int velocityY; // Snake Y 방향
    boolean gameOver = false;
    int stage = 1; // Stage 단계
    private Image head; // Snake Head 이미지
    private final Image up, down, right, left, toxic, coin, body, backgroundImage, trap;

    SoundPlayer backgroundMusic = new SoundPlayer("resources/background_music.wav"); // 배경 음악 초기화

    Game() {
        setPreferredSize(new Dimension(500,500));
        addKeyListener(this);
        snakeHead = new Tile(5, 5); // 시작 지점 설정
        snakeBody = new ArrayList<>();
        poisonTiles = new ArrayList<>();
        food = new Tile(); // Food 선언
        poison = new Tile(); // Poison 선언
        hurdle = new Tile();
        random = new Random();
        placeFood(); // Food 생성
        placePoison(); // Poison 생성

        gameLoop = new Timer(100, this); // ActionListener
        gameLoop.start(); // ActionListener start
        ImageIcon imgIcon = new ImageIcon("resources/aaa.png");
        backgroundImage = imgIcon.getImage();
        ImageIcon snakeIcon = new ImageIcon("resources/snakeimage.png");
        body = snakeIcon.getImage();
        ImageIcon rightmouth = new ImageIcon("resources/rightmouth.png");
        right = rightmouth.getImage();
        ImageIcon leftmouth = new ImageIcon("resources/leftmouth.png");
        left = leftmouth.getImage();
        ImageIcon upmouth = new ImageIcon("resources/upmouth.png");
        up = upmouth.getImage();
        ImageIcon downmouth = new ImageIcon("resources/downmouth.png");
        down = downmouth.getImage();
        ImageIcon poisonImage = new ImageIcon("resources/mole.png");
        toxic = poisonImage.getImage();
        ImageIcon coinImage = new ImageIcon("resources/weed.png");
        coin = coinImage.getImage();
        ImageIcon trapImage = new ImageIcon("resources/trap.png");
        trap = trapImage.getImage();

        head = right; // 초기 Head 방향

        // 배경 음악
        backgroundMusic.loop(); // 배경 음악 무한 반복 재생 시작
    }

    public void paintComponent(Graphics g) {  //ActionListener Fucntion
        super.paintComponent(g);
        draw(g);
        if (gameOver) {
          if(opp > 0) {
              opp -= 1;
              restartGame();
          }
        else
            {
                gameLoop.stop();
                ShowGameOverScreen(g);
            }
        }
        if(snakeBody.size() * 10 == 50 && stage < 3){
            stage++;
            boardWidth -= 100;
            boardHeight -= 100;
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.pack();
            frame.setLocationRelativeTo(null);
            restartGame(); // Game Reset
        }
        else if(stage == 3 && snakeBody.size() * 10 == 50){
            ShowGameClearScreen(g);
            gameLoop.stop();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, this);
        //SnakeHead 그리기
        g.drawImage(head, snakeHead.x * tileSize, snakeHead.y * tileSize, 25, 25, this);
        //SnakeBody 그리기
        for (Tile snakePart : snakeBody) {
            g.drawImage(body, snakePart.x * tileSize, snakePart.y * tileSize, 25, 25, this);
        }
        //Food 그리기
        g.drawImage(coin, food.x * tileSize, food.y * tileSize, 25, 25, this);
        //Poison 그리기
        for (Tile poison : poisonTiles) {
            g.drawImage(toxic, poison.x * tileSize, poison.y * tileSize, 25, 25, this);
        }
        if(stage >= 2){
            //Hurdle 그리기
            g.drawImage(trap, hurdle.x * tileSize, hurdle.y * tileSize, 25, 25, this);
        }
    }

    public void placeFood() { // Food 랜덤 배치
        do {
            food.x = random.nextInt(boardWidth / tileSize);
            food.y = random.nextInt(boardHeight / tileSize);
        } while(collision(food,poisonTiles) || collisionWithSnake(food) || collision(food,hurdle)); // Food가 Snake나 Poison,Hurdle이 있던 위치에 생성될 경우 다시 배치
    }

    public void placeHurdle() { // Food 랜덤 배치
        do {
            hurdle.x = random.nextInt(boardWidth / tileSize);
            hurdle.y = random.nextInt(boardHeight / tileSize);
        } while(collision(hurdle,poisonTiles) || collisionWithSnake(hurdle) || collision(hurdle,food)); // Hurdle이 Snake나 Poison,Food가 있던 위치에 생성될 경우 다시 배치
    }

    public void placePoison() { // Poison 랜덤 배치
        for (int i = 0; i < 3; i++) { // 3개의 Poison 배치
            Tile poison;
            do {
                poison = new Tile(random.nextInt(boardWidth / tileSize), random.nextInt(boardHeight / tileSize));
            } while (collision(poison, food) || collisionWithSnake(poison) || collision(poison, poisonTiles) || collision(poison,hurdle));
            poisonTiles.add(poison);
        }
    }

    public void placeSinglePoison(int i, Tile poison) { // 단일 Poison 랜덤 배치
        do {
            Tile temp = new Tile();
            temp.x = random.nextInt(boardWidth / tileSize);
            temp.y = random.nextInt(boardHeight / tileSize);
            if (!collision(temp, poisonTiles)) { // 기존 PoisonTiles에 존재하는 Poison 위치와 겹치는 지 확인
                poison = temp;
            }
        } while (collision(poison, food) || collisionWithSnake(poison)); // Food와 기존 Snake 위치에 겹치는 지 확인

        poisonTiles.set(i, poison); // 리스트 내에서 위치 업데이트
    }

    public boolean collision(Tile tile1, Tile tile2) { // 겹치는 지 확인
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public boolean collision(Tile tile, ArrayList<Tile> tiles) { // ArrayList 와 겹치는 지 확인
        for (Tile t : tiles) {
            if (collision(tile, t)) {
                return true;
            }
        }
        return false;
    }

    public boolean collisionWithSnake(Tile tile){ // Snake에 겹치는 지 확인
        if(collision(snakeHead,tile)){
            return true;
        }
        for(Tile snakePart : snakeBody){
            if(collision(snakePart,tile)){
                return true;
            }
        }
        return false;
    }

    public void move() {   // Snake Move
        // Eat Food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y)); // ArrayList increase
            placeFood();
        }

        if (collision(snakeHead, hurdle)) { // Hurdle과 겹치는 지 확인
            gameOver = true;
        }

        for (int i = 0; i < poisonTiles.size(); i++) {  // Eat Poison
            Tile poison = poisonTiles.get(i);
            if (collision(snakeHead, poison)) {
                if (snakeBody.isEmpty()) {
                    opp -= 1;
                    placeSinglePoison(i, poison); // 충돌한 Poison만 다시 배치
                    if (opp < 0) {
                        gameOver = true;
                    }
                } else {
                    snakeBody.remove(snakeBody.size() - 1); // ArrayList 감소
                    placeSinglePoison(i, poison); // 충돌한 Poison만 다시 배치
                    break; // 충돌한 Poison만 다시 배치 후 반복문 탈출
                }
            }
        }

        //Snake Body 움직임
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile preSnakePart = snakeBody.get(i - 1);
                snakePart.x = preSnakePart.x;
                snakePart.y = preSnakePart.y;
            }
        }
        //SnakeHead 이동
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //GameOver condition
        for (Tile snakePart : snakeBody) {
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        if (snakeHead.x * tileSize + 5 < 0 || (snakeHead.x+1)* tileSize > boardWidth + 5
                || snakeHead.y * tileSize + 5 < 0 || (snakeHead.y+1) * tileSize> boardHeight + 5) {
            gameOver = true;
        }
    }

    private void ShowGameOverScreen(Graphics g) { // 종료 메시지
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.BOLD, 50));
        g.drawString("Game Over", 150,200);
        g.setFont(new Font("arial", Font.BOLD, 20));
        g.drawString("Press SPACE to restart , ESC to exit", 100,250);
    }

    private void ShowGameClearScreen(Graphics g) { // Clear 메시지
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.BOLD, 50));
        g.drawString("Congratulation !!", 150,200);
        g.setFont(new Font("arial", Font.BOLD, 20));
        g.drawString("Press ESC to exit", 100,250);
    }

    public int getScore(){
        return snakeBody.size() * 10;
    } // 점수 리턴

    // 게임을 재시작하는 메서드
    private void restartGame() {
        // 게임 상태 초기화
        snakeHead = new Tile(5,5);
        head = right;
        snakeBody.clear();
        poisonTiles.clear();
        gameOver = false;
        velocityX = 0;
        velocityY = 0;
        placePoison();
        placeFood();
        placeHurdle();
        // 타이머 재시작
        gameLoop.restart();
    }

    @Override
    public void actionPerformed(ActionEvent e) { // 0.1초마다 repaint
        move();
        repaint();
        }

    @Override
    public void keyTyped(KeyEvent e) {     //Not need
    }

    @Override
    public void keyReleased(KeyEvent e) {    // Not need
    }

    @Override
    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            backgroundMusic.stop();
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){ // Exit 누를 시 게임 종료
            System.exit(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) { // Space 누를 시 게임 재실행
            stage = 1;
            boardHeight = 500;
            boardWidth = 500;
            opp = 3;
            restartGame();
        }
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            head = up;
            velocityX = 0;
            velocityY = -1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            head = down;
            velocityX = 0;
            velocityY = 1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            head = left;
            velocityX = -1;
            velocityY = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            head = right;
            velocityX = 1;
            velocityY = 0;
        }
     }
}
