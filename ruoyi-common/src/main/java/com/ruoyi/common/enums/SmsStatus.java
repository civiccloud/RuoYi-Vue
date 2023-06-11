package com.ruoyi.common.enums;

/**
 * 发送短信状态
 * 
 * @author ruoyi
 */
public enum SmsStatus
{
    /**
     * 1	  成功
     * 0	  其他系统错误
     * -1	  短信余额不足
     * -2	  资金账户被锁定
     * -3	  用户被锁定或未通过认证
     * -4	  号码在黑名单内
     * -5	  用户名或密码不正确
     * -6	  号码不正确
     * -7	  接口连接失败
     * -8	  号码格式错误
     * -9	  通道编号错误
     * -10	  定时发送时间不正确
     * -11	  没有输入短信内容
     * -12	  短信内容超出长度限制
     * -15	  内容含禁发关键字
     * -16	  超出发送时间范围
     * -17	  通道被关闭
     * -18	  短信内容没有签名 或 短信内容乱码
     * -30	  手机未认证
     * -31	  身份未认证
     * -32	  账户余额不足
     */
    YEBZ("-32", "账户余额不足"),
    SFWRZ("-31", "身份未认证"),
    SJWRZ("-30", "手机未认证"),
    MYQM("-18", "短信内容没有签名 或 短信内容乱码"),
    TDGB("-17", "通道被关闭"),
    CCFW("-16", "超出发送时间范围"),
    NRZJFS("-15", "内容含禁发关键字"),
    NRCCXZ("-12", "短信内容超出长度限制"),
    MYDXNR("-11", "没有输入短信内容"),
    DSFSSB("-10", "定时发送时间不正确"),
    TDBHCW("-9", "通道编号错误"),
    HMGSCW("-8", "号码格式错误"),
    JKLJSB("-7", "接口连接失败"),
    HMBZQ("-6", "号码不正确"),
    YHMMCW("-5", "用户名或密码不正确"),
    HMZHMD("-4", "号码在黑名单内"),
    YHBSD("-3", "用户被锁定或未通过认证"),
    ZHSD("-2", "资金账户被锁定"),
    YGBZ("-1", "短信余额不足"),
    ERROR("0", "其他系统错误"),
    SUCCESS("1", "成功");

    private final String code;
    private final String info;

    SmsStatus(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
