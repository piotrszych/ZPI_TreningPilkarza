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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import zip.android.treningpilkarski.logika.DataKeys;
import zip.android.treningpilkarski.logika.DataProvider;
import zip.android.treningpilkarski.logika.database.JSONParser;
import zip.android.treningpilkarski.logika.database.interfaces.ICommWithDB;

/**
 * Created by Karolina_2 on 2015-06-08.
 */
public class ATaskAfterSpecExercise extends AsyncTask<String, String, String>{

    private ProgressDialog dialogBox;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TABLE_NAME = "cwiczenie_uzytkownika";

    //JSON things
    private JSONParser _json_parser = new JSONParser();
    JSONArray jsonArray = null;

    //exercises
    private int oldUserExerciseID;
    private int exerciseID;
    private int userID;
    private int howManyToDo;
    private int side;
    private int distance;
    private int howManyDone;
    private Date nextExerciseDate;
    ICommWithDB db;
    Context context;
    HashMap<String, Integer> exerciseParametersToReturn;

    public ATaskAfterSpecExercise(Context context,
                                  ICommWithDB db,
                                  int oldUserExerciseID,
                                  int howManyDone,
                                  int exerciseID,
                                  int userID,
                                  int howManyToDo,
                                  int side,
                                  int distance,
                                  Date nextExerciseDate)
    {
        this.context = context;
        this.db = db;
        this.oldUserExerciseID = oldUserExerciseID;
        this.exerciseID = exerciseID;
        this.howManyDone = howManyDone;
        this.userID = userID;
        this.howManyToDo = howManyToDo;
        this.side = side;
        this.distance = distance;
        this.nextExerciseDate = nextExerciseDate;
    }

    @Override
    protected void onPreExecute(){
        dialogBox = new ProgressDialog(context);
        dialogBox.setMessage("Zapisuję ćwiczenie...");
        dialogBox.setCancelable(false);
        dialogBox.setIndeterminate(false);
        dialogBox.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... args) {
        exerciseParametersToReturn = new HashMap<>();
        List<NameValuePair> params = new ArrayList<>();
        String url = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/insert_spec_exercise.php";
        params.add(new BasicNameValuePair("id_cwiczenia", "" + exerciseID));
        params.add(new BasicNameValuePair("id_uzytkownika", "" + userID));
        params.add(new BasicNameValuePair("ilosc_do_wykonania", "" + howManyToDo));
        params.add(new BasicNameValuePair("id_strony", "" + side));
        params.add(new BasicNameValuePair("odleglosc", "" + distance));
        params.add(new BasicNameValuePair("data", nextExerciseDate.toString()));
        params.add(new BasicNameValuePair("czy_wykonane", 0 + ""));

        if(DataProvider.isNetworkAvailable(context)){
            Log.d(getClass().getSimpleName(), "isNetworkAvailable - true");
            JSONObject json = _json_parser.makeHttpRequest(url, "POST", params);
            try{
                Log.d(this.getClass().getSimpleName() + " got", json.toString());
                int success = json.getInt(TAG_SUCCESS);
                if(success == 1)
                {
                    List<NameValuePair> params_update = new ArrayList<>();
                    String url_update = "http://" + DataKeys.S_DATABASE_IP_ADDRESS + "/bazaphp/update_exercise.php";
                    params_update.add(new BasicNameValuePair("id", "" + oldUserExerciseID));
                    params_update.add(new BasicNameValuePair("ilosc_wykonanych", "" + howManyDone));
                    JSONObject json_update = _json_parser.makeHttpRequest(url_update, "POST", params_update);
                    int i_success_update = json_update.getInt(TAG_SUCCESS);
                    exerciseParametersToReturn.put("success_update", i_success_update);
                }
                exerciseParametersToReturn.put("success", success);
            }catch(JSONException e){
                e.printStackTrace();
                exerciseParametersToReturn.put("error", -1);
            }catch(NullPointerException e){
                e.printStackTrace();
                Log.d(getClass().getSimpleName(), "Nullpointer - no JSON");
                exerciseParametersToReturn.put("error", -2);
            }
        }
        else {
            Log.d(getClass().getSimpleName(), "isNetworkAvaileable - false");
            exerciseParametersToReturn.put("error", -10);    //no network
        }
        exerciseParametersToReturn.put("type", 2);

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        db.notifyActivity(exerciseParametersToReturn);
        dialogBox.dismiss();
    }
}
