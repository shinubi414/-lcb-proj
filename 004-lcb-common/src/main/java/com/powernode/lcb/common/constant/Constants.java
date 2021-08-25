package com.powernode.lcb.common.constant;

public class Constants {

    /**
     * 验证码
     */
    public static final String RANDOMCODE = "RANDOMVALIDATECODE";

    /**
     * 历史平均年化收益率
     */
    public static final String HISTORY_AVERAGE_RATE = "historyAverageRate";

    /**
     * 平台注册总人数
     */
    public static final String ALL_USER_COUNT = "allUserCount";

    /**
     * 平台累计投资金额
     */
    public static final String ALL_BID_MONEY = "allBidMoney";

    /**
     * 产品类型:新手宝0
     */
    public static final Integer PRODUCT_TYPE_X = 0;

    /**
     * 产品类型:优选产品1
     */
    public static final Integer PRODUCT_TYPE_U = 1;

    /**
     * 产品类型:散标产品2
     */
    public static final Integer PRODUCT_TYPE_S = 2;

    /**
     * 用户信息
     */
    public static final String SESSION_USER = "user";

    /**
     * 投资排行榜
     */
    public static final String INVEST_TOP = "investTop";

    /**
     * 唯一数字
     */
    public static final String ONLY_NUMBER = "onlyNumber";

    public static final int STATUS_OK = 2000;  //成功
    public static final int STATUS_ERROR = 2001; //发生错误
    public static final int STATUS_ERROR_LOGIN = 2002;  //未登录
    public static final int STATUS_ERROR_REALNAME = 2003;  //未实名
    public static final int STATUS_ERROR_MONRY = 2004;  //账户金额不足

}
