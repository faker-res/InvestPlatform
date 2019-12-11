package la.niub.abcapi.invest.seller.component.hanlp;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import la.niub.abcapi.invest.seller.component.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HanlpUtil {

    public static String delHtmlToContext(String context){
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(context);
        context=m_script.replaceAll(""); //过滤script标签

        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(context);
        context=m_style.replaceAll(""); //过滤style标签

        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(context);
        context=m_html.replaceAll(""); //过滤html标签
        context = context.replace("&nbsp;","");

        return context.trim(); //返回文本字符串
    }

    public static List<String> segmentWord(String... text) {
        List<String> words = new ArrayList<>();
        for (String t : text) {
            if (StringUtils.isEmpty(t)) {
                continue;
            }
            List<Term> termList = HanLP.segment(t);
            for (Term term : termList) {
                if (!words.contains(term.word)) {
                    words.add(term.word);
                }
            }
        }
        return words;
    }

    /**
     * 获取所有切割分词
     * @param str
     * @param length
     * @param remain_less
     * @return
     */
    public static List<String> getSplit(String str, Integer length,Boolean remain_less){
        Set<String> split = new HashSet<>();
        for (Integer i = 0;i<length;i++){
            split.addAll(StringUtil.lengthSplit(str,i,length,remain_less));
        }
        return new ArrayList<>(split);
    }

    /**
     * 获取切割中文字串的结果
     * @param content
     * @return
     */
    public static Set<String> getChineseSplit(String content){
        Set<String> split = new HashSet<>();
        List<String> chineses = StringUtil.getChinese(content);
        String chineses_join = String.join("",chineses);
        split.addAll(StringUtil.lengthSplit(chineses_join,0,2,true));
        split.addAll(StringUtil.lengthSplit(chineses_join,0,3,true));
        split.addAll(StringUtil.lengthSplit(chineses_join,0,4,true));
        split.addAll(StringUtil.lengthSplit(chineses_join,1,2,true));
        split.addAll(StringUtil.lengthSplit(chineses_join,1,3,true));
        split.addAll(StringUtil.lengthSplit(chineses_join,1,4,true));
        split.addAll(StringUtil.lengthSplit(chineses_join,2,3,true));
        split.addAll(StringUtil.lengthSplit(chineses_join,2,4,true));
        for (String chinese : chineses){
            split.addAll(StringUtil.lengthSplit(chinese,0,2,true));
            split.addAll(StringUtil.lengthSplit(chinese,0,3,true));
            split.addAll(StringUtil.lengthSplit(chinese,0,4,true));
            split.addAll(StringUtil.lengthSplit(chinese,1,2,true));
            split.addAll(StringUtil.lengthSplit(chinese,1,3,true));
            split.addAll(StringUtil.lengthSplit(chinese,1,4,true));
            split.addAll(StringUtil.lengthSplit(chinese,2,3,true));
            split.addAll(StringUtil.lengthSplit(chinese,2,4,true));
        }
        Set<String> _split = new HashSet<>();
        for (String item : split){
            if (item != null && item.length() > 1){
                _split.add(item);
            }
        }
        return _split;
    }
}
