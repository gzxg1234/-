package com.sanron.sunweather.common;

import com.sanron.sunweather.R;

public class IconProvider {
    /**
     * 获取小图标
     */
    public static int getTypeSmallIcon(String type, boolean isDay) {
        int id = 0;
        if ("小雨".equals(type)) {
            id = R.drawable.sw_yu1;
        } else if ("小到中雨".equals(type)) {
            id = R.drawable.sw_yu2;
        } else if ("中雨".equals(type)) {
            id = R.drawable.sw_yu3;
        } else if ("中到大雨".equals(type)) {
            id = R.drawable.sw_yu4;
        } else if ("大雨".equals(type)) {
            id = R.drawable.sw_yu5;
        } else if ("大到暴雨".equals(type)) {
            id = R.drawable.sw_yu6;
        } else if ("暴雨".equals(type)) {
            id = R.drawable.sw_yu7;
        } else if ("阵雨".equals(type)) {
            id = isDay ? R.drawable.sw_zy_day : R.drawable.sw_zy_night;
        } else if ("雷阵雨".equals(type)) {
            id = R.drawable.sw_lzy;
        } else if ("雷阵雨伴有冰雹".equals(type)) {
            id = R.drawable.sw_lzy_bb;
        } else if ("雨夹雪".equals(type)) {
            id = R.drawable.sw_yu_xue;
        } else if ("小雪".equals(type)) {
            id = R.drawable.sw_xue1;
        } else if ("中雪".equals(type)) {
            id = R.drawable.sw_xue2;
        } else if ("大雪".equals(type)) {
            id = R.drawable.sw_xue3;
        } else if ("阵雪".equals(type)) {
            id = isDay ? R.drawable.sw_zx_day : R.drawable.sw_zx_night;
        } else if ("晴".equals(type)) {
            id = isDay ? R.drawable.sw_qin_day : R.drawable.sw_qin_night;
        } else if ("阴".equals(type)) {
            id = R.drawable.sw_yin;
        } else if ("多云".equals(type)) {
            id = isDay ? R.drawable.sw_dy_day : R.drawable.sw_dy_night;
        } else if ("浮尘".equals(type)) {
            id = R.drawable.sw_fuchen;
        } else if ("雾".equals(type)) {
            id = isDay ? R.drawable.sw_wu_day : R.drawable.sw_wu_night;
        } else if ("冻雨".equals(type)) {
            id = R.drawable.sw_donyu;
        } else {
            id = R.drawable.sw_undefined;
        }
        return id;
    }

    /**
     * 获取天气类型图标
     */
    public static int getTypeIcon(String type, boolean isDay) {
        int id = 0;
        if ("小雨".equals(type)) {
            id = R.drawable.w_yu1;
        } else if ("小到中雨".equals(type)) {
            id = R.drawable.w_yu2;
        } else if ("中雨".equals(type)) {
            id = R.drawable.w_yu3;
        } else if ("中到大雨".equals(type)) {
            id = R.drawable.w_yu4;
        } else if ("大雨".equals(type)) {
            id = R.drawable.w_yu5;
        } else if ("大到暴雨".equals(type)) {
            id = R.drawable.w_yu6;
        } else if ("暴雨".equals(type)) {
            id = R.drawable.w_yu7;
        } else if ("阵雨".equals(type)) {
            id = isDay ? R.drawable.w_zy_day : R.drawable.w_zy_night;
        } else if ("雷阵雨".equals(type)) {
            id = R.drawable.w_lzy;
        } else if ("雷阵雨伴有冰雹".equals(type)) {
            id = R.drawable.w_lzy_bb;
        } else if ("雨夹雪".equals(type)) {
            id = R.drawable.w_yu_xue;
        } else if ("小雪".equals(type)) {
            id = R.drawable.w_xue1;
        } else if ("中雪".equals(type)) {
            id = R.drawable.w_xue2;
        } else if ("大雪".equals(type)) {
            id = R.drawable.w_xue3;
        } else if ("阵雪".equals(type)) {
            id = isDay ? R.drawable.w_zx_day : R.drawable.w_zx_night;
        } else if ("晴".equals(type)) {
            id = isDay ? R.drawable.w_qin_day : R.drawable.w_qin_night;
        } else if ("阴".equals(type)) {
            id = R.drawable.w_yin;
        } else if ("多云".equals(type)) {
            id = isDay ? R.drawable.w_dy_day : R.drawable.w_dy_night;
        } else if ("浮尘".equals(type)) {
            id = R.drawable.w_fuchen;
        } else if ("雾".equals(type)) {
            id = isDay ? R.drawable.w_wu_day : R.drawable.w_wu_night;
        } else if ("冻雨".equals(type)) {
            id = R.drawable.w_donyu;
        } else {
            id = R.drawable.w_undefined;
        }
        return id;
    }

