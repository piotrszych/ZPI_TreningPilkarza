package zip.android.treningpilkarski.logika.database.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
 * Created by Piotr on 2015-05-31.
 */
public class ATaskLoadHelpExercise extends AsyncTask<String, String, String> {

    //JSON finals
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TABLE_NAME = "d_cwiczenie";

    //progress dialog
    private ProgressDialog _dialogbox;

    //JSON things
    private JSONParser _json_parser = new JSONParser();
    JSONArray jsonArray = null;

    int i_id_cwiczenia;
    String s_nazwa_cwiczenia;
    String s_opis_cwiczenia;
    ICommWithDB comm;
    Context _internal_context;
    HashMap<String, String> _hashmap_toreturn = null;

    public ATaskLoadHelpExercise(Context context, ICommWithDB comm, int id_cwiczenia)
    {
        this.i_id_cwiczenia = id_cwiczenia;
        this.comm = comm;
        this._internal_context = context;
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
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/read_help_exercise_by_id_ps.php";
        params.add(new BasicNameValuePair("id_cwiczenia", this.i_id_cwiczenia + ""));

        if(DataProvider.isNetworkAvailable(_internal_context)) {
            Log.d(getClass().getSimpleName(), "isNetworkAvaileable - true");

            JSONObject json = _json_parser.makeHttpRequest(url, "GET", params);

            try {
                //sprawdzamy w logcat, co dostalismy
                Log.d(this.getClass().getSimpleName() + " got", json.toString());

                int i_success = json.getInt(TAG_SUCCESS);

                if (i_success == 1) {
                    jsonArray = json.getJSONArray(TAG_TABLE_NAME);

                    JSONObject tempJSONObject = jsonArray.getJSONObject(0);
                    s_nazwa_cwiczenia = tempJSONObject.getString("nazwa");
                    s_opis_cwiczenia = tempJSONObject.getString("opis");

                    _hashmap_toreturn = new HashMap<>();
                    _hashmap_toreturn.put("nazwa", s_nazwa_cwiczenia);
                    _hashmap_toreturn.put("opis", s_opis_cwiczenia);
                }
                else
                {
                    //TODO nie success :(

                    _hashmap_toreturn = new HashMap<>();
                    _hashmap_toreturn.put("error", "JSON success = " + i_success);
                }
            }
            catch (JSONException e_json)
            {
                e_json.printStackTrace();
                _hashmap_toreturn = new HashMap<>();
                _hashmap_toreturn.put("error", "JSON Exception");
            }
            catch (NullPointerException e_nptr)
            {
                e_nptr.printStackTrace();
                _hashmap_toreturn = new HashMap<>();
                _hashmap_toreturn.put("error", "Nullpointer in JSON");
            }
        } //if(DataProvider.isNetworkAvailable(_internal_context))
        else
        {
            _hashmap_toreturn = new HashMap<>();
            _hashmap_toreturn.put("error", "No network availeable");
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        super.onPostExecute(s);
        _dialogbox.dismiss();
        comm.notifyActivity(_hashmap_toreturn);
    }
}
