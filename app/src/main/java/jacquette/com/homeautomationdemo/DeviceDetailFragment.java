package jacquette.com.homeautomationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import jacquette.com.homeautomationdemo.devices.BinarySwitch;
import jacquette.com.homeautomationdemo.devices.Device;
import jacquette.com.homeautomationdemo.devices.MultiLevelSwitch;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DeviceDetailFragment extends Fragment {

    public static final String TAG = "DeviceDetailFragment";
    public static final String PARCEL = "parcel";

    // views - depending on the layout returned
    @Optional @InjectView(R.id.binary_switch_toggle) Button mBinaryToggle;
    @Optional @InjectView(R.id.multilevel_switch_toggle) Button mMultiLevelToggle;
    @Optional @InjectView(R.id.multilevel_swtich_seekbar) SeekBar mMultiLevelSeekBar;

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
                drawToggleOn(mBinaryToggle);
            } else {
                drawToggleOff(mBinaryToggle);
            }

            mBinaryToggle.setOnClickListener(mBinaryToggleListener);
        } else if (parent.device != null && parent.device instanceof MultiLevelSwitch) {
            rootView = inflater.inflate(R.layout.multilevel_switch_layout, container, false);
            ButterKnife.inject(this, rootView);

            // TODO set seekbar to current level
            if (parent.device.getIsOn()) {
                drawToggleOn(mMultiLevelToggle);
            } else {
                drawToggleOff(mMultiLevelToggle);
            }

            mMultiLevelToggle.setOnClickListener(mMultiLevelListener);
            mMultiLevelSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                private int mStepSize = 5;
                private int mCurrentProgress = 0;

                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    int progress = (Math.round(i / mStepSize)) * mStepSize;

                    // trying to cut down on the network requests a bit
                    if (progress != mCurrentProgress) {
                        seekBar.setProgress(progress);
                        mCurrentProgress = progress;
                        ((MultiLevelSwitch) parent.device).setLevel(parent, mCurrentProgress, new Callback<Void>() {
                            @Override
                            public void success(Void aVoid, Response response) {

                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        } else {
            rootView = inflater.inflate(R.layout.fragment_device_detail, container, false);
        }

        return rootView;
    }

    // device handlers

    /**
     *  The click listener for a binary switch.
     */
    private View.OnClickListener mBinaryToggleListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            final DeviceDetailActivity parent = (DeviceDetailActivity) getActivity();
            Device device = parent.device;
            final boolean oldState = device.getIsOn();

            Callback<Void> callback = new Callback<Void>() {
                @Override
                public void success(Void aVoid, Response response) {
                    if (oldState) {
                        drawToggleOff(mBinaryToggle);
                    } else {
                        drawToggleOn(mBinaryToggle);
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
     *  The click listener for a multilevel switch.
     */
    private View.OnClickListener mMultiLevelListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            final DeviceDetailActivity parent = (DeviceDetailActivity) getActivity();
            Device device = parent.device;
            final boolean oldState = device.getIsOn();

            Callback<Void> callback = new Callback<Void>() {
                @Override
                public void success(Void aVoid, Response response) {
                    if (oldState) {
                        drawToggleOff(mMultiLevelToggle);
                    } else {
                        drawToggleOn(mMultiLevelToggle);
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

    private void drawToggleOn(Button button) {
        button.setText(getString(R.string.binary_switch_on));
        button.setBackgroundColor(getResources()
                .getColor(android.R.color.holo_blue_bright));
    }

    private void drawToggleOff(Button button) {
        button.setText(getString(R.string.binary_switch_off));
        button.setBackgroundColor(getResources()
                .getColor(android.R.color.darker_gray));
    }
}
