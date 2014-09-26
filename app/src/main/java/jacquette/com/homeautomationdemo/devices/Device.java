package jacquette.com.homeautomationdemo.devices;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public abstract class Device implements IDevice, Parcelable {

    protected int id;
    protected String name;
    protected boolean isOn;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public boolean getIsOn() { return this.isOn; }

    public void setIsOn(boolean isOn) { this.isOn = isOn; }

    //

    protected String mSerializedDevice;

    protected Device(int id, String name, boolean isOn) {
        this.id = id;
        this.name = name;
        this.isOn = isOn;

        Gson gson = new Gson();
        mSerializedDevice = gson.toJson(this);
    }

    protected Device(Parcel source) {
        mSerializedDevice = source.readString();

        // get the basics
        Gson gson = new Gson();
        Device d = gson.fromJson(mSerializedDevice, Device.class);
        this.setId(d.getId());
        this.setName(d.getName());
        this.setIsOn(d.getIsOn());
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSerializedDevice);
    }
}
