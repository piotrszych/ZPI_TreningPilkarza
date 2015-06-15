package zip.android.treningpilkarski.exercise_fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.sql.Date;
import java.util.HashMap;
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;
import zip.android.treningpilkarski.R;
import zip.android.treningpilkarski.SimpleExerciseActivity;
import zip.android.treningpilkarski.logika.DataKeys;
import zip.android.treningpilkarski.logika.DataProvider;
import zip.android.treningpilkarski.logika.database.asyncTasks.ATaskAfterSpecExercise;
import zip.android.treningpilkarski.logika.database.asyncTasks.ATaskGetExerciseByExerciseID;
import zip.android.treningpilkarski.logika.database.interfaces.ICommWithDB;

/**
 * CREATED BY EDYTA
 */
public class SpecExerciseFragment extends Fragment implements ICommWithDB<HashMap<String, Integer>> {

    private int dawneCwiczenieUzytkownika;
    private int interwalCwiczenia;
    private int uzytkownikID;
    private int cwiczenieID;
    int iloscWykonanych = -1;
    int ileDoWykonania = -1;
    int odleglosc = -1; // do pobrania z bazy
    int strona = -1;
    TextView tv_twoja_wydajnosc;
    TextView tv_wymagana_wydajnosc;
    TextView tv_dystans;
    TextView tvIleWykonac;
    TextView tv_exercise_name;
    TextView tv_exercise_done;
    GifImageView iv_image;
    ImageView iv_spec_help;
    Button zakonczButton;

    NumberPicker picker;

