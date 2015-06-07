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
 * Background Async Task to Load all product by making HTTP Request
 * */

public class ATaskLoadTodayExercises extends AsyncTask<String, String, String> {
    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsList;
    ICommWithDB comm;

    // url to get all products list
    //private static String url_all_products = "http://192.168.0.105/baza/read_sides.php";
    //private static String url_all_products = "http://156.17.37.80/bazaphp/read_today_exercises.php";
    //private static String url_all_products = "http://api.androidhive.info/android_connect/get_all_products.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "cwiczenie_uzytkownika";
    private static final String TAG_PID = "id";
    private static final String TAG_NAME = "name";

    // products JSONArray
    JSONArray products = null;

    private Context _internal_context;
    private int _user_id;

    /**
     * Before starting background thread Show Progress Dialog
     */

    public ATaskLoadTodayExercises(Context context, ICommWithDB comm, int userid)
    {
        productsList = new ArrayList<>();
        this._user_id = userid;
        this.comm = comm;
        this._internal_context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(_internal_context);
        pDialog.setMessage("Wczytuję dzisiejsze ćwiczenia...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }


    /**
     * getting All products from url
     * */
    protected String doInBackground(String... args) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        //String url_all_products = "http://156.17.37.80/bazaphp/read_today_exercises.php";
        String url_all_products = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/read_exercise.php";
        params.add(new BasicNameValuePair("userID", "" + _user_id));

        if(DataProvider.isNetworkAvailable(_internal_context)) {
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);
            //System.out.println("Cos");
            //pDialog.setMessage("Siema");
            //pDialog.show();
            // Check your log cat for JSON reponse
            Log.d("All Products: ", json == null ? "NONE" : json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                //pDialog.setMessage("mcvodfdn");
                //pDialog.show();

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);
                    //pDialog.setMessage(products.length() + "");
                    // looping through All Products
                    Log.d("Error: ", "cos" + products.getJSONObject(0).getString("id") + "");
                    Log.d("Kontrola: ", products.isNull(0) + "");
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString("id");
                        String name = c.getString("nazwa");
                        String exer_id = c.getString("id_cwiczenia");
                        String czy_wykonane = c.getString("czy_wykonane");
                        //pDialog.setMessage(name);
                        //pDialog.show();

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put("id", id);
                        map.put("nazwa", name);
                        map.put("id_cwiczenia", exer_id);
                        map.put("czy_wykonane", czy_wykonane);

                        // adding HashList to ArrayList
                        productsList.add(map);
                    }
                } else {
                    String message = json.getString("message");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("id", "-2");
                    map.put("nazwa", "Brak ćwiczeń na dziś");
                    map.put("czy_wykonane", "0");
                    productsList.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException nptrexception) {
                nptrexception.printStackTrace();
                HashMap<String, String> map = new HashMap<>();
                map.put("id", "-1");
                map.put("nazwa", "Żadnych rekordów: nullpointer w JSON!");
                map.put("czy_wykonane", "0");
                // adding HashList to ArrayList
                productsList.add(map);
            }
        }
        else
        {
            //TODO nie ma sieci
            Log.d(this.getClass().getSimpleName(), "NO NETWORK");
        }
        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(String file_url) {
        pDialog.dismiss();
        comm.notifyActivity(productsList);
    }
}