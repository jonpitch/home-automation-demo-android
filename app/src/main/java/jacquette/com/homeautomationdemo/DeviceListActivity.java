package jacquette.com.homeautomationdemo;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import jacquette.com.homeautomationdemo.devices.Device;

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
    public void onItemSelected(Device device) {

        if (mTwoPane) {
            Bundle arguments = new Bundle();

            DeviceDetailFragment fragment = DeviceDetailFragment.newInstance();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.device_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, DeviceDetailActivity.class);
            detailIntent.putExtra(DeviceDetailFragment.PARCEL, device);
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
        } else if (id == R.id.action_beacon_demo) {
            Intent beaconDemo = new Intent(this, BeaconDistanceDemo.class);
            startActivity(beaconDemo);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
