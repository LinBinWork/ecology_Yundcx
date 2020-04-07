package com.westvalley.util;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 字符串工具类
 * @author oys
 */
public class StringUtil {
	
	public static boolean isEmpty(String s){
		return s == null || s.trim().length() == 0 ? true : false;
	}
	/**
	 * 判断是否包含中文字符
	 * @param strName
	 * @return
	 */
	public static boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c) == true) {
				return isChinese(c);
			} else {
				if(i == (ch.length-1)){
					return isChinese(c);
				}else{
					continue;
				}
			}
		}
		return false;
	}
	/**
	 * 判断是否包含中文字符
	 * @param c
	 * @return
	 */
	public  static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false; 
	}
	
	/**
	 * @param o
	 * @return
	 */
	public static String toStr(Object o){
		return o == null ? "" :  o.toString().trim();
	}
	
	/**
	 * 转换为正常数字
	 * @param s
	 * @return
	 */
	public static String toNum(String s){
		if(isEmpty(s))return "0";
		return s.replaceAll("[^\\d.-]+","").trim();
	}

	/**
	 * 转换为正常数字
	 * @param amt
	 * @return
	 */
	public static String toNum(double amt){
		String releaseAmt = String.valueOf(amt);
		if(releaseAmt.contains("E")){//如果是科学计数
			releaseAmt = new BigDecimal(amt).toString();
		}
		return releaseAmt;
	}

	/**
	 * 移动字符串中的html标签
	 * @param s
	 * @return
	 */
	public static String removeHTMLTag(String s){
		if (s == null) return "";
        s = weaver.general.Util.toExcelData(s);
        s = weaver.general.Util.replaceHtml(s);
        return s;
	}
	/**
	 * 转换URL的中文
	 * @param url
	 * @return
	 */
	public static String formatURL(String url,String code){
		String urlStr = "";
		//将中文分割出来 以UTF8编码
		String strs[] = url.split("/");
		if(strs.length==0) urlStr = url;
		int i = 0;
		try {
			for(String s : strs){
				if(i == (strs.length-1)){
					if(isChinese(s)){
						urlStr += URLEncoder.encode(s, code)+"";
					}else{
						urlStr += s+"";
					}
				}else{
					if(isChinese(s)){
						urlStr += URLEncoder.encode(s, code)+"/";
					}else{
						urlStr += s+"/";
					}
				}
				i++;
			}
		} catch (Exception e) {
			return null;
		}
		return urlStr;
	}
	/**
	 * 截取固定长度字符串
	 * @param str
	 * @param len
	 * @return
	 */
	public static String truncateStr(String str, int len) {
		if (str == null || str.equals("") || len == 0) {
			return "";
		}
		char[] charArr = str.toCharArray();
		int count = 0;
		StringBuilder sb = new StringBuilder("");
		for (char c : charArr) {
			if (count < len) {
				if (isChinese(c)) {
					if (count + 2 == len) {
						return sb.toString();

					} else {
						count = count + 3;
						sb.append(c);
					}
				} else {
					count = count + 1;
					sb.append(c);
				}
			} else {
				break;
			}
		}
		return sb.toString();
	}

	/**
	 *
	 * @param list
	 * @return 逗号分隔 1,2,3
	 */
	public static String list2Str(Collection<? extends Object> list){
		if(list == null || list.size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Object s : list) {
			if(i == 0){
				sb.append(s);
			}else{
				sb.append(",").append(s);
			}
			i++;
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		List<Integer> a = new ArrayList<>();
		a.add(1);
		a.add(2);
		a.add(2);
		a.add(2);
		a.add(2);
		System.out.println(list2Str(a));
	}
}

