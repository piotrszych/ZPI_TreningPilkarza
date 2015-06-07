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

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import zip.android.treningpilkarski.logika.DataKeys;
import zip.android.treningpilkarski.logika.DataProvider;
import zip.android.treningpilkarski.logika.database.JSONParser;
import zip.android.treningpilkarski.logika.database.interfaces.ICommWithDB;

/**
 * Created by Piotr on 2015-06-06.
 */
public class ATaskAfterExercise extends AsyncTask<String, String, String> {

    //progress dialog
    private ProgressDialog _dialogbox;

    //JSON finals
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TABLE_NAME = "cwiczenie_uzytkownika";

    //JSON things
    private JSONParser _json_parser = new JSONParser();
    JSONArray jsonArray = null;

    int i_old_id;
    int i_exer_id;
    int i_how_many;
    int i_user_id;
    Date date_data_kolejnego_cwiczenia;

    ICommWithDB comm;
    Context _internal_context;
    HashMap<String, Integer> _hashmap_toreturn = null;

    public ATaskAfterExercise(Context context, ICommWithDB comm, int user_id, int old_id, int exer_id, int how_many, Date data_kolejnego_cwiczenia)
    {
        this._internal_context = context;
        this.comm = comm;
        this.i_old_id = old_id;
        this.i_exer_id = exer_id;
        this.i_how_many = how_many;
        this.i_user_id = user_id;
        this.date_data_kolejnego_cwiczenia = data_kolejnego_cwiczenia;
    }

    @Override
    protected void onPreExecute() {
        _dialogbox = new ProgressDialog(_internal_context);
        _dialogbox.setMessage("Zapisuję ćwiczenie...");
        _dialogbox.setCancelable(false);
        _dialogbox.setIndeterminate(false);
        _dialogbox.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... args) {
        _hashmap_toreturn = new HashMap<>();

        //dodajemy parametry
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/insert_exercise.php";
        params.add(new BasicNameValuePair("id_cwiczenia", "" + i_exer_id));
        params.add(new BasicNameValuePair("id_uzytkownika", "" + i_user_id));
        params.add(new BasicNameValuePair("data", "" + date_data_kolejnego_cwiczenia.toString()));
        params.add(new BasicNameValuePair("ilosc_wykonanych", "" + i_how_many));

        if (DataProvider.isNetworkAvailable(_internal_context)) {
            Log.d(getClass().getSimpleName(), "isNetworkAvaileable - true");

            JSONObject json = _json_parser.makeHttpRequest(url, "POST", params);

            try {
                //sprawdzamy w logcat, co dostalismy
                Log.d(this.getClass().getSimpleName() + " got", json.toString());

                int i_success = json.getInt(TAG_SUCCESS);
                if(i_success == 1)
                {
                    List<NameValuePair> params_update = new ArrayList<>();
                    String url_update = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/update_exercise_simple_ps.php";
                    params_update.add(new BasicNameValuePair("id", "" + i_old_id));

                    JSONObject json_update = _json_parser.makeHttpRequest(url_update, "POST", params_update);
                    int i_success_update = json_update.getInt(TAG_SUCCESS);
                    _hashmap_toreturn.put("success_update", i_success_update);
                }
                _hashmap_toreturn.put("success", i_success);


            } catch (JSONException e) {
                e.printStackTrace();
                _hashmap_toreturn.put("error", -1);
            } catch (NullPointerException nptr) {
                nptr.printStackTrace();
                Log.d(getClass().getSimpleName(), "Nullpointer - no JSON");
                _hashmap_toreturn.put("error", -2);
                //_hashmap_toreturn = new HashMap<>();
                //_hashmap_toreturn.put("error", "Nie można nawiązać połączenia");
            }
        } else {
            Log.d(getClass().getSimpleName(), "isNetworkAvaileable - false");
            _hashmap_toreturn.put("error", -10);    //no network
        }
        _hashmap_toreturn.put("type", 2);

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        comm.notifyActivity(_hashmap_toreturn);
        _dialogbox.dismiss();
    }
}
