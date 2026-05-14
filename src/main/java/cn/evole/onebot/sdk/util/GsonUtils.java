package cn.evole.onebot.sdk.util;

import cn.evole.onebot.sdk.util.StringUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author cnlimiter
 * @since 2022/10/1 16:36
 * Version: 1.0
 */
public class GsonUtils {
    //线程安全的
    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization() //当Map的key为复杂对象时,需要开启该方法
//                .serializeNulls() //当字段值为空或null时，依然对该字段进行转换
//                .excludeFieldsWithoutExposeAnnotation()//打开Export注解，但打开了这个注解,副作用，要转换和不转换都要加注解
            .setDateFormat("yyyy-MM-dd HH:mm:ss")//序列化日期格式  "yyyy-MM-dd"
            .setPrettyPrinting() //自动格式化换行
            .disableHtmlEscaping() //防止特殊字符出现乱码
            .create();
    private static final Gson GSON_NULL = new GsonBuilder().enableComplexMapKeySerialization() //当Map的key为复杂对象时,需要开启该方法
            .serializeNulls() //当字段值为空或null时，依然对该字段进行转换
//                .excludeFieldsWithoutExposeAnnotation()//打开Export注解，但打开了这个注解,副作用，要转换和不转换都要加注解
            .setDateFormat("yyyy-MM-dd HH:mm:ss")//序列化日期格式  "yyyy-MM-dd"
            .setPrettyPrinting() //自动格式化换行
            .disableHtmlEscaping() //防止特殊字符出现乱码
            .create(); // 不过滤空值


    //获取gson解析器
    public static Gson getGson() {
        return GSON;
    }

    //获取gson解析器 有空值 解析
    public static Gson getNullGson() {
        return GSON_NULL;
    }

    /**
     * 根据对象返回json  过滤空值字段
     * @param object 对象
     * @return json
     */
    public static String toJsonStringIgnoreNull(Object object) {
        return GSON.toJson(object);
    }

    /**
     * 根据对象返回json  不过滤空值字段
     * @param object 对象
     * @return json
     */
    public static String toJsonString(Object object) {
        return GSON_NULL.toJson(object);
    }


    /**
     * 将字符串转化对象
     *
     * @param json     源字符串
     * @param classOfT 目标对象类
     * @param <T> T
     * @return T
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    /**
     * 将json转化为对应的实体对象
     *
     * <p>使用示例：
     * <pre>{@code
     *   // 单个对象
     *   User user = GsonUtils.fromJson(json, User.class);
     *
     *   // List
     *   List<User> list = GsonUtils.fromJson(json, new TypeToken<List<User>>() {}.getType());
     *
     *   // Map
     *   Map<String, User> map = GsonUtils.fromJson(json, new TypeToken<Map<String, User>>() {}.getType());
     * }</pre>
     *
     * @param <T>     目标对象类型
     * @param json    要转换的 JSON 字符串
     * @param typeOfT 目标类型，可通过 {@code new TypeToken<T>(){}.getType()} 获取
     * @return 转换后的实体对象
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    /**
     * 将 JSON 字符串转化为对应的实体对象
     *
     * <p>使用示例：
     * <pre>{@code
     *   // List
     *   List<User> list = fromJson(json, new TypeToken<List<T>>() {}.getType());
     *
     *   // Map
     *   Map<String, User> map = fromJson(json, new TypeToken<Map<String, T>>() {}.getType());
     *
     *   // List<Map>
     *   List<Map<String, User>> listMap = fromJson(json, new TypeToken<List<Map<String, T>>>() {}.getType());
     * }</pre>
     *
     * @param <T>     目标实体类型
     * @param json    要转换的 JSON 字符串
     * @param typeOfT 目标类型，通过 {@code new TypeToken<T>(){}.getType()} 获取
     * @return 转换后的实体对象
     */
    public static <T> T fromJson(JsonElement json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    /**
     * 转成 List
     *
     * @param <T>        列表元素类型
     * @param gsonString JSON 字符串
     * @param cls        元素类型 Class
     * @return 对象列表
     */
    public static <T> List<T> convertToList(String gsonString, Class<T> cls) {
        return GSON.fromJson(gsonString, new TypeToken<List<T>>() {
        }.getType());
    }

    /**
     * 转成 List 中有 Map 的
     *
     * @param <T>        Map 值类型
     * @param gsonString JSON 字符串
     * @return {@code List<Map<String, T>>}
     */
    public static <T> List<Map<String, T>> convertToListMaps(String gsonString) {
        return GSON.fromJson(gsonString, new TypeToken<List<Map<String, String>>>() {
        }.getType());
    }

    /**
     * 转成 Map
     *
     * @param <T>        Map 值类型
     * @param gsonString JSON 字符串
     * @return {@code Map<String, T>}
     */
    public static <T> Map<String, T> convertToMaps(String gsonString) {
        return GSON.fromJson(gsonString, new TypeToken<Map<String, T>>() {
        }.getType());
    }


    /**
     * 判断 JsonObject 中指定字段是否为字符串类型
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return 是否为字符串类型
     */
    public static boolean isStringValue(JsonObject pJson, String pMemberName) {
        return isValidPrimitive(pJson, pMemberName) && pJson.getAsJsonPrimitive(pMemberName).isString();
    }

    /**
     * 判断 JsonElement 是否为字符串类型
     *
     * @param pJson JsonElement 对象
     * @return 是否为字符串类型
     */
    public static boolean isStringValue(JsonElement pJson) {
        return pJson.isJsonPrimitive() && pJson.getAsJsonPrimitive().isString();
    }

    /**
     * 判断 JsonObject 中指定字段是否为数字类型
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return 是否为数字类型
     */
    public static boolean isNumberValue(JsonObject pJson, String pMemberName) {
        return isValidPrimitive(pJson, pMemberName) && pJson.getAsJsonPrimitive(pMemberName).isNumber();
    }

    /**
     * 判断 JsonElement 是否为数字类型
     *
     * @param pJson JsonElement 对象
     * @return 是否为数字类型
     */
    public static boolean isNumberValue(JsonElement pJson) {
        return pJson.isJsonPrimitive() && pJson.getAsJsonPrimitive().isNumber();
    }

    /**
     * 判断 JsonObject 中指定字段是否为布尔类型
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return 是否为布尔类型
     */
    public static boolean isBooleanValue(JsonObject pJson, String pMemberName) {
        return isValidPrimitive(pJson, pMemberName) && pJson.getAsJsonPrimitive(pMemberName).isBoolean();
    }

    /**
     * 判断 JsonElement 是否为布尔类型
     *
     * @param pJson JsonElement 对象
     * @return 是否为布尔类型
     */
    public static boolean isBooleanValue(JsonElement pJson) {
        return pJson.isJsonPrimitive() && pJson.getAsJsonPrimitive().isBoolean();
    }

    /**
     * 判断 JsonObject 中指定字段是否为数组类型
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return 是否为数组类型
     */
    public static boolean isArrayNode(JsonObject pJson, String pMemberName) {
        return isValidNode(pJson, pMemberName) && pJson.get(pMemberName).isJsonArray();
    }

    /**
     * 判断 JsonObject 中指定字段是否为对象类型
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return 是否为对象类型
     */
    public static boolean isObjectNode(JsonObject pJson, String pMemberName) {
        return isValidNode(pJson, pMemberName) && pJson.get(pMemberName).isJsonObject();
    }

    /**
     * 判断 JsonObject 中指定字段是否为基本类型（String、Java 基本类型或其包装类）
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return 是否为基本类型
     */
    public static boolean isValidPrimitive(JsonObject pJson, String pMemberName) {
        return isValidNode(pJson, pMemberName) && pJson.get(pMemberName).isJsonPrimitive();
    }

    /**
     * 判断 JsonObject 中是否包含指定字段
     *
     * @param pJson       JsonObject 对象，允许为 null
     * @param pMemberName 字段名
     * @return 是否包含该字段
     */
    public static boolean isValidNode(@Nullable JsonObject pJson, String pMemberName) {
        if (pJson == null) {
            return false;
        } else {
            return pJson.get(pMemberName) != null;
        }
    }

    /**
     * 获取 JsonObject 中指定字段的非 null 值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return 字段对应的 JsonElement
     * @throws JsonSyntaxException 字段不存在或为 null 时抛出
     */
    public static JsonElement getNonNull(JsonObject pJson, String pMemberName) {
        JsonElement jsonelement = pJson.get(pMemberName);
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            return jsonelement;
        } else {
            throw new JsonSyntaxException("Missing field " + pMemberName);
        }
    }

