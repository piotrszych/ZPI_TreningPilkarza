package zip.android.treningpilkarski;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import zip.android.treningpilkarski.exercise_fragments.HelpFragment;
import zip.android.treningpilkarski.exercise_fragments.SpecExerciseFragment;
import zip.android.treningpilkarski.exercise_fragments.ZegarFragment;
import zip.android.treningpilkarski.logika.DataKeys;


public class SimpleExerciseActivity extends ActionBarActivity {

    int b_if_help = 0;
    Bundle bundle_if_help = null;

    //shared preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_exercise);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //pozbycie sie action bara
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle bundle = getIntent().getExtras();
        //zabezpieczenie - sprawdzamy, czy mamy taki key
        if( bundle.containsKey(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE) )
        {
            if(bundle.getInt(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE) == 1)
            {

//                SimpleExerciseFragment fragment = new SimpleExerciseFragment();
                ZegarFragment fragment = new ZegarFragment();
                //przesylanie bundle'a dalej
                fragment.setArguments(bundle);

                if (savedInstanceState == null) {
                    getFragmentManager().beginTransaction()
                            .add(R.id.simple_exercise_container, fragment)
                            .commit();
                }
            }
            else if(bundle.getInt(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE) == 0)
            {
                HelpFragment fragment = new HelpFragment();
                //przesylanie bundla dalej -
                fragment.setArguments(bundle);
                if (savedInstanceState == null) {
                    getFragmentManager().beginTransaction()
                            .add(R.id.simple_exercise_container, fragment)
                            .commit();
                }
            }
            else if(bundle.getInt(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE) == 2)
            {
                //specjalistyczne cwiczenie~
                SpecExerciseFragment fragment = new SpecExerciseFragment();
                fragment.setArguments(bundle);
                if (savedInstanceState == null) {
                    getFragmentManager().beginTransaction()
                            .add(R.id.simple_exercise_container, fragment)
                            .commit();
                }

            }
        }//if( bundle.containsKey(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE) )
        else
        {
            Toast.makeText(getApplicationContext(), "Nie znaleziono intenta!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void setIfHelp(int value, Bundle bundle)
    {
        this.b_if_help = value;
        this.bundle_if_help = bundle;
    }

    @Override
    public void onBackPressed() {
        if(b_if_help == 1)
        {
            ZegarFragment sef = new ZegarFragment();
            sef.setArguments(bundle_if_help);
            setIfHelp(0, null);

            getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, sef).commit();
        }
        else if(b_if_help == 2)
        {
            SpecExerciseFragment sef = new SpecExerciseFragment();
            sef.setArguments(bundle_if_help);
            setIfHelp(0, null);

            getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, sef).commit();
        }
        else {
            super.onBackPressed();
        }
    }
}
