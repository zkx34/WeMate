package com.zkx.weipo.app.util;

import com.zkx.weipo.app.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/21.
 */
public class EmotionUtils implements Serializable {

    public static Map<String, Integer> emojiMap;

    static {
        emojiMap = new HashMap<String, Integer>();
        emojiMap.put("[微笑]", R.drawable.d_hehe);
        emojiMap.put("[呵呵]", R.drawable.d_hehe);
        emojiMap.put("[嘻嘻]", R.drawable.d_xixi);
        emojiMap.put("[哈哈]", R.drawable.d_haha);
        emojiMap.put("[爱你]", R.drawable.d_aini);
        emojiMap.put("[挖鼻屎]", R.drawable.d_wabishi);
        emojiMap.put("[吃惊]", R.drawable.d_chijing);
        emojiMap.put("[晕]", R.drawable.d_yun);
        emojiMap.put("[泪]", R.drawable.d_lei);
        emojiMap.put("[馋嘴]", R.drawable.d_chanzui);
        emojiMap.put("[抓狂]", R.drawable.d_zhuakuang);
        emojiMap.put("[哼]", R.drawable.d_heng);
        emojiMap.put("[可爱]", R.drawable.d_keai);
        emojiMap.put("[怒]", R.drawable.d_nu);
        emojiMap.put("[汗]", R.drawable.d_han);
        emojiMap.put("[害羞]", R.drawable.d_haixiu);
        emojiMap.put("[睡觉]", R.drawable.d_shuijiao);
        emojiMap.put("[钱]", R.drawable.d_qian);
        emojiMap.put("[偷笑]", R.drawable.d_touxiao);
        emojiMap.put("[笑cry]", R.drawable.d_xiaoku);
        emojiMap.put("[doge]", R.drawable.d_doge);
        emojiMap.put("[喵喵]", R.drawable.d_miao);
        emojiMap.put("[酷]", R.drawable.d_ku);
        emojiMap.put("[衰]", R.drawable.d_shuai);
        emojiMap.put("[闭嘴]", R.drawable.d_bizui);
        emojiMap.put("[鄙视]", R.drawable.d_bishi);
        emojiMap.put("[花心]", R.drawable.d_huaxin);
        emojiMap.put("[鼓掌]", R.drawable.d_guzhang);
        emojiMap.put("[悲伤]", R.drawable.d_beishang);
        emojiMap.put("[思考]", R.drawable.d_sikao);
        emojiMap.put("[生病]", R.drawable.d_shengbing);
        emojiMap.put("[亲亲]", R.drawable.d_qinqin);
        emojiMap.put("[怒骂]", R.drawable.d_numa);
        emojiMap.put("[太开心]", R.drawable.d_taikaixin);
        emojiMap.put("[懒得理你]", R.drawable.d_landelini);
        emojiMap.put("[右哼哼]", R.drawable.d_youhengheng);
        emojiMap.put("[左哼哼]", R.drawable.d_zuohengheng);
        emojiMap.put("[嘘]", R.drawable.d_xu);
        emojiMap.put("[委屈]", R.drawable.d_weiqu);
        emojiMap.put("[吐]", R.drawable.d_tu);
        emojiMap.put("[可怜]", R.drawable.d_kelian);
        emojiMap.put("[打哈气]", R.drawable.d_dahaqi);
        emojiMap.put("[失望]", R.drawable.d_shiwang);
        emojiMap.put("[顶]", R.drawable.d_ding);
        emojiMap.put("[疑问]", R.drawable.d_yiwen);
        emojiMap.put("[困]", R.drawable.d_kun);
        emojiMap.put("[感冒]", R.drawable.d_ganmao);
        emojiMap.put("[拜拜]", R.drawable.d_baibai);
        emojiMap.put("[黑线]", R.drawable.d_heixian);
        emojiMap.put("[阴险]", R.drawable.d_yinxian);
        emojiMap.put("[打脸]", R.drawable.d_dalian);
        emojiMap.put("[猪头]", R.drawable.d_zhutou);
        emojiMap.put("[熊猫]", R.drawable.d_xiongmao);
        emojiMap.put("[兔子]", R.drawable.d_tuzi);
        emojiMap.put("[good]",R.drawable.good);
        emojiMap.put("[赞]",R.drawable.zan);
        emojiMap.put("[挤眼]",R.drawable.zy_org);
        emojiMap.put("[心]",R.drawable.xin);
        emojiMap.put("[耶]",R.drawable.ye);
        emojiMap.put("[作揖]",R.drawable.zuoji);
        emojiMap.put("[发红包]",R.drawable.fhb);
        emojiMap.put("[月亮]",R.drawable.yl);
        emojiMap.put("[草泥马]",R.drawable.cnm);

    }

    public static int getImgByName(String imgName) {
        Integer integer = emojiMap.get(imgName);
        return integer == null ? -1 : integer;
    }
}