    /**
     * 将 JsonElement 转换为字符串
     *
     * @param pJson       JsonElement 对象
     * @param pMemberName 字段名（用于错误提示）
     * @return 字符串值
     * @throws JsonSyntaxException 不是字符串类型时抛出
     */
    public static String convertToString(JsonElement pJson, String pMemberName) {
        if (pJson.isJsonPrimitive()) {
            return pJson.getAsString();
        } else {
            throw new JsonSyntaxException("Expected " + pMemberName + " to be a string, was " + getType(pJson));
        }
    }

    /**
     * 获取 JsonObject 中指定字符串字段的值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return 字符串值
     * @throws JsonSyntaxException 字段不存在或不是字符串时抛出
     */
    public static String getAsString(JsonObject pJson, String pMemberName) {
        if (pJson.has(pMemberName)) {
            return convertToString(pJson.get(pMemberName), pMemberName);
        } else {
            throw new JsonSyntaxException("Missing " + pMemberName + ", expected to find a string");
        }
    }

    /**
     * 获取 JsonObject 中指定字符串字段的值，不存在时返回默认值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @param pFallback   默认值
     * @return 字符串值，或默认值
     */
    @Nullable
    @Contract("_,_,!null->!null;_,_,null->_")
    public static String getAsString(JsonObject pJson, String pMemberName, @Nullable String pFallback) {
        return pJson.has(pMemberName) ? convertToString(pJson.get(pMemberName), pMemberName) : pFallback;
    }

    /**
     * 将 JsonElement 转换为布尔值
     *
     * @param pJson       JsonElement 对象
     * @param pMemberName 字段名（用于错误提示）
     * @return 布尔值
     * @throws JsonSyntaxException 不是布尔类型时抛出
     */
    public static boolean convertToBoolean(JsonElement pJson, String pMemberName) {
        if (pJson.isJsonPrimitive()) {
            return pJson.getAsBoolean();
        } else {
            throw new JsonSyntaxException("Expected " + pMemberName + " to be a Boolean, was " + getType(pJson));
        }
    }

