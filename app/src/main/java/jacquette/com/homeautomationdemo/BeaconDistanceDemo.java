package jacquette.com.homeautomationdemo;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import java.util.List;

import jacquette.com.homeautomationdemo.api.ZApi;
import jacquette.com.homeautomationdemo.api.ZService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BeaconDistanceDemo extends Activity {

    public static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    public static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_distance_demo);

        // enable up
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // attach fragment
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(), PlaceholderFragment.TAG)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public static final String TAG = "RangingFragment";
        public BeaconManager beaconManager;
        private ZService mApi;

        public PlaceholderFragment() { }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);

            // create api
            mApi = ZApi.getApi(getActivity());

            beaconManager.setRangingListener(new BeaconManager.RangingListener() {
                @Override
                public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                    if (beacons.size() > 0) {
                        Beacon closest = beacons.get(0);
                        Utils.Proximity proximity = Utils.computeProximity(closest);

                        /*
                            Note: Hard-coding device ID for demo purposes.
                         */
                        if (proximity == Utils.Proximity.IMMEDIATE) {
                            // turn light on when close
                            mApi.toggleBinarySwitch(4, 255, new Callback<Void>() {
                                @Override
                                public void success(Void aVoid, Response response) {

                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Toast.makeText(getActivity(), "Unable to turn on light", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            // turn light off when not close
                            mApi.toggleBinarySwitch(4, 0, new Callback<Void>() {
                                @Override
                                public void success(Void aVoid, Response response) {

                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Toast.makeText(getActivity(), "Unable to turn off light", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        // turn light off when no beacons in range
                        mApi.toggleBinarySwitch(4, 0, new Callback<Void>() {
                            @Override
                            public void success(Void aVoid, Response response) {

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(getActivity(), "Unable to turn off light", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onStart() {
            super.onStart();

            final BeaconDistanceDemo parent = (BeaconDistanceDemo) getActivity();
            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    try {
                        beaconManager.startRanging(parent.ALL_ESTIMOTE_BEACONS);
                    } catch (RemoteException e) {
                        Toast.makeText(parent, "Unable to start ranging", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public void onStop() {
            super.onStop();

            final BeaconDistanceDemo parent = (BeaconDistanceDemo) getActivity();
            try {
                beaconManager.stopRanging(parent.ALL_ESTIMOTE_BEACONS);
            } catch (RemoteException e) {
                Toast.makeText(parent, "Could not stop ranging", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            beaconManager.disconnect();
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            beaconManager = new BeaconManager(activity);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_beacon_distance_demo, container, false);
            return rootView;
        }
    }
}
