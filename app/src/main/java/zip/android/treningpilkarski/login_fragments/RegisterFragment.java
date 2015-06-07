package zip.android.treningpilkarski.login_fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import zip.android.treningpilkarski.R;
import zip.android.treningpilkarski.logika.DataKeys;
import zip.android.treningpilkarski.logika.DataProvider;
import zip.android.treningpilkarski.logika.PasswordEncrypter;
import zip.android.treningpilkarski.logika.database.asyncTasks.ATaskGetPositionExercises;
import zip.android.treningpilkarski.logika.database.asyncTasks.ATaskLoginUser;
import zip.android.treningpilkarski.logika.database.asyncTasks.ATaskRegisterUser;
import zip.android.treningpilkarski.logika.database.interfaces.ICommWithDB;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements ICommWithDB<Integer> {

    //shared preferences
    SharedPreferences sp_appinternal;

    TextView tv_title;
    EditText et_username;
    EditText et_password;
    EditText et_password_repeat;
    Button button_register;
    ImageView iv_backarrow;
    Spinner spinnerPositions;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //pobieranie shared preferences
        sp_appinternal = getActivity().getSharedPreferences(DataKeys.S_SHAREDPREFERENCES_NAME_APPINTERNAL, Context.MODE_PRIVATE);

        //pobieranie kontrolek
        tv_title = (TextView) view.findViewById(R.id.tv_register_title);
        et_username = (EditText) view.findViewById(R.id.et_register_username);
        et_password = (EditText) view.findViewById(R.id.et_register_password);
        et_password_repeat = (EditText) view.findViewById(R.id.et_register_password_repeat);
        button_register = (Button) view.findViewById(R.id.button_register_register);
        iv_backarrow = (ImageView) view.findViewById(R.id.iv_register_backarrow);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRegister(view);
            }
        });

        //listenery powrotu do title
        iv_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReturnToTitle();
            }
        });
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReturnToTitle();
            }
        });

        //ustawianie fontow
        tv_title.setTypeface(DataProvider.TYPEFACE_TITLE_REGULAR);
        et_username.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        et_password.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        et_password_repeat.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        button_register.setTypeface(DataProvider.TYPEFACE_STANDARD_BOLD);

        addItemsIntoSpinner(view);

        return view;
    }

    public void addItemsIntoSpinner(View view){
        spinnerPositions = (Spinner) view.findViewById(R.id.spinnerPositions);
        List<String> positions = new ArrayList<>();
        positions.add("Ogólna");
        positions.add("Bramkarz");
        positions.add("Obrońca");
        positions.add("Pomocnik");
        positions.add("Napastnik");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, positions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPositions.setAdapter(adapter);
    }

    public void onReturnToTitle()
    {
        sp_appinternal.edit().putBoolean(DataKeys.S_APPINTERNAL_LOGIN_IFLOGIN, false).apply();
        getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, new TitleFragment()).commit();
    }

    public void onClickRegister(View view)
    {
        if( !et_password_repeat.getText().toString().equals(et_password.getText().toString()) )
        {
            Toast.makeText(getActivity().getApplicationContext()
                    , R.string.register_backend_passwords_mismatch
                    , Toast.LENGTH_SHORT).show();
        }//if( !et_password_repeat.getText().toString().equals(et_password.getText().toString()) )
        else
        {
            /*
            * Wymagania dot. hasla:
            *  - min 8 znakow
            *  - litery i cyfry dozwolone
            *  - bez znakow specjalnych
            * */

            String got_pass = et_password.getText().toString();
            String pattern = "^[a-zA-Z0-9]*$";
            if(!got_pass.matches(pattern))
            {
                Toast.makeText(getActivity().getApplicationContext()
                        , getString(R.string.register_toast_onlyletters)
                        , Toast.LENGTH_SHORT).show();
            }
            /*else if(got_pass.length() < 8)
            {
                Toast.makeText(getActivity().getApplicationContext()
                        , getString(R.string.register_toast_mineight)
                        , Toast.LENGTH_SHORT).show();
            }*/
            //TODO sprawdzanie czy haslo ma polskie znaki
            else
            {
                //TODO DB check if user is already in database; if not, add user, get back to title or login?
                int position = spinnerPositions.getSelectedItemPosition();
                ATaskRegisterUser ataskRegister = new ATaskRegisterUser(getActivity(), this, et_username.getText().toString(), PasswordEncrypter.computeSHA256Hash(et_password.getText().toString()), position+1);
                ataskRegister.execute();
                String toDisplay = et_username.getText().toString();
                toDisplay += " " + et_password.getText().toString();
                toDisplay += " " + spinnerPositions.getSelectedItem().toString();

                //addExercises(position);

                Toast.makeText(getActivity().getApplicationContext(), toDisplay, Toast.LENGTH_SHORT).show();
            }
            //getActivity().getFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment());
            //sp_appinternal.edit().putBoolean(DataKeys.S_APPINTERNAL_LOGIN_IFREGISTER, true).apply();
        }//if( !et_password_repeat.getText().toString().equals(et_password.getText().toString()) ) .. else
    }

    public void addExercises(int position){
        //ATaskGetPositionExercises aTaskGetPositionExercises = new ATaskGetPositionExercises(getActivity(), this, position);
        //aTaskGetPositionExercises.execute();
    }

    @Override
    public void notifyActivity(Integer objectSent) {
        if(objectSent != 1)
        {
            Toast.makeText(getActivity(), "Blad rejestracji!", Toast.LENGTH_SHORT).show();
        }
        else{
            getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, new LoginFragment()).commit();
        }
    }
}
