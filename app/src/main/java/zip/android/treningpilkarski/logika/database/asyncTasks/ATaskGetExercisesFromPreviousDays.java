package zip.android.treningpilkarski.logika.database.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import zip.android.treningpilkarski.logika.DataKeys;
import zip.android.treningpilkarski.logika.DataProvider;
import zip.android.treningpilkarski.logika.database.JSONParser;
import zip.android.treningpilkarski.logika.database.interfaces.ICommWithDB;

/**
 * Created by Piotr on 2015-06-04.
 */
public class ATaskGetExercisesFromPreviousDays extends AsyncTask<String, String, String>
{
    //JSON finals
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TABLE_NAME = "cwiczenie_uzytkownika";

    //JSON things
    private JSONParser _json_parser = new JSONParser();
    JSONArray jsonArray = null;

    int i_user_id;
    int i_how_many_days;
    ICommWithDB<ArrayList<HashMap<String, String>>> comm;
    Context _internal_context;
    ArrayList<HashMap<String, String>> _alist_hashmap_toreturn = null;

    public ATaskGetExercisesFromPreviousDays(Context context, ICommWithDB<ArrayList<HashMap<String, String>>> comm, int user_id, int howManyDays)
    {
        this._internal_context = context;
        this.comm = comm;
        this.i_user_id = user_id;
        this.i_how_many_days = howManyDays;
        _alist_hashmap_toreturn = new ArrayList<>();
    }


    public ATaskGetExercisesFromPreviousDays(Context context, ICommWithDB<ArrayList<HashMap<String, String>>> comm, int user_id)
    {
        //to samo co wyzej, ale dla tygodnia (domyslne)
        this(context, comm, user_id, 7);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... args) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        String url_all_products = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/read_exercises_historic_ps.php";
        params.add(new BasicNameValuePair("userID", "" + i_user_id));
        params.add(new BasicNameValuePair("interval", "" + i_how_many_days));

        if(DataProvider.isNetworkAvailable(_internal_context)) {
            JSONObject json = _json_parser.makeHttpRequest(url_all_products, "GET", params);

            try {
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json == null ? "NONE" : json.toString());
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                //pDialog.setMessage("mcvodfdn");
                //pDialog.show();

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    jsonArray = json.getJSONArray(TAG_TABLE_NAME);
                    // looping through All Products
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject temp_jsonObject = jsonArray.getJSONObject(i);

                        // Storing each json item in variable
                        // najpierw pobieramy stringi, nawet w miejsce intow, potem poparsujemy
                        String id_cwiczenia = temp_jsonObject.getString("id_cwiczenia");
                        String id = temp_jsonObject.getString("id");
                        String data_wykonania = temp_jsonObject.getString("data_wykonania");
                        String ilosc_wykonanych = temp_jsonObject.getString("ilosc_wykonanych");
                        String czy_wykonane = temp_jsonObject.getString("czy_wykonane");
                        String ilosc_do_wykonania = temp_jsonObject.getString("ilosc_do_wykonania");
                        String odleglosc = temp_jsonObject.getString("odleglosc");
                        String id_strony = temp_jsonObject.getString("id_strony");

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put("id", id);
                        map.put("id_cwiczenia", id_cwiczenia);
                        map.put("data_wykonania", data_wykonania);
                        map.put("ilosc_wykonanych", ilosc_wykonanych);
                        map.put("czy_wykonane", czy_wykonane);
                        map.put("ilosc_do_wykonania", ilosc_do_wykonania);
                        map.put("odleglosc", odleglosc);
                        map.put("id_strony", id_strony);

                        // adding HashMap to ArrayList
                        _alist_hashmap_toreturn.add(map);
                    }
                } else {
                    //TODO do something if success is not 0
                }
            } catch (JSONException e) {
                e.printStackTrace();
                HashMap<String, String> map = new HashMap<>();
                map.put("error", "JSON exception w " + getClass().getSimpleName() + "!");
                _alist_hashmap_toreturn.add(map);
            } catch (NullPointerException nptrexception) {
                nptrexception.printStackTrace();
                HashMap<String, String> map = new HashMap<>();
                map.put("error", "Żadnych rekordów: nullpointer w JSON!" );
                _alist_hashmap_toreturn.add(map);
            }
        }
        else
        {
            //TODO nie ma sieci
            Log.d(this.getClass().getSimpleName(), "NO NETWORK");
            HashMap<String, String> map = new HashMap<>();
            map.put("error", "Brak sieci!" );
            _alist_hashmap_toreturn.add(map);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        comm.notifyActivity(_alist_hashmap_toreturn);
    }
}
