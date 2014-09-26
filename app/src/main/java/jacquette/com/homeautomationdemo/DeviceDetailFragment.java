package jacquette.com.homeautomationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import jacquette.com.homeautomationdemo.devices.BinarySwitch;
import jacquette.com.homeautomationdemo.devices.Device;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DeviceDetailFragment extends Fragment {

    public static final String TAG = "DeviceDetailFragment";
    public static final String PARCEL = "parcel";

    // views - depending on the layout returned
    @Optional @InjectView(R.id.binary_switch_toggle) Button mBinaryToggle;

    public static DeviceDetailFragment newInstance() {
        DeviceDetailFragment fragment = new DeviceDetailFragment();
        return fragment;
    }

    public DeviceDetailFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate view depending on type of device
        View rootView;
        final DeviceDetailActivity parent = (DeviceDetailActivity) getActivity();

        if (parent.device != null && parent.device instanceof BinarySwitch) {
            rootView = inflater.inflate(R.layout.binary_switch_layout, container, false);
            ButterKnife.inject(this, rootView);

            if (parent.device.getIsOn()) {
                drawBinaryToggleOn();
            } else {
                drawBinaryToggleOff();
            }

            mBinaryToggle.setOnClickListener(mBinaryClickListener);
        } else {
            rootView = inflater.inflate(R.layout.fragment_device_detail, container, false);
        }

        return rootView;
    }

    // device handlers

    /**
     *  The click listener for a binary switch.
     */
    private View.OnClickListener mBinaryClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            final DeviceDetailActivity parent = (DeviceDetailActivity) getActivity();
            Device device = parent.device;
            final boolean oldState = device.getIsOn();

            Callback<Void> callback = new Callback<Void>() {
                @Override
                public void success(Void aVoid, Response response) {
                    if (oldState) {
                        drawBinaryToggleOff();
                    } else {
                        drawBinaryToggleOn();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast t = Toast.makeText(parent, getString(R.string.communication_error), Toast.LENGTH_SHORT);
                    t.show();
                }
            };

            if (oldState) {
                device.off(parent, callback);
            } else {
                device.on(parent, callback);
            }
        }
    };

    /**
     * Draw the binary toggle button in the On state
     */
    private void drawBinaryToggleOn() {
        mBinaryToggle.setText(getString(R.string.binary_switch_on));
        mBinaryToggle.setBackgroundColor(getResources()
                .getColor(android.R.color.holo_blue_bright));
    }

    /**
     * Draw the binary toggle button in the Off state
     */
    private void drawBinaryToggleOff() {
        mBinaryToggle.setText(getString(R.string.binary_switch_off));
        mBinaryToggle.setBackgroundColor(getResources()
                .getColor(android.R.color.darker_gray));
    }
}
