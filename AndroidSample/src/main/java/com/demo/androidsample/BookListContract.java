package com.demo.androidsample;

import android.provider.BaseColumns;

public final class BookListContract {

    public BookListContract() {}

        public static abstract class MessageEntry implements BaseColumns {
        public static final String TABLE_NAME = "message";
        public static final String COLUMN_NAME_TEXT = "text";
    }
}