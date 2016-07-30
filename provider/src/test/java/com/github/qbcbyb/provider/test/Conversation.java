package com.github.qbcbyb.provider.test;

import android.content.ContentValues;
import android.database.Cursor;

import com.github.qbcbyb.provider.TableModel;

import java.util.List;

/**
 * Created by qbcby on 2016/7/30.
 */
public class Conversation implements TableModel<Conversation, ConversationTableColumn> {
    private final DefaultImpls<Conversation, ConversationTableColumn> defaultImpls = new DefaultImpls<>(this);

    private String title;
    private Long unReadCount;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(Long unReadCount) {
        this.unReadCount = unReadCount;
    }

    public Long get_id() {
        return defaultImpls.get_id();
    }

    public void set_id(Long _id) {
        defaultImpls.set_id(_id);
    }

    @Override
    public void fromCursor(Cursor cursor) {
        defaultImpls.fromCursor(cursor);
    }

    @Override
    public ContentValues toContentValues() {
        return defaultImpls.toContentValues();
    }

    @Override
    public ContentValues toContentValues(ConversationTableColumn[] tableColumns) {
        return defaultImpls.toContentValues(tableColumns);
    }

    @Override
    public ConversationTableColumn[] defaultTableColumnArr() {
        return ConversationTableColumn.values();
    }
}
