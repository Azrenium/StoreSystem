package util.sql;

import util.ErrorResponse;

import java.util.ArrayList;

public class SelectResponse extends ErrorResponse {
    private ArrayList<String> columns = new ArrayList<>();
    private ArrayList<ArrayList<String>> rows = new ArrayList<>();

    public SelectResponse() {}

    public ArrayList<String> getColumns() {
        return columns;
    }

    public ArrayList<ArrayList<String>> getRows() {
        return rows;
    }
}
