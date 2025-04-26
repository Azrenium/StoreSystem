package window.management;

import data.Data;
import util.sql.Connect;
import util.sql.response.SelectResponse;
import window.Window;
import window.WindowManager;
import window.welcome.Welcome;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Database extends Window {
    private JPanel panel;
    private JTable table;
    private JButton addRowButton;
    private JButton deleteRowButton;
    private JButton backButton;
    private JTextField searchField;
    private JComboBox<Object> sortBox;
    private JComboBox<Object> filterBox;

    private ArrayList<String> columns = new ArrayList<>();
    private ArrayList<ArrayList<String>> rows = new ArrayList<>();

    private final DefaultTableModel model = new DefaultTableModel();

    public Database() {
        super();

        setContentPane(panel);

        refreshData();

        backButton.addActionListener(e -> {
            Data.setCurrentTable(null);

            WindowManager.startWindow(Welcome.class);

            dispose();
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                updateTable();
            }
        });

        addRowButton.addActionListener(e -> {

        });

        deleteRowButton.addActionListener(e -> {

        });
    }

    private void refreshData(){
        SelectResponse sr = Connect.select("select * from sakila." + Data.getCurrentTable());

        columns = sr.getColumns();
        rows = sr.getRows();

        model.setColumnIdentifiers(columns.toArray());
        table.setModel(model);

        filterBox.setModel(new DefaultComboBoxModel<Object>(columns.toArray()));

        updateTable();
    }

    private void updateTable(){
        model.setRowCount(0);

        rows.forEach(row -> {
            if(row.stream().anyMatch(it -> it.contains(searchField.getText()))) model.addRow(row.toArray());
        });
    }
}