    /**
     * 生活指数图标
     */
    public static int getLifeIndexIcon(String name) {
        int id = 0;
        if ("空调指数".equals(name)) {
            id = R.drawable.life_0;
        } else if ("过敏指数".equals(name)) {
            id = R.drawable.life_1;
        } else if ("晨练指数".equals(name)) {
            id = R.drawable.life_2;
        } else if ("舒适度指数".equals(name)) {
            id = R.drawable.life_3;
        } else if ("穿衣指数".equals(name)) {
            id = R.drawable.life_4;
        } else if ("防晒指数".equals(name)) {
            id = R.drawable.life_5;
        } else if ("逛街指数".equals(name)) {
            id = R.drawable.life_6;
        } else if ("感冒指数".equals(name)) {
            id = R.drawable.life_7;
        } else if ("划船指数".equals(name)) {
            id = R.drawable.life_8;
        } else if ("交通指数".equals(name)) {
            id = R.drawable.life_9;
        } else if ("钓鱼指数".equals(name)) {
            id = R.drawable.life_10;
        } else if ("路况指数".equals(name)) {
            id = R.drawable.life_11;
        } else if ("晾晒指数".equals(name)) {
            id = R.drawable.life_12;
        } else if ("美发指数".equals(name)) {
            id = R.drawable.life_13;
        } else if ("夜生活指数".equals(name)) {
            id = R.drawable.life_14;
        } else if ("饮酒指数".equals(name)) {
            id = R.drawable.life_15;
        } else if ("放风筝指数".equals(name)) {
            id = R.drawable.life_16;
        } else if ("空气指数".equals(name)) {
            id = R.drawable.life_17;
        } else if ("化妆指数".equals(name)) {
            id = R.drawable.life_18;
        } else if ("旅游指数".equals(name)) {
            id = R.drawable.life_19;
        } else if ("紫外线指数".equals(name)) {
            id = R.drawable.life_20;
        } else if ("寒潮指数".equals(name)) {
            id = R.drawable.life_21;
        } else if ("洗车指数".equals(name)) {
            id = R.drawable.life_22;
        } else if ("心情指数".equals(name)) {
            id = R.drawable.life_23;
        } else if ("运动指数".equals(name)) {
            id = R.drawable.life_24;
        } else if ("约会指数".equals(name)) {
            id = R.drawable.life_25;
        } else if ("雨伞指数".equals(name)) {
            id = R.drawable.life_26;
        } else if ("中暑指数".equals(name)) {
            id = R.drawable.life_27;
        } else if ("太阳镜指数".equals(name)) {
            id = R.drawable.life_28;
        }
        return id;
    }

