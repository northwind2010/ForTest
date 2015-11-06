//ADD COPYRIGHT BY DirCopy Mon Sep 02 10:42:07 CST 2013

/************************************************************************

* Copyright (C) 2003-2013 UMPay.com

* Copyright (C) 1997-2013 "Liu Sheng"

* Copyright (C) 1997-2013 "Liu Sheng"

* All rights reserved.

************************************************************************

* Copyleft  (C) 请遵守GPLv3+版权许可，同时保留所有文字和注释。

************************************************************************

* This file is part of BS3-Framework.

*

* BS3-Framework is free software: you can redistribute it and/or modify

* it under the terms of the GNU General Public License as published by

* the Free Software Foundation, either version 3 of the License, or

* (at your option) any later version.

*

* BS3-Framework is distributed in the hope that it will be useful,

* but WITHOUT ANY WARRANTY; without even the implied warranty of

* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the

* GNU General Public License for more details.

*

* You should have received a copy of the GNU General Public License

* along with BS3-Framework. If not, see < http://www.gnu.org/licenses/>.

************************************************************************

* The package of :

* com.bs.*

* com.bs2.*

* com.bs3.*

* is part of BS3-Framework written by Liu Sheng (nike.lius@gmail.com).

* Other contributors include :

* HouZhanBin,

* LiWei,

* YinShu,

* ZhangYao,

************************************************************************

* The package of

* org.apache.mina.filter.codec.http

* is part of AsyncWeb HTTP codec written by (dev@mina.apache.org), which

* has moved to mina-filter-codec-http as asyncweb-common-0.9.0-SNAPSHOT.jar

* or asyncweb-core-0.9.0-20070614.034125.jar.

* See < http://mina.apache.org/asyncweb-project/index.html');"> http://mina.apache.org/asyncweb-project/index.html>

* See < http://svn.apache.org/viewvc/mina/trunk/filter-codec-http/src/main/java/org/apache/mina/filter/codec/http/?pathrev=612014>

************************************************************************

* The package of

* org.apache.mina.integration.beans

* org.apache.mina.integration.jmx

* org.apache.mina.integration.ognl

* is part of Apache MINA Project written by (dev@mina.apache.org)

* See

************************************************************************

* Part of BS3-Framework source code is an implementation for patents

* which are also invented by Liu Sheng (nike.lius@gmail.com).

* See  http://www.soopat.com/Patent/201010506543 数据访问方法及装置

* See  http://www.soopat.com/Patent/2013xxxxxxxx 一种号码集合的存储方法和查询方法(TD1109459F)

************************************************************************

* About BS3-Framework and the Patent, we also agreed to Innovator's Patent Agreement (IPA)

*

* The Innovators Patent Agreement (IPA) is a new way to do patent assignment

* that keeps control in the hands of engineers and designers. It is a

* commitment from a company to its employees that patents can only be used

* for defensive purposes. The company will not use the patents in offensive

* litigation without the permission of the inventors. This control flows with

* the patents, so if the company sells the patents to others, the assignee

* can only use the patents as the inventor intended.

*

* See the Innovator’s Patent Agreement (IPA) for more details.

* See < https://github.com/twitter/innovators-patent-agreement>

************************************************************************

*/

//ADD COPYRIGHT END

package com.umpay;



import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;



/* 

 * http://www.down22.org/plus/view.php?aid=7879 简单方法在C#中取得汉字的拼音的首字母

 * http://wallimn.javaeye.com/blog/374917 C#实现汉字自动转拼音码

 * http://www.cnblogs.com/niexian/articles/1696685.html 汉字转拼音Java代码，应用于DWR【只转首字母】

 * http://edu.itbulo.com/200705/115148.htm 输入汉字自动转为拼音(jsp实现方式)

 * http://lbh087.javaeye.com/blog/206302 java 汉字转化为全拼（查表法，方法太笨）

 * http://www.javaeye.com/topic/814845 获取汉语拼音(net.sourceforge.pinyin4j)...据说对多音支持不好！比如  银行  会显示 yin xing

 * http://hi.baidu.com/suofang/blog/item/5a2e6ed097350384a0ec9c2f.html 用Java转化汉字为拼音全拼【转】

 */

public class MyPinyin {


	private final static int[] py_areacode = { 45217, 45253, 45761, 46318,

			46826, 47010, 47297, 47614, 48119, 48119, 49062, 49324, 49896,

			50371, 50614, 50622, 50906, 51387, 51446, 52218, 52698, 52698,

			52698, 52980, 53689, 54481 };



	public static char getPinyinFirst(char cnChar) throws Exception {

		byte[] arrCN = ("" + cnChar).getBytes("GBK");// [C#]

		char enChar = '*';

		if (arrCN.length > 1) {

			int c_area = (short) (arrCN[0] & 0xff);

			int c_pos0 = (short) (arrCN[1] & 0xff);

			int c_code = (c_area << 8) + c_pos0;

			enChar = '*'; // 缺省，未知值。

			for (int i = 0; i < 26; i++) {

				int max = 55290;

				if (i != 25)

					max = py_areacode[i + 1];

				if (py_areacode[i] <= c_code && c_code < max) {

					// return Encoding.Default.GetString(new

					// byte[]{(byte)(65+i)});

					enChar = (char) ('A' + i); // 'a'+i;

					break;

				}

			}

		} else

			enChar = cnChar;

		return enChar;

	}



	public static String getPinyinFirst(String strText) throws Exception {

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < strText.length(); i++) {

			char cn = strText.charAt(i);

			sb.append(getPinyinFirst(cn));

			// sb.append(getPinyinFirst2_err(cn));

		}