    public SpecExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spec_exercise, container, false);

        picker = (NumberPicker) view.findViewById(R.id.exercise_dialog_numberPicker);
        tv_twoja_wydajnosc = (TextView) view.findViewById(R.id.exercise_effect);
        tv_wymagana_wydajnosc = (TextView) view.findViewById(R.id.exercise_required);
        tv_dystans = (TextView) view.findViewById(R.id.exercise_distance);
        tvIleWykonac = (TextView) view.findViewById(R.id.exercise_series);
        zakonczButton = (Button) view.findViewById(R.id.button_zakoncz);
        tv_exercise_name = (TextView) view.findViewById(R.id.exercise_name);
        tv_exercise_done = (TextView) view.findViewById(R.id.exercise_done);
        iv_image = (GifImageView) view.findViewById(R.id.gif_help_gifholder);
        iv_spec_help = (ImageView) view.findViewById(R.id.iv_spec_help);

        tv_wymagana_wydajnosc.setText("Wymagana wydajność: 70%");

        zakonczButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obliczNastepneCwiczenia(cwiczenieID, picker.getValue());
            }
        });

        //wymaganaWydajnosc( ileDoWYkonania,  maxWykonanych);
        //odleglosc(odleglosc);
        Bundle extras = getArguments();
        Log.d("SpecExer bundle", extras == null ? "Brak bundle!" : extras.toString());
        ATaskGetExerciseByExerciseID asyncTask = new ATaskGetExerciseByExerciseID(getActivity(), this, extras.getInt(DataKeys.BUNDLE_KEY_USEREXERCISEID));
        asyncTask.execute();

        //do helpa
        iv_spec_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHelpClicked(v);
            }
        });

        //ustawienie tytulu
        tv_exercise_name.setText(extras.getString(DataKeys.BUNDLE_KEY_EXERCISENAME));

        //zaznaczamy, ze jestesmy w specExercise

        //zaznaczamy, ze jestesmy w zegarze
        //((SimpleExerciseActivity) getActivity()).setIfHelp(2, getArguments());

        //ustawienie czcionek
        tv_twoja_wydajnosc.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        tv_wymagana_wydajnosc.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        tv_dystans.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        tvIleWykonac.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        zakonczButton.setTypeface(DataProvider.TYPEFACE_STANDARD_BOLD);
        tv_exercise_name.setTypeface(DataProvider.TYPEFACE_TITLE_REGULAR);
        tv_exercise_done.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);

        return view;

    }
   /* public void wymaganaWydajnosc(int ileDoWYkonania, int maxWykonanych){
        double wymaganaWydajnosc = (int)(((double)ileDoWYkonania/(double)maxWykonanych) * 100);
        String message2 = "Wymagana wydajność: " + wymaganaWydajnosc + "%";
        tv_wymagana_wydajnosc.setText(message2);
    }*/

    /*public void odleglosc(int odleglosc){
        String message3 = "Odległość: " + odleglosc + " m";
        tv_dystans.setText(message3);
    }*/

    public void onHelpClicked(View view)
    {
        HelpFragment sef = new HelpFragment();
        Bundle bundle = new Bundle();
        //przekazywanie wszystkiego, co dostalismy, dalej, by umozliwic powrot
        bundle.putAll(getArguments());
        bundle.remove(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE);
        bundle.putInt(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE, 2);
        sef.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, sef).commit();
    }


    @Override
    public void notifyActivity(HashMap<String, Integer> objectSent) {
        if(objectSent.get("type") == 1){
            if(objectSent.containsKey("error")){

            }
            else{
                Log.d("SpecExer objGot", objectSent.toString());
                //iloscWykonanych = objectSent.get("ilosc_wykonanych");
                ileDoWykonania = objectSent.get("ilosc_do_wykonania");
                odleglosc = objectSent.get("odleglosc");
                strona = objectSent.get("id_strony");
                dawneCwiczenieUzytkownika = objectSent.get("ExerID");
                cwiczenieID = objectSent.get("ExerGenerID");
                uzytkownikID = objectSent.get("id_uzytkownika");
                interwalCwiczenia = objectSent.get("interwal");
                tv_dystans.setText("Odległość: " + odleglosc + " metrów");
                tvIleWykonac.setText("na " + ileDoWykonania);
                picker.setMinValue(0);
                picker.setMaxValue(ileDoWykonania);
                picker.setValue(0);
                picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int iloscWykonanych) {
                        double wydajnosc = round(((double)iloscWykonanych / (double)ileDoWykonania) * 100, 2);
                        //round(wydajnosc, 2);
                        //Toast.makeText(getActivity().getApplicationContext(),"Wydajnosc: " + (int) wydajnosc,Toast.LENGTH_SHORT).show();
                        String message1 = "Twoja wydajność: " + wydajnosc + "%";
                        tv_twoja_wydajnosc.setText(message1);
                    }
                });

                switch (strona)
                {
                    case 1:
                        iv_image.setImageResource(R.drawable.strona1);
                        break;
                    case 2:
                        iv_image.setImageResource(R.drawable.strona2);
                        break;
                    case 3:
                        iv_image.setImageResource(R.drawable.strona3);
                        break;
                    case 4:
                        iv_image.setImageResource(R.drawable.strona4);
                        break;
                    case 5:
                        iv_image.setImageResource(R.drawable.strona5);
                        break;
                    default:
                        //Zostaje placeholder
                        break;
                }
            }
        }
        else if(objectSent.get("type") == 2){
            if(objectSent.get("success") == 1)
            {
                //wszystko poprawnie
                Toast.makeText(getActivity().getApplicationContext(), "Poprawnie dodano ćwiczenie!", Toast.LENGTH_SHORT).show();
                getActivity().setResult(10);
                getActivity().finish();
            }
            else
            {
                //request nie przeszedl
                Toast.makeText(getActivity().getApplicationContext(), "Nie dodano cwicznia! Kod błędu: " + objectSent.get("success"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void obliczNastepneCwiczenia(int idCwiczenia, int iloscWykonanych){
        int nastepnaStrona = strona;
        int nastepnaIloscDoWykonania = ileDoWykonania;
        int nastepnaOdleglosc = odleglosc;

        double wspolczynnik = (double)iloscWykonanych / (double)ileDoWykonania;
        if(strona < 5){
            if(wspolczynnik >= 0.7){
                nastepnaStrona += 1;
                nastepnaOdleglosc = wybierzOdleglosc(odleglosc);
                nastepnaIloscDoWykonania = wybierzIleTrzebaWykonac();
            }
        }
        else{
            if(wspolczynnik >= 0.7){
                nastepnaStrona = 1;
                nastepnaOdleglosc = wybierzOdleglosc(odleglosc);
                nastepnaIloscDoWykonania = wybierzIleTrzebaWykonac();
            }
        }
        Log.d("Statistics", "Cwiczenie o id: " + idCwiczenia + " Wykonano: " + iloscWykonanych + " na " + ileDoWykonania + " Strona: " + strona + " Odleglosc: " +  odleglosc
                + "\nNowe cwiczenie" + idCwiczenia + " Do wykonania: " + nastepnaIloscDoWykonania + " Strona: " + nastepnaStrona + " Odleglosc: " + nastepnaOdleglosc);

        int ileWykonano = picker.getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, interwalCwiczenia);
        Date dataNastepnegoCwiczenia = new java.sql.Date(calendar.getTimeInMillis());
        ATaskAfterSpecExercise aTask = new ATaskAfterSpecExercise(getActivity(), this, dawneCwiczenieUzytkownika, ileWykonano,
                idCwiczenia, uzytkownikID, nastepnaIloscDoWykonania, nastepnaStrona,
                nastepnaOdleglosc, dataNastepnegoCwiczenia);
        aTask.execute();
    }

    public int wybierzOdleglosc(int dawnaOdleglosc){
        int odleglosc = dawnaOdleglosc;
        Random random = new Random();
        int oIleZwiekszac = random.nextInt(4) + 1;
        int losowaOdleglosc = random.nextInt(44) + 1;
        if(odleglosc <= 40){
            odleglosc += oIleZwiekszac;
        }
        else{
            odleglosc = losowaOdleglosc;
        }
        return odleglosc;
    }


    public int wybierzIleTrzebaWykonac(){
        Random random = new Random();
        return random.nextInt(20) + 30;
    }
}