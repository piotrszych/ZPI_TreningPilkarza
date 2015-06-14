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

public class ATaskGetExercisesNotDone extends AsyncTask<String, String, String>
{
    private ProgressDialog _dialogbox;

    //JSON finals
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TABLE_NAME = "cwiczenie_uzytkownika";

    //JSON things
    private JSONParser _json_parser = new JSONParser();
    JSONArray jsonArray = null;

    int i_user_id;
    ICommWithDB<Integer> comm;
    Context _internal_context;
    int i_status_toreturn;  //-10 - nie zaczeto, 1 - sukces, 100*sukces - blad sukcesu, -1 - jsonexception, -2 - nullpointer

    public ATaskGetExercisesNotDone(Context context, ICommWithDB<Integer> comm, int user_id)
    {
        this._internal_context = context;
        this.comm = comm;
        this.i_user_id = user_id;
        this.i_status_toreturn = -10;
    }

    @Override
    protected void onPreExecute() {
        _dialogbox = new ProgressDialog(_internal_context);
        _dialogbox.setMessage("Sprawdzam aktualność ćwiczeń...");
        _dialogbox.setCancelable(false);
        _dialogbox.setIndeterminate(false);
        _dialogbox.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... args) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        String url_all_products = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/read_exercises_not_done_ps.php";
        params.add(new BasicNameValuePair("userID", "" + i_user_id));

        if(DataProvider.isNetworkAvailable(_internal_context))
        {
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
                    i_status_toreturn = success;

                    jsonArray = json.getJSONArray(TAG_TABLE_NAME);
                    // looping through All Products
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject temp_jsonObject = jsonArray.getJSONObject(i);

                        // Storing each json item in variable
                        // najpierw pobieramy stringi, nawet w miejsce intow, potem poparsujemy
                        String id = temp_jsonObject.getString("id");
                        String id_cwiczenia = temp_jsonObject.getString("id_cwiczenia");
                        String ilosc_wykonanych = temp_jsonObject.getString("ilosc_wykonanych");

                        //UPDATE OLD EXERCISE
                        List<NameValuePair> params_update = new ArrayList<>();
                        String url_update = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/update_exercise_not_done_ps.php";
                        params_update.add(new BasicNameValuePair("id", "" + id));

                        JSONObject json_update = _json_parser.makeHttpRequest(url_update, "POST", params_update);
                        int i_success_update = json_update.getInt(TAG_SUCCESS);

                        //INSERT NEW EXERCISE
                        //TODO dodac zmienna date
                        List<NameValuePair> params_insert = new ArrayList<>();
                        String url_insert = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/insert_exercise_with_today_date_ps.php";
                        params_insert.add(new BasicNameValuePair("id_cwiczenia", "" + id_cwiczenia));
                        params_insert.add(new BasicNameValuePair("id_uzytkownika", "" + i_user_id));
                        params_insert.add(new BasicNameValuePair("ilosc_wykonanych", "" + ilosc_wykonanych));

                        JSONObject json_insert = _json_parser.makeHttpRequest(url_insert, "POST", params_insert);
                        int i_success_insert = json_insert.getInt(TAG_SUCCESS);

                        if(i_success_insert != 1)
                        {
                            i_status_toreturn = Integer.parseInt(id);
                            Log.d("NotDoneUPT", "Inserting id: " + id + ", i_success_insert = " + i_success_insert);
                        }
                        else if(i_success_update != 1)
                        {
                            i_status_toreturn = 10*i_success_update;
                            Log.d("NotDoneINS", "Updating id: " + id + ", i_success_update = " + i_success_update);
                        }

                    }
                } else {
                    //TEST bardzo test; do niczego to sie na razie nie przydaje
                    i_status_toreturn = success*100;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                i_status_toreturn = -1;
            } catch (NullPointerException nptrexception) {
                nptrexception.printStackTrace();
                i_status_toreturn = -2;
            }
        }
        else
        {
            //TODO nie ma sieci
            Log.d(this.getClass().getSimpleName(), "NO NETWORK");
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("NotDone toreturn", "" + i_status_toreturn);
        _dialogbox.dismiss();
        comm.notifyActivity(i_status_toreturn);
    }
}
