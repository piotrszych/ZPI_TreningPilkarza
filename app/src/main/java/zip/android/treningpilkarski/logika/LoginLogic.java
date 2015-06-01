package zip.android.treningpilkarski.logika;

import android.content.SharedPreferences;

import zip.android.treningpilkarski.test.TestDataProvider;

public class LoginLogic
{
    private static SharedPreferences sp_admin = null;

    public static void getSharedPreferencesAdmin(SharedPreferences sp)
    {
        sp_admin = sp;
    }

    //zwraca 0, jesli nie ma usera, 1 - jesli jest user ale niepoprawne haslo, 2 - jesli user i pass jest ok
    public static int checkIfLoginPasswordCorrect(String login, String password)
    {
        //TODO podpiecie do bazy danych

        if(sp_admin == null || !sp_admin.getBoolean(DataKeys.S_ADMINKEY_USEINTERNAL, false))
        {
            return TestDataProvider.userArrayContains(login, password);
        }
        else
        {
            //TODO pobieramy z bazy danych

        }

        return 0;
    }
}
