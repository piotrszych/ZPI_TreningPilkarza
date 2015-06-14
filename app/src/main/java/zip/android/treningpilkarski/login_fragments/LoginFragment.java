package zip.android.treningpilkarski.login_fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import zip.android.treningpilkarski.MainActivity;
import zip.android.treningpilkarski.R;
import zip.android.treningpilkarski.logika.DataProvider;
import zip.android.treningpilkarski.logika.PasswordEncrypter;
import zip.android.treningpilkarski.logika.DataKeys;
import zip.android.treningpilkarski.logika.database.asyncTasks.ATaskLoginUser;
import zip.android.treningpilkarski.logika.database.interfaces.ICommWithDB;
import zip.android.treningpilkarski.test.TestDataProvider;

public class LoginFragment extends Fragment implements ICommWithDB<HashMap<String, String>>{

    //do shared preferences
    SharedPreferences sp_user;
    SharedPreferences sp_admin;
    SharedPreferences sp_appinternal;

    //kontrolki
    TextView tv_title;
    EditText et_username;
    EditText et_password;
    Button button_login;
    CheckBox cb_rememberUser;
    CheckBox cb_autologin;
    ImageView iv_backarrow;

    //dane pobrane
    String s_login = null;
    String s_passHash = null;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //TestDatabase.initializeDatabaseFromIS(view);

        //pobieranie shared preferences
        sp_user = getActivity().getSharedPreferences(DataKeys.S_SHAREDPREFERENCES_NAME_USER, Context.MODE_PRIVATE);
//        sp_admin = getActivity().getSharedPreferences(DataKeys.S_SHAREDPREFERENCES_NAME_ADMINOPTIONS, Context.MODE_PRIVATE);
        sp_appinternal = getActivity().getSharedPreferences(DataKeys.S_SHAREDPREFERENCES_NAME_APPINTERNAL, Context.MODE_PRIVATE);

        //wczytywanie kontrolek
        tv_title = (TextView) view.findViewById(R.id.tv_login_title);
        et_username = (EditText) view.findViewById(R.id.et_login_username);
        et_password = (EditText) view.findViewById(R.id.et_login_password);
        button_login = (Button) view.findViewById(R.id.button_login_login);
        cb_autologin = (CheckBox) view.findViewById(R.id.cb_login_loginAutomatically);
        cb_rememberUser = (CheckBox) view.findViewById(R.id.cb_login_rememberUser);
        iv_backarrow = (ImageView) view.findViewById(R.id.iv_login_backarrow);

