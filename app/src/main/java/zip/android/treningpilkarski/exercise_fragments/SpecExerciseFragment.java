package zip.android.treningpilkarski.exercise_fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import zip.android.treningpilkarski.R;

/**
 * CREATED BY EDYTA
 */
public class SpecExerciseFragment extends Fragment {

    int iloscWykonanych = 9;
    int ileDoWYkonania = 87;
    int maxWykonanych = 100; // do pobrania z bazy
    int odleglosc = 30; // do pobrania z bazy
    TextView tv_twoja_wydajnosc;
    TextView tv_wymagana_wydajnosc;
    TextView tv_dystans;

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
        picker.setMinValue(0);
        picker.setMaxValue((int)maxWykonanych);
        picker.setValue(1);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int iloscWykonanych) {
                double wydajnosc = ((double)iloscWykonanych / (double)maxWykonanych) * 100;
                //Toast.makeText(getActivity().getApplicationContext(),"Wydajnosc: " + (int) wydajnosc,Toast.LENGTH_SHORT).show();
                String message1 = "Twoja wydajność: " + wydajnosc + "%";
                tv_twoja_wydajnosc.setText(message1);

            }
        });
        wymaganaWydajnosc( ileDoWYkonania,  maxWykonanych);
        odleglosc(odleglosc);

        return view;

    }
    public void wymaganaWydajnosc(int ileDoWYkonania, int maxWykonanych){
        double wymaganaWydajnosc = (int)(((double)ileDoWYkonania/(double)maxWykonanych) * 100);
        String message2 = "Wymagana wydajność: " + wymaganaWydajnosc + "%";
        tv_wymagana_wydajnosc.setText(message2);
    }

    public void odleglosc(int odleglosc){
        String message3 = "Odległość: " + odleglosc + " m";
        tv_dystans.setText(message3);
    }


}
