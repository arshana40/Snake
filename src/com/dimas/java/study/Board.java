package com.dimas.java.study;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.lang.Math; //(Arya update 1): Biar gampang hitung-hitungannya

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 400;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private  int RAND_POS = 30;
    private  int DELAY = 100;
    private double rumus = 1; //(Arya update 1): Buat balancing speed
    private int scorenya;
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];
    private final int i[] = new int[ALL_DOTS];
    private final int j[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;
    private int apple_i;
    private int apple_j;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image apple_merah;
    private Image head;
    private Object TAdapter;

    public Board() {

        initBoard();
    }

    private void initBoard() {
        //Mencetak background game beserta assetnya
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }


    private void loadImages() {
        //asset gambar untuk game snake
        ImageIcon iid = new ImageIcon("src/com/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/com/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/com/head.png");
        head = iih.getImage();

        ImageIcon iir = new ImageIcon("src/com/apple_merah.png");
        apple_merah = iir.getImage();
    }

    private void initGame() {

        dots = 3; //awal game ada tiga titik tubuh

        for (int z = 0; z < dots; z++) {//posisi awal snake pada saat game dimulai
            x[z] = 50 - z * 10;
            y[z] = 10;
        }

        locateApple();//menempatkan apelnya berada dimana

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {//fungsi untuk menggambar snake
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        if (inGame) {
          if (dots%2 == 0){//akan muncul jika titik yang ada berjumlah kelipatan 2
              g.drawImage(apple_merah, apple_i,apple_j,this);//maka akan muncul apel merah
          }

            g.drawImage(apple, apple_x, apple_y, this);//menampilkan apel hijau

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);//menggambar kepala
                } else {
                    g.drawImage(ball, x[z], y[z], this);//menggambar tubuh
                }
            }

            Toolkit.getDefaultToolkit().sync();

            Toolkit.getDefaultToolkit().sync();
            String msg ;
            if(rumus== 8) {
            	msg = "Score = " + scorenya + " || MAX SPEED" ; //Biar keren
            }
            else {
            	msg = "Score = " + scorenya + " || Level = " + rumus;
            }

            Font small = new Font("Helvetica", Font.BOLD, 10);
            FontMetrics metr = getFontMetrics(small);

            g.setColor(Color.white);
            g.setFont(small);
            g.drawString(msg, 5, B_HEIGHT - (B_HEIGHT - 10));
        } else {
            gameOver(g);
            new TAdapter();
        }
    }

    private void gameOver(Graphics g) {
        inGame = false;
        String msg = "Game Over \n" + "Tekan Spasi untuk Lanjut";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkAppleMerah(){

        if ((x[0] == apple_i) && (y[0] == apple_j)) {
            dots++;
            scorenya = scorenya + 10;
            System.out.println(scorenya);
            //(Arya update 1): Rumusmu aku ubah, terus while aku ganti jadi if
            if(rumus != 8) {
            	
            	DELAY = (int) (0.8+DELAY-50/rumus/1.6);
            	timer.setDelay(DELAY);
            	rumus++;
            }
            System.out.println("SPEED= " + DELAY);
            locateApple();
        }
    }
    private void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            scorenya = scorenya + 5;
            locateApple();
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {//jika keluar batas maka akan game over dan memakan badanya

        for (int z = dots; z > 0; z--) {//memakan bagian badan
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {//menentukan posisi tempat apel secara random
        if (dots % 2 == 0) {
            int k = (int) (Math.random() * RAND_POS);
            apple_i = ((k * DOT_SIZE));

            k = (int) (Math.random() * RAND_POS);
            apple_j = ((k * DOT_SIZE));
        }

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {
            checkApple();//apa yang terjadi jika memakan apel hijau
            checkAppleMerah();//apa yang terjadi jika memakan apel merah
            checkCollision();//batas
            move();
        }
        repaint();
    }

    public class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) { //Saat menekan tombol arah
            if (inGame==false){
                if (e.getKeyCode() == KeyEvent.VK_SPACE){
                    inGame=true;
                    rightDirection = true;
                    leftDirection = false;
                    upDirection = false;
                    downDirection = false;
                    //(Arya update 1): aku tambahi supaya ngulangnya enak, dari awal lagi
                    scorenya = 0 ;
                    rumus = 1;
                    DELAY = 100;
                    
                    initBoard();
                }
            }


            if ((e.getKeyCode() == KeyEvent.VK_LEFT) && (!rightDirection)) {//Arah kiri
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((e.getKeyCode() == KeyEvent.VK_RIGHT) && (!leftDirection)) {//Arah kanan
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((e.getKeyCode() == KeyEvent.VK_UP) && (!downDirection)) {//Arah atas
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((e.getKeyCode() == KeyEvent.VK_DOWN) && (!upDirection)) {//Arah bawah
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}
