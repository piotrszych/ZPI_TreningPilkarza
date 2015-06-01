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

public class ATaskLoginUser extends AsyncTask<String, String, String>
{
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
    ICommWithDB comm;
    Context _internal_context;
    HashMap<String, String> _hashmap_toreturn = null;

    public ATaskLoginUser(Context context, ICommWithDB comm, String login, String password)
    {
        this.s_login = login;
        this.s_password = password;
        this.comm = comm;
        this._internal_context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        _dialogbox = new ProgressDialog(_internal_context);
        _dialogbox.setMessage("Logowanie...");
        _dialogbox.setIndeterminate(false);
        _dialogbox.setCancelable(false);
        _dialogbox.show();

    }

    @Override
    protected String doInBackground(String... args) {
        //dodajemy parametry
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/check_user.php";
        params.add(new BasicNameValuePair("login", "'" + s_login + "'"));
        params.add(new BasicNameValuePair("haslo", "'" + s_password + "'"));

        if(DataProvider.isNetworkAvailable(_internal_context)) {
            Log.d(getClass().getSimpleName(), "isNetworkAvaileable - true");

            JSONObject json = _json_parser.makeHttpRequest(url, "GET", params);

            try {
                //sprawdzamy w logcat, co dostalismy
                Log.d(this.getClass().getSimpleName() + " got", json.toString());

                int i_success = json.getInt(TAG_SUCCESS);

                if (i_success == 1) {
                    jsonArray = json.getJSONArray(TAG_TABLE_NAME);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject tempJSONObject = jsonArray.getJSONObject(i);
                        String username = s_login;
                        String id = tempJSONObject.getString("id");

                        _hashmap_toreturn = new HashMap<>();
                        _hashmap_toreturn.put("id", id);
                        _hashmap_toreturn.put("login", username);

                    }
                } else if (i_success == -1)    //wrong password
                {
                    _hashmap_toreturn = new HashMap<>();
                    _hashmap_toreturn.put("error", "Niepoprawne hasło!");
                } else if (i_success == -2)    //no such user
                {
                    _hashmap_toreturn = new HashMap<>();
                    _hashmap_toreturn.put("error", "Nie ma takiego użytkownika w bazie!");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException nptr) {
                nptr.printStackTrace();
                Log.d(getClass().getSimpleName(), "Nullpointer - no JSON");
                _hashmap_toreturn = new HashMap<>();
                _hashmap_toreturn.put("error", "Nie można nawiązać połączenia");
            }
        }
        else
        {
            Log.d(getClass().getSimpleName(), "isNetworkAvaileable - false");
            _hashmap_toreturn = new HashMap<>();
            _hashmap_toreturn.put("error", "Nie ma dostępnego połączenia!");
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        _dialogbox.dismiss();
        comm.notifyActivity(_hashmap_toreturn);
    }
}
