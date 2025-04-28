package data;

import util.sql.Table;
import util.sql.response.row.Manager;

public class Data {
    private static Manager manager = null;
    private static Table currentTable = null;

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

    public static void setCurrentTable(Table currentTable){
        synchronized(Data.class){
            Data.currentTable = currentTable;
        }
    }

    public static Table getCurrentTable(){
        synchronized(Data.class){
            return currentTable;
        }
    }

    private Data() {
    }
}
