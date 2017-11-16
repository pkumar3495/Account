package com.pkr.account.db;

import android.provider.BaseColumns;

/**
 * Created by Prashant on 10/23/2017.
 */

public class TaskContract {

    public static final String DB_NAME = "com.pkr.account.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "account";
        public static final String COL_EMAIL = "email";
        public static final String COL_PASS = "password";
        public static final String COL_LOGIN = "login";
    }

}
