package jacquette.com.homeautomationdemo.devices;

import android.content.Context;

import retrofit.Callback;

interface IDevice {

    public void on(Context context, Callback<Void> callback);
    public void off(Context context, Callback<Void> callback);
}
