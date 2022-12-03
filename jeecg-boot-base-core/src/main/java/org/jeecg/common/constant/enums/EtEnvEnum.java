package org.jeecg.common.constant.enums;

/**
 * 埋点环境枚举
 * @author: xiaowei.song
 */
public enum EtEnvEnum {

    /**
     * 测试环境
     */
    TEST("test"),
    /**
     * uat环境
     *  */
    UAT("uat"),

    /**
     * 生产环境
     *  */
    PROD("prod");

    /**
     * 编码标识
     */
    String etEnv;
    /**
     * 代码生成器模板路径
     */

    /**
     * 构造器
     * @param etEnv 埋点环境
     */
    EtEnvEnum(String etEnv) {
        this.etEnv = etEnv;
    }

    /**
     * 根据code找枚举
     *
     * @param etEnv
     * @return
     */
    public static EtEnvEnum getEtEnvEnumByEnv(String etEnv) {
        for (EtEnvEnum e : EtEnvEnum.values()) {
            if (e.etEnv.equals(etEnv)) {
                return e;
            }
        }
        return null;
    }
}
