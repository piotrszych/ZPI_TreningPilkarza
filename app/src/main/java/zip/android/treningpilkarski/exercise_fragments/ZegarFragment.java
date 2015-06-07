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
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;

import zip.android.treningpilkarski.R;
import zip.android.treningpilkarski.SimpleExerciseActivity;
import zip.android.treningpilkarski.logika.DataKeys;
import zip.android.treningpilkarski.logika.DataProvider;
import zip.android.treningpilkarski.logika.MatmaPodstawowychCwiczen;
import zip.android.treningpilkarski.logika.database.asyncTasks.ATaskAfterExercise;
import zip.android.treningpilkarski.logika.database.asyncTasks.ATaskGetExerciseByExerciseID;
import zip.android.treningpilkarski.logika.database.interfaces.ICommWithDB;

public class ZegarFragment extends Fragment implements ICommWithDB<HashMap<String, Integer>>, DialogInterface.OnClickListener {

    int i_howmany_done;
    NumberPicker picker;

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
    int i_id_cwiczenia_usera;
    int i_interwal;
    int i_id_cwiczenia_calego;
    boolean flaga_asyn_tast= false;

    //counter ile razy wykonujemy serie
    int i_counter_how_many_series = 0;

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
//                    tv_help.setVisibility(View.INVISIBLE);
//                    tv_help.setClickable(false);
                    tv_help.setText(R.string.zegar_tv_howto_end);
                    tv_help.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            koniec_cwiczen();
                            tv_help.setText(R.string.zegar_tv_howto);
                            tv_help.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onHelpClicked(v);
                                }
                            });
                        }
                    });
                }
                else {
                    button_rozpocznij.setText(R.string.zegar_button_start);
                    i_counter_how_many_series = 0;
                    stop();
                    //pokazywanie pomocy
//                    tv_help.setVisibility(View.VISIBLE);
//                    tv_help.setClickable(true);
                    tv_help.setText(R.string.zegar_tv_howto);
                    tv_help.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onHelpClicked(v);
                        }
                    });

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

        //zaznaczamy, ze jestesmy w zegarze
        ((SimpleExerciseActivity) getActivity()).setIfHelp(false, null);

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
            if (i_id_cwiczenia_calego == 5) //jesli biegi
            {
                cwiczenia(300, 60);
                licznik = i_current > 5 ? 5 : i_current;
            }
            else if(i_id_cwiczenia_calego == 6)       //jesli deska
            {
                cwiczenia(30, 5);
                licznik = i_current > 9 ? 9 : i_current;
            }
            else {
                MatmaPodstawowychCwiczen matmaPodstawowychCwiczen = new MatmaPodstawowychCwiczen();
                matmaPodstawowychCwiczen.run(i_id_cwiczenia_calego, i_current, i_previous);
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
        tv_ilosc_przerwa.setText(getArguments().getString(DataKeys.BUNDLE_KEY_EXERCISENAME));
       // textView_przerwa.setVisibility(android.view.View.INVISIBLE);
         countDownTimer = new CountDownTimer(progressBar.getMax()*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;

                progressBar.setProgress((int)seconds);
            }

            @Override
            public void onFinish() {
                i_counter_how_many_series++;
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
        stop();
        progressBar.setProgress(0);
        button_rozpocznij.setText(R.string.zegar_button_start);
        //Toast.makeText(getActivity().getApplicationContext(), "Koniec cwiczen", Toast.LENGTH_SHORT).show();
        AlertDialog dialog = null;
        Log.d("TestZegar", "" + i_id_cwiczenia_calego);
        switch (i_id_cwiczenia_calego)
        {
            case 5: //bieganiecreateDialogForOtherExercise();
                dialog = (AlertDialog) createDialogForOtherExercise();
                break;
            case 6: //pompki
                dialog = (AlertDialog) createDialogForOtherExercise();
                break;
            default:
                dialog = (AlertDialog) createDialog();
                break;
        }
        if(dialog != null) dialog.show(); else Toast.makeText(getActivity(), "Dialog is null!", Toast.LENGTH_SHORT).show();
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
        if(objectSent.get("type") == 1) //przyjmujemy z ATaskGetExerciseByExerciseID
        {
            if(objectSent.containsKey("error"))
            {
                //TODO error; disable buttons? go back to list? finish Activity?
                //error == -1 -> nullptr
                //error == -2 -> jsonException
            }
            else
            {
                i_current = objectSent.get("Current");
                i_previous = objectSent.get("Previous");
                i_interwal = objectSent.get("interwal");
                i_id_cwiczenia_usera = objectSent.get("ExerID");
                i_id_cwiczenia_calego = objectSent.get("ExerGenerID");
                Log.d("Zegar", "Curr:" + i_current + ", prev:" + i_previous + ", exerUserID:" + i_id_cwiczenia_usera + ", exerGenerID: " + i_id_cwiczenia_calego);
                flaga_asyn_tast = true;
                //konczym ladowanie
//        _dialogbox.dismiss();
            }
        }
        else if(objectSent.get("type") == 2)    //przyjmujemy z ATaskAfterExercise
        {
            Log.d("Zegar from ATaskAfter", objectSent.toString());

            if(objectSent.containsKey("error"))
            {
                //mamy error
                switch(objectSent.get("error"))
                {
                    case -1:
                        //error -1  - JSONException
                        Toast.makeText(getActivity().getApplicationContext(), "JSON Exception podczas dodawania ćwiczenia!", Toast.LENGTH_SHORT).show();
                        break;
                    case -2:
                        //error -2  - NullPointer Exception w ATask
                        Toast.makeText(getActivity().getApplicationContext(), "Nullpointer podczas dodawania ćwiczenia!", Toast.LENGTH_SHORT).show();
                        break;
                    case -10:
                        //error -10 - no network
                        Toast.makeText(getActivity().getApplicationContext(), "Brak połączenia z siecią!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getActivity().getApplicationContext(), "Nieznany błąd, kod: " + objectSent.get("error"), Toast.LENGTH_SHORT).show();
                        break;
                }//switch(objectSent.get("error"))
            }//if(objectSent.containsKey("error"))
            else
            {
                //jest ok, sprawdzamy succes tag
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

        }//else if(objectSent.get("type") == 2)    //przyjmujemy z ATaskAfterExercise

    }//public void notifyActivity(HashMap<String, Integer> objectSent) {

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
        bundle.putInt(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE, 1);
        sef.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.simple_exercise_container, sef).commit();
    }

    private Dialog createDialog()
    {
        //uzywamy getView(), wiec trzeba wywolywac to po createView. Ale wywolujemy to dopiero dla utworzonej listy, wiec raczej bezpieczne
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_layout, null);
        dialog.setView(v);
        dialog.setTitle("Podsumowanie");
        TextView tv = (TextView) v.findViewById(R.id.exercise_dialog_howmany_label);
        tv.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        picker = (NumberPicker) v.findViewById(R.id.exercise_dialog_numberPicker);
        picker.setMinValue(0);
        picker.setMaxValue(1000);
        picker.setValue(1);     //TODO ustawic dobry value pickera - w zaleznosci od matmy
        dialog.setPositiveButton("OK", this);

        return dialog.create();
    }//private Dialog createDialog(String title, String[] s_params)

    private Dialog createDialogForOtherExercise()
    {
//        if(i_id_cwiczenia_calego == 5)
//        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_another_exercises_fragment, null);
            dialog.setView(v);
            dialog.setTitle("Podsumowanie");
            TextView tv = (TextView) v.findViewById(R.id.dialog_another_exe_number);
            TextView tv_howmany_label = (TextView) v.findViewById(R.id.exercise_dialog_howmany_label);
            TextView tv_howmany_series_label = (TextView) v.findViewById(R.id.textView3);

            tv.setText("" + i_counter_how_many_series);

            tv.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
            tv_howmany_label.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
            tv_howmany_series_label.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);

            dialog.setPositiveButton("OK", this);
//        }
//        else if(i_id_cwiczenia_calego == 6)
//        {
//            //TODO deska
//        }
        return dialog.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        //ustawianie daty kolejnego cwiczenia
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, i_interwal);
        Date data = new Date(calendar.getTimeInMillis());

        Log.d("Zegar date", data.toString());
        int i_how_many = 0;

        switch (i_id_cwiczenia_calego)
        {
            case 5: //biegi
            case 6: //deska
                Log.d("Zegar stop", "" + i_counter_how_many_series);
                i_how_many = i_counter_how_many_series;
                break;
            default:
                i_how_many = picker.getValue();
                Log.d("Zegar picker", ""+i_how_many);
                break;
        }

        ATaskAfterExercise aTaskAfterExercise = new ATaskAfterExercise(getActivity()
                , ZegarFragment.this
                , getArguments().getInt(DataKeys.BUNDLE_KEY_USERID)
                , getArguments().getInt(DataKeys.BUNDLE_KEY_USEREXERCISEID)
                , getArguments().getInt(DataKeys.BUNDLE_KEY_EXERCISEID)
                , i_how_many
                , data);
        aTaskAfterExercise.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }
}
