package com.demo.architect.data.helper;

public class Constants {
    public static final String USER_TYPE = "SP";
    public static final String APP_CODE = "IDS";
    //trạng thái chờ upload
    public static final int WAITING_UPLOAD = 0;

    //trạng thái đã upload
    public static final int UPLOADED = 1;
    //trạng thái chưa xác nhận
    public static final int UNCONFIRMED = 1;
    //trạng thái đã xác nhận
    public static final int CONFIRMED= 2;

    public static final int RCEIVED= 3;
    //trạng thái kết thúc
    public static final int FINISHED= 1;

    //trạng thái chưa kết thúc
    public static final int NOT_FINISHED= 0;
    public static final String RESET_DATA = "KEY_RESET_DATA";
    public static final String ACTION_RESET_DATA = "ACTION_RESET_DATA";
}
