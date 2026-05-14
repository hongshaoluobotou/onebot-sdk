package cn.evole.onebot.sdk.response.misc;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author cnlimiter
 * @since 2025/2/9 01:44
 */
@Data
public class UrlResp {

    @SerializedName("url")
    private String url;

}
