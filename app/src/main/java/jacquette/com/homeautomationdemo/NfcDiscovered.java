package jacquette.com.homeautomationdemo;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jacquette.com.homeautomationdemo.api.ZApi;
import jacquette.com.homeautomationdemo.api.ZService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NfcDiscovered extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_discovered);

        // enable up button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // add fragment
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(), PlaceholderFragment.TAG)
                    .commit();
        }
    }

    public void onResume() {
        super.onResume();
        final Context context = this;

        // get the URI from the tag and parse
        Uri uri = getIntent().getData();
        if (uri.getScheme().equals("had")) {
            String typeOfDevice = uri.getHost();
            List<String> pathSegments = uri.getPathSegments();

            if (typeOfDevice.equals("binary")) {
                final int id = Integer.parseInt(pathSegments.get(0));
                final String switchMode = pathSegments.get(1);
                int mode = switchMode.equals("on") ? 255 : 0;

                // perform the action
                ZService api = ZApi.getApi(this);
                api.toggleBinarySwitch(id, mode, new Callback<Void>() {
                    @Override
                    public void success(Void aVoid, Response response) {
                        PlaceholderFragment fragment = (PlaceholderFragment) getFragmentManager()
                                .findFragmentByTag(PlaceholderFragment.TAG);
                        fragment.redraw("Binary Switch " + String.valueOf(id) + " turned " + switchMode);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast t = Toast.makeText(context, "NFC Error", Toast.LENGTH_SHORT);
                        t.show();
                    }
                });
            }
        }

        /*
        NdefMessage[] msgs = null;
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
        }

        // process ndef array
        if (msgs != null && msgs.length > 0) {
            NdefMessage scanned = msgs[0];
        }*/
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
        } else if (id == android.R.id.home) {
            Intent home = new Intent(this, DeviceListActivity.class);
            startActivity(home);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public static final String TAG = "PlaceholderFragment";

        @InjectView(R.id.nfc_tag_result) TextView nfcResult;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_nfc_discovered, container, false);
            ButterKnife.inject(this, rootView);
            return rootView;
        }

        public void redraw(String message) {
            nfcResult.setText(message);
        }
    }
}
