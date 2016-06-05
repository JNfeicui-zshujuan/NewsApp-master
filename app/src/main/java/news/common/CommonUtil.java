package news.common;

/**
 * Created by zhengshujuan on 2016/5/30.
 */
/*
* 联网存储数据
* */
public class CommonUtil  {
    //联网的IP
    public static String NETIP="118.244.212.82";
    //联网的路径
    public static String NETPATH="http://"+NETIP+":8080/newsClient";
    //SharedPreferences保存用户名键
    public  static final String SHARE_USER_NAME="userName";
    //SharedPreferences保存用户密码
    public static final String SHARE_USER_PWD="userPed";
    //保存是否是第一次登陆
    public static final String SHARE_IS_FRIST_RUN="isFristRun";
    //SharedPreference存储路径
    public static final String SHAREPATH="news_share";
}
