package zip.android.treningpilkarski.exercise_fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;
import zip.android.treningpilkarski.R;
import zip.android.treningpilkarski.SimpleExerciseActivity;
import zip.android.treningpilkarski.logika.DataKeys;
import zip.android.treningpilkarski.logika.DataProvider;
import zip.android.treningpilkarski.logika.database.asyncTasks.ATaskLoadHelpExercise;
import zip.android.treningpilkarski.logika.database.interfaces.ICommWithDB;

public class HelpFragment extends Fragment implements ICommWithDB<HashMap<String, String>> {

    TextView tv_exercise_name;
    TextView tv_description;
    ImageView iv_backarrow;
    GifImageView gif_image;
    int b_from_exercise;

    //TODO przy przechodzeniu z SimpleExerciseFragment nie chowa się klawiatura numeryczna. Ogarnac!

    public HelpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        //pobieranie kontrolek
        tv_exercise_name = (TextView) view.findViewById(R.id.tv_help_exercisename);
        tv_description = (TextView) view.findViewById(R.id.tv_help_description);
        iv_backarrow = (ImageView) view.findViewById(R.id.iv_help_backarrow);
        gif_image = (GifImageView) view.findViewById(R.id.gif_help_gifholder);

        Bundle bundle = getArguments();
        Log.d(getClass().getSimpleName(), bundle.toString());
        b_from_exercise = bundle.getInt(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE);
        int exercise_id = bundle.getInt(DataKeys.BUNDLE_KEY_EXERCISEID);
        Log.d("HelpFragment got", "EXER_ID " + exercise_id);
        ATaskLoadHelpExercise atask = new ATaskLoadHelpExercise(getActivity(), this, exercise_id);
        atask.execute();

        //getDefaultData();

        //dodanie listenerow
        tv_exercise_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackArrowPressed();
            }
        });
        iv_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackArrowPressed();
            }
        });

        //zaznaczanie, ze jestesmy w helpFragment i przyszliśmy z ExerciseFragment
        if(b_from_exercise == 1)
        {
            ((SimpleExerciseActivity) getActivity()).setIfHelp(1, getArguments());
        }
        if(b_from_exercise == 2)
        {
            ((SimpleExerciseActivity) getActivity()).setIfHelp(2, getArguments());
        }

        //ustawianie czcionek
        tv_exercise_name.setTypeface(DataProvider.TYPEFACE_TITLE_REGULAR);
        tv_description.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        return view;
    }//public View onCreateView...

    public void onBackArrowPressed()
    {
        if(b_from_exercise == 1)
        {
            ZegarFragment sef = new ZegarFragment();
            Bundle bundle = getArguments();
            sef.setArguments(bundle);
            ((SimpleExerciseActivity) getActivity()).setIfHelp(0, null);

            getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, sef).commit();
        }
        else if(b_from_exercise == 2)
        {
            SpecExerciseFragment sef = new SpecExerciseFragment();
            Bundle bundle = getArguments();
            sef.setArguments(bundle);
            ((SimpleExerciseActivity) getActivity()).setIfHelp(0, null);

            getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, sef).commit();
        }
        else if(b_from_exercise == 0)
        {
            getActivity().finish();
        }
    }

    public void getDefaultData() {
        tv_exercise_name.setText("Pompki");
        gif_image.setImageResource(R.drawable.exercise_pompki);
        tv_description.setText("    Połóż się płasko na brzuchu.\n" +
                "    Umieść dłonie płasko na ziemi na wysokości barków, troszkę szerzej niż szerokość Twoich barków.\n" +
                "    Utrzymuj ciało w pozycji wyprostowanej.\n" +
                "    Unieś ciało do góry rozprostowując ramiona, wciąż utrzymując ciało w pozycji wyprostowanej. Unikaj tendencji wyginania tułowia do tyłu.\n" +
                "    Ciało powinno opierać się teraz jedynie na dłoniach i palcach stóp, będąc cały czas w pozycji wyprostowanej.\n" +
                "    Kolejne pompki wykonuj poprzez unoszenie i opuszczanie ciała jedynie poprzez zginanie i rozprostowywanie ramion.\n" +
                "    Nie kładź się na ziemi pomiędzy pompkami. Od pierwszej do ostatniej pompki kontakt z podłożem powinny mieć tylko palce u stóp i dłonie.\n");
    }

    @Override
    public void notifyActivity(HashMap<String, String> objectSent) {
        if(objectSent.containsKey("error"))
        {
            //TODO obsluga bledu
            Toast.makeText(getActivity(), objectSent.get("error"), Toast.LENGTH_SHORT).show();
        }
        else
        {
            //wszystko ok; ladujemy cwiczenia
            tv_exercise_name.setText(objectSent.get("nazwa"));
            tv_description.setText(objectSent.get("opis"));
            Bundle bundle = getArguments();
            int exercise_id = bundle.getInt(DataKeys.BUNDLE_KEY_EXERCISEID);

            //            1 - brzuszki
            //            2 - pompki
            //            3 - przysiady
            //            4 - drazek
            //            5 - bieganie

            switch(exercise_id){
                case 1: gif_image.setImageResource(R.drawable.exercise_brzuszki);
                    break;
                case 2: gif_image.setImageResource(R.drawable.exercise_pompki);
                    break;
                case 3: gif_image.setImageResource(R.drawable.exercise_przysiady);
                    break;
                case 4: gif_image.setImageResource(R.drawable.exercise_drazek);
                    break;
                case 5: gif_image.setImageResource(R.drawable.exercise_biegi);
                    break;
                case 6: gif_image.setImageResource(R.drawable.exercise_deska);
                    break;
                case 12:    //strzaly na bramke po ziemi
                    gif_image.setImageResource(R.drawable.strzaly_po_ziemi);
                    break;
                case 13:    //strzaly na bramke z powietrza
                    gif_image.setImageResource(R.drawable.strzaly_z_powietrza);
                    break;
                case 14:    //strzaly na bramke glowa z wyskoku
                    //TODO nie ma
                    break;
                case 15:    //rzuty wolne
                    gif_image.setImageResource(R.drawable.rzuty_wolne);
                    break;
                case 16:    //podania po ziemi
                    gif_image.setImageResource(R.drawable.podania_ziemia);
                    break;
                case 17:    //podania lobem
                    gif_image.setImageResource(R.drawable.podania_lobem);
                    break;
                case 18:    //obrona toczacej sie pilki
                    gif_image.setImageResource(R.drawable.obrona_toczacej);
                    break;
                case 19:    //obrona strzalow na wysokosci klatki piersiowej
                    gif_image.setImageResource(R.drawable.obrona_klatka_piersiowa);
                    break;
                case 20:    //obrona wysokich podan bez wyskoku
                    gif_image.setImageResource(R.drawable.obrona_bez_wyskoku);
                    break;
                case 21:    //obrona z wyskokiem
                    //TODO nie ma
                    break;
                case 22:    //robinsonada
                    gif_image.setImageResource(R.drawable.robinsonada);
                    break;

                default: gif_image.setImageResource(R.drawable.logo);
            }
        }
    }
}
