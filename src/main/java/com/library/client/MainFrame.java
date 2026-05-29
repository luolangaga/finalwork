package com.library.client;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;
    private BorrowPanel borrowPanel;
    private ReturnPanel returnPanel;
    private QueryPanel queryPanel;
    private JLabel statusLabel;

    public MainFrame() {
        setTitle("图书馆管理系统");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        borrowPanel = new BorrowPanel(this);
        returnPanel = new ReturnPanel(this);
        queryPanel = new QueryPanel(this);
        statusLabel = new JLabel("就绪");

        tabbedPane.addTab("借阅", borrowPanel);
        tabbedPane.addTab("归还", returnPanel);
        tabbedPane.addTab("查询", queryPanel);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame().setVisible(true);
        });
    }
}
