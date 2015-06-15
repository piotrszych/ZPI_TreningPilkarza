package zip.android.treningpilkarski.main_fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import zip.android.treningpilkarski.R;
import zip.android.treningpilkarski.SimpleExerciseActivity;
import zip.android.treningpilkarski.logika.DataKeys;
import zip.android.treningpilkarski.logika.DataProvider;
import zip.android.treningpilkarski.logika.database.asyncTasks.ATaskLoadTodayExercises;
import zip.android.treningpilkarski.logika.database.interfaces.ICommWithDB;
import zip.android.treningpilkarski.test.TestDataProvider;

//Fragment wyswietlajacy listView

public class ListFragment extends Fragment
        implements ICommWithDB<ArrayList<HashMap<String, String>>>
        , AdapterView.OnItemLongClickListener
        , AdapterView.OnItemClickListener{

    //shared preferences
    SharedPreferences sp_appinternal;
    SharedPreferences sp_adminOptions;

   /*ZMIENNE*/
    protected ListView listView;
    protected ArrayList<String> alist_listValues;
    protected StringsArrayAdapter adapter_custom;
    protected String[] s_listValues;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //wczytanie listy stringow
        loadListValues();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState)
    {
        //pobieramy SharedPreferences
        sp_appinternal = getActivity().getSharedPreferences(DataKeys.S_SHAREDPREFERENCES_NAME_APPINTERNAL, Context.MODE_PRIVATE);
        sp_adminOptions = getActivity().getSharedPreferences(DataKeys.S_SHAREDPREFERENCES_NAME_ADMINOPTIONS, Context.MODE_PRIVATE);

        //bedziemy uzywac view wewnatrz tej metody, wiec tworzymy go najpierw, potem mozemy go zwrocic
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        /*Inizjalizacja zmiennych*/
        listView = (ListView) view.findViewById(R.id.fragment_list);
        alist_listValues = new ArrayList<>();

        Log.d("DataBundle ListFragment", getArguments().toString());
        int i_userid = getArguments().getInt(DataKeys.BUNDLE_KEY_USERID, -1);
        ATaskLoadTodayExercises atask = new ATaskLoadTodayExercises(getActivity(), this, i_userid);
        atask.execute();

        //dodanie wartosci do adaptera, adaptera do listy
        adapter_custom = new StringsArrayAdapter(view.getContext(), R.layout.line_single, alist_listValues);
        listView.setAdapter(adapter_custom);



        return view;
    }//public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState)

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //tworzy okienko dialogowe z opcjami do wyboru, pojawiajace sie po wybraniu itemka z listy
    private Dialog createDialog(String title, String[] s_params)
    {
        //uzywamy getView(), wiec trzeba wywolywac to po createView. Ale wywolujemy to dopiero dla utworzonej listy, wiec raczej bezpieczne
        AlertDialog.Builder dialog = new AlertDialog.Builder(getView().getContext());
        dialog.setTitle(title).setItems(s_params, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
        return dialog.create();
    }//private Dialog createDialog(String title, String[] s_params)

    public void loadListValues()
    {
        s_listValues = TestDataProvider.listfragment_strings;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        refreshList();
        if(requestCode == 10 && data != null)
        {
            int got_data = data.getIntExtra("i_idCwiczenia", -1);
            //Toast.makeText(getView().getContext(), "JESTEM, " + got_data, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void notifyActivity(ArrayList<HashMap<String, String>> objectSent) {

        ArrayList<HashMap<String, String>> _to_use = new ArrayList<>();

        Log.d("ObjectSent", objectSent.toString());

        //'czyscimy' liste z rzeczy, ktore sa juz zrobione
        for(HashMap<String, String> object : objectSent)
        {
            if(Integer.parseInt(object.get("czy_wykonane")) == 0
                    || Integer.parseInt(object.get("id")) == -1
                    || Integer.parseInt(object.get("id")) == -2)
            {
                _to_use.add(object);
            }
        }

        Log.d("ToUse", _to_use.toString());
        if(_to_use.isEmpty())
        {
            if(DataProvider.isNetworkAvailable(getActivity()))
            {
                //jest net
                HashMap<String, String> map = new HashMap<>();
                map.put("id", "-2");
                map.put("nazwa", "Brak ćwiczeń na dziś");
                map.put("czy_wykonane", "0");
                _to_use.add(map);
            }
            else
            {
                //nie ma neta
                HashMap<String, String> map = new HashMap<>();
                map.put("id", "-1");
                map.put("nazwa", "Nie ma połączenia z Internetem!");
                map.put("czy_wykonane", "0");
                _to_use.add(map);
            }
        }

        HashMapArrayAdapter adapter = new HashMapArrayAdapter(getActivity(), R.layout.line_single, _to_use);
        listView.setAdapter(adapter);
        switch (_to_use.get(0).get("id"))
        {
            case "-1":
                //nullptr w json
                listView.setOnItemClickListener(null);
                listView.setOnItemLongClickListener(null);
                break;
            case "-2":
                //brak cwiczen na dzis
                listView.setOnItemClickListener(null);
                listView.setOnItemLongClickListener(null);
                break;
            default:
                // wszystko ok, sa cwiczenia
                listView.setOnItemClickListener(this);
                listView.setOnItemLongClickListener(this);
                break;
        }
    } //public void notifyActivity(ArrayList<HashMap<String, String>> objectSent) {

    public void refreshList()
    {
        int i_userid = getArguments().getInt(DataKeys.BUNDLE_KEY_USERID, -1);
        ATaskLoadTodayExercises atask = new ATaskLoadTodayExercises(getActivity(), this, i_userid);
        atask.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> tag = (HashMap<String, String>) view.getTag();
        if(Integer.parseInt(tag.get("id_cwiczenia")) < 10) {
            //wywolywanie fragmentu z cwiczeniem zwyklym

            Intent intent = new Intent(getView().getContext(), SimpleExerciseActivity.class);
            //sygnalizujemy, ze chcemy fragment cwiczenia, a nie pomocy
            // cwiczenie - 1, help - 0;
            intent.putExtra(DataKeys.BUNDLE_KEY_USERID, getArguments().getInt(DataKeys.BUNDLE_KEY_USERID, -1));
            intent.putExtra(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE, 1);
            intent.putExtra(DataKeys.BUNDLE_KEY_USEREXERCISEID, Integer.parseInt(tag.get("id")));
            intent.putExtra(DataKeys.BUNDLE_KEY_EXERCISEID, Integer.parseInt(tag.get("id_cwiczenia")));
            intent.putExtra(DataKeys.BUNDLE_KEY_EXERCISENAME, tag.get("nazwa"));

            startActivityForResult(intent, 10);
        }
        else
        {
            //wywolywanie fragmentu z cwiczeniem specjalistycznym
            Intent intent = new Intent(getView().getContext(), SimpleExerciseActivity.class);
            intent.putExtra(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE, 2);
            intent.putExtra(DataKeys.BUNDLE_KEY_USEREXERCISEID, Integer.parseInt(tag.get("id")));
            intent.putExtra(DataKeys.BUNDLE_KEY_EXERCISEID, Integer.parseInt(tag.get("id_cwiczenia")));
            intent.putExtra(DataKeys.BUNDLE_KEY_EXERCISENAME, tag.get("nazwa"));

            startActivityForResult(intent, 10);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getView().getContext(), SimpleExerciseActivity.class);
        //sygnalizujemy, ze chcemy fragment pomocy, a nie cwiczenia
        // cwiczenie - true, help - false;
        HashMap<String, String> tag = (HashMap<String, String>) view.getTag();
        intent.putExtra(DataKeys.INTENT_LISTTOEXERCISE_IFEXERCISE, false);
        intent.putExtra(DataKeys.BUNDLE_KEY_USEREXERCISEID, Integer.parseInt(tag.get("id")));
        intent.putExtra(DataKeys.BUNDLE_KEY_EXERCISEID, Integer.parseInt(tag.get("id_cwiczenia")));
        intent.putExtra(DataKeys.BUNDLE_KEY_EXERCISENAME, tag.get("nazwa"));

        Log.d("ListFragment sent", "EXER_ID " + Integer.parseInt(tag.get("id_cwiczenia")));

        startActivityForResult(intent, 10);

        return true;
    }

    //wlasny ArrayAdapter napisany tak, zeby wspolpracowal z list_single.xml
    private class StringsArrayAdapter extends ArrayAdapter<String>
    {
        public StringsArrayAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }//public CustomArrayAdapter(Context context, int resource, List<String> objects)

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            String list_item = getItem(position);

            if(convertView == null)
            {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.line_single, parent, false);
            }

            TextView tv_mainText = (TextView) convertView.findViewById(R.id.firstLine);
            tv_mainText.setText(list_item);
            convertView.setTag(position);
            tv_mainText.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);

            return convertView;
        }//public View getView(int position, View convertView, ViewGroup parent)
    }//private class CustomArrayAdapter extends ArrayAdapter<String>

    //druga wersja ArrayAdaptera - tak, by wspolpracowala z danymi z bazy
    private class HashMapArrayAdapter extends ArrayAdapter< HashMap<String, String> >
    {
        boolean b_enabled_flag;

        public HashMapArrayAdapter(Context context, int resource, List<HashMap<String, String>> objects) {
            super(context, resource, objects);
            b_enabled_flag = true;
        }//public CustomArrayAdapter(Context context, int resource, List<String> objects)
        public HashMapArrayAdapter(Context context, int resource, List<HashMap<String, String>> objects, boolean flag) {
            this(context, resource, objects);
            b_enabled_flag = flag;
        }//public CustomArrayAdapter(Context context, int resource, List<String> objects, boolean flag)
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            HashMap<String, String> list_item = getItem(position);

            if(convertView == null)
            {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.line_single, parent, false);
            }

            TextView tv_mainText = (TextView) convertView.findViewById(R.id.firstLine);
            tv_mainText.setText(list_item.get("nazwa"));
            HashMap<String, String> to_tag_with = new HashMap<>();
            to_tag_with.put("id", "" + list_item.get("id"));
            to_tag_with.put("nazwa", "" + list_item.get("nazwa"));
            to_tag_with.put("id_cwiczenia", "" + list_item.get("id_cwiczenia"));
            convertView.setTag(to_tag_with);
//            convertView.setTag(list_item.get("id"));
//            convertView.setTag(2, list_item.get("nazwa"));
            tv_mainText.setTypeface(DataProvider.TYPEFACE_STANDARD_REGULAR);

            return convertView;
        }//public View getView(int position, View convertView, ViewGroup parent)

        @Override
        public boolean areAllItemsEnabled() {
            return b_enabled_flag;
        }

    }//private class CustomArrayAdapter extends ArrayAdapter<String>
}//public class ListFragment extends Fragment
