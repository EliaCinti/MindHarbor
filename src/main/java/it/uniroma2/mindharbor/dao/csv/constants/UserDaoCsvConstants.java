package it.uniroma2.mindharbor.dao.csv.constants;

public class UserDaoCsvConstants {
    private UserDaoCsvConstants() {
        /* empty constructor : no instance */
    }
    public static final String PATH_NAME_USER = "db/csv/user_db.csv";
    public static final int USER_INDEX_USERNAME = 0;
    public static final int USER_INDEX_PASSWORD = 1;
    public static final int USER_INDEX_FIRST_NAME = 2;
    public static final int USER_INDEX_LAST_NAME = 3;
    public static final int USER_INDEX_TYPE = 4;
    public static final int USER_INDEX_GENDER = 5;
    public static final String USER_EXIST = "User already exists";
}
