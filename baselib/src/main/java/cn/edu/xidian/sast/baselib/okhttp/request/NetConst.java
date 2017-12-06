package cn.edu.xidian.sast.baselib.okhttp.request;

/**
 * Created by ShiningForever on 2017/12/5.
 */

public class NetConst {
    // 本地使用的网络数据处理错误码，约定：全部使用负值
    // 处理比如网络失败、JSON数据解析异常这种错误，不处理逻辑上的失败
    public static final int NET_IO_ERROR = -1;
    public static final int BAD_RESPONSE_ERROR = -2;
    public static final int JSON_PARSE_ERROR = -3;
    public static final int RET_STATUS_ERROR = -4;
    public static final int UNKNOWN = -5;

    // 与服务器端约定好的JSON中的状态码，这里是逻辑上的状态
    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;
    public static final int EXT = 2;

    // 返回数据JSON字段映射
    public static final String RET_STATUS = "status";
    public static final String RET_MSG = "msg";
    public static final String RET_DATA = "data";
}
