package jacquette.com.homeautomationdemo;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import jacquette.com.homeautomationdemo.api.ZApi;
import jacquette.com.homeautomationdemo.api.ZService;
import jacquette.com.homeautomationdemo.api.response.DevicesResponse;
import jacquette.com.homeautomationdemo.devices.BinarySwitch;
import jacquette.com.homeautomationdemo.devices.Device;
import jacquette.com.homeautomationdemo.ui.DeviceAdapter;

public class DeviceListFragment extends ListFragment {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Callbacks mCallbacks = mDeviceCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private List<Device> mDevices = new ArrayList<Device>();

    public interface Callbacks {
        public void onItemSelected(Device device);
    }

    private static Callbacks mDeviceCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(Device device) { }
    };

    public DeviceListFragment() { }

    public static DeviceListFragment newInstance() {
        DeviceListFragment fragment = new DeviceListFragment();
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DeviceListTask task = new DeviceListTask(getActivity());
        task.execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = mDeviceCallbacks;
        mDevices.clear();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * Fetch the list of available devices
     */
    private class DeviceListTask extends AsyncTask<Void, Void, DevicesResponse> {

        private Context mContext;

        public DeviceListTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected DevicesResponse doInBackground(Void... voids) {
            ZService api = ZApi.getApi(mContext);
            return api.getDevices();
        }

        @Override
        protected void onPostExecute(DevicesResponse response) {
            if (mDeviceCallbacks != null) {
                // get list of devices
                for (DevicesResponse.Device d : response.data.devices) {
                    if (d.deviceType.equals("switchBinary")) {
                        boolean on = d.metrics.level.equals("off") ? false : true;
                        String[] parts = d.metrics.title.split(" ");
                        String[] idParts = parts[1].split(":");

                        mDevices.add(new BinarySwitch(Integer.valueOf(idParts[0]), d.metrics.title, on));
                    }
                }

                setListAdapter(new DeviceAdapter(getActivity(), R.layout.device_list_item, mDevices));
                getListView().deferNotifyDataSetChanged();
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        mCallbacks.onItemSelected(mDevices.get(position));
    }
}