    /**
     * 获取 JsonObject 中指定布尔字段的值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return 布尔值
     * @throws JsonSyntaxException 字段不存在或不是布尔类型时抛出
     */
    public static boolean getAsBoolean(JsonObject pJson, String pMemberName) {
        if (pJson.has(pMemberName)) {
            return convertToBoolean(pJson.get(pMemberName), pMemberName);
        } else {
            throw new JsonSyntaxException("Missing " + pMemberName + ", expected to find a Boolean");
        }
    }

    /**
     * 获取 JsonObject 中指定布尔字段的值，不存在时返回默认值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @param pFallback   默认值
     * @return 布尔值，或默认值
     */
    public static boolean getAsBoolean(JsonObject pJson, String pMemberName, boolean pFallback) {
        return pJson.has(pMemberName) ? convertToBoolean(pJson.get(pMemberName), pMemberName) : pFallback;
    }

    /**
     * 将 JsonElement 转换为 double
     *
     * @param pJson       JsonElement 对象
     * @param pMemberName 字段名（用于错误提示）
     * @return double 值
     * @throws JsonSyntaxException 不是数字类型时抛出
     */
    public static double convertToDouble(JsonElement pJson, String pMemberName) {
        if (pJson.isJsonPrimitive() && pJson.getAsJsonPrimitive().isNumber()) {
            return pJson.getAsDouble();
        } else {
            throw new JsonSyntaxException("Expected " + pMemberName + " to be a Double, was " + getType(pJson));
        }
    }

    /**
     * 获取 JsonObject 中指定 double 字段的值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return double 值
     * @throws JsonSyntaxException 字段不存在或不是数字时抛出
     */
    public static double getAsDouble(JsonObject pJson, String pMemberName) {
        if (pJson.has(pMemberName)) {
            return convertToDouble(pJson.get(pMemberName), pMemberName);
        } else {
            throw new JsonSyntaxException("Missing " + pMemberName + ", expected to find a Double");
        }
    }

    /**
     * 获取 JsonObject 中指定 double 字段的值，不存在时返回默认值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @param pFallback   默认值
     * @return double 值，或默认值
     */
    public static double getAsDouble(JsonObject pJson, String pMemberName, double pFallback) {
        return pJson.has(pMemberName) ? convertToDouble(pJson.get(pMemberName), pMemberName) : pFallback;
    }

    /**
     * 将 JsonElement 转换为 float
     *
     * @param pJson       JsonElement 对象
     * @param pMemberName 字段名（用于错误提示）
     * @return float 值
     * @throws JsonSyntaxException 不是数字类型时抛出
     */
    public static float convertToFloat(JsonElement pJson, String pMemberName) {
        if (pJson.isJsonPrimitive() && pJson.getAsJsonPrimitive().isNumber()) {
            return pJson.getAsFloat();
        } else {
            throw new JsonSyntaxException("Expected " + pMemberName + " to be a Float, was " + getType(pJson));
        }
    }

    /**
     * 获取 JsonObject 中指定 float 字段的值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return float 值
     * @throws JsonSyntaxException 字段不存在或不是数字时抛出
     */
    public static float getAsFloat(JsonObject pJson, String pMemberName) {
        if (pJson.has(pMemberName)) {
            return convertToFloat(pJson.get(pMemberName), pMemberName);
        } else {
            throw new JsonSyntaxException("Missing " + pMemberName + ", expected to find a Float");
        }
    }

    /**
     * 获取 JsonObject 中指定 float 字段的值，不存在时返回默认值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @param pFallback   默认值
     * @return float 值，或默认值
     */
    public static float getAsFloat(JsonObject pJson, String pMemberName, float pFallback) {
        return pJson.has(pMemberName) ? convertToFloat(pJson.get(pMemberName), pMemberName) : pFallback;
    }

    /**
     * 将 JsonElement 转换为 long
     *
     * @param pJson       JsonElement 对象
     * @param pMemberName 字段名（用于错误提示）
     * @return long 值
     * @throws JsonSyntaxException 不是数字类型时抛出
     */
    public static long convertToLong(JsonElement pJson, String pMemberName) {
        if (pJson.isJsonPrimitive() && pJson.getAsJsonPrimitive().isNumber()) {
            return pJson.getAsLong();
        } else {
            throw new JsonSyntaxException("Expected " + pMemberName + " to be a Long, was " + getType(pJson));
        }
    }

    /**
     * 获取 JsonObject 中指定 long 字段的值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return long 值
     * @throws JsonSyntaxException 字段不存在或不是数字时抛出
     */
    public static long getAsLong(JsonObject pJson, String pMemberName) {
        if (pJson.has(pMemberName)) {
            return convertToLong(pJson.get(pMemberName), pMemberName);
        } else {
            throw new JsonSyntaxException("Missing " + pMemberName + ", expected to find a Long");
        }
    }

    /**
     * 获取 JsonObject 中指定 long 字段的值，不存在时返回默认值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @param pFallback   默认值
     * @return long 值，或默认值
     */
    public static long getAsLong(JsonObject pJson, String pMemberName, long pFallback) {
        return pJson.has(pMemberName) ? convertToLong(pJson.get(pMemberName), pMemberName) : pFallback;
    }

    /**
     * 将 JsonElement 转换为 int
     *
     * @param pJson       JsonElement 对象
     * @param pMemberName 字段名（用于错误提示）
     * @return int 值
     * @throws JsonSyntaxException 不是数字类型时抛出
     */
    public static int convertToInt(JsonElement pJson, String pMemberName) {
        if (pJson.isJsonPrimitive() && pJson.getAsJsonPrimitive().isNumber()) {
            return pJson.getAsInt();
        } else {
            throw new JsonSyntaxException("Expected " + pMemberName + " to be a Int, was " + getType(pJson));
        }
    }

