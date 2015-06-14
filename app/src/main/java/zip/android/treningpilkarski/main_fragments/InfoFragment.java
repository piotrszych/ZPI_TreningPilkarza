package zip.android.treningpilkarski.main_fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import zip.android.treningpilkarski.R;
import zip.android.treningpilkarski.logika.DataKeys;
import zip.android.treningpilkarski.logika.DataProvider;
import zip.android.treningpilkarski.logika.database.asyncTasks.ATaskGetExerciseNames;
import zip.android.treningpilkarski.logika.database.asyncTasks.ATaskGetExercisesFromPreviousDays;
import zip.android.treningpilkarski.logika.database.asyncTasks.ATaskLoadHistoricExerciseByID;
import zip.android.treningpilkarski.logika.database.interfaces.ICommWithDB;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment implements ICommWithDB<ArrayList<HashMap<String, String>>> {

    //kontrolki
    TextView tv_name;
    TextView tv_quote;
    TextView tv_date;
    Button button_choose;
    ListView lv_history_holder;
    TextView tv_info_history;
    TextView tv_info_timedate;

    //listy poszczegolnych cwiczen
    ArrayList<HashMap<String, String>> alist_brzuszki;
    ArrayList<HashMap<String, String>> alist_pompki;
    ArrayList<HashMap<String, String>> alist_przysiady;
    ArrayList<HashMap<String, String>> alist_drazek;
    ArrayList<HashMap<String, String>> alist_bieganie;

    int i_userID;
    int i_dialog_id_choosen;
    int[] i_exercise_ids;
    String[] s_exercise_names;

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        //pobieranie dzisiejszej daty
        Date date_today = new Date(Calendar.getInstance().getTimeInMillis());

        i_dialog_id_choosen = 0;

        //pobieranie kontrolek
        tv_name = (TextView) view.findViewById(R.id.tv_info_imie);
        tv_quote = (TextView) view.findViewById(R.id.tv_info_quote);
        //tv_date = (TextView) view.findViewById(R.id.tv_date);
        button_choose = (Button) view.findViewById(R.id.button_info_choose);
        lv_history_holder = (ListView) view.findViewById(R.id.lv_info_historyView);
        tv_info_history = (TextView) view.findViewById(R.id.tv_info_history);
        tv_info_timedate = (TextView) view.findViewById(R.id.tv_info_activity_name);

        tv_name.setText(getString(R.string.info_witaj) + " " + getArguments().getString(DataKeys.BUNDLE_KEY_USERNAME));

        //dodanie cytatu
        tv_quote.setText("\"" + getQuote() + "\"");

        //tv_date.setText("Dziś jest "+date_today.toString()+". \nTwój wymarzony dzień na ćwiczenia!");

        final String s_dialog_tytul = "Wybierz";
        final String[] s_cwiczenia = {"Brzuszki", "Pompki", "Przysiady", "Drążek", "Bieganie"};

        button_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = (AlertDialog) createDialog(s_dialog_tytul, s_exercise_names);
                dialog.show();
            }
        });
        tv_info_history.setText("Brzuszki: " + getString(R.string.info_history));

        //ArrayList<HashMap<String, String>> alist_dummyValues = setDummyValues();

        //TO CO JEST POD TA LINIJKA MA BYC NA KONCU!
        //ustawianie czcionek
