package zip.android.treningpilkarski;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import zip.android.treningpilkarski.exercise_fragments.HelpFragment;
import zip.android.treningpilkarski.exercise_fragments.SimpleExerciseFragment;
import zip.android.treningpilkarski.exercise_fragments.ZegarFragment;
import zip.android.treningpilkarski.logika.DataKeys;


public class SimpleExerciseActivity extends ActionBarActivity {

    //TODO oprogramowac back button (powrot z helpa)

    //shared preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_exercise);

        //pozbycie sie action bara
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle bundle = getIntent().getExtras();
        //zabezpieczenie - sprawdzamy, czy mamy taki key
        if( bundle.containsKey(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE) )
        {
            if(bundle.getBoolean(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE))
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
            else
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
        }//if( bundle.containsKey(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE) )
        else
        {
            Toast.makeText(getApplicationContext(), "Nie znaleziono intenta!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simple_exercise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