    /**
     * 获取 JsonObject 中指定 int 字段的值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return int 值
     * @throws JsonSyntaxException 字段不存在或不是数字时抛出
     */
    public static int getAsInt(JsonObject pJson, String pMemberName) {
        if (pJson.has(pMemberName)) {
            return convertToInt(pJson.get(pMemberName), pMemberName);
        } else {
            throw new JsonSyntaxException("Missing " + pMemberName + ", expected to find a Int");
        }
    }

    /**
     * 获取 JsonObject 中指定 int 字段的值，不存在时返回默认值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @param pFallback   默认值
     * @return int 值，或默认值
     */
    public static int getAsInt(JsonObject pJson, String pMemberName, int pFallback) {
        return pJson.has(pMemberName) ? convertToInt(pJson.get(pMemberName), pMemberName) : pFallback;
    }

    /**
     * 将 JsonElement 转换为 byte
     *
     * @param pJson       JsonElement 对象
     * @param pMemberName 字段名（用于错误提示）
     * @return byte 值
     * @throws JsonSyntaxException 不是数字类型时抛出
     */
    public static byte convertToByte(JsonElement pJson, String pMemberName) {
        if (pJson.isJsonPrimitive() && pJson.getAsJsonPrimitive().isNumber()) {
            return pJson.getAsByte();
        } else {
            throw new JsonSyntaxException("Expected " + pMemberName + " to be a Byte, was " + getType(pJson));
        }
    }

    /**
     * 获取 JsonObject 中指定 byte 字段的值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return byte 值
     * @throws JsonSyntaxException 字段不存在或不是数字时抛出
     */
    public static byte getAsByte(JsonObject pJson, String pMemberName) {
        if (pJson.has(pMemberName)) {
            return convertToByte(pJson.get(pMemberName), pMemberName);
        } else {
            throw new JsonSyntaxException("Missing " + pMemberName + ", expected to find a Byte");
        }
    }

    /**
     * 获取 JsonObject 中指定 byte 字段的值，不存在时返回默认值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @param pFallback   默认值
     * @return byte 值，或默认值
     */
    public static byte getAsByte(JsonObject pJson, String pMemberName, byte pFallback) {
        return pJson.has(pMemberName) ? convertToByte(pJson.get(pMemberName), pMemberName) : pFallback;
    }

    /**
     * 将 JsonElement 转换为 char
     *
     * @param pJson       JsonElement 对象
     * @param pMemberName 字段名（用于错误提示）
     * @return char 值
     * @throws JsonSyntaxException 不是数字类型时抛出
     */
    public static char convertToCharacter(JsonElement pJson, String pMemberName) {
        if (pJson.isJsonPrimitive() && pJson.getAsJsonPrimitive().isNumber()) {
            return pJson.getAsCharacter();
        } else {
            throw new JsonSyntaxException("Expected " + pMemberName + " to be a Character, was " + getType(pJson));
        }
    }

    /**
     * 获取 JsonObject 中指定 char 字段的值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return char 值
     * @throws JsonSyntaxException 字段不存在或不是数字时抛出
     */
    public static char getAsCharacter(JsonObject pJson, String pMemberName) {
        if (pJson.has(pMemberName)) {
            return convertToCharacter(pJson.get(pMemberName), pMemberName);
        } else {
            throw new JsonSyntaxException("Missing " + pMemberName + ", expected to find a Character");
        }
    }

    /**
     * 获取 JsonObject 中指定 char 字段的值，不存在时返回默认值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @param pFallback   默认值
     * @return char 值，或默认值
     */
    public static char getAsCharacter(JsonObject pJson, String pMemberName, char pFallback) {
        return pJson.has(pMemberName) ? convertToCharacter(pJson.get(pMemberName), pMemberName) : pFallback;
    }

    /**
     * 将 JsonElement 转换为 BigDecimal
     *
     * @param pJson       JsonElement 对象
     * @param pMemberName 字段名（用于错误提示）
     * @return BigDecimal 值
     * @throws JsonSyntaxException 不是数字类型时抛出
     */
    public static BigDecimal convertToBigDecimal(JsonElement pJson, String pMemberName) {
        if (pJson.isJsonPrimitive() && pJson.getAsJsonPrimitive().isNumber()) {
            return pJson.getAsBigDecimal();
        } else {
            throw new JsonSyntaxException("Expected " + pMemberName + " to be a BigDecimal, was " + getType(pJson));
        }
    }

    /**
     * 获取 JsonObject 中指定 BigDecimal 字段的值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return BigDecimal 值
     * @throws JsonSyntaxException 字段不存在或不是数字时抛出
     */
    public static BigDecimal getAsBigDecimal(JsonObject pJson, String pMemberName) {
        if (pJson.has(pMemberName)) {
            return convertToBigDecimal(pJson.get(pMemberName), pMemberName);
        } else {
            throw new JsonSyntaxException("Missing " + pMemberName + ", expected to find a BigDecimal");
        }
    }

