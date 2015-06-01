package zip.android.treningpilkarski.test;

import java.util.ArrayList;

import zip.android.treningpilkarski.logika.PasswordEncrypter;

public class TestDataProvider
{
    //nasz uzytkownik
    public static TestUserData currentUser = null;

    //przykładowe dane dla Listy w ListFragment
    public static String [] listfragment_strings = {"Pompki","Brzuszki","Przysiady","Drążek"};

    //przykladowe dane do logowania
    public static ArrayList<TestUserData> alist_users = new ArrayList<>();

    //zwraca 0, jesli nie ma usera, 1 - jesli jest user ale niepoprawne haslo, 2 - jesli user i pass jest ok
    public static int userArrayContains(String user, String pass)
    {
        for(TestUserData t: alist_users)
        {
            if( t.getLogin().equals(user) )
            {
                if(t.getPassword().equals(PasswordEncrypter.computeSHA256Hash(pass)))
                {
                    return 2;
                }
                return 1;
            }//if( t.getLogin().equals(user) )
        }//for(TestUserData t: alist_users)
        return 0;
    }//public static int userArrayContains(String user, String pass)

    public static TestUserData getUser(String user, String pass, boolean isHashed)
    {
        if(!isHashed)
            return getUser(user, pass);
        else
            for(TestUserData t: alist_users)
                if(t.getLogin().equals(user) && t.getPassword().equals(pass))
                    return t;
        return null;
    }

    public static TestUserData getUser(String user, String pass)
    {
        for(TestUserData t: alist_users)
            if(t.getLogin().equals(user) && t.getPassword().equals( PasswordEncrypter.computeSHA256Hash(pass) ))
                return t;

        return null;
    }//public static TestUserData getUser(String user, String pass)

    public static void populateUsers()
    {
        alist_users.add(new TestUserData("user", "admin"));
        alist_users.add(new TestUserData("piotr", "haslo"));
        alist_users.add(new TestUserData("michal", "palka"));
    }
}