        //dodanie listenerow
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonLogin_OnClick();
            }
        });
        cb_autologin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    cb_rememberUser.setChecked(true);
                    cb_rememberUser.setEnabled(false);
                }
                if (!isChecked) {
                    cb_rememberUser.setEnabled(true);
                }
            }
        });

        //dodanie listenerow "powrotnych"
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReturnToTitle();
            }
        });
        iv_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReturnToTitle();
            }
        });

        //pobieranie wartosci z shared preferences
        boolean b_autologin = sp_user.getBoolean(DataKeys.S_KEY_AUTOLOGIN, false);
        if(b_autologin)
        {
            String s_user = sp_user.getString(DataKeys.S_KEY_USERNAME, "_NOUSER");
            String s_pass = sp_user.getString(DataKeys.S_KEY_PASSWORD, "_NOPASS");
            TestDataProvider.currentUser = TestDataProvider.getUser(s_user, s_pass, true);
            et_username.setText(s_user);
            cb_rememberUser.setChecked(true);
            startActivityForResult(new Intent(view.getContext(), MainActivity.class), 1);
        }
        else {
            boolean b_rememberuser = sp_user.getBoolean(DataKeys.S_KEY_REMEMBERUSERNAME, false);
            String s_got_username = sp_user.getString(DataKeys.S_KEY_USERNAME, "_NOUSER");
            if (b_rememberuser) {
                et_username.setText(s_got_username);
            }
            cb_rememberUser.setChecked(b_rememberuser);
        }

        if(getArguments() != null && getArguments().containsKey("USERNAME_FROM_LOGIN"))
        {
            et_username.setText(getArguments().getString("USERNAME_FROM_LOGIN"));
        }

        //ustawienie czcionek
        tv_title.setTypeface(DataProvider.TYPEFACE_TITLE_REGULAR);
        et_username.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        et_password.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        cb_rememberUser.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        cb_autologin.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        button_login.setTypeface(DataProvider.TYPEFACE_STANDARD_BOLD);


        return view;
    }

    public void onReturnToTitle()
    {
        sp_appinternal.edit().putBoolean(DataKeys.S_APPINTERNAL_LOGIN_IFLOGIN, false).apply();
        getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, new TitleFragment()).commit();
    }

    public void buttonLogin_OnClick()
    {
        String user = et_username.getText().toString();
        String pass = et_password.getText().toString();
        s_login = user;
        s_passHash = PasswordEncrypter.computeSHA256Hash(pass);

//        if(!sp_admin.getBoolean(DataKeys.S_ADMINKEY_USEINTERNAL, false)) {
//            switch (TestDataProvider.userArrayContains(user, pass)) {
//                case 0:
//                    //nie ma takiego usera
//                    Toast.makeText(getView().getContext(), "Brak takiego usera!", Toast.LENGTH_SHORT).show();
//                    break;
//                case 1:
//                    //jest user, zle haslo
//                    Toast.makeText(getView().getContext(), "Dobry user, zle haslo!", Toast.LENGTH_SHORT).show();
//                    break;
//                case 2:
//                    //jest user, dobre haslo
//
//                    SharedPreferences.Editor editor = sp_user.edit();
//                    editor.putString(DataKeys.S_KEY_USERNAME, user);
//                    //sprawdzenie, czy pamietamy usera
//                    if (cb_rememberUser.isChecked()) {
//                        editor.putBoolean(DataKeys.S_KEY_REMEMBERUSERNAME, true);
//                        //TODELETE line below
//                        Toast.makeText(getView().getContext(), "Saved user: " + user, Toast.LENGTH_SHORT).show();
//                        if (cb_autologin.isChecked()) {
//                            editor.putBoolean(DataKeys.S_KEY_AUTOLOGIN, true);
//                            editor.putString(DataKeys.S_KEY_PASSWORD, PasswordEncrypter.computeSHA256Hash(pass));
//                        } else {
//                            editor.putBoolean(DataKeys.S_KEY_AUTOLOGIN, false);
//                            editor.remove(DataKeys.S_KEY_PASSWORD);
//                        }
//                    } else {
//                        editor.putBoolean(DataKeys.S_KEY_REMEMBERUSERNAME, false);
//                        editor.putBoolean(DataKeys.S_KEY_AUTOLOGIN, false);
//                        editor.remove(DataKeys.S_KEY_PASSWORD);
//                        //TODELETE line below
//                        Toast.makeText(getView().getContext(), "Removed user", Toast.LENGTH_SHORT).show();
//                    }
//                    editor.apply();
//
//                    TestDataProvider.currentUser = TestDataProvider.getUser(user, pass);
//                    startActivityForResult(new Intent(getView().getContext(), MainActivity.class), 1);
//                    break;
//                default:
//                    //tu nie powinno nas byc
//                    Toast.makeText(getView().getContext(), "Jak to zrobiles?!", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }//if(!sp_admin.getBoolean(DataKeys.S_ADMINKEY_USEINTERNAL, false))
//        else    //uzywamy neta
//        {
            ATaskLoginUser atask_login = new ATaskLoginUser(getActivity(), this, user, PasswordEncrypter.computeSHA256Hash(pass));
            atask_login.execute();
//        }

    }//public void buttonLogin_OnClick()

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 1)
        {
            getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, new TitleFragment()).commit();
        }
    }

    @Override
    public void notifyActivity(HashMap<String, String> objectSent) {

        if(objectSent.containsKey("error"))
        {
            Toast.makeText(getActivity(), objectSent.get("error"), Toast.LENGTH_SHORT).show();
        }
        else
        {
            int id = Integer.parseInt(objectSent.get("id"));
            String username = objectSent.get("login");

            //jest user, dobre haslo

            SharedPreferences.Editor editor = sp_user.edit();
            //sprawdzenie, czy pamietamy usera
            if (cb_rememberUser.isChecked()) {
                editor.putBoolean(DataKeys.S_KEY_REMEMBERUSERNAME, true);
                editor.putString(DataKeys.S_KEY_USERNAME, username);

                if (cb_autologin.isChecked()) {
                    editor.putBoolean(DataKeys.S_KEY_AUTOLOGIN, true);
                    editor.putInt(DataKeys.BUNDLE_KEY_USERID, id);
                    editor.putString(DataKeys.S_KEY_PASSWORD, s_passHash);
                } else {
                    editor.putInt(DataKeys.S_KEY_USERID, id);
                    editor.putBoolean(DataKeys.S_KEY_AUTOLOGIN, false);
                    editor.remove(DataKeys.S_KEY_PASSWORD);
                    editor.remove(DataKeys.S_KEY_USERID);
                }
            } else {
                editor.putBoolean(DataKeys.S_KEY_REMEMBERUSERNAME, false);
                editor.putBoolean(DataKeys.S_KEY_AUTOLOGIN, false);
                editor.remove(DataKeys.S_KEY_PASSWORD);
                editor.remove(DataKeys.S_KEY_USERID);
            }
            editor.apply();

            Intent intent = new Intent(getActivity(), MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(DataKeys.BUNDLE_KEY_USERID, id);
            bundle.putString(DataKeys.BUNDLE_KEY_USERNAME, username);
            intent.putExtra(DataKeys.BUNDLE_NAME_USER, bundle);
            startActivityForResult(intent, 1);
        }

    }
}
