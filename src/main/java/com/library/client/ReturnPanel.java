package com.library.client;

import com.library.util.HttpClientUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ReturnPanel extends JPanel {

    private MainFrame parent;
    private JTextField resourceIdField;
    private JTextArea infoArea;
    private JButton returnBtn;
    private JButton clearBtn;

    public ReturnPanel(MainFrame parent) {
        this.parent = parent;
        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        resourceIdField = new JTextField(20);
        infoArea = new JTextArea(8, 30);
        infoArea.setEditable(false);
        returnBtn = new JButton("确认归还");
        clearBtn = new JButton("清空");

        returnBtn.addActionListener(this::doReturn);
        clearBtn.addActionListener(e -> clearForm());
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        inputPanel.add(new JLabel("资源编号:"));
        inputPanel.add(resourceIdField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(returnBtn);
        buttonPanel.add(clearBtn);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(infoArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void doReturn(ActionEvent e) {
        String resourceId = resourceIdField.getText().trim();
        if (resourceId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写资源编号");
            return;
        }
        try {
            HttpClientUtil.returnResource(resourceId);
            infoArea.setText("归还成功！\n资源编号: " + resourceId);
            parent.setStatus("归还操作完成");
        } catch (Exception ex) {
            infoArea.setText("归还失败: " + ex.getMessage());
            parent.setStatus("归还失败");
        }
    }

    private void clearForm() {
        resourceIdField.setText("");
        infoArea.setText("");
    }
}