    /**
     * 获取 JsonObject 中指定 BigDecimal 字段的值，不存在时返回默认值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @param pFallback   默认值
     * @return BigDecimal 值，或默认值
     */
    public static BigDecimal getAsBigDecimal(JsonObject pJson, String pMemberName, BigDecimal pFallback) {
        return pJson.has(pMemberName) ? convertToBigDecimal(pJson.get(pMemberName), pMemberName) : pFallback;
    }

    /**
     * 将 JsonElement 转换为 BigInteger
     *
     * @param pJson       JsonElement 对象
     * @param pMemberName 字段名（用于错误提示）
     * @return BigInteger 值
     * @throws JsonSyntaxException 不是数字类型时抛出
     */
    public static BigInteger convertToBigInteger(JsonElement pJson, String pMemberName) {
        if (pJson.isJsonPrimitive() && pJson.getAsJsonPrimitive().isNumber()) {
            return pJson.getAsBigInteger();
        } else {
            throw new JsonSyntaxException("Expected " + pMemberName + " to be a BigInteger, was " + getType(pJson));
        }
    }

    /**
     * 获取 JsonObject 中指定 BigInteger 字段的值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return BigInteger 值
     * @throws JsonSyntaxException 字段不存在或不是数字时抛出
     */
    public static BigInteger getAsBigInteger(JsonObject pJson, String pMemberName) {
        if (pJson.has(pMemberName)) {
            return convertToBigInteger(pJson.get(pMemberName), pMemberName);
        } else {
            throw new JsonSyntaxException("Missing " + pMemberName + ", expected to find a BigInteger");
        }
    }

    /**
     * 获取 JsonObject 中指定 BigInteger 字段的值，不存在时返回默认值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @param pFallback   默认值
     * @return BigInteger 值，或默认值
     */
    public static BigInteger getAsBigInteger(JsonObject pJson, String pMemberName, BigInteger pFallback) {
        return pJson.has(pMemberName) ? convertToBigInteger(pJson.get(pMemberName), pMemberName) : pFallback;
    }

    /**
     * 将 JsonElement 转换为 short
     *
     * @param pJson       JsonElement 对象
     * @param pMemberName 字段名（用于错误提示）
     * @return short 值
     * @throws JsonSyntaxException 不是数字类型时抛出
     */
    public static short convertToShort(JsonElement pJson, String pMemberName) {
        if (pJson.isJsonPrimitive() && pJson.getAsJsonPrimitive().isNumber()) {
            return pJson.getAsShort();
        } else {
            throw new JsonSyntaxException("Expected " + pMemberName + " to be a Short, was " + getType(pJson));
        }
    }

    /**
     * 获取 JsonObject 中指定 short 字段的值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return short 值
     * @throws JsonSyntaxException 字段不存在或不是数字时抛出
     */
    public static short getAsShort(JsonObject pJson, String pMemberName) {
        if (pJson.has(pMemberName)) {
            return convertToShort(pJson.get(pMemberName), pMemberName);
        } else {
            throw new JsonSyntaxException("Missing " + pMemberName + ", expected to find a Short");
        }
    }

    /**
     * 获取 JsonObject 中指定 short 字段的值，不存在时返回默认值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @param pFallback   默认值
     * @return short 值，或默认值
     */
    public static short getAsShort(JsonObject pJson, String pMemberName, short pFallback) {
        return pJson.has(pMemberName) ? convertToShort(pJson.get(pMemberName), pMemberName) : pFallback;
    }

    /**
     * 将 JsonElement 转换为 JsonObject
     *
     * @param pJson       JsonElement 对象
     * @param pMemberName 字段名（用于错误提示）
     * @return JsonObject 对象
     * @throws JsonSyntaxException 不是对象类型时抛出
     */
    public static JsonObject convertToJsonObject(JsonElement pJson, String pMemberName) {
        if (pJson.isJsonObject()) {
            return pJson.getAsJsonObject();
        } else {
            throw new JsonSyntaxException("Expected " + pMemberName + " to be a JsonObject, was " + getType(pJson));
        }
    }

    /**
     * 获取 JsonObject 中指定 JsonObject 字段的值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return JsonObject 值
     * @throws JsonSyntaxException 字段不存在或不是对象类型时抛出
     */
    public static JsonObject getAsJsonObject(JsonObject pJson, String pMemberName) {
        if (pJson.has(pMemberName)) {
            return convertToJsonObject(pJson.get(pMemberName), pMemberName);
        } else {
            throw new JsonSyntaxException("Missing " + pMemberName + ", expected to find a JsonObject");
        }
    }

    /**
     * 获取 JsonObject 中指定 JsonObject 字段的值，不存在时返回默认值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @param pFallback   默认值
     * @return JsonObject 值，或默认值
     */
    @Nullable
    @Contract("_,_,!null->!null;_,_,null->_")
    public static JsonObject getAsJsonObject(JsonObject pJson, String pMemberName, @Nullable JsonObject pFallback) {
        return pJson.has(pMemberName) ? convertToJsonObject(pJson.get(pMemberName), pMemberName) : pFallback;
    }

    /**
     * 将 JsonElement 转换为 JsonArray
     *
     * @param pJson       JsonElement 对象
     * @param pMemberName 字段名（用于错误提示）
     * @return JsonArray 对象
     * @throws JsonSyntaxException 不是数组类型时抛出
     */
    public static JsonArray convertToJsonArray(JsonElement pJson, String pMemberName) {
        if (pJson.isJsonArray()) {
            return pJson.getAsJsonArray();
        } else {
            throw new JsonSyntaxException("Expected " + pMemberName + " to be a JsonArray, was " + getType(pJson));
        }
    }

