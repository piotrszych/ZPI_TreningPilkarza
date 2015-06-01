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
import android.widget.TextView;
import android.widget.Toast;

import zip.android.treningpilkarski.MainActivity;
import zip.android.treningpilkarski.R;
import zip.android.treningpilkarski.logika.DataKeys;
import zip.android.treningpilkarski.logika.DataProvider;
import zip.android.treningpilkarski.test.TestDataProvider;

public class TitleFragment extends Fragment {


    //shared preferences
    SharedPreferences sp_appinternal;
    SharedPreferences sp_user;

    public TitleFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_title, container, false);

        //pobieramy shared preferences
        sp_appinternal = getActivity().getSharedPreferences(DataKeys.S_SHAREDPREFERENCES_NAME_APPINTERNAL, Context.MODE_PRIVATE);
        sp_user = getActivity().getSharedPreferences(DataKeys.S_SHAREDPREFERENCES_NAME_USER, Context.MODE_PRIVATE);

        //pobieramy kontrolki
        Button button_login = (Button) view.findViewById(R.id.button_title_login);
        TextView tv_register = (TextView) view.findViewById(R.id.tv_title_register_goto);

        //w zaleznosci od tego, czy mamy autologin, czy nie, oferujemy rozna funkcjonalnosc
        boolean b_autologin = sp_user.getBoolean(DataKeys.S_KEY_AUTOLOGIN, false);
        if(b_autologin)
        {
            //podmieniamy teksty, ustawiamy odpowiednie listenery
            button_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onGotoAppClick(view);
                }
            });
            button_login.setText(getString(R.string.title_autologin_button_login));

            tv_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLogoutClick(view);
                }
            });
            tv_register.setText("Nie " +
                    sp_user.getString(DataKeys.S_KEY_USERNAME, "_NONAME")
                    + "? "
                    + getString(R.string.title_autologin_logout));
        } //if(b_autologin)
        else
        {
            button_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLoginClick(view);
                }
            });
            tv_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRegisterClick(view);
                }
            });
        } //if(b_autologin)...else

        TextView tv_title = (TextView) view.findViewById(R.id.tv_title_appname);

        //zmieniamy fonty bo czemu nie
        tv_register.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        button_login.setTypeface(DataProvider.TYPEFACE_STANDARD_BOLD);
        tv_title.setTypeface(DataProvider.TYPEFACE_TITLE_REGULAR);

        return view;
    }//public View onCreateView

    public void onLoginClick(View view)
    {
        sp_appinternal.edit().putBoolean(DataKeys.S_APPINTERNAL_LOGIN_IFLOGIN, true).apply();
        getActivity().getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, new LoginFragment()).commit();
    }

    public void onRegisterClick(View view)
    {
        sp_appinternal.edit().putBoolean(DataKeys.S_APPINTERNAL_LOGIN_IFREGISTER, true).apply();
        getActivity().getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, new RegisterFragment()).commit();
    }

    public void onGotoAppClick(View view)
    {
        String s_user = sp_user.getString(DataKeys.S_KEY_USERNAME, "_NOUSER");
        String s_pass = sp_user.getString(DataKeys.S_KEY_PASSWORD, "_NOPASS");
        int i_userid = sp_user.getInt(DataKeys.BUNDLE_KEY_USERID, -1);
        TestDataProvider.currentUser = TestDataProvider.getUser(s_user, s_pass, true);
//        startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));

        Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(DataKeys.BUNDLE_KEY_USERNAME, s_user);
        bundle.putInt(DataKeys.BUNDLE_KEY_USERID, i_userid);
        i.putExtra(DataKeys.BUNDLE_NAME_USER, bundle);
        startActivityForResult(i, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1)
        {
            onLogoutClick(null);
        }
    }

    public void onLogoutClick(View view)
    {
        SharedPreferences.Editor editor = sp_user.edit();
        editor.remove(DataKeys.S_KEY_PASSWORD);
        editor.remove(DataKeys.S_KEY_AUTOLOGIN);
        editor.apply();
        //wywolujemy swoj fragment jeszcze raz, coby nam dobre listenery i teksty sie pojawily
        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.title_toast_logout), Toast.LENGTH_SHORT).show();
        getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, new TitleFragment()).commit();
    }

}