		return sb.toString();

	}



	public static String getPinyinFull(String strText) {

		return PY3.getFullSpell(strText, ' ');

	}



	// ---------------------------------

	private static class PY0_err { // 根据C#改造

		// #region 拼音编码

		private static int[] pyValue = new int[] { -20319, -20317, -20304,

				-20295, -20292, -20283, -20265, -20257, -20242, -20230, -20051,

				-20036, -20032, -20026, -20002, -19990, -19986, -19982, -19976,

				-19805, -19784, -19775, -19774, -19763, -19756, -19751, -19746,

				-19741, -19739, -19728, -19725, -19715, -19540, -19531, -19525,

				-19515, -19500, -19484, -19479, -19467, -19289, -19288, -19281,

				-19275, -19270, -19263, -19261, -19249, -19243, -19242, -19238,

				-19235, -19227, -19224, -19218, -19212, -19038, -19023, -19018,

				-19006, -19003, -18996, -18977, -18961, -18952, -18783, -18774,

				-18773, -18763, -18756, -18741, -18735, -18731, -18722, -18710,

				-18697, -18696, -18526, -18518, -18501, -18490, -18478, -18463,

				-18448, -18447, -18446, -18239, -18237, -18231, -18220, -18211,

				-18201, -18184, -18183, -18181, -18012, -17997, -17988, -17970,

				-17964, -17961, -17950, -17947, -17931, -17928, -17922, -17759,

				-17752, -17733, -17730, -17721, -17703, -17701, -17697, -17692,

				-17683, -17676, -17496, -17487, -17482, -17468, -17454, -17433,

				-17427, -17417, -17202, -17185, -16983, -16970, -16942, -16915,

				-16733, -16708, -16706, -16689, -16664, -16657, -16647, -16474,

				-16470, -16465, -16459, -16452, -16448, -16433, -16429, -16427,

				-16423, -16419, -16412, -16407, -16403, -16401, -16393, -16220,

				-16216, -16212, -16205, -16202, -16187, -16180, -16171, -16169,

				-16158, -16155, -15959, -15958, -15944, -15933, -15920, -15915,

				-15903, -15889, -15878, -15707, -15701, -15681, -15667, -15661,

				-15659, -15652, -15640, -15631, -15625, -15454, -15448, -15436,

				-15435, -15419, -15416, -15408, -15394, -15385, -15377, -15375,

				-15369, -15363, -15362, -15183, -15180, -15165, -15158, -15153,

				-15150, -15149, -15144, -15143, -15141, -15140, -15139, -15128,

				-15121, -15119, -15117, -15110, -15109, -14941, -14937, -14933,

				-14930, -14929, -14928, -14926, -14922, -14921, -14914, -14908,

				-14902, -14894, -14889, -14882, -14873, -14871, -14857, -14678,

				-14674, -14670, -14668, -14663, -14654, -14645, -14630, -14594,

				-14429, -14407, -14399, -14384, -14379, -14368, -14355, -14353,

				-14345, -14170, -14159, -14151, -14149, -14145, -14140, -14137,

				-14135, -14125, -14123, -14122, -14112, -14109, -14099, -14097,

				-14094, -14092, -14090, -14087, -14083, -13917, -13914, -13910,

				-13907, -13906, -13905, -13896, -13894, -13878, -13870, -13859,

				-13847, -13831, -13658, -13611, -13601, -13406, -13404, -13400,

				-13398, -13395, -13391, -13387, -13383, -13367, -13359, -13356,

				-13343, -13340, -13329, -13326, -13318, -13147, -13138, -13120,

				-13107, -13096, -13095, -13091, -13076, -13068, -13063, -13060,

				-12888, -12875, -12871, -12860, -12858, -12852, -12849, -12838,

				-12831, -12829, -12812, -12802, -12607, -12597, -12594, -12585,

				-12556, -12359, -12346, -12320, -12300, -12120, -12099, -12089,

				-12074, -12067, -12058, -12039, -11867, -11861, -11847, -11831,

				-11798, -11781, -11604, -11589, -11536, -11358, -11340, -11339,

				-11324, -11303, -11097, -11077, -11067, -11055, -11052, -11045,

				-11041, -11038, -11024, -11020, -11019, -11018, -11014, -10838,

				-10832, -10815, -10800, -10790, -10780, -10764, -10587, -10544,

				-10533, -10519, -10331, -10329, -10328, -10322, -10315, -10309,

				-10307, -10296, -10281, -10274, -10270, -10262, -10260, -10256,

				-10254 };

		private static String[] pyName = new String[] { "A", "Ai", "An", "Ang",

				"Ao", "Ba", "Bai", "Ban", "Bang", "Bao", "Bei", "Ben", "Beng",

				"Bi", "Bian", "Biao", "Bie", "Bin", "Bing", "Bo", "Bu", "Ba",

				"Cai", "Can", "Cang", "Cao", "Ce", "Ceng", "Cha", "Chai",

				"Chan", "Chang", "Chao", "Che", "Chen", "Cheng", "Chi",

				"Chong", "Chou", "Chu", "Chuai", "Chuan", "Chuang", "Chui",

				"Chun", "Chuo", "Ci", "Cong", "Cou", "Cu", "Cuan", "Cui",

				"Cun", "Cuo", "Da", "Dai", "Dan", "Dang", "Dao", "De", "Deng",

				"Di", "Dian", "Diao", "Die", "Ding", "Diu", "Dong", "Dou",

				"Du", "Duan", "Dui", "Dun", "Duo", "E", "En", "Er", "Fa",

				"Fan", "Fang", "Fei", "Fen", "Feng", "Fo", "Fou", "Fu", "Ga",

				"Gai", "Gan", "Gang", "Gao", "Ge", "Gei", "Gen", "Geng",

				"Gong", "Gou", "Gu", "Gua", "Guai", "Guan", "Guang", "Gui",

				"Gun", "Guo", "Ha", "Hai", "Han", "Hang", "Hao", "He", "Hei",

				"Hen", "Heng", "Hong", "Hou", "Hu", "Hua", "Huai", "Huan",

				"Huang", "Hui", "Hun", "Huo", "Ji", "Jia", "Jian", "Jiang",

				"Jiao", "Jie", "Jin", "Jing", "Jiong", "Jiu", "Ju", "Juan",

				"Jue", "Jun", "Ka", "Kai", "Kan", "Kang", "Kao", "Ke", "Ken",

				"Keng", "Kong", "Kou", "Ku", "Kua", "Kuai", "Kuan", "Kuang",

				"Kui", "Kun", "Kuo", "La", "Lai", "Lan", "Lang", "Lao", "Le",

				"Lei", "Leng", "Li", "Lia", "Lian", "Liang", "Liao", "Lie",

				"Lin", "Ling", "Liu", "Long", "Lou", "Lu", "Lv", "Luan", "Lue",

				"Lun", "Luo", "Ma", "Mai", "Man", "Mang", "Mao", "Me", "Mei",

				"Men", "Meng", "Mi", "Mian", "Miao", "Mie", "Min", "Ming",

				"Miu", "Mo", "Mou", "Mu", "Na", "Nai", "Nan", "Nang", "Nao",

				"Ne", "Nei", "Nen", "Neng", "Ni", "Nian", "Niang", "Niao",

				"Nie", "Nin", "Ning", "Niu", "Nong", "Nu", "Nv", "Nuan", "Nue",

				"Nuo", "O", "Ou", "Pa", "Pai", "Pan", "Pang", "Pao", "Pei",

				"Pen", "Peng", "Pi", "Pian", "Piao", "Pie", "Pin", "Ping",

				"Po", "Pu", "Qi", "Qia", "Qian", "Qiang", "Qiao", "Qie", "Qin",

				"Qing", "Qiong", "Qiu", "Qu", "Quan", "Que", "Qun", "Ran",

				"Rang", "Rao", "Re", "Ren", "Reng", "Ri", "Rong", "Rou", "Ru",

				"Ruan", "Rui", "Run", "Ruo", "Sa", "Sai", "San", "Sang", "Sao",

				"Se", "Sen", "Seng", "Sha", "Shai", "Shan", "Shang", "Shao",

				"She", "Shen", "Sheng", "Shi", "Shou", "Shu", "Shua", "Shuai",

				"Shuan", "Shuang", "Shui", "Shun", "Shuo", "Si", "Song", "Sou",

				"Su", "Suan", "Sui", "Sun", "Suo", "Ta", "Tai", "Tan", "Tang",

				"Tao", "Te", "Teng", "Ti", "Tian", "Tiao", "Tie", "Ting",

				"Tong", "Tou", "Tu", "Tuan", "Tui", "Tun", "Tuo", "Wa", "Wai",

				"Wan", "Wang", "Wei", "Wen", "Weng", "Wo", "Wu", "Xi", "Xia",

				"Xian", "Xiang", "Xiao", "Xie", "Xin", "Xing", "Xiong", "Xiu",

				"Xu", "Xuan", "Xue", "Xun", "Ya", "Yan", "Yang", "Yao", "Ye",

				"Yi", "Yin", "Ying", "Yo", "Yong", "You", "Yu", "Yuan", "Yue",

				"Yun", "Za", "Zai", "Zan", "Zang", "Zao", "Ze", "Zei", "Zen",

				"Zeng", "Zha", "Zhai", "Zhan", "Zhang", "Zhao", "Zhe", "Zhen",

				"Zheng", "Zhi", "Zhong", "Zhou", "Zhu", "Zhua", "Zhuai",

				"Zhuan", "Zhuang", "Zhui", "Zhun", "Zhuo", "Zi", "Zong", "Zou",

				"Zu", "Zuan", "Zui", "Zun", "Zuo" };

		// / 把汉字转换成拼音(全拼)

		// / <param name="hzString">汉字字符串</param>

		// / <returns>转换后的拼音(全拼)字符串</returns>

		private static final Pattern py_pattern_gb = Pattern.compile(

				"^[\u4e00-\u9fa5]$", Pattern.CASE_INSENSITIVE);// 匹配中文字符



		public static String getPinyinLetters(String hzString) {

			// Regex regex = new Regex("^[\u4e00-\u9fa5]$");// 匹配中文字符

			// Matcher matcher = py_pattern_gb.matcher("^[\u4e00-\u9fa5]$");

			byte[] array = new byte[2];

			String pyString = "";

			int chrAsc = 0;

			int i1 = 0;

			int i2 = 0;

			char[] noWChar = hzString.toCharArray();

			;

			for (int j = 0; j < noWChar.length; j++) {// 中文字符

				// if (regex.IsMatch(noWChar[j].ToString())){

				String char_s = Character.valueOf(noWChar[j]).toString();

				Matcher matcher = py_pattern_gb.matcher(char_s);

				if (matcher.matches()) {

					// array =

					// System.Text.Encoding.Default.GetBytes(noWChar[j].ToString());

					array = char_s.getBytes();

					i1 = (short) (array[0]);

					i2 = (short) (array[1]);

					chrAsc = i1 * 256 + i2 - 65536;

					if (chrAsc > 0 && chrAsc < 160) {

						pyString += noWChar[j];

					} else {// 修正部分文字

						if (chrAsc == -9254) { // 修正“圳”字

							pyString += "Zhen";

						} else {

							for (int i = (pyValue.length - 1); i >= 0; i--) {

								if (pyValue[i] <= chrAsc) {

									pyString += pyName[i];

									break;

								}

							}

						}

					}

				} else {// 非中文字符

					pyString += char_s;// noWChar[j].ToString();

				}

			}

			return pyString;

		}



		// / 取单个字符的拼音声母 (拼音首字母)

		// / @return 拼音首字母</returns>

		private static String getPinyinFirst2_err(char c) {

			String s2 = Character.valueOf(c).toString();

			byte[] array = new byte[2];

			// array = System.Text.Encoding.Default.GetBytes(c);

			array = s2.getBytes(Charset.forName("UTF-16LE"));

			if (array.length < 2) {
//				System.out.println("getPinyinFirst2(%s) = %s", c, MyUtil.bcd(array));
//				_log.warn("getPinyinFirst2(%s) = %s", c, MyUtil.bcd(array));

			}

			int i = (short) (array[0] - '\0') * 256

					+ ((short) (array[1] - '\0'));

			if (i < 0xB0A1)

				return "*";

			if (i < 0xB0C5)

				return "a";

			if (i < 0xB2C1)

				return "b";

			if (i < 0xB4EE)

				return "c";

			if (i < 0xB6EA)

				return "d";

			if (i < 0xB7A2)

				return "e";

			if (i < 0xB8C1)

				return "f";

			if (i < 0xB9FE)

				return "g";

			if (i < 0xBBF7)

				return "h";

			if (i < 0xBFA6)

				return "g";

			if (i < 0xC0AC)

				return "k";

			if (i < 0xC2E8)

				return "l";

			if (i < 0xC4C3)

				return "m";

			if (i < 0xC5B6)

				return "n";

			if (i < 0xC5BE)

				return "o";

			if (i < 0xC6DA)

				return "p";

			if (i < 0xC8BB)

				return "q";

			if (i < 0xC8F6)

				return "r";

			if (i < 0xCBFA)

				return "s";

			if (i < 0xCDDA)

				return "t";

			if (i < 0xCEF4)

				return "w";

			if (i < 0xD1B9)

				return "x";

			if (i < 0xD4D1)

				return "y";

			if (i < 0xD7FA)

				return "z";



			return "*";

		}

	}



	private static class PY1_err { // 根据C#改造

		// @see http://www.blogjava.net/vvgg/articles/117663.html

		// 输入汉字自动转为拼音(jsp实现方式)

		public static final Hashtable ht = new Hashtable(10);



		private static String g(Integer n) {

			int num = n.intValue();

			if (num > 0 && num < 160) {

				return String.valueOf((char) num);

			} else if (num < -20319 || num > -10247) {

				return "";

			} else {

				if (ht.size() == 0) {

					b();

				}

				while (!ht.containsKey(Integer.toString(num)))

					num--;

				return ht.get(Integer.toString(num)).toString();

			}

		}



		private static void b() {

			ht.put("-20319", "a");

			ht.put("-20317", "ai");

			ht.put("-20304", "an");

			ht.put("-20295", "ang");

			ht.put("-20292", "ao");

			ht.put("-20283", "ba");

			ht.put("-20265", "bai");

			ht.put("-20257", "ban");

			ht.put("-20242", "bang");

			ht.put("-20230", "bao");

			ht.put("-20051", "bei");

			ht.put("-20036", "ben");

			ht.put("-20032", "beng");

			ht.put("-20026", "bi");

			ht.put("-20002", "bian");

			ht.put("-19990", "biao");

			ht.put("-19986", "bie");

			ht.put("-19982", "bin");

			ht.put("-19976", "bing");

			ht.put("-19805", "bo");

			ht.put("-19784", "bu");

			ht.put("-19775", "ca");

			ht.put("-19774", "cai");

			ht.put("-19763", "can");

			ht.put("-19756", "cang");

			ht.put("-19751", "cao");

			ht.put("-19746", "ce");

			ht.put("-19741", "ceng");

			ht.put("-19739", "cha");

			ht.put("-19728", "chai");

			ht.put("-19725", "chan");

			ht.put("-19715", "chang");

			ht.put("-19540", "chao");

			ht.put("-19531", "che");

			ht.put("-19525", "chen");

			ht.put("-19515", "cheng");

			ht.put("-19500", "chi");

			ht.put("-19484", "chong");

			ht.put("-19479", "chou");

			ht.put("-19467", "chu");

			ht.put("-19289", "chuai");

			ht.put("-19288", "chuan");

			ht.put("-19281", "chuang");

			ht.put("-19275", "chui");

			ht.put("-19270", "chun");

			ht.put("-19263", "chuo");

			ht.put("-19261", "ci");

			ht.put("-19249", "cong");

			ht.put("-19243", "cou");

			ht.put("-19242", "cu");

			ht.put("-19238", "cuan");

			ht.put("-19235", "cui");

			ht.put("-19227", "cun");

			ht.put("-19224", "cuo");

			ht.put("-19218", "da");

			ht.put("-19212", "dai");

			ht.put("-19038", "dan");

			ht.put("-19023", "dang");

			ht.put("-19018", "dao");

			ht.put("-19006", "de");

			ht.put("-19003", "deng");

			ht.put("-18996", "di");

			ht.put("-18977", "dian");

			ht.put("-18961", "diao");

			ht.put("-18952", "die");

			ht.put("-18783", "ding");

			ht.put("-18774", "diu");

			ht.put("-18773", "dong");

			ht.put("-18763", "dou");

			ht.put("-18756", "du");

			ht.put("-18741", "duan");

			ht.put("-18735", "dui");

			ht.put("-18731", "dun");

			ht.put("-18722", "duo");

			ht.put("-18710", "e");

			ht.put("-18697", "en");

			ht.put("-18696", "er");

			ht.put("-18526", "fa");

			ht.put("-18518", "fan");

			ht.put("-18501", "fang");

			ht.put("-18490", "fei");

			ht.put("-18478", "fen");

			ht.put("-18463", "feng");

			ht.put("-18448", "fo");

			ht.put("-18447", "fou");

			ht.put("-18446", "fu");

			ht.put("-18239", "ga");

			ht.put("-18237", "gai");

			ht.put("-18231", "gan");

			ht.put("-18220", "gang");

			ht.put("-18211", "gao");

			ht.put("-18201", "ge");

			ht.put("-18184", "gei");

			ht.put("-18183", "gen");

			ht.put("-18181", "geng");

			ht.put("-18012", "gong");

			ht.put("-17997", "gou");

			ht.put("-17988", "gu");

			ht.put("-17970", "gua");

			ht.put("-17964", "guai");

			ht.put("-17961", "guan");

			ht.put("-17950", "guang");

			ht.put("-17947", "gui");

			ht.put("-17931", "gun");

			ht.put("-17928", "guo");

			ht.put("-17922", "ha");

			ht.put("-17759", "hai");

			ht.put("-17752", "han");

			ht.put("-17733", "hang");

			ht.put("-17730", "hao");

			ht.put("-17721", "he");

			ht.put("-17703", "hei");

			ht.put("-17701", "hen");

			ht.put("-17697", "heng");

			ht.put("-17692", "hong");

			ht.put("-17683", "hou");

			ht.put("-17676", "hu");

			ht.put("-17496", "hua");

			ht.put("-17487", "huai");

			ht.put("-17482", "huan");

			ht.put("-17468", "huang");

			ht.put("-17454", "hui");

			ht.put("-17433", "hun");

			ht.put("-17427", "huo");

			ht.put("-17417", "ji");

			ht.put("-17202", "jia");

			ht.put("-17185", "jian");

			ht.put("-16983", "jiang");

			ht.put("-16970", "jiao");

			ht.put("-16942", "jie");

			ht.put("-16915", "jin");

			ht.put("-16733", "jing");

			ht.put("-16708", "jiong");

			ht.put("-16706", "jiu");

			ht.put("-16689", "ju");

			ht.put("-16664", "juan");

			ht.put("-16657", "jue");

			ht.put("-16647", "jun");

			ht.put("-16474", "ka");

			ht.put("-16470", "kai");

			ht.put("-16465", "kan");

			ht.put("-16459", "kang");

			ht.put("-16452", "kao");

			ht.put("-16448", "ke");

			ht.put("-16433", "ken");

			ht.put("-16429", "keng");

			ht.put("-16427", "kong");

			ht.put("-16423", "kou");

			ht.put("-16419", "ku");

			ht.put("-16412", "kua");

			ht.put("-16407", "kuai");

			ht.put("-16403", "kuan");

			ht.put("-16401", "kuang");

			ht.put("-16393", "kui");

			ht.put("-16220", "kun");

			ht.put("-16216", "kuo");

			ht.put("-16212", "la");

			ht.put("-16205", "lai");

			ht.put("-16202", "lan");

			ht.put("-16187", "lang");

			ht.put("-16180", "lao");

			ht.put("-16171", "le");

			ht.put("-16169", "lei");

			ht.put("-16158", "leng");

			ht.put("-16155", "li");

			ht.put("-15959", "lia");

			ht.put("-15958", "lian");

			ht.put("-15944", "liang");

			ht.put("-15933", "liao");

			ht.put("-15920", "lie");

			ht.put("-15915", "lin");

			ht.put("-15903", "ling");

			ht.put("-15889", "liu");

			ht.put("-15878", "long");

			ht.put("-15707", "lou");

			ht.put("-15701", "lu");

			ht.put("-15681", "lv");

			ht.put("-15667", "luan");

			ht.put("-15661", "lue");

			ht.put("-15659", "lun");

			ht.put("-15652", "luo");

			ht.put("-15640", "ma");

			ht.put("-15631", "mai");

			ht.put("-15625", "man");

			ht.put("-15454", "mang");

			ht.put("-15448", "mao");

			ht.put("-15436", "me");

			ht.put("-15435", "mei");

			ht.put("-15419", "men");

			ht.put("-15416", "meng");

			ht.put("-15408", "mi");

			ht.put("-15394", "mian");

			ht.put("-15385", "miao");

			ht.put("-15377", "mie");

			ht.put("-15375", "min");

			ht.put("-15369", "ming");

			ht.put("-15363", "miu");

			ht.put("-15362", "mo");

			ht.put("-15183", "mou");

			ht.put("-15180", "mu");

			ht.put("-15165", "na");

			ht.put("-15158", "nai");

			ht.put("-15153", "nan");

			ht.put("-15150", "nang");

			ht.put("-15149", "nao");

			ht.put("-15144", "ne");

			ht.put("-15143", "nei");

			ht.put("-15141", "nen");

			ht.put("-15140", "neng");

			ht.put("-15139", "ni");

			ht.put("-15128", "nian");

			ht.put("-15121", "niang");

			ht.put("-15119", "niao");

			ht.put("-15117", "nie");

			ht.put("-15110", "nin");

			ht.put("-15109", "ning");

			ht.put("-14941", "niu");

			ht.put("-14937", "nong");

			ht.put("-14933", "nu");

			ht.put("-14930", "nv");

			ht.put("-14929", "nuan");

			ht.put("-14928", "nue");

			ht.put("-14926", "nuo");

			ht.put("-14922", "o");

			ht.put("-14921", "ou");

			ht.put("-14914", "pa");

			ht.put("-14908", "pai");

			ht.put("-14902", "pan");

			ht.put("-14894", "pang");

			ht.put("-14889", "pao");

			ht.put("-14882", "pei");

			ht.put("-14873", "pen");

			ht.put("-14871", "peng");

			ht.put("-14857", "pi");

			ht.put("-14678", "pian");

			ht.put("-14674", "piao");

			ht.put("-14670", "pie");

			ht.put("-14668", "pin");

			ht.put("-14663", "ping");

			ht.put("-14654", "po");

			ht.put("-14645", "pu");

			ht.put("-14630", "qi");

			ht.put("-14594", "qia");

			ht.put("-14429", "qian");

			ht.put("-14407", "qiang");

			ht.put("-14399", "qiao");

			ht.put("-14384", "qie");

			ht.put("-14379", "qin");

			ht.put("-14368", "qing");

			ht.put("-14355", "qiong");

			ht.put("-14353", "qiu");

			ht.put("-14345", "qu");

			ht.put("-14170", "quan");

			ht.put("-14159", "que");

			ht.put("-14151", "qun");

			ht.put("-14149", "ran");

			ht.put("-14145", "rang");

			ht.put("-14140", "rao");

			ht.put("-14137", "re");

			ht.put("-14135", "ren");

			ht.put("-14125", "reng");

			ht.put("-14123", "ri");

			ht.put("-14122", "rong");

			ht.put("-14112", "rou");

			ht.put("-14109", "ru");

			ht.put("-14099", "ruan");

			ht.put("-14097", "rui");

			ht.put("-14094", "run");

			ht.put("-14092", "ruo");

			ht.put("-14090", "sa");

			ht.put("-14087", "sai");

			ht.put("-14083", "san");

			ht.put("-13917", "sang");

			ht.put("-13914", "sao");

			ht.put("-13910", "se");

			ht.put("-13907", "sen");

			ht.put("-13906", "seng");

			ht.put("-13905", "sha");

			ht.put("-13896", "shai");

			ht.put("-13894", "shan");

			ht.put("-13878", "shang");

			ht.put("-13870", "shao");

			ht.put("-13859", "she");

			ht.put("-13847", "shen");

			ht.put("-13831", "sheng");

			ht.put("-13658", "shi");

			ht.put("-13611", "shou");

			ht.put("-13601", "shu");

			ht.put("-13406", "shua");

			ht.put("-13404", "shuai");

			ht.put("-13400", "shuan");

			ht.put("-13398", "shuang");

			ht.put("-13395", "shui");

			ht.put("-13391", "shun");

			ht.put("-13387", "shuo");

			ht.put("-13383", "si");

			ht.put("-13367", "song");

			ht.put("-13359", "sou");

			ht.put("-13356", "su");

			ht.put("-13343", "suan");

			ht.put("-13340", "sui");

			ht.put("-13329", "sun");

			ht.put("-13326", "suo");

			ht.put("-13318", "ta");

			ht.put("-13147", "tai");

			ht.put("-13138", "tan");

			ht.put("-13120", "tang");

			ht.put("-13107", "tao");

			ht.put("-13096", "te");

			ht.put("-13095", "teng");

			ht.put("-13091", "ti");

			ht.put("-13076", "tian");

			ht.put("-13068", "tiao");

			ht.put("-13063", "tie");

			ht.put("-13060", "ting");

			ht.put("-12888", "tong");

			ht.put("-12875", "tou");

			ht.put("-12871", "tu");

			ht.put("-12860", "tuan");

			ht.put("-12858", "tui");

			ht.put("-12852", "tun");

			ht.put("-12849", "tuo");

			ht.put("-12838", "wa");

			ht.put("-12831", "wai");

			ht.put("-12829", "wan");

			ht.put("-12812", "wang");

			ht.put("-12802", "wei");

			ht.put("-12607", "wen");

			ht.put("-12597", "weng");

			ht.put("-12594", "wo");

			ht.put("-12585", "wu");

			ht.put("-12556", "xi");

			ht.put("-12359", "xia");

			ht.put("-12346", "xian");

			ht.put("-12320", "xiang");

			ht.put("-12300", "xiao");

			ht.put("-12120", "xie");

			ht.put("-12099", "xin");

			ht.put("-12089", "xing");

			ht.put("-12074", "xiong");

			ht.put("-12067", "xiu");

			ht.put("-12058", "xu");

			ht.put("-12039", "xuan");

			ht.put("-11867", "xue");

			ht.put("-11861", "xun");

			ht.put("-11847", "ya");

			ht.put("-11831", "yan");

			ht.put("-11798", "yang");

			ht.put("-11781", "yao");

			ht.put("-11604", "ye");

			ht.put("-11589", "yi");

			ht.put("-11536", "yin");

			ht.put("-11358", "ying");

			ht.put("-11340", "yo");

			ht.put("-11339", "yong");

			ht.put("-11324", "you");

			ht.put("-11303", "yu");

			ht.put("-11097", "yuan");

			ht.put("-11077", "yue");

			ht.put("-11067", "yun");

			ht.put("-11055", "za");

			ht.put("-11052", "zai");

			ht.put("-11045", "zan");

			ht.put("-11041", "zang");

			ht.put("-11038", "zao");

			ht.put("-11024", "ze");

			ht.put("-11020", "zei");

			ht.put("-11019", "zen");

			ht.put("-11018", "zeng");

			ht.put("-11014", "zha");

			ht.put("-10838", "zhai");

			ht.put("-10832", "zhan");

			ht.put("-10815", "zhang");

			ht.put("-10800", "zhao");

			ht.put("-10790", "zhe");

			ht.put("-10780", "zhen");

			ht.put("-10764", "zheng");

			ht.put("-10587", "zhi");

			ht.put("-10544", "zhong");

			ht.put("-10533", "zhou");

			ht.put("-10519", "zhu");

			ht.put("-10331", "zhua");

			ht.put("-10329", "zhuai");

			ht.put("-10328", "zhuan");

			ht.put("-10322", "zhuang");

			ht.put("-10315", "zhui");

			ht.put("-10309", "zhun");

			ht.put("-10307", "zhuo");

			ht.put("-10296", "zi");

			ht.put("-10281", "zong");

			ht.put("-10274", "zou");

			ht.put("-10270", "zu");

			ht.put("-10262", "zuan");

			ht.put("-10260", "zui");

			ht.put("-10256", "zun");

			ht.put("-10254", "zuo");

			ht.put("-10247", "zz");

		}



		public static String getPinyinLetters2(String str) {

			char[] hz = str.toCharArray();

			int len = str.length();

			int p, q;

			String ret = "";

			for (int i = 0; i < len; i++) {

				p = (int) hz[i];

				if (p > 160) {

					q = (int) hz[++i];

					p = p * 256 + q - 65536;

				}

				ret += g(new Integer(p));

				ret = ret + " " + String.valueOf(p);

			}

			return ret;

		}

	}



	// ---------------------------------

	private static class PY2 {

		// http://lbh087.javaeye.com/blog/206302 java 汉字转化为全拼

		private static LinkedHashMap<String, String> spellMap = null;

		static {

			if (spellMap == null) {

				spellMap = new LinkedHashMap<String, String>(20901);

			}

			initialize();

		}



		private static void initialize() {

			spellMap.put("129-74", "you");

			spellMap.put("129-99", "dou");

			spellMap.put("199-235", "qing");

		}



		/**

		 * 获得单个汉字的Ascii，并用"-"连接成一个字符串

		 * 

		 * @param cn

		 *            char 汉字字符

		 * @return string 错误返回 空字符串,否则返回ascii

		 */

		public static String getCnAscii(char cn) {

			byte[] bytes = null;

			try {

				bytes = (String.valueOf(cn)).getBytes("GBK");

			} catch (Exception ex) {

				bytes = (String.valueOf(cn)).getBytes();

			}



			if (bytes == null || bytes.length > 2 || bytes.length <= 0) { // 错误

				return "";

			}

			if (bytes.length == 1) { // 英文字符

				return new String(bytes);

			}

			if (bytes.length == 2) { // 中文字符

				int hightByte = 256 + bytes[0];

				int lowByte = 256 + bytes[1];



				String ascii = hightByte + "-" + lowByte;



				return ascii;

			}



			return ""; // 错误

		}



		/**

		 * 根据ASCII码连接成的字符串到SpellMap中查找对应的拼音

		 * 

		 * @param ascii

		 *            字符对应的ASCII连接的字符串

		 * @return String 拼音,首先判断是否是中文如果是英文直接返回字符，如果是中文返回拼音,

		 * 

		 *         否则到SpellMap中查找,如果没有找到拼音,则返回null,如果找到则返回拼音.

		 */

		public static String getSpellByAscii(String ascii) {

			if (ascii.indexOf("-") > -1) {

				return (String) spellMap.get(ascii);

			} else {

				return ascii;

			}

		}



		/**

		 * 返回字符串的全拼,是汉字转化为全拼,其它字符不进行转换

		 * 

		 * @param cnStr

		 *            String字符串

		 * @return String 转换成全拼后的字符串

		 */

		public static String getFullSpell(String cnStr) {

			if (null == cnStr || "".equals(cnStr.trim())) {

				return cnStr;

			}



			char[] chars = cnStr.toCharArray();

			StringBuffer retuBuf = new StringBuffer();

			for (int i = 0, Len = chars.length; i < Len; i++) {

				String ascii = getCnAscii(chars[i]);

				//_log.debug("cnToSpell:%s:%s", chars[i], ascii);

				if (ascii.length() == 0) { // 取ascii时出错

					retuBuf.append(chars[i]);

				} else {

					String spell = getSpellByAscii(ascii);
					
//					System.out.println();
//					_log.debug("cnToSpell %s %s %s", chars[i], ascii, spell);

					if (spell == null) {

						retuBuf.append(chars[i]);

					} else {

						retuBuf.append(spell);

					} // end of if spell == null

				} // end of if ascii <= -20400

			} // end of for



			return retuBuf.toString();

		}



		/**

		 * 获取汉语字符串的声母组合，每个汉字取拼音的第一个字符组成的一个字符串

		 * 

		 * @param cnStr

		 *            汉字的字符串

		 * @return 每个汉字拼音的第一个字母所组成的汉字

		 */

		public static String getFirstSpell(String cnStr) {

			if (null == cnStr || "".equals(cnStr.trim())) {

				return cnStr;

			}



			char[] chars = cnStr.toCharArray();

			StringBuffer retuBuf = new StringBuffer();



			String ascii = getCnAscii(chars[0]);

			if (ascii.length() == 1) { // 取ascii时出错

				retuBuf.append(chars[0]);

			} else {



				String spell = getSpellByAscii(ascii).substring(0, 1);

				if (spell == null) {

					retuBuf.append(chars[0]);

				} else {

					retuBuf.append(spell);

				} // end of if spell == null

			} // end of if ascii <= -20400



			return retuBuf.toString();

		}

	}



	private static class PY3 {

		//http://hi.baidu.com/suofang/blog/item/5a2e6ed097350384a0ec9c2f.html 用Java转化汉字为拼音全拼【转】

		private static LinkedHashMap<String, Integer> spellMap = null;

		private static void m_init() {

			if (spellMap == null) {

				spellMap = new LinkedHashMap<String, Integer>(400);

				m_init_map();

			}

		}

		private static void m_init_map() {

			//汉字的区位码转化为10进制数,其中区码为第一个字节,位码为第二个字节。 

			//查阅：http://dev.csdn.net/develop/article/44/44077.shtm

			spellMap.put("a", -20319);

			spellMap.put("ai", -20317);

			spellMap.put("an", -20304);

			spellMap.put("ang", -20295);

			spellMap.put("ao", -20292);

			spellMap.put("ba", -20283);

			spellMap.put("bai", -20265);

			spellMap.put("ban", -20257);

			spellMap.put("bang", -20242);

			spellMap.put("bao", -20230);

			spellMap.put("bei", -20051);

			spellMap.put("ben", -20036);

			spellMap.put("beng", -20032);

			spellMap.put("bi", -20026);

			spellMap.put("bian", -20002);

			spellMap.put("biao", -19990);

			spellMap.put("bie", -19986);

			spellMap.put("bin", -19982);

			spellMap.put("bing", -19976);

			spellMap.put("bo", -19805);

			spellMap.put("bu", -19784);

			spellMap.put("ca", -19775);

			spellMap.put("cai", -19774);

			spellMap.put("can", -19763);

			spellMap.put("cang", -19756);

			spellMap.put("cao", -19751);

			spellMap.put("ce", -19746);

			spellMap.put("ceng", -19741);

			spellMap.put("cha", -19739);

			spellMap.put("chai", -19728);

			spellMap.put("chan", -19725);

			spellMap.put("chang", -19715);

			spellMap.put("chao", -19540);

			spellMap.put("che", -19531);

			spellMap.put("chen", -19525);

			spellMap.put("cheng", -19515);

			spellMap.put("chi", -19500);

			spellMap.put("chong", -19484);

			spellMap.put("chou", -19479);

			spellMap.put("chu", -19467);

			spellMap.put("chuai", -19289);

			spellMap.put("chuan", -19288);

			spellMap.put("chuang", -19281);

			spellMap.put("chui", -19275);

			spellMap.put("chun", -19270);

			spellMap.put("chuo", -19263);

			spellMap.put("ci", -19261);

			spellMap.put("cong", -19249);

			spellMap.put("cou", -19243);

			spellMap.put("cu", -19242);

			spellMap.put("cuan", -19238);

			spellMap.put("cui", -19235);

			spellMap.put("cun", -19227);

			spellMap.put("cuo", -19224);

			spellMap.put("da", -19218);

			spellMap.put("dai", -19212);

			spellMap.put("dan", -19038);

			spellMap.put("dang", -19023);

			spellMap.put("dao", -19018);

			spellMap.put("de", -19006);

			spellMap.put("deng", -19003);

			spellMap.put("di", -18996);

			spellMap.put("dian", -18977);

			spellMap.put("diao", -18961);

			spellMap.put("die", -18952);

			spellMap.put("ding", -18783);

			spellMap.put("diu", -18774);

			spellMap.put("dong", -18773);

			spellMap.put("dou", -18763);

			spellMap.put("du", -18756);

			spellMap.put("duan", -18741);

			spellMap.put("dui", -18735);

			spellMap.put("dun", -18731);

			spellMap.put("duo", -18722);

			spellMap.put("e", -18710);

			spellMap.put("en", -18697);

			spellMap.put("er", -18696);

			spellMap.put("fa", -18526);

			spellMap.put("fan", -18518);

			spellMap.put("fang", -18501);

			spellMap.put("fei", -18490);

			spellMap.put("fen", -18478);

			spellMap.put("feng", -18463);

			spellMap.put("fo", -18448);

			spellMap.put("fou", -18447);

			spellMap.put("fu", -18446);

			spellMap.put("ga", -18239);

			spellMap.put("gai", -18237);

			spellMap.put("gan", -18231);

			spellMap.put("gang", -18220);

			spellMap.put("gao", -18211);

			spellMap.put("ge", -18201);

			spellMap.put("gei", -18184);

			spellMap.put("gen", -18183);

			spellMap.put("geng", -18181);

			spellMap.put("gong", -18012);

			spellMap.put("gou", -17997);

			spellMap.put("gu", -17988);

			spellMap.put("gua", -17970);

			spellMap.put("guai", -17964);

			spellMap.put("guan", -17961);

			spellMap.put("guang", -17950);

			spellMap.put("gui", -17947);

			spellMap.put("gun", -17931);

			spellMap.put("guo", -17928);

			spellMap.put("ha", -17922);

			spellMap.put("hai", -17759);

			spellMap.put("han", -17752);

			spellMap.put("hang", -17733);

			spellMap.put("hao", -17730);

			spellMap.put("he", -17721);

			spellMap.put("hei", -17703);

			spellMap.put("hen", -17701);

			spellMap.put("heng", -17697);

			spellMap.put("hong", -17692);

			spellMap.put("hou", -17683);

			spellMap.put("hu", -17676);

			spellMap.put("hua", -17496);

			spellMap.put("huai", -17487);

			spellMap.put("huan", -17482);

			spellMap.put("huang", -17468);

			spellMap.put("hui", -17454);

			spellMap.put("hun", -17433);

			spellMap.put("huo", -17427);

			spellMap.put("ji", -17417);

			spellMap.put("jia", -17202);

			spellMap.put("jian", -17185);

			spellMap.put("jiang", -16983);

			spellMap.put("jiao", -16970);

			spellMap.put("jie", -16942);

			spellMap.put("jin", -16915);

			spellMap.put("jing", -16733);

			spellMap.put("jiong", -16708);

			spellMap.put("jiu", -16706);

			spellMap.put("ju", -16689);

			spellMap.put("juan", -16664);

			spellMap.put("jue", -16657);

			spellMap.put("jun", -16647);

			spellMap.put("ka", -16474);

			spellMap.put("kai", -16470);

			spellMap.put("kan", -16465);

			spellMap.put("kang", -16459);

			spellMap.put("kao", -16452);

			spellMap.put("ke", -16448);

			spellMap.put("ken", -16433);

			spellMap.put("keng", -16429);

			spellMap.put("kong", -16427);

			spellMap.put("kou", -16423);

			spellMap.put("ku", -16419);

			spellMap.put("kua", -16412);

			spellMap.put("kuai", -16407);

			spellMap.put("kuan", -16403);

			spellMap.put("kuang", -16401);

			spellMap.put("kui", -16393);

			spellMap.put("kun", -16220);

			spellMap.put("kuo", -16216);

			spellMap.put("la", -16212);

			spellMap.put("lai", -16205);

			spellMap.put("lan", -16202);

			spellMap.put("lang", -16187);

			spellMap.put("lao", -16180);

			spellMap.put("le", -16171);

			spellMap.put("lei", -16169);

			spellMap.put("leng", -16158);

			spellMap.put("li", -16155);

			spellMap.put("lia", -15959);

			spellMap.put("lian", -15958);

			spellMap.put("liang", -15944);

			spellMap.put("liao", -15933);

			spellMap.put("lie", -15920);

			spellMap.put("lin", -15915);

			spellMap.put("ling", -15903);

			spellMap.put("liu", -15889);

			spellMap.put("long", -15878);

			spellMap.put("lou", -15707);

			spellMap.put("lu", -15701);

			spellMap.put("lv", -15681);

			spellMap.put("luan", -15667);

			spellMap.put("lue", -15661);

			spellMap.put("lun", -15659);

			spellMap.put("luo", -15652);

			spellMap.put("ma", -15640);

			spellMap.put("mai", -15631);

			spellMap.put("man", -15625);

			spellMap.put("mang", -15454);

			spellMap.put("mao", -15448);

			spellMap.put("me", -15436);

			spellMap.put("mei", -15435);

			spellMap.put("men", -15419);

			spellMap.put("meng", -15416);

			spellMap.put("mi", -15408);

			spellMap.put("mian", -15394);

			spellMap.put("miao", -15385);

			spellMap.put("mie", -15377);

			spellMap.put("min", -15375);

			spellMap.put("ming", -15369);

			spellMap.put("miu", -15363);

			spellMap.put("mo", -15362);

			spellMap.put("mou", -15183);

			spellMap.put("mu", -15180);

			spellMap.put("na", -15165);

			spellMap.put("nai", -15158);

			spellMap.put("nan", -15153);

			spellMap.put("nang", -15150);

			spellMap.put("nao", -15149);

			spellMap.put("ne", -15144);

			spellMap.put("nei", -15143);

			spellMap.put("nen", -15141);

			spellMap.put("neng", -15140);

			spellMap.put("ni", -15139);

			spellMap.put("nian", -15128);

			spellMap.put("niang", -15121);

			spellMap.put("niao", -15119);

			spellMap.put("nie", -15117);

			spellMap.put("nin", -15110);

			spellMap.put("ning", -15109);

			spellMap.put("niu", -14941);

			spellMap.put("nong", -14937);

			spellMap.put("nu", -14933);

			spellMap.put("nv", -14930);

			spellMap.put("nuan", -14929);

			spellMap.put("nue", -14928);

			spellMap.put("nuo", -14926);

			spellMap.put("o", -14922);

			spellMap.put("ou", -14921);

			spellMap.put("pa", -14914);

			spellMap.put("pai", -14908);

			spellMap.put("pan", -14902);

			spellMap.put("pang", -14894);

			spellMap.put("pao", -14889);

			spellMap.put("pei", -14882);

			spellMap.put("pen", -14873);

			spellMap.put("peng", -14871);

			spellMap.put("pi", -14857);

			spellMap.put("pian", -14678);

			spellMap.put("piao", -14674);

			spellMap.put("pie", -14670);

			spellMap.put("pin", -14668);

			spellMap.put("ping", -14663);

			spellMap.put("po", -14654);

			spellMap.put("pu", -14645);

			spellMap.put("qi", -14630);

			spellMap.put("qia", -14594);

			spellMap.put("qian", -14429);

			spellMap.put("qiang", -14407);

			spellMap.put("qiao", -14399);

			spellMap.put("qie", -14384);

			spellMap.put("qin", -14379);

			spellMap.put("qing", -14368);

			spellMap.put("qiong", -14355);

			spellMap.put("qiu", -14353);

			spellMap.put("qu", -14345);

			spellMap.put("quan", -14170);

			spellMap.put("que", -14159);

			spellMap.put("qun", -14151);

			spellMap.put("ran", -14149);

			spellMap.put("rang", -14145);

			spellMap.put("rao", -14140);

			spellMap.put("re", -14137);

			spellMap.put("ren", -14135);

			spellMap.put("reng", -14125);

			spellMap.put("ri", -14123);

			spellMap.put("rong", -14122);

			spellMap.put("rou", -14112);

			spellMap.put("ru", -14109);

			spellMap.put("ruan", -14099);

			spellMap.put("rui", -14097);

			spellMap.put("run", -14094);

			spellMap.put("ruo", -14092);

			spellMap.put("sa", -14090);

			spellMap.put("sai", -14087);

			spellMap.put("san", -14083);

			spellMap.put("sang", -13917);

			spellMap.put("sao", -13914);

			spellMap.put("se", -13910);

			spellMap.put("sen", -13907);

			spellMap.put("seng", -13906);

			spellMap.put("sha", -13905);

			spellMap.put("shai", -13896);

			spellMap.put("shan", -13894);

			spellMap.put("shang", -13878);

			spellMap.put("shao", -13870);

			spellMap.put("she", -13859);

			spellMap.put("shen", -13847);

			spellMap.put("sheng", -13831);

			spellMap.put("shi", -13658);

			spellMap.put("shou", -13611);

			spellMap.put("shu", -13601);

			spellMap.put("shua", -13406);

			spellMap.put("shuai", -13404);

			spellMap.put("shuan", -13400);

			spellMap.put("shuang", -13398);

			spellMap.put("shui", -13395);

			spellMap.put("shun", -13391);

			spellMap.put("shuo", -13387);

			spellMap.put("si", -13383);

			spellMap.put("song", -13367);

			spellMap.put("sou", -13359);

			spellMap.put("su", -13356);

			spellMap.put("suan", -13343);

			spellMap.put("sui", -13340);

			spellMap.put("sun", -13329);

			spellMap.put("suo", -13326);

			spellMap.put("ta", -13318);

			spellMap.put("tai", -13147);

			spellMap.put("tan", -13138);

			spellMap.put("tang", -13120);

			spellMap.put("tao", -13107);

			spellMap.put("te", -13096);

			spellMap.put("teng", -13095);

			spellMap.put("ti", -13091);

			spellMap.put("tian", -13076);

			spellMap.put("tiao", -13068);

			spellMap.put("tie", -13063);

			spellMap.put("ting", -13060);

			spellMap.put("tong", -12888);

			spellMap.put("tou", -12875);

			spellMap.put("tu", -12871);

			spellMap.put("tuan", -12860);

			spellMap.put("tui", -12858);

			spellMap.put("tun", -12852);

			spellMap.put("tuo", -12849);

			spellMap.put("wa", -12838);

			spellMap.put("wai", -12831);

			spellMap.put("wan", -12829);

			spellMap.put("wang", -12812);

			spellMap.put("wei", -12802);

			spellMap.put("wen", -12607);

			spellMap.put("weng", -12597);

			spellMap.put("wo", -12594);

			spellMap.put("wu", -12585);

			spellMap.put("xi", -12556);

			spellMap.put("xia", -12359);

			spellMap.put("xian", -12346);

			spellMap.put("xiang", -12320);

			spellMap.put("xiao", -12300);

			spellMap.put("xie", -12120);

			spellMap.put("xin", -12099);

			spellMap.put("xing", -12089);

			spellMap.put("xiong", -12074);

			spellMap.put("xiu", -12067);

			spellMap.put("xu", -12058);

			spellMap.put("xuan", -12039);

			spellMap.put("xue", -11867);

			spellMap.put("xun", -11861);

			spellMap.put("ya", -11847);

			spellMap.put("yan", -11831);

			spellMap.put("yang", -11798);

			spellMap.put("yao", -11781);

			spellMap.put("ye", -11604);

			spellMap.put("yi", -11589);

			spellMap.put("yin", -11536);

			spellMap.put("ying", -11358);

			spellMap.put("yo", -11340);

			spellMap.put("yong", -11339);

			spellMap.put("you", -11324);

			spellMap.put("yu", -11303);

			spellMap.put("yuan", -11097);

			spellMap.put("yue", -11077);

			spellMap.put("yun", -11067);

			spellMap.put("za", -11055);

			spellMap.put("zai", -11052);

			spellMap.put("zan", -11045);

			spellMap.put("zang", -11041);

			spellMap.put("zao", -11038);

			spellMap.put("ze", -11024);

			spellMap.put("zei", -11020);

			spellMap.put("zen", -11019);

			spellMap.put("zeng", -11018);

			spellMap.put("zha", -11014);

			spellMap.put("zhai", -10838);

			spellMap.put("zhan", -10832);

			spellMap.put("zhang", -10815);

			spellMap.put("zhao", -10800);

			spellMap.put("zhe", -10790);

			spellMap.put("zhen", -10780);

			spellMap.put("zheng", -10764);

			spellMap.put("zhi", -10587);

			spellMap.put("zhong", -10544);

			spellMap.put("zhou", -10533);

			spellMap.put("zhu", -10519);

			spellMap.put("zhua", -10331);

			spellMap.put("zhuai", -10329);

			spellMap.put("zhuan", -10328);

			spellMap.put("zhuang", -10322);

			spellMap.put("zhui", -10315);

			spellMap.put("zhun", -10309);

			spellMap.put("zhuo", -10307);

			spellMap.put("zi", -10296);

			spellMap.put("zong", -10281);

			spellMap.put("zou", -10274);

			spellMap.put("zu", -10270);

			spellMap.put("zuan", -10262);

			spellMap.put("zui", -10260);

			spellMap.put("zun", -10256);

			spellMap.put("zuo", -10254);

		}



		/**

		 * 获得单个汉字的Ascii.

		 * 

		 * @param cn

		 *            char 汉字字符

		 * @return int 错误返回 0,否则返回ascii

		 */

		public static int getCnAscii(char cn) {

			byte[] bytes = (String.valueOf(cn)).getBytes();

			if (bytes == null || bytes.length > 2 || bytes.length <= 0) { // 错误

				return 0;

			}

			if (bytes.length == 1) { // 英文字符

				return bytes[0];

			}

			if (bytes.length == 2) { // 中文字符

				int hightByte = 256 + bytes[0];

				int lowByte = 256 + bytes[1];



				int ascii = (256 * hightByte + lowByte) - 256 * 256;



				// System.out.println("ASCII=" + ascii);



				return ascii;

			}



			return 0; // 错误

		}



		/**

		 * 根据ASCII码到SpellMap中查找对应的拼音

		 * 

		 * @param ascii

		 *            int 字符对应的ASCII

		 * @return String 拼音,首先判断ASCII是否>0&<160,如果是返回对应的字符, <BR>

		 *         否则到SpellMap中查找,如果没有找到拼音,则返回null,如果找到则返回拼音.

		 */

		public static String getSpellByAscii(int ascii) {

			if (ascii > 0 && ascii < 160) { // 单字符

				return String.valueOf((char) ascii);

			}



			if (ascii < -20319 || ascii > -10247) { // 不知道的字符

				return null;

			}



			Set<String> keySet = spellMap.keySet();

			Iterator<String> it = keySet.iterator();



			String spell0 = null;

			;

			String spell = null;



			int asciiRang0 = -20319;

			int asciiRang;

			while (it.hasNext()) {



				spell = (String) it.next();

				Object valObj = spellMap.get(spell);

				if (valObj instanceof Integer) {

					asciiRang = ((Integer) valObj).intValue();



					if (ascii >= asciiRang0 && ascii < asciiRang) { // 区间找到

						return (spell0 == null) ? spell : spell0;

					} else {

						spell0 = spell;

						asciiRang0 = asciiRang;

					}

				}

			}



			return null;



		}



		/**

		 * 返回字符串的全拼,是汉字转化为全拼,其它字符不进行转换

		 * 

		 * @param cnStr

		 *            String 字符串

		 * @return String 转换成全拼后的字符串

		 */

		public static String getFullSpell(String cnStr, char split) {

			m_init();

			if (null == cnStr || "".equals(cnStr.trim())) {

				return cnStr;

			}

			char[] chars = cnStr.toCharArray();

			StringBuffer retuBuf = new StringBuffer();

			for (int i = 0, Len = chars.length; i < Len; i++) {

				int ascii = getCnAscii(chars[i]);

				if (ascii == 0) { // 取ascii时出错

					retuBuf.append(chars[i]);

				} else {

					String spell = getSpellByAscii(ascii);

					if (spell == null) {

						retuBuf.append(chars[i]);

					} else {

						retuBuf.append(spell).append(split);

					} // end of if spell == null

				} // end of if ascii <= -20400

			} // end of for



			return retuBuf.toString();

		}

	}
	private static Logger _log = Logger.getRootLogger();


	static class Test {

		public static void main(String[] args) throws Exception {

			 String s0 = "测试类=1234";
			 System.out.println(getPinyinFirst(s0));
			 System.out.println(getPinyinFull(s0));
//			String s0 = "请在此处输入中文";	//

			_log.debug(s0);

			_log.debug(getPinyinFirst(s0));

			_log.debug(getPinyinFull(s0));	//==PY3.getFullSpell(s0)

			_log.debug(PY0_err.getPinyinLetters(s0));

			_log.debug(PY2.getFullSpell(s0));

//			_log.debug(PY2.getFirstSpell(s0));//可能有异常。

		}

	}

}

