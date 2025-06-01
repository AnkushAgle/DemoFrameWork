package com.aa.utility;
public enum PolicyTables {
    MATERIAL_POLICY("MSCSYSENG"),
    SALES_POLICY("MSCSYSSAL"),
    FINANCE_POLICY("MSCSYSFIN"),
    QC_POLICY("MSCSYSQC"),
    SHOP_POLICY("MSCSYSSHP"),
    MRP_POLICY("MSCMRP"),
    PURCHASE_POLICY("MSCSYSPUR"),
    SYSTEM_POLICY("MSCSysFlags"),
    INVENTORY_POLICY("MSCSYSINV");

    private final String tableName;

    // Constructor
    PolicyTables(String tableName) {
        this.tableName = tableName;
    }

    // Getter method to retrieve the table name
    public String getTableName() {
        return tableName;
    }
}
