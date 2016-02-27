package org.oway_team.oway;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.oway_team.oway.json.JSONNavigationItem;

import java.util.List;

public class MainActivity extends Activity /*AppCompatActivity*/ {
    AddItemsFragment mAddItemsFragment;
    MapsFragment mMapsFragment;

    private static final String TAG = "OWay-Main";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAddItemsFragment = new AddItemsFragment();
        mMapsFragment = new MapsFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_fragment_holder, mMapsFragment);
        fragmentTransaction.hide(mMapsFragment);
        fragmentTransaction.add(R.id.main_fragment_holder, mAddItemsFragment);

        fragmentTransaction.commit();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onRouteRequested(List<JSONNavigationItem>items) {
        Log.d(TAG, "Route requested");
        //TODO: Some api interaction
        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.replace(R.id.main_fragment_holder, mMapsFragment);
        ft.show(mMapsFragment);
        ft.hide(mAddItemsFragment);
        ft.addToBackStack(null);
        ft.commit();
        mMapsFragment.postPoints(items);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
