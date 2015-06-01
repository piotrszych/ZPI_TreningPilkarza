package zip.android.treningpilkarski.logika;

public class DataKeys
{
    //adres ip bazy danych
    public static final String S_DATABASE_IP_ADDRESS = "156.17.37.219";

    //shared preferences app session data
    public static final String S_SHAREDPREFERENCES_NAME_APPINTERNAL = "SP_APPINTERNAL";
    public static final String S_APPINTERNAL_LOGIN_IFREGISTER = "B_IFREGISTER";
    public static final String S_APPINTERNAL_LOGIN_IFLOGIN = "B_IFLOGIN";
    public static final String S_APPINTERNAL_MAINTOEXERCISE_WHICHFRAGMENT = "FRAGMENT_ID";

    //shared preferences user data
    public static final String S_SHAREDPREFERENCES_NAME_USER = "SP_USER";
    public static final String S_KEY_USERNAME = "USERNAME";
    public static final String S_KEY_PASSWORD = "PASSWORD";
    public static final String S_KEY_USERID = "USERID";
    public static final String S_KEY_REMEMBERUSERNAME = "B_REMEMBERUSER";
    public static final String S_KEY_AUTOLOGIN = "B_AUTOLOGIN";

    //shared preferences admin options
    public static final String S_SHAREDPREFERENCES_NAME_ADMINOPTIONS = "SP_ADMIN";
    public static final String S_ADMINKEY_USEINTERNAL = "B_USEINTERNAL";

    //internal storage users
    public static final String S_ISTORAGE_NAME_ALLUSERS = "IS_ALLUSERS";
    public static final String S_KEY_IS_ALLUSERS = "S_ALLUSERS";

    //Intent - z ListFramgent do SimpleExerciseActivity
    public static final String INTENT_LISTTOEXERCISE_IFEXERCISE = "IF_EXERCISE";
    public static final String BUNDLE_KEY_USEREXERCISEID = "BUN_USEREXERCISEID";
    public static final String BUNDLE_KEY_EXERCISEID = "BUN_EXERCISEID";
    public static final String BUNDLE_KEY_EXERCISENAME = "BUN_EXERCISENAME";

    //ListFragment - tag key of list object
    public static final int TAG_KEY_EXERCISEID = 101;

    //do bundle - dane uniwersalne
    public static final String BUNDLE_NAME_USER = "USERBUNDLE";
    public static final String BUNDLE_KEY_USERNAME = "BUN_USERNAME";
    public static final String BUNDLE_KEY_USERID = "BUN_USERID";
}
