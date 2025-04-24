package data;

import util.sql.response.table.Manager;

public class Data {
    private static Manager manager = null;
    private static String currentTable = null;

    public static void setManager(Manager manager){
        synchronized(Data.class){
            Data.manager = manager;
        }
    }

    public static Manager getManager(){
        synchronized(Data.class){
            return manager;
        }
    }

    public static void setCurrentTable(String currentTable){
        synchronized(Data.class){
            Data.currentTable = currentTable;
        }
    }

    public static String getCurrentTable(){
        synchronized(Data.class){
            return currentTable;
        }
    }

    private Data() {
    }
}
