package cn.evole.onebot.sdk.action;

/**
 * @author : cnlimiter
 */
public interface BaseBot extends OneBot, GoCQHTTPExtend, GensokyoExtend, LagrangeExtend, LLOneBotExtend{
    long getSelfId();
}
