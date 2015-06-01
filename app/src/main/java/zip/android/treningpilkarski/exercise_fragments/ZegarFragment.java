package zip.android.treningpilkarski.exercise_fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import zip.android.treningpilkarski.R;
import zip.android.treningpilkarski.logika.DataKeys;
import zip.android.treningpilkarski.logika.DataProvider;
import zip.android.treningpilkarski.logika.MatmaPodstawowychCwiczen;
import zip.android.treningpilkarski.logika.database.asyncTasks.ATaskGetExerciseByExerciseID;
import zip.android.treningpilkarski.logika.database.interfaces.ICommWithDB;

public class ZegarFragment extends Fragment implements ICommWithDB<HashMap<String, Integer>> {

    SoundPool soundPool;
    int sound;

    ProgressBar progressBar;
    boolean flaga= false;

    //kontrolki
    TextView tv_ilosc_przerwa;
    TextView tv_name;
    TextView tv_serie_left;
    TextView tv_help;
    Button button_rozpocznij;

    int licznik ;
    CountDownTimer countDownTimer;//TODO countDownTimer.cancel() masz to zorvib przy backBattoni gdzie kol;wiek gdzie chcesz zastopowac zegar

    //zmienne, w ktorych przechowujemy result async taska   //TODO oprogramowac bledy neta!
    int i_current;
    int i_previous;
    int i_id_cwiczenia;
    boolean flaga_asyn_tast= false;

    //coby asynctask blokowal activity
    ProgressDialog _dialogbox;

    public ZegarFragment(){
        //Required empty constructor
    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                   Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_zegar, container, false);

        //pobieranie kontrolkow
        tv_name = (TextView) view.findViewById(R.id.tv_exercise_name);
        tv_ilosc_przerwa = (TextView) view.findViewById(R.id.textView);
        tv_serie_left = (TextView) view.findViewById(R.id.tv_exercise_serie_left);
        tv_help = (TextView) view.findViewById(R.id.tv_exercise_help);
        button_rozpocznij = (Button) view.findViewById(R.id.button_exercise_begin);

        Bundle extras = getArguments();
        Log.d("Zegar extras", extras.toString());
        tv_name.setText(extras.getString(DataKeys.BUNDLE_KEY_EXERCISENAME));

        ATaskGetExerciseByExerciseID asyncTask = new ATaskGetExerciseByExerciseID(getActivity(), this, extras.getInt(DataKeys.BUNDLE_KEY_USEREXERCISEID));
        asyncTask.execute();

        progressBar = (ProgressBar)view.findViewById(R.id.progressBarToday);
        progressBar.setVisibility(View.INVISIBLE);

        /**DZWIEK*/
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        sound = soundPool.load(getActivity().getApplicationContext(),R.raw.glass,1);

