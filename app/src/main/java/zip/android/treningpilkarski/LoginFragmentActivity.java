package zip.android.treningpilkarski;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.sql.Date;
import java.util.Calendar;

import zip.android.treningpilkarski.logika.DataKeys;
import zip.android.treningpilkarski.logika.DataProvider;
import zip.android.treningpilkarski.login_fragments.TitleFragment;
import zip.android.treningpilkarski.test.TestDataProvider;


public class LoginFragmentActivity extends ActionBarActivity {

    SharedPreferences sp_appinternal;
    SharedPreferences sp_user;

    //TEST
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //KAROLINA - baza danych
        /*if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/
        //END KAROLINA - baza danych

        //inicjalizacja DataProvider
        DataProvider.initializeTypefaces(getAssets());

        //TEST do usuniecia
        TestDataProvider.populateUsers();

        sp_appinternal = getSharedPreferences(DataKeys.S_SHAREDPREFERENCES_NAME_APPINTERNAL, Context.MODE_PRIVATE);
        sp_user = getSharedPreferences(DataKeys.S_SHAREDPREFERENCES_NAME_USER, Context.MODE_PRIVATE);
        //czyscimy sp_appinternal
        sp_appinternal.edit().remove(DataKeys.S_APPINTERNAL_LOGIN_IFREGISTER).apply();
        sp_appinternal.edit().remove(DataKeys.S_APPINTERNAL_LOGIN_IFLOGIN).apply();

        setContentView(R.layout.activity_login_fragment);

        //pozbycie sie action bara
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.hide();
        /*//TEST DATA
        Calendar cal = Calendar.getInstance();
        date = new Date(Calendar.getInstance().getTimeInMillis());
        cal.setTime(date);
        cal.add(Calendar.DATE, 10);
        date.setTime(cal.getTimeInMillis());*/
//        Toast.makeText(this, "CURRDATE " + date.toString(), Toast.LENGTH_LONG).show();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.simple_exercise_container, new TitleFragment())
                    .commit();
        }

    }

    @Override
    public void onBackPressed() {
        //jesli jestesmy w Register, wracamy do Title
        if( sp_appinternal.getBoolean(DataKeys.S_APPINTERNAL_LOGIN_IFREGISTER, false) )
        {
            sp_appinternal.edit().putBoolean(DataKeys.S_APPINTERNAL_LOGIN_IFREGISTER, false).apply();
            getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, new TitleFragment()).commit();
        }
        //jesli jestesmy w Login, wracamy do Title
        else if( sp_appinternal.getBoolean(DataKeys.S_APPINTERNAL_LOGIN_IFLOGIN, false) )
        {
            sp_appinternal.edit().putBoolean(DataKeys.S_APPINTERNAL_LOGIN_IFLOGIN, false).apply();
            getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, new TitleFragment()).commit();
        }
        //robimy to co zwykle (zamykamy apke)
        else
        {
            super.onBackPressed();
        }
    }//public void onBackPressed()

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(getApplicationContext(), "On Activity result of login", Toast.LENGTH_SHORT).show();

        if(resultCode == 1)
        {
            //TODO nie finish! Ustawic wszystko tak, by uzytkownik mogl sie zalogowac jeszcze raz
            finish();
        }
    }
}
