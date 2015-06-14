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
public class ATaskGetExerciseNames extends AsyncTask<String, String, String> {

    private ProgressDialog _dialogbox;

    //JSON finals
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TABLE_NAME = "d_cwiczenie";

    //JSON things
    private JSONParser _json_parser = new JSONParser();
    JSONArray jsonArray = null;

    ICommWithDB<ArrayList<HashMap<String, String>>> comm;
    Context _internal_context;
    int i_user_id;
    ArrayList<HashMap<String, String>> _alist_hashmap_toreturn = null;

    public ATaskGetExerciseNames(Context context, ICommWithDB<ArrayList<HashMap<String, String>>> comm, int userID)
    {
        this._internal_context = context;
        this.comm = comm;
        _alist_hashmap_toreturn = new ArrayList<>();
        this.i_user_id = userID;
        HashMap<String, String> map = new HashMap<>();
        map.put("atask_type", "EXERCISE_NAMES");
        _alist_hashmap_toreturn.add(map);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        _dialogbox = new ProgressDialog(_internal_context);
        _dialogbox.setMessage("Wczytuję nazwy ćwiczeń...");
        _dialogbox.setIndeterminate(false);
        _dialogbox.setCancelable(false);
        _dialogbox.show();
    }

    @Override
    protected String doInBackground(String... args) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        String url_all_products = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/read_exercise_names_ps.php";
        params.add(new BasicNameValuePair("userID", "" + i_user_id));

        if(DataProvider.isNetworkAvailable(_internal_context))
        {
            JSONObject json = _json_parser.makeHttpRequest(url_all_products, "GET", params);
            // Check your log cat for JSON reponse
            Log.d("Historic got: ", json == null ? "NONE" : json.toString());
            // Checking for SUCCESS TAG
            int success = 0;
            try {
                success = json.getInt(TAG_SUCCESS);

                if(success == 1)
                {
                    jsonArray = json.getJSONArray(TAG_TABLE_NAME);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject temp_jsonObject = jsonArray.getJSONObject(i);

                        //pobieramy dane
                        String s_id = temp_jsonObject.getString("id");
                        String s_nazwa = temp_jsonObject.getString("nazwa");

                        HashMap<String, String> map = new HashMap<>();

                        map.put("id", s_id);
                        map.put("nazwa", s_nazwa);

                        _alist_hashmap_toreturn.add(map);
                    }
                }
                else
                {
                    //TODO nie sukces
                }
            } catch (JSONException e) {
                e.printStackTrace();
                //TODO jsonException
            }
            catch (NullPointerException nptr)
            {
                nptr.printStackTrace();
                //TODO nullpointer
            }
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
