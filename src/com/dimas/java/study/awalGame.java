package com.dimas.java.study;

import javax.swing.*;

public class awalGame extends javax.swing.JFrame {
    public awalGame(){
        initComponent();
        setLocationRelativeTo(this);
    }

    private void initComponent() {

    }

    private JPanel panel1;
    private JButton playButton;

    private void playButton(java.awt.event.ActionEvent evt){
        this.dispose();
        JFrame ex = new Snake();
        ex.setVisible(true);
    }
}
