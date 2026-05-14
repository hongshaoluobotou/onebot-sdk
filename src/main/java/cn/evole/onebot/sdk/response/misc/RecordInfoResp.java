package cn.evole.onebot.sdk.response.misc;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cnlimiter
 * @since 2024/1/17 19:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordInfoResp {
    @SerializedName("file") String file;
    @SerializedName("filename") String fileName;
    @SerializedName("md5") String md5;
    @SerializedName("file_type") String fileType;
}
