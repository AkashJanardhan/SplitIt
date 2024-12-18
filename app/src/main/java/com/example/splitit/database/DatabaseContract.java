package com.example.splitit.database;

import android.provider.BaseColumns;

public final class DatabaseContract {

    private DatabaseContract() {}

    public static class GroupEntry implements BaseColumns {
        public static final String TABLE_NAME = "groups";
        public static final String COLUMN_NAME_GROUP_NAME = "name";
    }

    public static class MemberEntry implements BaseColumns {
        public static final String TABLE_NAME = "members";
        public static final String COLUMN_NAME_MEMBER_NAME = "name";
        public static final String COLUMN_NAME_GROUP_ID = "group_id";
        public static final String COLUMN_NAME_MEMBER_IMAGE = "image_uri"; // New column
    }


    public static class ExpenseEntry implements BaseColumns {
        public static final String TABLE_NAME = "expenses";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_PAYER_ID = "payer_id";
        public static final String COLUMN_NAME_GROUP_ID = "group_id";
    }
}