        //soundPool.play(sound,1f,1f,0,0,1.5f);
        button_rozpocznij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button_rozpocznij.getText().toString().equals(getString(R.string.zegar_button_start))){
                        //cwiczenia();
                    button_rozpocznij.setText(R.string.zegar_button_stop);
                    start();
                    if(flaga) {
                      flaga=false;
                        Toast.makeText(getActivity().getApplicationContext(), "OD NOWA CWICZYSZ", Toast.LENGTH_SHORT).show();
                    }
                    //ukrywanie pomocy
                    tv_help.setVisibility(View.INVISIBLE);
                    tv_help.setClickable(false);
                }
                else {
                    button_rozpocznij.setText(R.string.zegar_button_start);
                    stop();
                    //pokazywanie pomocy
                    tv_help.setVisibility(View.VISIBLE);
                    tv_help.setClickable(true);

                flaga=true;}
                }

        });
       // czekaj(); // sztywna metoda czekajaca 3 sekundy

        //dodanie listenera, by umozliwic przejscie do help
        tv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHelpClicked(v);
            }
        });

        //dodanie czcionek
        tv_name.setTypeface(DataProvider.TYPEFACE_TITLE_REGULAR);
        tv_ilosc_przerwa.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        tv_serie_left.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        tv_help.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        button_rozpocznij.setTypeface(DataProvider.TYPEFACE_STANDARD_BOLD);

        return view;
        // mojTimer();
    }

    public void start(){
        if(flaga_asyn_tast) {
            if (i_id_cwiczenia==10) //TODO  sprwdzenie czy begi
            {
                cwiczenia(300, 30);
                licznik = 9;
            }
            else {
                MatmaPodstawowychCwiczen matmaPodstawowychCwiczen = new MatmaPodstawowychCwiczen();
                matmaPodstawowychCwiczen.run(i_id_cwiczenia,i_current,i_previous);
                Log.d("Zegar", "Curr:" + i_current + ", prev:" + i_previous + ", exerID:" + i_id_cwiczenia);
                int wartosc_powotorzen = matmaPodstawowychCwiczen.getPowtorzenia_do_wykonania();

                if (wartosc_powotorzen <= 10) {
                    cwiczenia(10, 5);
                    licznik = matmaPodstawowychCwiczen.seria.length - 1;
                } else if (wartosc_powotorzen <= 50 && wartosc_powotorzen > 20) {
                    cwiczenia(10, 10);
                    licznik = matmaPodstawowychCwiczen.seria.length - 1;
                } else if (wartosc_powotorzen <=80 && wartosc_powotorzen > 50) {
                    cwiczenia(20, 10);
                    licznik = matmaPodstawowychCwiczen.seria.length  - 1;
                } else if (wartosc_powotorzen <=120 &&wartosc_powotorzen > 80 ) {
                    cwiczenia(30, 10);
                    licznik = matmaPodstawowychCwiczen.seria.length  - 1;
                }
                else if (wartosc_powotorzen <=200 &&wartosc_powotorzen > 120) {
                    cwiczenia(40, 15);
                    licznik = matmaPodstawowychCwiczen.seria.length  - 1;
                }
                else if (wartosc_powotorzen <=300 &&wartosc_powotorzen > 200) {
                    cwiczenia(40, 20);
                    licznik = matmaPodstawowychCwiczen.seria.length  - 1;
                }
                else if (wartosc_powotorzen <=900 &&wartosc_powotorzen > 300) {
                    cwiczenia(50, 30);
                    licznik = matmaPodstawowychCwiczen.seria.length  - 1;
                }
            }
        }
    }

    public void stop(){
       progressBar.setProgress(progressBar.getProgress());
       if(countDownTimer != null) countDownTimer.cancel();

    }

    public void cwiczenia(final int czas,final int czasPrzerwy){

        // int czas = ilosc*1000;
       // Toast.makeText(getActivity().getApplicationContext(),"Cwiczena",Toast.LENGTH_SHORT).show();

        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setMax(czas);
        progressBar.setVisibility(View.VISIBLE);
        tv_ilosc_przerwa.setVisibility(View.VISIBLE);
        tv_ilosc_przerwa.setText("pompki");
       // textView_przerwa.setVisibility(android.view.View.INVISIBLE);
         countDownTimer = new CountDownTimer(progressBar.getMax()*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;

                progressBar.setProgress((int)seconds);
            }

            @Override
            public void onFinish() {
                soundPool.play(sound,1f,1f,0,0,1.5f);
                progressBar.setMax(0);
                if(licznik  > 0) {
                    przerwa(czas,czasPrzerwy);
                }
                    else koniec_cwiczen();


            }
        }.start();
    }


    private void przerwa(final int odCwiczen,final int czasPrzerwy) {
        progressBar.setProgress(0);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setMax(czasPrzerwy);
        progressBar.setVisibility(View.VISIBLE);
        tv_ilosc_przerwa.setVisibility(View.VISIBLE);
        tv_ilosc_przerwa.setText("PRZERWA");
         countDownTimer = new CountDownTimer(progressBar.getMax()*1000,100) {

            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                //ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBarToday);
                progressBar.setProgress(progressBar.getMax()-((int)(seconds/60*100)+(int)(seconds%60)));
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                soundPool.play(sound,1,1,0,0,1);
                licznik--;
                cwiczenia(odCwiczen,czasPrzerwy);

            }
        }.start();
    }

    public void koniec_cwiczen(){
        progressBar.setProgress(0);
        button_rozpocznij.setText(R.string.zegar_button_start);
        Toast.makeText(getActivity().getApplicationContext(), "Koniec cwiczen", Toast.LENGTH_SHORT).show();
        AlertDialog dialog = (AlertDialog) createDialog();
        dialog.show();
        //TODO cancel task?
//        flaga = false;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

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

    @Override
    public void notifyActivity(HashMap<String, Integer> objectSent) {
         i_current = objectSent.get("Current");
         i_previous = objectSent.get("Previous");
       //  i_id_cwiczenia = objectSent.get("ExerID");
        i_id_cwiczenia = 2;
        Log.d("Zegar", "Curr:" + i_current + ", prev:"+i_previous + ", exerID:" + i_id_cwiczenia);
         flaga_asyn_tast = true;

        //konczym ladowanie
//        _dialogbox.dismiss();

    }

    @Override
    public void onResume() {
        super.onResume();
        //pokazywanie progressDialog
//        _dialogbox = new ProgressDialog(getActivity());
//        _dialogbox.setMessage("Przygotowuję ćwiczenie...");
//        _dialogbox.setIndeterminate(false);
//        _dialogbox.setCancelable(false);
//        _dialogbox.show();
    }

    @Override
    public void onPause() {
        super.onPause();
      //  if(countDownTimer != null) countDownTimer.cancel();
        if(_dialogbox != null && _dialogbox.isShowing()) _dialogbox.dismiss();
    }

    public void onHelpClicked(View view)
    {
        HelpFragment sef = new HelpFragment();
        Bundle bundle = new Bundle();
        //przekazywanie wszystkiego, co dostalismy, dalej, by umozliwic powrot
        bundle.putAll(getArguments());
        bundle.remove(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE);
        bundle.putBoolean(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE, true);
        sef.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, sef).commit();
    }

    private Dialog createDialog()
    {
        //uzywamy getView(), wiec trzeba wywolywac to po createView. Ale wywolujemy to dopiero dla utworzonej listy, wiec raczej bezpieczne
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialog.setView(inflater.inflate(R.layout.dialog_layout,null));
        dialog.setTitle("Podsumowanie");
        dialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               //TODO asyntask
            }
        });

        return dialog.create();
    }//private Dialog createDialog(String title, String[] s_params)

}
