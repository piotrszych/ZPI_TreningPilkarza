package zip.android.treningpilkarski;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import zip.android.treningpilkarski.logika.DataKeys;
import zip.android.treningpilkarski.test.TestDataProvider;
import zip.android.treningpilkarski.test.TestUserData;

/*
* TA AKTYWNOSC NIE POWINNA ZNALEZC SIE W GOTOWEJ APLIKACJI.
* */

public class AdminActivity extends ActionBarActivity {

    //shared preferences
    SharedPreferences sharedPreferences_user;
    SharedPreferences sharedPreferences_adminOptions;

    //kontrolki
    CheckBox cb_useInternalStorage;
    TextView tv_sharedpreferences_container;
    TextView tv_sharedpreferences_admin_container;
    TextView tv_internalStorage_allusers_container;
    Button button_resetSharedPreferences;
    Button button_resetSharedPreferences_admin;
    Button button_useInternalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        sharedPreferences_user = getSharedPreferences(DataKeys.S_SHAREDPREFERENCES_NAME_USER, Context.MODE_PRIVATE);
        sharedPreferences_adminOptions = getSharedPreferences(DataKeys.S_SHAREDPREFERENCES_NAME_ADMINOPTIONS, Context.MODE_PRIVATE);

        cb_useInternalStorage = (CheckBox) findViewById(R.id.cb_admin_useInternalStorage);
        tv_sharedpreferences_container = (TextView) findViewById(R.id.tv_sharedpreferences_container);
        tv_sharedpreferences_admin_container = (TextView) findViewById(R.id.tv_sharedpreferences_admin_container);
        tv_internalStorage_allusers_container = (TextView) findViewById(R.id.tv_internalstorage_admin_container);
        button_resetSharedPreferences = (Button) findViewById(R.id.button_resetSharedPreferences);
        button_resetSharedPreferences_admin = (Button) findViewById(R.id.button_resetSharedPreferences_admin);
        button_useInternalStorage = (Button) findViewById(R.id.button_useInternalStorage);

        tv_sharedpreferences_container.setText(sharedPreferences_user.getAll().toString());
        tv_sharedpreferences_admin_container.setText(sharedPreferences_adminOptions.getAll().toString());

        cb_useInternalStorage.setChecked(sharedPreferences_adminOptions.getBoolean(DataKeys.S_ADMINKEY_USEINTERNAL, false));

        updateInternalStorageContainer();
    }

    //iteruje po wszystkich kluczach z SharedPreferences usera i wywoluje dla kazdego 'remove'
    //potem odswieza kontrolki
    public void resetSharedPreferences(View view)
    {
        Map map = sharedPreferences_user.getAll();
        Set<String> keySet = map.keySet();
        SharedPreferences.Editor editor = sharedPreferences_user.edit();
        for(String key : keySet)
        {
            editor.remove(key);
        }
        editor.apply();

        //refresh textview
        tv_sharedpreferences_container.setText(sharedPreferences_user.getAll().toString());
    }//public void resetSharedPreferences(View view)

    //iteruje po wszystkich kluczach z SharedPreferences adminOptions i wywoluje dla kazdego 'remove'
    //potem odswieza kontrolki
    public void resetSharedPreferencesAdmin(View view)
    {
        Map map = sharedPreferences_adminOptions.getAll();
        Set<String> keySet = map.keySet();
        SharedPreferences.Editor editor = sharedPreferences_adminOptions.edit();
        for(String key : keySet)
        {
            editor.remove(key);
        }
        editor.apply();

        //refresh textview
        tv_sharedpreferences_admin_container.setText(sharedPreferences_adminOptions.getAll().toString());
        cb_useInternalStorage.setChecked(false);
    }//public void resetSharedPreferencesAdmin(View view)

    //pobiera opcje administracyjne
    //na razie - czy uzywac InternalStorage? //NOTYETIMPLEMENTED
    public void getAdminOptions(View view)
    {
        SharedPreferences.Editor editor = sharedPreferences_adminOptions.edit();
        editor.putBoolean(DataKeys.S_ADMINKEY_USEINTERNAL, cb_useInternalStorage.isChecked());
        editor.apply();
        tv_sharedpreferences_admin_container.setText(sharedPreferences_adminOptions.getAll().toString());
    }//public void getAdminOptions(View view)

    public void saveUsersToInternalStorage(View view)
    {
        StringBuilder toInternalStorage = new StringBuilder();
        for(TestUserData tud : TestDataProvider.alist_users)
        {
            toInternalStorage.append(tud.getObjectString());
            toInternalStorage.append("=ENDOBJECT\n");
        }
        toInternalStorage.append("==EOF");
        try {
            FileOutputStream fos = openFileOutput(DataKeys.S_ISTORAGE_NAME_ALLUSERS, Context.MODE_PRIVATE);
            fos.write(toInternalStorage.toString().getBytes());
            Toast.makeText(view.getContext(), "Chyba zapisano :P", Toast.LENGTH_SHORT).show();
            updateInternalStorageContainer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateInternalStorageContainer()
    {
        try {
            FileInputStream fis = openFileInput(DataKeys.S_ISTORAGE_NAME_ALLUSERS);
            int c;
            String temp="";
            while( (c = fis.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            tv_internalStorage_allusers_container.setText(temp);
        } catch (IOException e) {
            tv_internalStorage_allusers_container.setText("BRAK");
            Toast.makeText(getApplicationContext(), "Nie znaleziono: " + DataKeys.S_ADMINKEY_USEINTERNAL, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void tempButtonDoer()
    {
        //TODO TempButton do wykorzystania, na razie nic nie robi
        Toast.makeText(getApplicationContext(), "Na razie nic nie robie", Toast.LENGTH_SHORT).show();
    }
}
