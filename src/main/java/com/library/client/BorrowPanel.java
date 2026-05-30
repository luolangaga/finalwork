package com.library.client;

import com.library.util.HttpClientUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BorrowPanel extends JPanel {

    private MainFrame parent;
    private JTextField borrowerIdField;
    private JTextField resourceIdField;
    private JTextArea infoArea;
    private JButton borrowBtn;
    private JButton clearBtn;

    public BorrowPanel(MainFrame parent) {
        this.parent = parent;
        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        borrowerIdField = new JTextField(20);
        resourceIdField = new JTextField(20);
        infoArea = new JTextArea(8, 30);
        infoArea.setEditable(false);
        borrowBtn = new JButton("确认借阅");
        clearBtn = new JButton("清空");

        borrowBtn.addActionListener(this::doBorrow);
        clearBtn.addActionListener(e -> clearForm());
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.add(new JLabel("借阅者编号:"));
        inputPanel.add(borrowerIdField);
        inputPanel.add(new JLabel("资源编号:"));
        inputPanel.add(resourceIdField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(borrowBtn);
        buttonPanel.add(clearBtn);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(infoArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void doBorrow(ActionEvent e) {
        String borrowerId = borrowerIdField.getText().trim();
        String resourceId = resourceIdField.getText().trim();
        if (borrowerId.isEmpty() || resourceId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "请填写借阅者编号和资源编号");
            return;
        }
        try {
            String result = HttpClientUtil.borrowResource(
                    borrowerId, resourceId);
            infoArea.setText("借阅成功！\n" + result);
            parent.setStatus("借阅操作完成");
        } catch (Exception ex) {
            infoArea.setText("借阅失败: " + ex.getMessage());
            parent.setStatus("借阅失败");
        }
    }

    private void clearForm() {
        borrowerIdField.setText("");
        resourceIdField.setText("");
        infoArea.setText("");
    }
}
