package jacquette.com.homeautomationdemo;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;

import jacquette.com.homeautomationdemo.devices.BinarySwitch;
import jacquette.com.homeautomationdemo.devices.Device;

public class DeviceDetailActivity extends Activity {

    public int mDeviceId;
    public Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        // TODO get args - two pane mode
        Bundle extras = getIntent().getExtras();
        device = extras.getParcelable(DeviceDetailFragment.PARCEL);
        mDeviceId = device.getId();

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(device.getName());

        // attach fragment
        if (savedInstanceState == null) {
            DeviceDetailFragment fragment = DeviceDetailFragment.newInstance();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.device_detail_container, fragment, fragment.TAG)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, DeviceListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
