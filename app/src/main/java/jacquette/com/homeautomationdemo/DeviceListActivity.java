package jacquette.com.homeautomationdemo;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public class DeviceListActivity extends Activity
        implements DeviceListFragment.Callbacks {

    // whether or not the app is in two-pane mode
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        if (findViewById(R.id.device_detail_container) != null) {
            mTwoPane = true;

            ((DeviceListFragment) getFragmentManager()
                    .findFragmentById(R.id.device_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(DeviceDetailFragment.ARG_ITEM_ID, id);
            DeviceDetailFragment fragment = new DeviceDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.device_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, DeviceDetailActivity.class);
            detailIntent.putExtra(DeviceDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