    /**
     * 获取 JsonObject 中指定 JsonArray 字段的值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @return JsonArray 值
     * @throws JsonSyntaxException 字段不存在或不是数组类型时抛出
     */
    public static JsonArray getAsJsonArray(JsonObject pJson, String pMemberName) {
        if (pJson.has(pMemberName)) {
            return convertToJsonArray(pJson.get(pMemberName), pMemberName);
        } else {
            throw new JsonSyntaxException("Missing " + pMemberName + ", expected to find a JsonArray");
        }
    }

    /**
     * 获取 JsonObject 中指定 JsonArray 字段的值，不存在时返回默认值
     *
     * @param pJson       JsonObject 对象
     * @param pMemberName 字段名
     * @param pFallback   默认值
     * @return JsonArray 值，或默认值
     */
    @Nullable
    @Contract("_,_,!null->!null;_,_,null->_")
    public static JsonArray getAsJsonArray(JsonObject pJson, String pMemberName, @Nullable JsonArray pFallback) {
        return pJson.has(pMemberName) ? convertToJsonArray(pJson.get(pMemberName), pMemberName) : pFallback;
    }

    /**
     * 将 JsonElement 反序列化为指定类型的对象
     *
     * @param <T>        目标类型
     * @param pJson      JsonElement 对象，允许为 null
     * @param pMemberName 字段名（用于错误提示）
     * @param pContext   反序列化上下文
     * @param pAdapter   目标类型 Class
     * @return 反序列化后的对象
     * @throws JsonSyntaxException 字段不存在或为 null 时抛出
     */
    public static <T> T convertToObject(@Nullable JsonElement pJson, String pMemberName, JsonDeserializationContext pContext, Class<? extends T> pAdapter) {
        if (pJson != null) {
            return pContext.deserialize(pJson, pAdapter);
        } else {
            throw new JsonSyntaxException("Missing " + pMemberName);
        }
    }

    /**
     * 获取 JsonObject 中指定字段并反序列化为指定类型
     *
     * @param <T>        目标类型
     * @param pJson      JsonObject 对象
     * @param pMemberName 字段名
     * @param pContext   反序列化上下文
     * @param pAdapter   目标类型 Class
     * @return 反序列化后的对象
     * @throws JsonSyntaxException 字段不存在时抛出
     */
    public static <T> T getAsObject(JsonObject pJson, String pMemberName, JsonDeserializationContext pContext, Class<? extends T> pAdapter) {
        if (pJson.has(pMemberName)) {
            return convertToObject(pJson.get(pMemberName), pMemberName, pContext, pAdapter);
        } else {
            throw new JsonSyntaxException("Missing " + pMemberName);
        }
    }

    /**
     * 获取 JsonObject 中指定字段并反序列化为指定类型，不存在时返回默认值
     *
     * @param <T>        目标类型
     * @param pJson      JsonObject 对象
     * @param pMemberName 字段名
     * @param pFallback  默认值
     * @param pContext   反序列化上下文
     * @param pAdapter   目标类型 Class
     * @return 反序列化后的对象，或默认值
     */
    @Nullable
    @Contract("_,_,!null,_,_->!null;_,_,null,_,_->_")
    public static <T> T getAsObject(
            JsonObject pJson, String pMemberName, @Nullable T pFallback, JsonDeserializationContext pContext, Class<? extends T> pAdapter
    ) {
        return (T)(pJson.has(pMemberName) ? convertToObject(pJson.get(pMemberName), pMemberName, pContext, pAdapter) : pFallback);
    }

    /**
     * 获取 JsonElement 的类型描述
     *
     * @param pJson JsonElement 对象，允许为 null
     * @return 人类可读的类型描述
     */
    public static String getType(@Nullable JsonElement pJson) {
        String s = StringUtils.abbreviateMiddle(String.valueOf(pJson), "...", 10);
        if (pJson == null) {
            return "null (missing)";
        } else if (pJson.isJsonNull()) {
            return "null (json)";
        } else if (pJson.isJsonArray()) {
            return "an array (" + s + ")";
        } else if (pJson.isJsonObject()) {
            return "an object (" + s + ")";
        } else {
            if (pJson.isJsonPrimitive()) {
                JsonPrimitive jsonprimitive = pJson.getAsJsonPrimitive();
                if (jsonprimitive.isNumber()) {
                    return "a number (" + s + ")";
                }

                if (jsonprimitive.isBoolean()) {
                    return "a boolean (" + s + ")";
                }
            }

            return s;
        }
    }

    /**
     * 从 Reader 中读取并反序列化为指定类型，允许返回 null
     *
     * @param <T>       目标类型
     * @param pGson     Gson 实例
     * @param pReader   Reader 对象
     * @param pAdapter  目标类型 Class
     * @param pLenient  是否宽松解析
     * @return 反序列化后的对象，可能为 null
     * @throws JsonParseException 解析失败时抛出
     */
    @Nullable
    public static <T> T fromNullableJson(Gson pGson, Reader pReader, Class<T> pAdapter, boolean pLenient) {
        try {
            JsonReader jsonreader = new JsonReader(pReader);
            jsonreader.setLenient(pLenient);
            return pGson.getAdapter(pAdapter).read(jsonreader);
        } catch (IOException ioexception) {
            throw new JsonParseException(ioexception);
        }
    }

