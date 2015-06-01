package zip.android.treningpilkarski.logika;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;

import zip.android.treningpilkarski.test.TestUserData;

public class DataProvider
{
    //customowe fonty
    public static Typeface TYPEFACE_STANDARD_REGULAR;       //standardowa
    public static Typeface TYPEFACE_STANDARD_BOLD;          //standardowa pogrubiona
    public static Typeface TYPEFACE_TITLE_REGULAR;          //do tytulu

    public static void initializeTypefaces(AssetManager assetManager)
    {
        TYPEFACE_STANDARD_REGULAR = Typeface.createFromAsset(assetManager, "fonts/TitilliumWeb.ttf");
        TYPEFACE_STANDARD_BOLD = Typeface.createFromAsset(assetManager, "fonts/TitilliumWeb_Bold.ttf");
        TYPEFACE_TITLE_REGULAR = Typeface.createFromAsset(assetManager, "fonts/StraightNewRegular.ttf");
    }

    //sprawdza, czy jest dostep do internetu
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
