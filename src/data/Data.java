package data;

import util.sql.response.table.Manager;

public class Data {
    private static Manager manager = null;

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

    private Data() {
    }
}
