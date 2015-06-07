package zip.android.treningpilkarski.exercise_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import zip.android.treningpilkarski.R;
import zip.android.treningpilkarski.logika.DataKeys;
import zip.android.treningpilkarski.logika.MatmaPodstawowychCwiczen;
import zip.android.treningpilkarski.test.TestDataProvider;
import zip.android.treningpilkarski.test.TestUserData;

public class SimpleExerciseFragment extends Fragment {

    //TODO ogarnac tak, by przy wchodzeniu do aktywnosci nie bylo focusu na edit Text!
    //TODO po rozpoczeciu cwiczenia ukrywac przycisk pomocy?

    TestUserData user;

    TextView tv_exercise_name;
    TextView tv_seria1;
    TextView tv_seria2;
    TextView tv_seria3;
    TextView tv_seria4;
    TextView tv_seria5;
    EditText et_ile_zrobione;
    Button button_ok;
    Button button_fill;
    ImageView iv_help;

    int i_idCwiczenia;
    int i_dzienCwiczenia;
    int i_ostatniaLiczbaPowtorzen;

    public SimpleExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.user = TestDataProvider.currentUser;
        Bundle bundle = getArguments();
        i_idCwiczenia = bundle.getInt("i_idCwiczenia");
        i_dzienCwiczenia = bundle.getInt("i_dzien");
        i_ostatniaLiczbaPowtorzen = bundle.getInt("i_ile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_exercise, container, false);

        //ladowanie kontrolek
        loadControls(view);

        //pobieranie danych z aktywnosci
        this.tv_exercise_name.setText(TestDataProvider.listfragment_strings[i_idCwiczenia]);

        MatmaPodstawowychCwiczen matma = new MatmaPodstawowychCwiczen();

//        final int[] math_result = matma.run(i_dzienCwiczenia, i_ostatniaLiczbaPowtorzen, -1);   //todo
//        tv_seria1.setText("Seria 1: " + math_result[0]);
//        tv_seria2.setText("Seria 2: " + math_result[1]);
//        tv_seria3.setText("Seria 3: " + math_result[2]);
//        tv_seria4.setText("Seria 4: " + math_result[3]);
//        tv_seria5.setText("Seria 5: " + math_result[4]);
//
//        et_ile_zrobione.setText(0 + "");

        //listenery buttonow
        button_fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = 0;
//                result = math_result[4];
                et_ile_zrobione.setText("" +result);
            }
        });

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO DB save exercise as done; get how many did; put new exercise with new date.

                user.setOstatnie(i_idCwiczenia, Integer.parseInt(et_ile_zrobione.getText().toString()));
                user.kolejnyDzien(i_idCwiczenia);
                Intent intent = new Intent();
                intent.putExtra("i_idCwiczenia", i_idCwiczenia);
                getActivity().setResult(10, intent);
                getActivity().finish();
            }
        });

        iv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHelpClicked();
            }
        });

        return view;
    }//public View onCreateView(LayoutInflater inflater, ViewGroup container, ...)

    private void loadControls(View view)
    {
        this.tv_exercise_name = (TextView) view.findViewById(R.id.tv_exercise_name);
        this.tv_seria1 = (TextView) view.findViewById(R.id.tv_seria1);
        this.tv_seria2 = (TextView) view.findViewById(R.id.tv_seria2);
        this.tv_seria3 = (TextView) view.findViewById(R.id.tv_seria3);
        this.tv_seria4 = (TextView) view.findViewById(R.id.tv_seria4);
        this.tv_seria5 = (TextView) view.findViewById(R.id.tv_seria5);
        this.et_ile_zrobione = (EditText) view.findViewById(R.id.et_ile);
        this.iv_help = (ImageView) view.findViewById(R.id.iv_simpleexercise_help);

        button_fill = (Button) view.findViewById(R.id.button_auto);
        button_ok = (Button) view.findViewById(R.id.button_ok);
    }//private void loadControls(View view)

    public void onHelpClicked()
    {
        HelpFragment sef = new HelpFragment();
        Bundle bundle = new Bundle();
        //przekazywanie wszystkiego, co dostalismy, dalej, by umozliwic powrot
        bundle.putAll(getArguments());
        bundle.remove(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE);
        bundle.putInt(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE, 1);
        sef.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, sef).commit();
    }
}
