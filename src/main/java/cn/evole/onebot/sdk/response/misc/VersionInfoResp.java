package cn.evole.onebot.sdk.response.misc;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author cnlimiter
 * @since 2025/2/9 01:44
 */
@Data
public class VersionInfoResp {

    @SerializedName("app_name")
    private String appName;

    @SerializedName("app_version")
    private String appVersion;

    @SerializedName("protocol_version")
    private String protocolVersion;

    @SerializedName("version")
    private String version;
}
