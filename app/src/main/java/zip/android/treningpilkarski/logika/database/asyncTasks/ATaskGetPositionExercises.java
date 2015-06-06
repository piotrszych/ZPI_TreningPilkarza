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
 * Created by Karolina_2 on 2015-06-06.
 */
public class ATaskGetPositionExercises extends AsyncTask<String, String, String> {

    ProgressDialog _dialogbox;

    //JSON finals
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TABLE_NAME = "cwiczenie_uzytkownika";

    //JSON things
    private JSONParser _json_parser = new JSONParser();
    JSONArray jsonArray = null;

    int positionID;
    ICommWithDB comm;
    Context _internal_context;
    HashMap<String, Integer> _hashmap_toreturn = null;

    public ATaskGetPositionExercises(Context context, ICommWithDB comm, int positionID) {
        this.comm = comm;
        this._internal_context = context;
        this.positionID = positionID;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        _dialogbox = new ProgressDialog(_internal_context);
//        _dialogbox.setMessage("Logowanie...");
//        _dialogbox.setIndeterminate(false);
//        _dialogbox.setCancelable(false);
//        _dialogbox.show();

    }

    @Override
    protected String doInBackground(String... args) {
        //dodajemy parametry
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/read_exercises_by_position.php";
        params.add(new BasicNameValuePair("id_pozycji", "" + positionID));

        JSONObject json = _json_parser.makeHttpRequest(url, "GET", params);
        //sprawdzamy w logcat, co dostalismy
        Log.d(this.getClass().getSimpleName() + " got", json.toString());

        try {
            int i_success = json.getInt(TAG_SUCCESS);
            if (i_success == 1) {
                jsonArray = json.getJSONArray(TAG_TABLE_NAME);

                JSONObject tempJSONObject = jsonArray.getJSONObject(0);
                //int i_current_exercise = tempJSONObject.getString("ilosc_wykonanych") != null ? Integer.parseInt(tempJSONObject.getString("ilosc_wykonanych")) : 0;
                int i_exercise = Integer.parseInt(tempJSONObject.getString("id_cwiczenia"));
                //Log.d("CurExerciseID got", "" + i_current_exercise);
                Log.d("ExerciseID got", "" + i_exercise);

                _hashmap_toreturn = new HashMap<>();
                _hashmap_toreturn.put("Current", i_exercise);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException nptr) {
            nptr.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
//        _dialogbox.dismiss();
        comm.notifyActivity(_hashmap_toreturn);
    }
}
