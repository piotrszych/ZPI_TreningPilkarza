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
import zip.android.treningpilkarski.logika.DataKeys;
import zip.android.treningpilkarski.logika.DataProvider;
import zip.android.treningpilkarski.logika.database.asyncTasks.ATaskLoadHelpExercise;
import zip.android.treningpilkarski.logika.database.interfaces.ICommWithDB;

public class HelpFragment extends Fragment implements ICommWithDB<HashMap<String, String>> {

    TextView tv_exercise_name;
    TextView tv_description;
    ImageView iv_backarrow;
    GifImageView gif_image;
    boolean b_from_exercise;

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
        System.out.println("FLAGG");
        System.out.println(bundle.toString());
        b_from_exercise = bundle.getBoolean(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE);
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

        //ustawianie czcionek
        tv_exercise_name.setTypeface(DataProvider.TYPEFACE_TITLE_REGULAR);
        tv_description.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        return view;
    }//public View onCreateView...

    public void onBackArrowPressed()
    {
        if(b_from_exercise)
        {

            //TODO z jakiego cwiczenia przyszlismy? getArguments().getInt(DataKeys.BUNDLE_KEY_USEREXERCISEID);
            ZegarFragment sef = new ZegarFragment();
            Bundle bundle = getArguments();
            sef.setArguments(bundle);

            getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, sef).commit();
        }
        else
        {
            getActivity().finish();
        }
    }

    public void getDefaultData()
    {
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
            //TODO set obrazek
            gif_image.setImageResource(R.drawable.exercise_pompki);
        }
    }
}
