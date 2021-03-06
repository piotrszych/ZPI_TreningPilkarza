package zip.android.treningpilkarski.logika.database.asyncTasks;

import android.app.ProgressDialog;
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
import zip.android.treningpilkarski.logika.database.JSONParser;
import zip.android.treningpilkarski.logika.database.interfaces.ICommWithDB;

/**
 * Created by Piotr on 2015-05-30.
 */
public class ATaskGetExerciseByExerciseID extends AsyncTask<String, String, String>
{
    //JSON finals
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TABLE_NAME = "cwiczenie_uzytkownika";

    //JSON things
    private JSONParser _json_parser = new JSONParser();
    JSONArray jsonArray = null;

    int i_exercise_id;
    ICommWithDB comm;
    Context _internal_context;
    HashMap<String, Integer> _hashmap_toreturn = null;

    public ATaskGetExerciseByExerciseID(Context context, ICommWithDB comm, int exercise_id)
    {
        this.comm = comm;
        this._internal_context = context;
        this.i_exercise_id = exercise_id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... args) {
        //dodajemy parametry
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/read_exercise_by_id_ps.php";
        params.add(new BasicNameValuePair("id_cwiczenia", "" + i_exercise_id));

        JSONObject json = _json_parser.makeHttpRequest(url, "GET", params);
        //sprawdzamy w logcat, co dostalismy
        Log.d(this.getClass().getSimpleName() + " got", json.toString());

        try {
            int i_success = json.getInt(TAG_SUCCESS);
            if(i_success == 1)
            {
                jsonArray = json.getJSONArray(TAG_TABLE_NAME);

                JSONObject tempJSONObject = jsonArray.getJSONObject(0);
                int i_current_exercise = tempJSONObject.getString("ilosc_wykonanych") != null ? Integer.parseInt(tempJSONObject.getString("ilosc_wykonanych")) : 0;
                int i_exercise = Integer.parseInt(tempJSONObject.getString("id_cwiczenia"));
                int i_interwal = Integer.parseInt(tempJSONObject.getString("interwal"));
                Log.d("CurExerciseID got", ""+i_current_exercise);
                Log.d("ExerciseID got", "" + i_exercise);
                Log.d("Interwal got", "" + i_interwal);

                _hashmap_toreturn = new HashMap<>();
                _hashmap_toreturn.put("Current", i_current_exercise);
                _hashmap_toreturn.put("interwal", i_interwal);

                //pobranie poprzedniego cwiczenia
                List<NameValuePair> params_internal = new ArrayList<NameValuePair>();
                String url_internal = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/read_last_exercise_by_id_ps.php";
                params_internal.add(new BasicNameValuePair("id_cwiczenia", "" + i_exercise));
                JSONObject json_internal = _json_parser.makeHttpRequest(url_internal, "GET", params_internal);
                int i_success_internal = json_internal.getInt(TAG_SUCCESS);
                if(i_success_internal == 1)
                {
                    JSONArray jsonArrayInternal = json_internal.getJSONArray(TAG_TABLE_NAME);
                    JSONObject tempJSONObjectInternal = jsonArrayInternal.getJSONObject(0);
                    String ilosc_wykonanych = tempJSONObjectInternal.getString("ilosc_wykonanych");
                    int i_previous_exercise;
                    if(ilosc_wykonanych.equals("null"))
                    {
                        // blad
                        i_previous_exercise = 0;
                        Log.d("PrevExerciseID got", "" + i_previous_exercise);
                    }
                    else
                    {
                        i_previous_exercise = Integer.parseInt(ilosc_wykonanych);
                        Log.d("PrevExerciseID got", "" + i_previous_exercise);
                    }
                    _hashmap_toreturn.put("Previous", i_previous_exercise);
                    _hashmap_toreturn.put("ExerID", i_exercise_id);     //TODO zwracac poprawne ID
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (NullPointerException nptr)
        {
            nptr.printStackTrace();
        }
        _hashmap_toreturn.put("type", 1);       //pokazujemy, skąd przyszlismy

        return null;
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        comm.notifyActivity(_hashmap_toreturn);
    }
}
