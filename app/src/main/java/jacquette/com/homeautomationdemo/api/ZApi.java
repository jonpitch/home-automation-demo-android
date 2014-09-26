package jacquette.com.homeautomationdemo.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import jacquette.com.homeautomationdemo.SettingsActivity;
import retrofit.RestAdapter;

public class ZApi {

    /**
     * Get an instance of the ZService.
     * @param context
     * @return ZService
     */
    public static ZService getApi(Context context) {
        // get user specified url
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String host = preferences.getString(SettingsActivity.CONTROLLER_IP, "192.168.1.1");

        // configure service
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://" + host + ":8083")
                .build();

        return restAdapter.create(ZService.class);
    }
}
