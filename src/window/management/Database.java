package window.management;

import data.Data;
import util.ErrorResponse;
import util.sql.Connect;
import util.sql.type.DataType;
import util.sql.Table;
import util.sql.response.SelectResponse;
import util.sql.type.IntType;
import util.sql.type.StringType;
import window.Window;
import window.WindowManager;
import window.welcome.Welcome;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

        setupData();

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
            Table current = Data.getCurrentTable();

            StringBuilder data = new StringBuilder();
            StringBuilder values = new StringBuilder();

            for (DataType reqData : current.getReqData()) {
                String ans = JOptionPane.showInputDialog("Enter " + reqData.getName() + ": ");

                data.append(reqData.getName()).append(", ");

                if(reqData instanceof IntType) {
                    values.append(ans).append(", ");
                } else if(reqData instanceof StringType){
                    values.append("\"").append(ans).append("\"").append(", ");
                }
            }

            if(Data.getCurrentTable() == Table.STORE) {
                data.append("managerID, ");
                values.append(Data.getManager().getId()).append(", ");
            }

            data.delete(data.length() - 2, data.length());
            values.delete(values.length() - 2, values.length());

            String query = "insert into sakila." + Data.getCurrentTable().getTableName() + " (" + data + ") values (" + values + ")";

            ErrorResponse response = Connect.insert(query);

            if(!response.isValid()){
                JOptionPane.showMessageDialog(null, response.getErrorMessagesAsString());
                return;
            }

            refreshData();
        });

        deleteRowButton.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row < 0) return;

            int index = (table.getColumnModel().getColumnIndex(Data.getCurrentTable().getPKey()));
            int pKey = Integer.parseInt(table.getValueAt(row, index).toString());

            ErrorResponse response = Connect.deleteRow(Data.getCurrentTable().getTableName(), Data.getCurrentTable().getPKey(), pKey);

            if(!response.isValid()){
                JOptionPane.showMessageDialog(null, response.getErrorMessagesAsString());
            }

            refreshData();
        });

        filterBox.addActionListener(e -> {
            refreshData();
        });

        sortBox.addActionListener(e -> {
            refreshData();
        });

        table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                return super.getTableCellEditorComponent(table, value, isSelected, row, column); // this method initializes the editor component (e.g., a JTextField) with the current cell value.
            }

            @Override
            public boolean stopCellEditing() {
                String newValue = getCellEditorValue().toString();

                int row = table.getEditingRow();
                int column = table.getEditingColumn();

                String columnName = table.getColumnName(column);

                int index = (table.getColumnModel().getColumnIndex(Data.getCurrentTable().getPKey()));
                String id = (table.getValueAt(row, index).toString());

                ErrorResponse response = null;

                for (DataType reqData : Data.getCurrentTable().getReqData()) {
                    if(reqData.getName().equals(columnName)) {
                        try {
                            if (reqData instanceof IntType) {
                                response = Connect.updateDatabase(Data.getCurrentTable().getTableName(), Data.getCurrentTable().getPKey(),
                                        Integer.parseInt(id), columnName, Integer.parseInt(newValue));
                            } else if (reqData instanceof StringType) {
                                response = Connect.updateDatabase(Data.getCurrentTable().getTableName(), Data.getCurrentTable().getPKey(),
                                        Integer.parseInt(id), columnName, newValue);
                            }
                        } catch (Exception e){
                            response = new ErrorResponse();
                            response.addErrorMessage("Wrong Input!");
                        }

                        break;
                    }
                }

                if(response == null || !response.isValid()) {
                    JOptionPane.showMessageDialog(null, response == null ? "No value updated." : response.getErrorMessagesAsString());
                }

                boolean toReturn = super.stopCellEditing();

                refreshData();

                return toReturn;
            }
        });
    }

    private void setupData(){
        String query = "select * from sakila." + Data.getCurrentTable().getTableName();

        SelectResponse sr = Connect.select(query);

        columns = sr.getColumns();
        rows = sr.getRows();

        model.setColumnIdentifiers(columns.toArray());
        table.setModel(model);

        filterBox.setModel(new DefaultComboBoxModel<>(columns.toArray()));

        updateTable();
    }

    private void refreshData(){
        String filter = filterBox.getSelectedItem() == null ? Data.getCurrentTable().getPKey() : filterBox.getSelectedItem().toString();

        String query = "select * from sakila." + Data.getCurrentTable().getTableName() + " " +
                "order by " + filter + " " + sortBox.getSelectedItem();

        SelectResponse sr = Connect.select(query);

        columns = sr.getColumns();
        rows = sr.getRows();

        updateTable();
    }

    private void updateTable(){
        model.setRowCount(0);

        rows.forEach(row -> {
            if(row.stream().anyMatch(it -> it.contains(searchField.getText()))) model.addRow(row.toArray());
        });
    }
}