    /**
     * 从 Reader 中读取并反序列化为指定类型，不允许返回 null
     *
     * @param <T>       目标类型
     * @param pGson     Gson 实例
     * @param pReader   Reader 对象
     * @param pAdapter  目标类型 Class
     * @param pLenient  是否宽松解析
     * @return 反序列化后的对象
     * @throws JsonParseException 解析失败或结果为 null 时抛出
     */
    public static <T> T fromJson(Gson pGson, Reader pReader, Class<T> pAdapter, boolean pLenient) {
        T t = fromNullableJson(pGson, pReader, pAdapter, pLenient);
        if (t == null) {
            throw new JsonParseException("JSON data was null or empty");
        } else {
            return t;
        }
    }

    /**
     * 从 Reader 中读取并反序列化为指定类型，允许返回 null
     *
     * @param <T>       目标类型
     * @param pGson     Gson 实例
     * @param pReader   Reader 对象
     * @param pType     目标类型 Token
     * @param pLenient  是否宽松解析
     * @return 反序列化后的对象，可能为 null
     * @throws JsonParseException 解析失败时抛出
     */
    @Nullable
    public static <T> T fromNullableJson(Gson pGson, Reader pReader, TypeToken<T> pType, boolean pLenient) {
        try {
            JsonReader jsonreader = new JsonReader(pReader);
            jsonreader.setLenient(pLenient);
            return pGson.getAdapter(pType).read(jsonreader);
        } catch (IOException ioexception) {
            throw new JsonParseException(ioexception);
        }
    }

    /**
     * 从 Reader 中读取并反序列化为指定类型，不允许返回 null
     *
     * @param <T>       目标类型
     * @param pGson     Gson 实例
     * @param pReader   Reader 对象
     * @param pType     目标类型 Token
     * @param pLenient  是否宽松解析
     * @return 反序列化后的对象
     * @throws JsonParseException 解析失败或结果为 null 时抛出
     */
    public static <T> T fromJson(Gson pGson, Reader pReader, TypeToken<T> pType, boolean pLenient) {
        T t = fromNullableJson(pGson, pReader, pType, pLenient);
        if (t == null) {
            throw new JsonParseException("JSON data was null or empty");
        } else {
            return t;
        }
    }

    /**
     * 从 JSON 字符串中读取并反序列化为指定类型，允许返回 null
     *
     * @param <T>       目标类型
     * @param pGson     Gson 实例
     * @param pJson     JSON 字符串
     * @param pType     目标类型 Token
     * @param pLenient  是否宽松解析
     * @return 反序列化后的对象，可能为 null
     * @throws JsonParseException 解析失败时抛出
     */
    @Nullable
    public static <T> T fromNullableJson(Gson pGson, String pJson, TypeToken<T> pType, boolean pLenient) {
        return fromNullableJson(pGson, new StringReader(pJson), pType, pLenient);
    }

    /**
     * 从 JSON 字符串中读取并反序列化为指定类型
     *
     * @param <T>       目标类型
     * @param pGson     Gson 实例
     * @param pJson     JSON 字符串
     * @param pAdapter  目标类型 Class
     * @param pLenient  是否宽松解析
     * @return 反序列化后的对象
     * @throws JsonParseException 解析失败或结果为 null 时抛出
     */
    public static <T> T fromJson(Gson pGson, String pJson, Class<T> pAdapter, boolean pLenient) {
        return fromJson(pGson, new StringReader(pJson), pAdapter, pLenient);
    }

    /**
     * 从 JSON 字符串中读取并反序列化为指定类型，允许返回 null
     *
     * @param <T>       目标类型
     * @param pGson     Gson 实例
     * @param pJson     JSON 字符串
     * @param pAdapter  目标类型 Class
     * @param pLenient  是否宽松解析
     * @return 反序列化后的对象，可能为 null
     * @throws JsonParseException 解析失败时抛出
     */
    @Nullable
    public static <T> T fromNullableJson(Gson pGson, String pJson, Class<T> pAdapter, boolean pLenient) {
        return fromNullableJson(pGson, new StringReader(pJson), pAdapter, pLenient);
    }

    /**
     * 从 Reader 中读取并反序列化为指定类型
     *
     * @param <T>       目标类型
     * @param pGson     Gson 实例
     * @param pReader   Reader 对象
     * @param pType     目标类型 Token
     * @return 反序列化后的对象
     * @throws JsonParseException 解析失败或结果为 null 时抛出
     */
    public static <T> T fromJson(Gson pGson, Reader pReader, TypeToken<T> pType) {
        return fromJson(pGson, pReader, pType, false);
    }

    /**
     * 从 JSON 字符串中读取并反序列化为指定类型，允许返回 null
     *
     * @param <T>       目标类型
     * @param pGson     Gson 实例
     * @param pJson     JSON 字符串
     * @param pType     目标类型 Token
     * @return 反序列化后的对象，可能为 null
     * @throws JsonParseException 解析失败时抛出
     */
    @Nullable
    public static <T> T fromNullableJson(Gson pGson, String pJson, TypeToken<T> pType) {
        return fromNullableJson(pGson, pJson, pType, false);
    }

    /**
     * 从 Reader 中读取并反序列化为指定类型
     *
     * @param <T>       目标类型
     * @param pGson     Gson 实例
     * @param pReader   Reader 对象
     * @param pJsonClass 目标类型 Class
     * @return 反序列化后的对象
     * @throws JsonParseException 解析失败或结果为 null 时抛出
     */
    public static <T> T fromJson(Gson pGson, Reader pReader, Class<T> pJsonClass) {
        return fromJson(pGson, pReader, pJsonClass, false);
    }

