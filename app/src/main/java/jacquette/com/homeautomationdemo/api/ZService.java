package jacquette.com.homeautomationdemo.api;

import jacquette.com.homeautomationdemo.api.response.DevicesResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface ZService {

    /**
     * Get paired ZWave devices
     */
    @GET("/ZAutomation/api/v1/devices?limit=0")
    DevicesResponse getDevices();

    /**
     * Toggle a binary switch.
     * @param deviceId The id of the device to toggle.
     * @param toggle The toggle mode: 255 = on, 0 = off
     * @param callback The callback executed on success
     */
    @POST("/ZWaveAPI/Run/devices[{id}].instances[0].SwitchBinary.Set({toggle})")
    void toggleBinarySwitch(
            @Path("id") int deviceId,
            @Path("toggle") int toggle,
            Callback<Void> callback
    );
}
