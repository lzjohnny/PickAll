package cn.edu.xidian.sast.baselib.okhttp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ShiningForever on 2017/10/2.
 */

public class JsonUtils {
    private static Gson gson = null;

    static {
        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
    }

    private JsonUtils() {
    }

    public static String beanToJsonStr(Object object) {
        String jsonStr = gson.toJson(object);
        return jsonStr;
    }

    public static <T> T jsonStrToBean(String jsonString, Class<T> cls) {
        T beanObject = gson.fromJson(jsonString, cls);
        return beanObject;
    }

    public static <T> T jsonObjectToBean(JSONObject jsonObject, Class<T> cls) {
        T beanObject = gson.fromJson(jsonObject.toString(), cls);
        return beanObject;
    }

    // 泛型在编译期类型被擦除导致报错，解决方法1
    public static <T> List<T> jsonToList(String jsonString, Class<T> cls) {
        List<T> list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
        }.getType());
        return list;
    }

    // 泛型在编译期类型被擦除导致报错，解决方法2
    public static <T> List<T> jsonToList2(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }

    // 转成list中有map的
    public static <T> List<Map<String, T>> jsonToListMaps(String jsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(jsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    public static <T> Map<String, T> jsonToMap(String jsonString) throws JsonSyntaxException, JsonParseException {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(jsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }
}