    public static int getWarningIcon(String warning, String degree) {
        int id = 0;
        if ("暴雨".equals(warning)) {
            if ("蓝色".equals(degree)) id = R.drawable.warn_by1;
            else if ("黄色".equals(degree)) id = R.drawable.warn_by2;
            else if ("橙色".equals(degree)) id = R.drawable.warn_by3;
            else if ("红色".equals(degree)) id = R.drawable.warn_by4;
        } else if ("台风".equals(warning)) {
            if ("蓝色".equals(degree)) id = R.drawable.warn_tf1;
            else if ("黄色".equals(degree)) id = R.drawable.warn_tf2;
            else if ("橙色".equals(degree)) id = R.drawable.warn_tf3;
            else if ("红色".equals(degree)) id = R.drawable.warn_tf4;
        } else if ("暴雪".equals(warning)) {
            if ("蓝色".equals(degree)) id = R.drawable.warn_bx1;
            else if ("黄色".equals(degree)) id = R.drawable.warn_bx2;
            else if ("橙色".equals(degree)) id = R.drawable.warn_bx3;
            else if ("红色".equals(degree)) id = R.drawable.warn_bx4;
        } else if ("寒潮".equals(warning)) {
            if ("蓝色".equals(degree)) id = R.drawable.warn_hc1;
            else if ("黄色".equals(degree)) id = R.drawable.warn_hc2;
            else if ("橙色".equals(degree)) id = R.drawable.warn_hc3;
            else if ("红色".equals(degree)) id = R.drawable.warn_hc4;
        } else if ("大风".equals(warning)) {
            if ("蓝色".equals(degree)) id = R.drawable.warn_df1;
            else if ("黄色".equals(degree)) id = R.drawable.warn_df2;
            else if ("橙色".equals(degree)) id = R.drawable.warn_df3;
            else if ("红色".equals(degree)) id = R.drawable.warn_df4;
        } else if ("沙尘暴".equals(warning)) {
            if ("蓝色".equals(degree)) id = R.drawable.warn_scb1;
            else if ("黄色".equals(degree)) id = R.drawable.warn_scb2;
            else if ("橙色".equals(degree)) id = R.drawable.warn_scb3;
            else if ("红色".equals(degree)) id = R.drawable.warn_scb4;
        } else if ("高温".equals(warning)) {
            if ("蓝色".equals(degree)) id = R.drawable.warn_gw1;
            else if ("黄色".equals(degree)) id = R.drawable.warn_gw2;
            else if ("橙色".equals(degree)) id = R.drawable.warn_gw3;
            else if ("红色".equals(degree)) id = R.drawable.warn_gw4;
        } else if ("干旱".equals(warning)) {
            if ("蓝色".equals(degree)) id = R.drawable.warn_gh1;
            else if ("黄色".equals(degree)) id = R.drawable.warn_gh2;
            else if ("橙色".equals(degree)) id = R.drawable.warn_gh3;
            else if ("红色".equals(degree)) id = R.drawable.warn_gh4;
        } else if ("雷电".equals(warning)) {
            if ("蓝色".equals(degree)) id = R.drawable.warn_ld1;
            else if ("黄色".equals(degree)) id = R.drawable.warn_ld2;
            else if ("橙色".equals(degree)) id = R.drawable.warn_ld3;
            else if ("红色".equals(degree)) id = R.drawable.warn_ld4;
        } else if ("冰雹".equals(warning)) {
            if ("蓝色".equals(degree)) id = R.drawable.warn_bb1;
            else if ("黄色".equals(degree)) id = R.drawable.warn_bb2;
            else if ("橙色".equals(degree)) id = R.drawable.warn_bb3;
            else if ("红色".equals(degree)) id = R.drawable.warn_bb4;
        } else if ("霜冻".equals(warning)) {
            if ("蓝色".equals(degree)) id = R.drawable.warn_sd1;
            else if ("黄色".equals(degree)) id = R.drawable.warn_sd2;
            else if ("橙色".equals(degree)) id = R.drawable.warn_sd3;
            else if ("红色".equals(degree)) id = R.drawable.warn_sd4;
        } else if ("大雾".equals(warning)) {
            if ("蓝色".equals(degree)) id = R.drawable.warn_dw1;
            else if ("黄色".equals(degree)) id = R.drawable.warn_dw2;
            else if ("橙色".equals(degree)) id = R.drawable.warn_dw3;
            else if ("红色".equals(degree)) id = R.drawable.warn_dw4;
        } else if ("霾".equals(warning)) {
            if ("蓝色".equals(degree)) id = R.drawable.warn_mai1;
            else if ("黄色".equals(degree)) id = R.drawable.warn_mai2;
            else if ("橙色".equals(degree)) id = R.drawable.warn_mai3;
            else if ("红色".equals(degree)) id = R.drawable.warn_mai4;
        } else if ("道路结冰".equals(warning)) {
            if ("蓝色".equals(degree)) id = R.drawable.warn_dljb1;
            else if ("黄色".equals(degree)) id = R.drawable.warn_dljb2;
            else if ("橙色".equals(degree)) id = R.drawable.warn_dljb3;
            else if ("红色".equals(degree)) id = R.drawable.warn_dljb4;
        }
        return id;
    }
}
