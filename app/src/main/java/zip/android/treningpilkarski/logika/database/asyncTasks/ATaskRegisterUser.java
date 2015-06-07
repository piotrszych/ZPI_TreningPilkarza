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
 * Created by Karolina_2 on 2015-06-05.
 */

    public class ATaskRegisterUser extends AsyncTask<String, String, String> {
    //JSON finals
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TABLE_NAME = "uzytkownik";

    //progress dialog
    private ProgressDialog _dialogbox;

    //JSON things
    private JSONParser _json_parser = new JSONParser();
    JSONArray jsonArray = null;

    String s_login;
    String s_password;
    int position;
    ICommWithDB comm;
    Context _internal_context;
    //HashMap<String, String> _hashmap_toreturn = null;
    int toReturn = 999;

    public ATaskRegisterUser(Context context, ICommWithDB comm, String login, String password, int position) {
        this.s_login = login;
        this.s_password = password;
        this.comm = comm;
        this._internal_context = context;
        this.position = position;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        _dialogbox = new ProgressDialog(_internal_context);
        _dialogbox.setMessage("Rejestracja...");
        _dialogbox.setIndeterminate(false);
        _dialogbox.setCancelable(false);
        _dialogbox.show();

    }

    @Override
    protected String doInBackground(String... args) {
        //dodajemy parametry
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/register_user.php";
        params.add(new BasicNameValuePair("login", "'" + s_login + "'"));
        params.add(new BasicNameValuePair("haslo", "'" + s_password + "'"));
        params.add(new BasicNameValuePair("pozycja", "'" + position + "'"));

        if (DataProvider.isNetworkAvailable(_internal_context)) {
            Log.d(getClass().getSimpleName(), "isNetworkAvaileable - true");

            JSONObject json = _json_parser.makeHttpRequest(url, "POST", params);

            try {
                //sprawdzamy w logcat, co dostalismy
                Log.d(this.getClass().getSimpleName() + " got", json.toString());

                int i_success = json.getInt(TAG_SUCCESS);
                toReturn = i_success;

                if(i_success == 1) {
                    List<NameValuePair> params_internal = new ArrayList<NameValuePair>();
                    String url_internal = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/read_exercises_by_position.php";
                    params_internal.add(new BasicNameValuePair("pozycja", "" + position));
                    JSONObject json_internal = _json_parser.makeHttpRequest(url_internal, "GET", params_internal);
                    int i_success_internal = json_internal.getInt(TAG_SUCCESS);
                    if (i_success_internal == 1) {
                        JSONArray jsonArr = json_internal.getJSONArray("cwiczenie_pozycji");
                        Log.d("Info", "Yoo" + jsonArr.toString());
                        List<Integer> exercisesList = new ArrayList<>();
                        //int[] array = null;
                        Object[] exercisesArray = exercisesList.toArray();
                        List<NameValuePair> exercisesNVP = new ArrayList<NameValuePair>();
                        String urlExercises = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/insert_first_exercises.php";
                        for (int i = 0; i < jsonArr.length(); i++) {
                            int exercise = jsonArr.getJSONObject(i).getInt("id_cwiczenia");
                            exercisesNVP.add(new BasicNameValuePair("cwiczenia[]", "" + exercise));
                        }
                        exercisesNVP.add(new BasicNameValuePair("login", s_login));
                        JSONObject jsonReq = _json_parser.makeHttpRequest(urlExercises, "POST", exercisesNVP);
                        int success = jsonReq.getInt(TAG_SUCCESS);
                        Log.d("SUKCES", success + "");
                        //System.out.println(jsonArr.toString());
                        //JSONObject tempJSONObjectInternal = jsonArrayInternal.getJSONObject(0);
                        //int i_previous_exercise = Integer.parseInt(tempJSONObjectInternal.getString("ilosc_wykonanych"));
                        //Log.d("PrevExerciseID got", ""+i_previous_exercise);
                        //_hashmap_toreturn.put("Previous", i_previous_exercise);
                        //_hashmap_toreturn.put("ExerID", i_exercise_id);     //TODO zwracac poprawne ID
                    }
                }

                /*if (i_success == 1) {
                    toReturn = i_success;
                    *//*jsonArray = json.getJSONArray(TAG_TABLE_NAME);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject tempJSONObject = jsonArray.getJSONObject(i);
                        String username = s_login;
                        String id = tempJSONObject.getString("id");

                        _hashmap_toreturn = new HashMap<>();
                        _hashmap_toreturn.put("id", id);
                        _hashmap_toreturn.put("login", username);

                    }*//*
                } else if (i_success == -1)    //wrong password
                {
                    toReturn = i_success;
                } else if (i_success == -2)    //no such user
                {
                    _hashmap_toreturn = new HashMap<>();
                    _hashmap_toreturn.put("error", "Nie ma takiego użytkownika w bazie!");
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException nptr) {
                nptr.printStackTrace();
                Log.d(getClass().getSimpleName(), "Nullpointer - no JSON");
                toReturn = -3;
                //_hashmap_toreturn = new HashMap<>();
                //_hashmap_toreturn.put("error", "Nie można nawiązać połączenia");
            }
        } else {
            Log.d(getClass().getSimpleName(), "isNetworkAvaileable - false");
            toReturn = -4;
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        _dialogbox.dismiss();
        comm.notifyActivity(toReturn);
    }
}
