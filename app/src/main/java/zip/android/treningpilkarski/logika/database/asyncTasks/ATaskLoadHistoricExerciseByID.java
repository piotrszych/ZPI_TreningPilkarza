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
import zip.android.treningpilkarski.logika.DataProvider;
import zip.android.treningpilkarski.logika.database.JSONParser;
import zip.android.treningpilkarski.logika.database.interfaces.ICommWithDB;

/**
 * Created by Piotr on 2015-06-14.
 */
public class ATaskLoadHistoricExerciseByID extends AsyncTask<String, String, String> {

    private ProgressDialog _dialogbox;

    //JSON finals
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TABLE_NAME = "cwiczenie_uzytkownika";

    //JSON things
    private JSONParser _json_parser = new JSONParser();
    JSONArray jsonArray = null;

    int i_user_id;
    int i_exercise_id;
    ICommWithDB<ArrayList<HashMap<String, String>>> comm;
    Context _internal_context;
    ArrayList<HashMap<String, String>> _alist_hashmap_toreturn = null;

    public ATaskLoadHistoricExerciseByID(Context context, ICommWithDB<ArrayList<HashMap<String, String>>> comm, int userID, int exerciseID)
    {
        this._internal_context = context;
        this.comm = comm;
        this.i_user_id = userID;
        this.i_exercise_id = exerciseID;
        _alist_hashmap_toreturn = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("atask_type", "SINGLE");
        map.put("exer_id", "" + i_exercise_id);
        _alist_hashmap_toreturn.add(map);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        _dialogbox = new ProgressDialog(_internal_context);
        _dialogbox.setMessage("Wczytuję...");
        _dialogbox.setIndeterminate(false);
        _dialogbox.setCancelable(false);
        _dialogbox.show();

    }

    @Override
    protected String doInBackground(String... args) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        String url_all_products = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/read_exercises_historic_by_id_ps.php";
        params.add(new BasicNameValuePair("userID", "" + i_user_id));
        params.add(new BasicNameValuePair("id_cwiczenia", "" + i_exercise_id));

        if(DataProvider.isNetworkAvailable(_internal_context))
        {
            JSONObject json = _json_parser.makeHttpRequest(url_all_products, "GET", params);
            // Check your log cat for JSON reponse
            Log.d("Historic got: ", json == null ? "NONE" : json.toString());
            // Checking for SUCCESS TAG
            try {
                int success = json.getInt(TAG_SUCCESS);

                if(success == 1)
                {
                    jsonArray = json.getJSONArray(TAG_TABLE_NAME);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject temp_jsonObject = jsonArray.getJSONObject(i);

                        //pobieramy dane
                        String s_data = temp_jsonObject.getString("data_wykonania");
                        String s_ile_zrobil = temp_jsonObject.getString("ilosc_wykonanych");

                        HashMap<String, String> map = new HashMap<>();

                        map.put("data_wykonania", s_data);
                        map.put("ilosc_wykonanych", s_ile_zrobil);

                        _alist_hashmap_toreturn.add(map);
                    }
                }
                else
                {
                    //TODO nie mamy sukcesu; ogarnac
                }

            } catch (JSONException e) {
                e.printStackTrace();
                //TODO error przekazac
            }
            catch (NullPointerException nptr)
            {
                nptr.printStackTrace();
                //TODO nullpointer ogarnac
            }
            //pDialog.setMessage("mcvodfdn");
            //pDialog.show();
        }
        else
        {
            //TODO nie ma neta
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        _dialogbox.dismiss();
        comm.notifyActivity(_alist_hashmap_toreturn);
    }
}
