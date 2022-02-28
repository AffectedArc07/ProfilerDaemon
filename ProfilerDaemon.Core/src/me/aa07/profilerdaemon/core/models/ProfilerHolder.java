package me.aa07.profilerdaemon.core.models;

import com.google.gson.annotations.SerializedName;

public class ProfilerHolder {
    // Need to use annotations here as checkstyle wont allow `round_id`
    @SerializedName("round_id")
    public int roundId;
    @SerializedName("profile_data")
    public String profilerData;
}