    /**
     * 从 JSON 字符串中读取并反序列化为指定类型
     *
     * @param <T>       目标类型
     * @param pGson     Gson 实例
     * @param pJson     JSON 字符串
     * @param pAdapter  目标类型 Class
     * @return 反序列化后的对象
     * @throws JsonParseException 解析失败或结果为 null 时抛出
     */
    public static <T> T fromJson(Gson pGson, String pJson, Class<T> pAdapter) {
        return fromJson(pGson, pJson, pAdapter, false);
    }

    /**
     * 解析 JSON 字符串为 JsonObject
     *
     * @param pJson     JSON 字符串
     * @param pLenient  是否宽松解析
     * @return JsonObject 对象
     */
    public static JsonObject parse(String pJson, boolean pLenient) {
        return parse(new StringReader(pJson), pLenient);
    }

    /**
     * 从 Reader 中解析为 JsonObject
     *
     * @param pReader   Reader 对象
     * @param pLenient  是否宽松解析
     * @return JsonObject 对象
     */
    public static JsonObject parse(Reader pReader, boolean pLenient) {
        return fromJson(GSON, pReader, JsonObject.class, pLenient);
    }

    /**
     * 解析 JSON 字符串为 JsonObject
     *
     * @param pJson JSON 字符串
     * @return JsonObject 对象
     */
    public static JsonObject parse(String pJson) {
        return parse(pJson, false);
    }

    /**
     * 从 Reader 中解析为 JsonObject
     *
     * @param pReader Reader 对象
     * @return JsonObject 对象
     */
    public static JsonObject parse(Reader pReader) {
        return parse(pReader, false);
    }

    /**
     * 解析 JSON 字符串为 JsonArray
     *
     * @param pString JSON 字符串
     * @return JsonArray 对象
     */
    public static JsonArray parseArray(String pString) {
        return parseArray(new StringReader(pString));
    }

    /**
     * 从 Reader 中解析为 JsonArray
     *
     * @param pReader Reader 对象
     * @return JsonArray 对象
     */
    public static JsonArray parseArray(Reader pReader) {
        return fromJson(GSON, pReader, JsonArray.class, false);
    }

    /**
     * 将 JsonElement 转换为稳定的字符串表示（键按字母序排序）
     *
     * @param pJson JsonElement 对象
     * @return 稳定的 JSON 字符串
     */
    public static String toStableString(JsonElement pJson) {
        StringWriter stringwriter = new StringWriter();
        JsonWriter jsonwriter = new JsonWriter(stringwriter);

        try {
            writeValue(jsonwriter, pJson, Comparator.naturalOrder());
        } catch (IOException ioexception) {
            throw new AssertionError(ioexception);
        }

        return stringwriter.toString();
    }

    /**
     * 将 JsonElement 写入 JsonWriter
     *
     * @param pWriter     JsonWriter 对象
     * @param pJsonElement 要写入的 JsonElement，允许为 null
     * @param pSorter     键排序器，允许为 null（不排序）
     * @throws IOException 写入失败时抛出
     */
    public static void writeValue(JsonWriter pWriter, @Nullable JsonElement pJsonElement, @Nullable Comparator<String> pSorter) throws IOException {
        if (pJsonElement == null || pJsonElement.isJsonNull()) {
            pWriter.nullValue();
        } else if (pJsonElement.isJsonPrimitive()) {
            JsonPrimitive jsonprimitive = pJsonElement.getAsJsonPrimitive();
            if (jsonprimitive.isNumber()) {
                pWriter.value(jsonprimitive.getAsNumber());
            } else if (jsonprimitive.isBoolean()) {
                pWriter.value(jsonprimitive.getAsBoolean());
            } else {
                pWriter.value(jsonprimitive.getAsString());
            }
        } else if (pJsonElement.isJsonArray()) {
            pWriter.beginArray();

            for(JsonElement jsonelement : pJsonElement.getAsJsonArray()) {
                writeValue(pWriter, jsonelement, pSorter);
            }

            pWriter.endArray();
        } else {
            if (!pJsonElement.isJsonObject()) {
                throw new IllegalArgumentException("Couldn't write " + pJsonElement.getClass());
            }

            pWriter.beginObject();

            for(Map.Entry<String, JsonElement> entry : sortByKeyIfNeeded(pJsonElement.getAsJsonObject().entrySet(), pSorter)) {
                pWriter.name(entry.getKey());
                writeValue(pWriter, entry.getValue(), pSorter);
            }

            pWriter.endObject();
        }
    }

    /**
     * 如果需要，按键排序 Map 条目
     *
     * @param pEntries  原始条目集合
     * @param pSorter   键排序器，为 null 时不排序
     * @return 排序后的条目集合
     */
    private static Collection<Map.Entry<String, JsonElement>> sortByKeyIfNeeded(
            Collection<Map.Entry<String, JsonElement>> pEntries, @Nullable Comparator<String> pSorter
    ) {
        if (pSorter == null) {
            return pEntries;
        } else {
            List<Map.Entry<String, JsonElement>> list = new ArrayList<>(pEntries);
            list.sort(Map.Entry.comparingByKey(pSorter));
            return list;
        }
    }

}
