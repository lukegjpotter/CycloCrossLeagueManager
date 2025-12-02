package com.lukegjpotter.tools.cyclocrossleaguemanager.common.model;

import java.util.HashMap;

public record ColumnAndOrderRecord(HashMap<String, String> columnAndOrder) {

    public String putColumnAndOrder(String column, String order) {
        return columnAndOrder.put(column, order);
    }
}