//        TextView tv_listitem_history_time = (TextView) view.findViewById(R.id.tv_listitem_history_time);
//        TextView tv_listitem_history_date = (TextView) view.findViewById(R.id.tv_listitem_history_date);
//        tv_listitem_history_date.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
//        tv_listitem_history_time.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        tv_name.setTypeface(DataProvider.TYPEFACE_TITLE_REGULAR);
        tv_quote.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        tv_info_history.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
        tv_info_timedate.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);

        i_userID = getArguments().getInt(DataKeys.BUNDLE_KEY_USERID, -1);
        //ATaskGetExercisesFromPreviousDays atask_getExercises = new ATaskGetExercisesFromPreviousDays(getActivity(), this, i_userid);
        //atask_getExercises.execute();
        ATaskGetExerciseNames atask_getNames = new ATaskGetExerciseNames(getActivity(), this);
        atask_getNames.execute();

        return view;
    }

    public void refreshList()
    {
        int i_userid = getArguments().getInt(DataKeys.BUNDLE_KEY_USERID, -1);
        ATaskGetExercisesFromPreviousDays atask_getExercises = new ATaskGetExercisesFromPreviousDays(getActivity(), this, i_userid);
        atask_getExercises.execute();
        tv_info_history.setText("Brzuszki: " + getString(R.string.info_history));
    }

    /*private ArrayList<HashMap<String, String>> setDummyValues()
    {
        ArrayList<HashMap<String, String>> alist_toreturn = new ArrayList<>();
        HashMap<String, String > dummy1 = new HashMap<>();
        dummy1.put("time", "Czas:");
        dummy1.put("date", "2015-01-01");
        alist_toreturn.add(dummy1);
        HashMap<String, String > dummy2 = new HashMap<>();
        dummy2.put("time", "Ilość:");
        dummy2.put("date", "256");
        alist_toreturn.add(dummy2);

        HashMap<String, String > dummy3 = new HashMap<>();
        dummy3.put("time", "Ilość:");
        dummy3.put("date", "256");
        alist_toreturn.add(dummy3);

        HashMap<String, String > dummy4 = new HashMap<>();
        dummy4.put("time", "Ilość:");
        dummy4.put("date", "256");
        alist_toreturn.add(dummy4);

        HashMap<String, String > dummy5 = new HashMap<>();
        dummy5.put("time", "Ilość:");
        dummy5.put("date", "256");
        alist_toreturn.add(dummy5);

        HashMap<String, String > dummy6 = new HashMap<>();
        dummy6.put("time", "Ilość:");
        dummy6.put("date", "256");
        alist_toreturn.add(dummy6);

        return alist_toreturn;
    }
*/
    private String getQuote()
    {
        String[] quotes = getResources().getStringArray(R.array.info_quotes);
        Random random = new Random();
        return quotes[random.nextInt(quotes.length)];
    }

    //tworzy okienko dialogowe z opcjami do wyboru, pojawiajace sie po wybraniu itemka z listy

    private Dialog createDialog(String title, final String[] s_params)
    {
        //uzywamy getView(), wiec trzeba wywolywac to po createView. Ale wywolujemy to dopiero dla utworzonej listy, wiec raczej bezpieczne
        AlertDialog.Builder dialog = new AlertDialog.Builder(getView().getContext());
        Log.d("createDialog", s_params.toString());
        dialog.setTitle(title).setItems(s_params, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                HistoryListAdapter adapter = null;
//                String toTitle = "";
//                switch (which)
//                {
//                    case 0: //brzuszki
//                        adapter = new HistoryListAdapter(getActivity(), R.layout.history_list_item, alist_brzuszki);
//                        break;
//                    case 1: //pompki
//                        adapter = new HistoryListAdapter(getActivity(), R.layout.history_list_item, alist_pompki);
//                        break;
//                    case 2: //przysiady
//                        adapter = new HistoryListAdapter(getActivity(), R.layout.history_list_item, alist_przysiady);
//                        break;
//                    case 3: //drazek
//                        adapter = new HistoryListAdapter(getActivity(), R.layout.history_list_item, alist_drazek);
//                        break;
//                    case 4: //bieganie
//                        adapter = new HistoryListAdapter(getActivity(), R.layout.history_list_item, alist_bieganie);
//                        break;
//                    default:
//                        //TODO wymyslec, co robic
//                        break;
//                }
//                toTitle += s_params[which] + ": " + getString(R.string.info_history);
//
//                lv_history_holder.setAdapter(adapter);
//                tv_info_history.setText(toTitle);
//
//                try {
//                    finalize();
//                } catch (Throwable throwable) {
//                    throwable.printStackTrace();
//                }
                i_dialog_id_choosen = which;
//                Toast.makeText(getActivity(), "wybrales: " + i_exercise_ids[which] + ", czyli " + s_exercise_names[which], Toast.LENGTH_SHORT).show();
                ATaskLoadHistoricExerciseByID atask_loadExerciseHistory = new ATaskLoadHistoricExerciseByID(getActivity(), InfoFragment.this, i_userID, i_exercise_ids[which] );
                atask_loadExerciseHistory.execute();
            }
        });
        return dialog.create();
    }//private Dialog createDialog(String title, String[] s_params)

    @Override
    public void notifyActivity(ArrayList<HashMap<String, String>> objectSent)
    {
        /*Log.d(getClass().getSimpleName(), objectSent.toString());
        //TODO obsluga bledu
        alist_brzuszki = new ArrayList<>();
        alist_pompki = new ArrayList<>();
        alist_przysiady = new ArrayList<>();
        alist_drazek = new ArrayList<>();
        alist_bieganie = new ArrayList<>();

        ArrayList<HashMap<String, String>> alist_niewykonane = new ArrayList<>();

        for(HashMap<String, String> map : objectSent)
        {
            if(Integer.parseInt(map.get("czy_wykonane")) == 0)
            {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    java.util.Date util_date = format.parse(map.get("data_wykonania"));
                    java.util.Date dateWithoutTime = format.parse(format.format(new java.util.Date()));

                    Log.d("util_date", util_date.toString());
                    Log.d("dateWithoutTime", dateWithoutTime.toString());

                    //TODO linijka nizej nie dziala (if(util_date.before(dateWithoutTime)))
                    if(util_date.before(dateWithoutTime))
                    {
                        alist_niewykonane.add(map);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("niewykonane", alist_niewykonane.toString());

        //TODO nie dziala wykrywanie niewykonanych cwiczen

        //obsluga niewykonanych cwiczen

        for (int i = 0; i < objectSent.size(); i++) {
            HashMap<String, String> hmap_got = objectSent.get(i);
            int id_cwiczenia = Integer.parseInt(hmap_got.get("id_cwiczenia"));
            String s_iloscwykonanych = hmap_got.get("ilosc_wykonanych");
            String s_datawykonania = hmap_got.get("data_wykonania");

            HashMap<String, String> map = new HashMap<>();

            switch (id_cwiczenia)
            {
                case 1: //brzuszki
                    map.put("time", s_iloscwykonanych);
                    map.put("date", s_datawykonania);
                    alist_brzuszki.add(map);
                    break;
                case 2: //pompki
                    map.put("time", s_iloscwykonanych);
                    map.put("date", s_datawykonania);
                    alist_pompki.add(map);
                    break;
                case 3: //przysiady
                    map.put("time", s_iloscwykonanych);
                    map.put("date", s_datawykonania);
                    alist_przysiady.add(map);
                    break;
                case 4: //drazek
                    map.put("time", s_iloscwykonanych);
                    map.put("date", s_datawykonania);
                    alist_drazek.add(map);
                    break;
                case 5: //bieganie
                    map.put("time", s_iloscwykonanych);
                    map.put("date", s_datawykonania);
                    alist_bieganie.add(map);
                    break;
                default:
                    //TODO wymyslec, co robic
                    break;
            }
        }

        HistoryListAdapter adapter = new HistoryListAdapter(getActivity(), R.layout.history_list_item, alist_brzuszki);
        lv_history_holder.setAdapter(adapter);*/

        if(objectSent != null && !objectSent.isEmpty())
        {
            if(objectSent.get(0).get("atask_type").equals("SINGLE"))
            {
                //TODO poprawic, by bylo dobrze
                ArrayList<HashMap<String, String>> alist_foradapter = new ArrayList<>();

                for(int i = 1; i < objectSent.size() - 1; i++)
                {
                    HashMap<String, String> map_toalist = new HashMap<>();
                    HashMap<String, String> map_got_current = objectSent.get(i);
                    HashMap<String, String> map_got_next = objectSent.get(i + 1);
                    map_toalist.put("time", map_got_next.get("ilosc_wykonanych"));
                    map_toalist.put("date", map_got_current.get("data_wykonania"));

                    alist_foradapter.add(map_toalist);
                }

                if(alist_foradapter.isEmpty())
                {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("time", "Brak historii");
                    map.put("date", "");
                    alist_foradapter.add(map);
                }

                HistoryListAdapter adapter = new HistoryListAdapter(getActivity(), R.layout.history_list_item, alist_foradapter);
                lv_history_holder.setAdapter(adapter);

                tv_info_history.setText(s_exercise_names[i_dialog_id_choosen] + ": " + getString(R.string.info_history));
            }
            else if(objectSent.get(0).get("atask_type").equals("EXERCISE_NAMES"))
            {
                //obsluga wczytywania nazw wszystkich cwiczen

                s_exercise_names = new String[objectSent.size() - 1];
                i_exercise_ids = new int[objectSent.size() - 1];

                for(int i = 1; i < objectSent.size(); i++)
                {
                    s_exercise_names[i-1] = objectSent.get(i).get("nazwa");
                    i_exercise_ids[i-1] = Integer.parseInt(objectSent.get(i).get("id"));
                }
                //TODO wywolac ATask dla brzuszkow?
                ATaskLoadHistoricExerciseByID atask_getHistoryByExerID = new ATaskLoadHistoricExerciseByID(getActivity(), this, i_userID, 1);
                atask_getHistoryByExerID.execute();
            }
            else
            {
                Log.d("InfoFragment blad", "niewlasciwy atask_type!");
                Toast.makeText(getActivity(), "Niewlasciwy atask_type w InfoFragment:notifyActivity!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class HistoryListAdapter extends ArrayAdapter<HashMap<String,String>>{

        public HistoryListAdapter(Context context, int resource, List<HashMap<String, String>> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            HashMap<String,String> listItem = getItem(position);
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_list_item, parent, false);
            }

            TextView time = (TextView)convertView.findViewById(R.id.tv_listitem_history_time);
            TextView date = (TextView)convertView.findViewById(R.id.tv_listitem_history_date);

            time.setText(listItem.get("time"));
            time.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);
            date.setText(listItem.get("date"));
            date.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);

            return convertView;
        }

    }

}
