package zip.android.treningpilkarski.test.database;

import android.view.View;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import zip.android.treningpilkarski.logika.DataKeys;

public class TestDatabase {

    public static ArrayList<TestDBUser> alist_userLogins = new ArrayList<>();
    public static ArrayList<TestDBUserExercise> alist_userExercises = new ArrayList<>();

    public static void initializeDatabaseFromIS(View view)
    {
        //TODO pobieranie innych rzeczy z internalStorage

        try {
            FileInputStream fis = view.getContext().openFileInput(DataKeys.S_ISTORAGE_NAME_ALLUSERS);
            int c;
            String temp="";
            while( (c = fis.read()) != -1){
                temp = temp + Character.toString((char)c);
            }

            StringTokenizer st = new StringTokenizer(temp);

            ArrayList<String> logins = new ArrayList<>();
            ArrayList<String> passwords = new ArrayList<>();

            while (st.hasMoreTokens())
            {
                String s_current = st.nextToken();
                if(s_current.startsWith("UserID:"))
                {
                    int idUser = Integer.parseInt(s_current.substring(7));
                    String login = st.nextToken().substring(2);
                    String pass = st.nextToken().substring(7);
                    alist_userLogins.add(new TestDBUser(idUser, login, pass));
                    st.nextToken();     //pomijamy zapisane haslo plaintekstem
                    s_current = st.nextToken();
                    while(s_current.startsWith("EXER"))
                    {
                        int idExercise = Integer.parseInt(s_current.substring(4, 7));   //EXER000:lacznieIle;atr1;atr2;atr3;atr4;byloPominiete;data
                        int atrs[] = new int[6];
                        GregorianCalendar date = null;
                        int position = 0;
                        StringTokenizer st_exercise = new StringTokenizer(s_current.substring(8), ";");
                        while (st_exercise.hasMoreTokens()) {
                            if (position == 5) {
                                //TODO obslugaDaty
                                StringTokenizer st_exercise_date = new StringTokenizer(st_exercise.nextToken(), "-");
                                if(!st_exercise_date.nextToken().equals("null"))
                                {
                                    date = new GregorianCalendar(
                                            Integer.parseInt(st_exercise_date.nextToken()),
                                            Integer.parseInt(st_exercise_date.nextToken()),
                                            Integer.parseInt(st_exercise_date.nextToken())
                                    );
                                }
                            }
                            else
                            {
                                atrs[position] = Integer.parseInt(st_exercise.nextToken());
                                position++;
                            }
                        }//while (st_exercise.hasMoreTokens())
                        TestDBUserExercise tdbue = new TestDBUserExercise(
                                idUser,
                                idExercise,
                                atrs[0],
                                atrs[1],
                                atrs[2],
                                atrs[3],
                                atrs[4],
                                atrs[5],
                                date);
                        alist_userExercises.add(tdbue);

                        s_current = st.nextToken();
                    }//while(s_current.startsWith("EXER"))

                }
                //pobieramy login
                else if(s_current.startsWith("L:")) {
                    //Toast.makeText(view.getContext(), s_current.substring(2), Toast.LENGTH_SHORT).show();
                    logins.add(s_current.substring(2));
                }
                //pobieramy haslo (hash)
                else if(s_current.startsWith("P_HASH:"))
                {
                    //Toast.makeText(view.getContext(), s_current.substring(7), Toast.LENGTH_SHORT).show();
                    passwords.add(s_current.substring(7));
                }
            }//while (st.hasMoreTokens())

            //TEST petla nizej do usuniecia
            if(logins.size() != passwords.size())
            {
                Toast.makeText(view.getContext(), "Logins size is not the same as the passes size!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                for(int i = 0; i < logins.size(); i++) {
                    alist_userLogins.add(new TestDBUser(alist_userLogins.size(), logins.get(i), passwords.get(i)));
                }
            }

        } catch (IOException e) {
            Toast.makeText(view.getContext(), "Nie znaleziono: " + DataKeys.S_ADMINKEY_USEINTERNAL, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }//public static void initializeDatabaseFromIS(View view)

    public static boolean containsLogin(String login)
    {
        for(TestDBUser user : alist_userLogins)
            if(user.getLogin().equals(login))
                return true;

        return false;
    }
}//public class TestDatabase
