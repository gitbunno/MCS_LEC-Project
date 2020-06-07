package com.example.myapplication.Databases;

public class DBSchema {

    public static final class UserTable{
        public static final String NAME = "user_table";

        public static final class Cols{
            public static final String USER_ID = "user_id";
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
            public static final String PHONE = "phone";
            public static final String DOB = "date_of_birth";
            public static final String GENDER = "gender";
            public static final String EMAIL = "email";
        }
    }

    public static final class ItemTable{
        public static final String NAME = "item_table";

        public static final class Cols{
            public static final String ITEM_ID = "item_id";
            public static final String USER_ID = "user_id";
            public static final String ITEM_NAME = "item_name";
            public static final String PURCHASE_DATE = "purchase_date";
            public static final String AMOUNT = "amount";
        }
    }
}
