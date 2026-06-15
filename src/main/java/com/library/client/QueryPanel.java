package com.library.client;

import com.library.util.HttpClientUtil;
import com.library.model.dto.ResourceDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

public class QueryPanel extends JPanel {

    private MainFrame parent;
    private JTextField keywordField;
    private JComboBox<String> typeCombo;
    private JButton searchBtn;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public QueryPanel(MainFrame parent) {
        this.parent = parent;
        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        keywordField = new JTextField(20);
        typeCombo = new JComboBox<>(
                new String[]{"全部", "BOOK", "MAGAZINE", "DVD", "EBOOK"});
        searchBtn = new JButton("搜索");

        String[] cols = {"编号", "标题", "类型", "状态", "借阅者", "详情"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        resultTable = new JTable(tableModel);

        searchBtn.addActionListener(this::doSearch);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("关键词:"));
        searchPanel.add(keywordField);
        searchPanel.add(new JLabel("类型:"));
        searchPanel.add(typeCombo);
        searchPanel.add(searchBtn);

        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);
    }

    private void doSearch(ActionEvent e) {
        String keyword = keywordField.getText().trim();
        String type = (String) typeCombo.getSelectedItem();
        try {
            List<ResourceDTO> results =
                    HttpClientUtil.searchResources(keyword, type);
            tableModel.setRowCount(0);
            for (ResourceDTO r : results) {
                String detail = formatExtraAttrs(r.getExtraAttrs());
                tableModel.addRow(new Object[]{
                        r.getId(), r.getTitle(),
                        r.getType(), r.getStatus(),
                        r.getBorrowerId(), detail});
            }
            parent.setStatus("查询到 " + results.size() + " 条记录");
        } catch (Exception ex) {
            parent.setStatus("查询失败: " + ex.getMessage());
        }
    }

    private String formatExtraAttrs(Map<String, Object> attrs) {
        if (attrs == null || attrs.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        attrs.forEach((k, v) -> sb.append(k).append("=").append(v).append("; "));
        return sb.toString().trim();
    }
}