package jacquette.com.homeautomationdemo.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import jacquette.com.homeautomationdemo.R;
import jacquette.com.homeautomationdemo.devices.Device;

public class DeviceAdapter extends ArrayAdapter<Device> {

    @Optional @InjectView(R.id.device_list_name) TextView mDeviceName;
    @Optional @InjectView(R.id.device_list_status) TextView mDeviceStatus;

    public DeviceAdapter(Context context, int resource, List<Device> devices) {
        super(context, resource, devices);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Device device = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_list_item, parent, false);
        }

        // view injection
        ButterKnife.inject(this, convertView);
        mDeviceName.setText(device.getName());
        mDeviceStatus.setText(device.getIsOn() ? "On" : "Off");

        return convertView;
    }
}
