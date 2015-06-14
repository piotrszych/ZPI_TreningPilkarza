package zip.android.treningpilkarski;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Locale;

import zip.android.treningpilkarski.logika.DataKeys;
import zip.android.treningpilkarski.logika.DataProvider;
import zip.android.treningpilkarski.logika.database.asyncTasks.ATaskGetExercisesNotDone;
import zip.android.treningpilkarski.logika.database.interfaces.ICommWithDB;
import zip.android.treningpilkarski.main_fragments.InfoFragment;
import zip.android.treningpilkarski.main_fragments.ListFragment;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener, ICommWithDB<Integer> {

    SectionsPagerAdapter mSectionsPagerAdapter;
    InfoFragment frag_infoFragment;
    SharedPreferences sharedPreferences;

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/
        DataProvider.initializeTypefaces(getAssets());

        sharedPreferences = getSharedPreferences(DataKeys.S_SHAREDPREFERENCES_NAME_USER, Context.MODE_PRIVATE);

        int user_id = getIntent().getBundleExtra(DataKeys.BUNDLE_NAME_USER).getInt(DataKeys.BUNDLE_KEY_USERID);
        ATaskGetExercisesNotDone atask_checkNotDone = new ATaskGetExercisesNotDone(this, this, user_id);
        atask_checkNotDone.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.item_main_admin) {
            startActivity(new Intent(getApplicationContext(), AdminActivity.class));
        }
        if (id == R.id.item_main_logout)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(DataKeys.S_KEY_PASSWORD);
            editor.remove(DataKeys.S_KEY_AUTOLOGIN);
            editor.apply();

            setResult(1);

            super.onBackPressed();
        }
        if (id == R.id.item_main_refresh_listfragment)
        {
            ListFragment listFragment = (ListFragment) mSectionsPagerAdapter.getItem(1);
            listFragment.refreshList();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    private static long back_pressed;
    @Override
    public void onBackPressed(){
        if (back_pressed + 2000 > System.currentTimeMillis()){
            setResult(1);
            finish();
        }
        else{
            Toast.makeText(getBaseContext(), getString(R.string.exit_warning), Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    private void loadPagerAdapter()
    {
// Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getIntent().getBundleExtra(DataKeys.BUNDLE_NAME_USER));
        //frag_infoFragment = new InfoFragment();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public void notifyActivity(Integer objectSent) {
//        Toast.makeText(this, "Got in main: " + objectSent, Toast.LENGTH_SHORT).show();
        loadPagerAdapter();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        InfoFragment frag_infoFragment;
        ListFragment frag_listFragment;

        public SectionsPagerAdapter(FragmentManager fm, Bundle args) {
            super(fm);
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            frag_infoFragment = new InfoFragment();
            frag_infoFragment.setArguments(args);
            frag_listFragment = new ListFragment();
            frag_listFragment.setArguments(args);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position)
            {
                case 0:
                    return frag_infoFragment;
                case 1:
                    return frag_listFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages. <- nope, make that two.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.main_title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.main_title_section2).toUpperCase(l);
            }
            return null;
        }
    }
}