package pe.com.mipredio.utils;

import android.Manifest;

import pe.com.mipredio.R;

public class Consts {
    public static final String _STATUS_REGISTER = "completado";
    public static final int _COLOR_REGISTER = R.color.teal_500;
    public static final int _IMG_REGISTER = R.drawable.ic_done_all_24;

    public static final String _STATUS_PENDING = "pendiente";
    public static final int _COLOR_PENDING = R.color.grey_400;
    public static final int _IMG_PENDING = R.drawable.ic_done_24;

    public static final String _STATUS_SEND = "enviado";
    public static final int _COLOR_SEND = R.color.teal_500;
    public static final int _IMG_SEND = R.drawable.ic_send_24;

    // Notifications ID
    public static final int NOTIFICATION_CLOSE_DAY = 1;

    public static final String BREAK_LINE = "\n";

    public static final String LOGIN_MODE = "entry_mode"; // account, anonymous
    public static final String TOKEN = "_token";


    public static final Integer ERROR_UNAUTHORIZED = 401;
    public static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    public  static  final  int CAMERA_REQUEST_CODE = 100;

}
