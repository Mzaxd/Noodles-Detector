package com.mzaxd.noodles.enums;

/**
 * @author Mzaxd
 * @since 2023-02-05 11:03
 */
public enum AppHttpCodeEnum {
    /** 操作成功通用返回信息 */
    SUCCESS(200, "操作成功"),

    NEED_LOGIN(401, "需要登录后操作"),

    NO_OPERATOR_AUTH(403, "无权限操作"),

    SYSTEM_ERROR(500, "出现错误"),

    EMAIL_EXIST(503, "邮箱已存在"),

    LOGIN_ERROR(505, "邮箱或密码错误"),

    CONTENT_NOT_NULL(506, "评论不能为空"),

    FILE_TYPE_ERROR(507, "文件类型错误，请上传png文件"),

    USERNAME_NOT_NULL(508, "用户名不能为空" ),

    PASSWORD_NOT_NULL(509, "密码不能为空" ),

    NICKNAME_NOT_NULL(510, "昵称不能为空" ),

    EMAIL_NOT_NULL(511, "邮箱不能为空" ),

    NICKNAME_EXIST(512, "昵称已存在" ),

    EXIST_ASSOCIATION_CONTAINER(513, "操作失败，存在关联的容器" );

    /** 统一响应码 */
    final int code;
    /** 响应信息 */
    final String msg;

    AppHttpCodeEnum(int code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
