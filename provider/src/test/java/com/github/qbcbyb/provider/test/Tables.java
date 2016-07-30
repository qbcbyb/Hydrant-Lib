package com.github.qbcbyb.provider.test;

import com.github.qbcbyb.provider.SQLiteTable;

/**
 * Created by qbcby on 2016/7/30.
 */
public class Tables {
    public static final SQLiteTable ConversationTable = new SQLiteTable<>("CONVERSATION", ConversationTableColumn.values());

    public static void init() {
        // do nothing
    }
}
