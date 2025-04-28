package util.sql;

import util.sql.type.DataType;
import util.sql.type.IntType;
import util.sql.type.StringType;

public enum Table {
    STORE("store", "id",
            new StringType("location")),
    EMPLOYEE("employee", "id",
            new StringType("first_name"), new StringType("last_name"), new StringType("address"), new StringType("position"), new IntType("storeID")),
    PRODUCT("product", "id",
            new StringType("name"), new StringType("description")),
    STORE_PRODUCT_LINK("store_has_product", null,
            new IntType("store_id"), new IntType("product_id"));

    private final String tableName;
    private final String pKey;
    private final DataType[] reqData;

    Table(String tableName, String pKey, DataType... requiredData) {
        this.tableName = tableName;
        this.pKey = pKey;
        this.reqData = requiredData;
    }

    public String getTableName() {
        return tableName;
    }

    public DataType[] getReqData() {
        return reqData;
    }

    public String getPKey() {
        return pKey;
    }
}
