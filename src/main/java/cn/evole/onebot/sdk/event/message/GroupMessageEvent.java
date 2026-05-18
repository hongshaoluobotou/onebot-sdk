package cn.evole.onebot.sdk.event.message;

import cn.evole.onebot.sdk.entity.Anonymous;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 群聊消息
 *
 * <p>例子：获取到群聊事件 {@code GroupMessageEvent} 后，
 * 用{@code for (ArrayMsg item : event.getArrayMsg()){}}逐条拆分消息，
 * 再根据{@code MsgType type = item.getType()}判断不同的消息类型 text/image/face/dice 来处理。
 * </p>
 *
 * <pre>{@code
 * void handleBySwitch() {
 *     GroupMessageEvent event = ...
 *
 *     // 把不同格式的 message 统一成 arrayMsg 并缓存，以方便后面循环处理
 *     List<ArrayMsg> arrayMsgs = BotUtils.rawToArrayMsg(event.getRawMessage());
 *
 *     for (ArrayMsg item : arrayMsgs) {
 *         MsgType type = item.getType();
 *         Map<String, String> data = item.getData();
 *         switch(type) {
 *             case text:
 *                 // TODO: 做点什么
 *             case at:
 *                 // TODO: 做点什么
 *         }
 *     }
 * }
 * }</pre>
 *
 * @author cnlimiter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GroupMessageEvent extends MessageEvent {

    @SerializedName( "message_id")
    private int messageId;

    @SerializedName( "sub_type")
    private String subType;

    @SerializedName( "group_id")
    private long groupId;

    @SerializedName( "anonymous")
    private Anonymous anonymous;

    @SerializedName( "sender")
    private GroupSender sender;

    /**
     * sender信息
     */
    @Data
    public static class GroupSender {

        @SerializedName( "user_id")
        private String userId;

        @SerializedName( "nickname")
        private String nickname;

        @SerializedName( "card")
        private String card;

        @SerializedName( "sex")
        private String sex;

        @SerializedName( "age")
        private int age;

        @SerializedName( "area")
        private String area;

        @SerializedName( "level")
        private String level;

        @SerializedName( "role")
        private String role;

        @SerializedName( "title")
        private String title;

    }

}
