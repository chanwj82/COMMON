package com.pagoda.common;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtil {

	private static final Logger log = LoggerFactory.getLogger(CommonUtil.class);
	public static String yyyyMMdd = "yyyyMMdd";
	public static String yyyyMMddHHmm = "yyyyMMddHHmm";
	public static String yyyyMMddHHmmss = "yyyyMMddHHmmss";
	public static Locale locale = java.util.Locale.KOREAN;

	public static String enKey ="dlQmsdldutlsslaWkdrnldua";

	/**
	 * 비밀번호 암호화
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encryptPassword(String data) throws Exception	{
		if(data == null)
		{
			return "";
		} else
		{
			byte plainText[] = null;
			byte hashValue[] = null;
			plainText = data.getBytes();
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			hashValue = md.digest(plainText);
			return new String(Base64.encodeBase64(hashValue));
		}
	}

	/**
	 * 숫자인지 체크하여 숫자이면 콤마까지 찍어서 리턴
	 * @param str
	 * @return
	 */
	public static String getCommaNumber(String str, String format) {
		try {

			DecimalFormat df = new DecimalFormat(format);

			return df.format(Double.parseDouble(str));
		} catch (NumberFormatException e) {
			return str;
		}
	}

	/**
	 * parameter 셋팅시
	 * null 인경우 '' 를 리턴한다.
	 *
	 * @param param
	 * @return
	 */
	public static String nullToBlank(String param)
	{
		if (param == null || "null".equals(param))
		{
			return "";
		}

		return param;
	}

	/**
	 * 현재일시를 특정형식(yyyyMMddHHmmss)으로 반환
	 *
	 * @param as_format
	 * @return String
	 */
	public static String getDateTime(String as_format)
	{
		// 변수선언 및 초기화.
		Calendar oCalendar = Calendar.getInstance( );
		SimpleDateFormat dateFormat = new SimpleDateFormat(as_format);
		return dateFormat.format(oCalendar.getTime());
	}

	public static String getTimeStr(Object timeObj) {
		return getTimeStr(timeObj.toString());
	}

	/**
	 * 시간 형태로 변환 tt:mm:ss
	 *
	 * @param as_format
	 * @return String
	 */
	public static String getTimeStr(String timeStr)
	{
		String rtnStr = timeStr;

		if (rtnStr.length() == 4) {
			rtnStr = rtnStr.substring(0, 2) + ":" + rtnStr.substring(2, 4);
		} else if (rtnStr.length() == 6) {
			rtnStr = rtnStr.substring(0, 2) + ":" + rtnStr.substring(2, 4) + ":" + rtnStr.substring(4, 6);
		}

		return rtnStr;
	}

	/**
	 * 어제일시를 특정형식(yyyyMMddHHmmss)으로 반환
	 *
	 * @param as_format
	 * @return String
	 */
	public static String getYesterday(String as_format)
	{
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DATE, -1);

		//        System.out.println("어제 년: " +  cal.get(Calendar.YEAR));
		//        System.out.println("어제 월: " + (cal.get(Calendar.MONTH) + 1));
		//        System.out.println("어제 일: " +  cal.get(Calendar.DAY_OF_MONTH));

		// 24시간 전의 날짜, 시간, 시간대를
		// Sun Dec 10 13:50:52 KST 2006 이런 식으로 한 줄로 출력
		SimpleDateFormat dateFormat = new SimpleDateFormat(as_format);
		return dateFormat.format(cal.getTime());
	}

	public static String getDayfromToday(String as_format, int ii)
	{
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DATE, ii);
		SimpleDateFormat dateFormat = new SimpleDateFormat(as_format);
		return dateFormat.format(cal.getTime());
	}
//	/**
//	 * json 관련 객체
//	 * @param list
//	 * @return
//	 * @throws Exception
//	 */
//	public static JSONObject setJsonfromList(List list) throws Exception
//	{
//		JSONObject json = new JSONObject();
//		JSONArray jsonArr = new JSONArray();
//
//		if(list != null && list.size() > 0)
//		{
//			if (list.size() == 1 ) {
//				json = setJsonfromMap((Map)list.get(0));
//			} else {
//				for(int i=0 ; i < list.size() ; i++) {
//					jsonArr.add(setJsonfromMap((Map)list.get(i)) );
//				}
//				json.put("row", jsonArr);
//			}
//
//		} else {
//			json.put("jsonstatus", "리스트의 결과가 없습니다.");
//		}
//
//		return json;
//	}
//
//	public static JSONObject setJsonArrfromList(List list) throws Exception
//	{
//		JSONObject json = new JSONObject();
//		JSONArray jsonArr = new JSONArray();
//
//		if(list != null && list.size() > 0)
//		{
//			if (list.size() == 1 ) {
//				jsonArr.add(setJsonfromMap((Map)list.get(0)) );
//				json.put("row", jsonArr);
//			} else {
//				for(int i=0 ; i < list.size() ; i++) {
//					jsonArr.add(setJsonfromMap((Map)list.get(i)) );
//				}
//				json.put("row", jsonArr);
//			}
//
//		} else {
//			json.put("jsonstatus", "리스트의 결과가 없습니다.");
//		}
//
//		return json;
//	}
//
//	public static JSONObject setJsonfromMap(Map mVal) throws Exception
//	{
//		JSONObject json = new JSONObject();
//		if(mVal == null) {
//			return json;
//		}
//		Collection coll =  mVal.keySet();
//		Iterator iter = coll.iterator();
//		while(iter.hasNext()){
//			String col = (String) iter.next();
//
//			String value = (mVal.get(col) ==null)? "": mVal.get(col).toString();
//			json.put(col, getString(value));
//
//		}
//		return json;
//	}
//
//	public static JSONObject setJsonfromSaveCount(int cnt) throws Exception
//	{
//		JSONObject json = new JSONObject();
//
//		if(cnt  > 0)
//		{
//			json.put("cnt", String.valueOf(cnt));
//		} else {
//			json.put("cnt", "-1");
//		}
//
//		return json;
//	}

	public static String getString(Object obj)
	{
		if(obj == null) {
			return "";
		}

		return obj.toString().trim();
	}

	public static String getString(Object obj, int len)
	{
		if(obj == null) {
			return "";
		}

		return obj.toString().trim().length() > len ? obj.toString().trim().substring(0, len) + "..." : obj.toString().trim();
	}

	public static String getString(Object obj, String def)
	{
		if(obj == null) {
			return def;
		}

		return obj.toString();
	}

	public static String getStringEmpty(Object obj, String def)
	{
		if(obj == null || "".equals(obj.toString().trim())) {
			return def;
		}

		return obj.toString();
	}

	public static int getInt(Object obj)
	{
		if(obj == null || "".equals(obj.toString().trim())) {
			return 0;
		}

		return Integer.parseInt(obj.toString().trim());
	}

	public static Object[] getParamObj(String[] col, Map paramMap)
	{
		Object[] sqlObj = new Object[col.length];
		for (int i=0; i< col.length ; i++){
			sqlObj[i] = paramMap.get(col[i]);
		}
		return sqlObj;
	}

	/**

    문자열중 지정한 문자열을 찾아서 새로운 문자열로 바꾸는 함수
    origianl    대상 문자열
    oldstr      찾을 문자열
    newstr      바꿀 문자열
    return      바뀐 결과

	 */

	public static String replace(String original, String oldstr, String newstr)

	{
		String convert = new String();
		int pos = 0;
		int begin = 0;
		pos = original.indexOf(oldstr);

		if(pos == -1) {
			return original;
		}
		while(pos != -1)
		{
			convert = convert + original.substring(begin, pos) + newstr;
			begin = pos + oldstr.length();
			pos = original.indexOf(oldstr, begin);
		}
		convert = convert + original.substring(begin);
		return convert;
	}

	/**

    내용중 HTML 툭수기호인 문자를 HTML 특수기호 형식으로 변환합니다.
    htmlstr     바꿀 대상인 문자열
    return      바뀐 결과
    PHP의 htmlspecialchars와 유사한 결과를 반환합니다.

	 */

	public static String convertHtmlchars(String htmlstr)

	{
		htmlstr = htmlstr.replaceAll("\\<", "&lt;");
		htmlstr = htmlstr.replaceAll("\\>", "&gt;");
		htmlstr = htmlstr.replaceAll("\\\"", "&quot;");
		htmlstr = htmlstr.replaceAll("&nbsp;", "&amp;nbsp;");
		return htmlstr;

	}

	public static HashMap getSearchMap(HashMap paramMap) {
		HashMap searchMap = new HashMap();

		Collection coll = paramMap.keySet();
		Iterator iter = coll.iterator();
		while(iter.hasNext()){
			String col = (String) iter.next();

			if (col.substring(0, 1).equals("se")) {
				searchMap.put(col, getString(paramMap.get(col)));
			}
		}
		return searchMap;
	}

	public static String getSearchHiddenText(HashMap paramMap) {
		String searchHiddenText = "";

		Collection coll = paramMap.keySet();
		Iterator iter = coll.iterator();
		while(iter.hasNext()){
			String col = (String) iter.next();

			if (col.substring(0, 1).equals("s")) {
				searchHiddenText += "<input type=\"hidden\" id=\"" + col + "\" name=\"" + col + "\" value=\"" + getString(paramMap.get(col)) + "\">\n";
			}
		}
		return searchHiddenText;
	}

	public static void alertHref(HttpServletResponse response, String msg, String page) throws Exception  {
		String outStr = "";

		outStr += "<script language='javascript'>";
		if (!msg.equals("")) {
			outStr += "    alert('" + msg + "');\n";
		}
		outStr += "    document.location.href='" + page + "';";
		outStr += "</script>";

		response.setContentType("text/html");
		response.setHeader("Content-type", "text/html;charset=euc-kr");
		PrintWriter out = response.getWriter();

		out.print(outStr);
		out.close();
	}

	public static void alertSubmit(HttpServletResponse response, String msg, HashMap param, String action) throws Exception  {
		String outStr = "";

		Iterator iterator = param.keySet().iterator();
		outStr += "<form name=\"frm\" method=\"post\" action=\"" + action + "\">\n";

		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			outStr += "<input type=\"hidden\" name=\"" + key + "\" value=\"" + param.get(key) + "\"/>\n";
		}

		outStr += "</form>\n";

		outStr += "<script language='javascript'>\n";
		if (!msg.equals("")) {
			outStr += "    alert('" + msg + "');\n";
		}
		outStr += "    document.frm.submit();\n";
		outStr += "</script>\n";

		response.setContentType("text/html");
		response.setHeader("Content-type", "text/html;charset=euc-kr");
		PrintWriter out = response.getWriter();

		out.print(outStr);
		out.close();
	}

	public static void alertHistoryBack(HttpServletResponse response, String msg) throws Exception  {
		String outStr = "";

		outStr += "<script language='javascript'>";
		if (!msg.equals("")) {
			outStr += "    alert('" + msg + "');\n";
		}
		outStr += "    history.back();";
		outStr += "</script>";

		response.setContentType("text/html");
		response.setHeader("Content-type", "text/html;charset=euc-kr");
		PrintWriter out = response.getWriter();

		out.print(outStr);
		out.close();
	}

	public static String mapObjToStr(Map map, String key) throws Exception {
		return mapObjToStr(map, key, "");
	}

	public static String mapObjToStr(Map map, String key, String dValue) throws Exception {
		if (map == null || map.get(key) == null) {
			return dValue;
		} else {
			return map.get(key).toString();
		}
	}

	public static int mapObjToInt(Map map, String key) throws Exception {
		return mapObjToInt(map, key, 0);
	}

	public static int mapObjToInt(Map map, String key, int dValue) throws Exception {
		if (map == null || map.get(key) == null) {
			return dValue;
		} else {
			try {
				return Integer.parseInt(map.get(key).toString());
			} catch (Exception e) {
				e.printStackTrace();
				return dValue;
			}
		}
	}

	public static String unscript(Object obj) {
		return unscript(ObjectUtils.toString(obj));
	}

	public static String unscript(String data) {
		if ((data == null) || (data.trim().equals(""))) {
			return "";
		}

		String ret = data;

		ret = ret.replaceAll("<(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;script");
		ret = ret.replaceAll("</(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;/script");

		ret = ret.replaceAll("<(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;object");
		ret = ret.replaceAll("</(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;/object");

		ret = ret.replaceAll("<(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;applet");
		ret = ret.replaceAll("</(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;/applet");

		ret = ret.replaceAll("<(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");
		ret = ret.replaceAll("</(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");

		ret = ret.replaceAll("<(F|f)(O|o)(R|r)(M|m)", "&lt;form");
		ret = ret.replaceAll("</(F|f)(O|o)(R|r)(M|m)", "&lt;form");

		return ret;
	}

	public static String getPhysicalWebrootPath() throws Exception {
		String webRootPath = CommonUtil.class.getResource("/").getPath();
		webRootPath = webRootPath.substring(0, webRootPath.indexOf("WEB-INF")-1);

		return webRootPath;
	}

	public static String[] getObjectToStringArr(Object obj) throws Exception {
		String[] retArr = null;

		if (obj instanceof String[]){
			retArr = (String[])obj;
		}else if (obj instanceof String){
			retArr = new String[1];
			retArr[0] = (String)obj;
		}

		return retArr;
	}

	public static int[] getObjectToIntArr(Object obj) throws Exception {
		int[] retArr = null;

		if (obj instanceof int[]){
			retArr = (int[])obj;
		}else if (obj instanceof Integer){
			retArr = new int[1];
			retArr[0] = Integer.parseInt(obj.toString());
		}else if (obj == null){
			retArr = new int[1];
			retArr[0] = 0;
		}

		return retArr;
	}


	private static Calendar getCalendar( String dt )
	{
		Calendar cal = Calendar.getInstance();

		int yyyy = Integer.parseInt( dt.substring( 0, 4 ) );
		int MM = Integer.parseInt( dt.substring( 4, 6 ) ) - 1;
		int dd = Integer.parseInt( dt.substring( 6, 8 ) );

		int HH = 0;
		int mm = 0;
		int ss = 0;

		if( dt.length() == 8 )
		{
			cal.set( yyyy, MM, dd );
		}
		else if( dt.length() == 12 )
		{
			HH = Integer.parseInt( dt.substring( 8, 10 ) );
			mm = Integer.parseInt( dt.substring( 10, 12 ) );
			cal.set( yyyy, MM, dd, HH, mm );
		}
		else if( dt.length() == 14 )
		{
			HH = Integer.parseInt( dt.substring( 8, 10 ) );
			mm = Integer.parseInt( dt.substring( 10, 12 ) );
			ss = Integer.parseInt( dt.substring( 12, 14 ) );
			cal.set( yyyy, MM, dd, HH, mm, ss );
		}

		return cal;
	}

	/**
	 * 오늘날짜 : yyyyMMdd
	 *
	 * @return
	 */
	public static String getDate()
	{
		return getDate( yyyyMMdd );
	}

	/**
	 * 오늘날짜
	 *
	 * @param dateFormat
	 *            포멧
	 * @return
	 */
	public static String getDate( String dateFormat )
	{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat( dateFormat );
		return sdf.format( c.getTime() );
	}

	/**
	 * 조건일 주의 시작일
	 *
	 * <pre>
	 * 예제)
	 * String sampleDate = getFirstDayOfWeek(&quot;20081010&quot;);
	 * </pre>
	 *
	 * @param dt
	 * @return
	 * @throws Exception
	 */
	public static String getFirstDayOfWeek( String dt ) throws Exception
	{
		String dateFormat = getDateFormat( dt );
		SimpleDateFormat sdf = new SimpleDateFormat( dateFormat, locale );

		Calendar c = getCalendar( dt );
		c.add( Calendar.DATE, ( c.get( Calendar.DAY_OF_WEEK ) - 1 ) * -1 );

		return sdf.format( c.getTime() );
	}

	/**
	 * 조건일 주의 종료일
	 *
	 * <pre>
	 * 예제)
	 * String sampleDate = getLastDayOfWeek(&quot;20081010&quot;);
	 * </pre>
	 *
	 * @param dt
	 *            날짜
	 * @return
	 * @throws Exception
	 */
	public static String getLastDayOfWeek( String dt ) throws Exception
	{
		String dateFormat = getDateFormat( dt );
		SimpleDateFormat sdf = new SimpleDateFormat( dateFormat, locale );

		Calendar c = getCalendar( dt );
		c.add( Calendar.DATE, 7 - c.get( Calendar.DAY_OF_WEEK ) );

		return sdf.format( c.getTime() );
	}

	/**
	 * 기본포멧 일 더하기 세가지 포멧만 허용(yyyyMMdd, yyyyMMddHHmm, yyyyMMddHHmmss)
	 *
	 * <pre>
	 * 예제)
	 * String sampleDate = addDate(&quot;20081010&quot;, 1);
	 * </pre>
	 *
	 * @param dt
	 *            날짜
	 * @param addNum
	 *            더한 날
	 * @return
	 */
	public static String addDate( String dt, int addNum ) throws Exception
	{
		return addDate( dt, addNum, Calendar.DATE );
	}

	/**
	 * 날짜 더하기 세가지 포멧만 허용(yyyyMMdd, yyyyMMddHHmm, yyyyMMddHHmmss)
	 *
	 * <pre>
	 * 예제)
	 * String sampleDate = addDate(&quot;20081010&quot;, 1);
	 * </pre>
	 *
	 * @param dt
	 *            날짜
	 * @param addNum
	 *            더한 날
	 * @param calConst
	 *            Calendar.DATE, Calendar.MONTH
	 * @return
	 * @throws Exception
	 */
	private static String addDate( String dt, int addNum, int calConst ) throws Exception
	{
		String dateFormat = getDateFormat( dt );
		SimpleDateFormat sdf = new SimpleDateFormat( dateFormat, locale );

		Calendar c = getCalendar( dt );
		c.add( calConst, addNum );

		return sdf.format( c.getTime() );
	}

	/**
	 * 조건일 월의 시작일
	 *
	 * <pre>
	 * 예제)
	 * String sampleDate = getFirstDayOfMonth(&quot;20081010&quot;);
	 * </pre>
	 *
	 * @param dt
	 *            날짜
	 * @return
	 * @throws Exception
	 */
	public static String getFirstDayOfMonth( String dt ) throws Exception
	{
		String dateFormat = getDateFormat( dt );
		SimpleDateFormat sdf = new SimpleDateFormat( dateFormat, locale );

		dt = dt.substring( 0, 6 ) + "01" + dt.substring( 8 );
		Calendar c = getCalendar( dt );

		return sdf.format( c.getTime() );
	}

	/**
	 * 조건일 월의 종료일
	 *
	 * <pre>
	 * 예제)
	 * String sampleDate = getLastDayOfMonth(&quot;20081010&quot;);
	 * </pre>
	 *
	 * @param dt
	 *            날짜
	 * @return
	 * @throws Exception
	 */
	public static String getLastDayOfMonth( String dt ) throws Exception
	{
		String dateFormat = getDateFormat( dt );
		SimpleDateFormat sdf = new SimpleDateFormat( dateFormat, locale );

		Calendar c = getCalendar( dt );
		c.set( Calendar.DATE, c.getActualMaximum( Calendar.DAY_OF_MONTH ) );

		return sdf.format( c.getTime() );
	}

	/**
	 * 날짜에 해당하는 포멧을 반환 날짜문자열 길이로 세가지중 한가지 타입선택 (yyyyMMdd, yyyyMMddHHmm, yyyyMMddHHmmss)
	 *
	 * @param dt
	 * @return
	 * @throws Exception
	 */
	private static String getDateFormat( String dt ) throws Exception
	{
		int dtlen = dt.length();
		String dateFormat = "";
		if( dtlen == 8 ) {
			dateFormat = yyyyMMdd;
		} else if( dtlen == 12 ) {
			dateFormat = yyyyMMddHHmm;
		} else if( dtlen == 14 ) {
			dateFormat = yyyyMMddHHmmss;
		} else {
			throw new Exception( "허용된 날짜 포멧이 아닙니다. 가능한 포멧 : yyyyMMdd, yyyyMMddHHmm, yyyyMMddHHmmss" );
		}

		return dateFormat;
	}

	/**
	 * 
	 * 해당 글의 수정권힌이 있는지 체크
	 * 
	 * @param sMap
	 * @param wMap
	 * @param oId
	 * @throws Exception
	 */
	public static void checkModifyAuth(Map sMap, Map wMap, String oId) throws Exception {
		String sessionId = ObjectUtils.toString(sMap.get("pagoda.id"));
		String writer = ObjectUtils.toString(wMap.get(oId));

		if ("".equals(sessionId) || !sessionId.equals(writer)) {
			throw new Exception("수정 권한이 없습니다.");
		}
	}

	

	/**
	 *  //암호화
		    Base64Utils base64 = new Base64Utils();  //요놈선언
		    String encryptKey = "abcdefghijk";     //key 선언



		    String W_ORG_FG = "암호화할문자열임";



		   //seed + base64 암호화, 복호화
		    String EN_ORG_FG = base64.encrypt(W_ORG_FG,encryptKey);
		    String DE_ORG_FG = base64.decrypt(EN_ORG_FG,encryptKey);


		    //base64로만 암호화, 복호화
		    EN_ORG_FG = base64.base64Encoding(W_ORG_FG);

 		DE_ORG_FG = base64.decrypt(EN_ORG_FG);

	 * 
	 * 
	 * @param str
	 * @param key
	 * @return
	 */
/*
	//Base64 + Seed 암호화
	@SuppressWarnings("deprecation")
	public static String encrypt(String str, String key)
	{
		if (key.length() != 24) {
			return "";
		}

		try {
			String strResult;
			String strTemp = "";
			strResult = "";
			BASE64Encoder encoder = new BASE64Encoder();
			SeedAlg seedAlg = new SeedAlg(key.getBytes());
			strTemp = new String(encoder.encode(seedAlg.encrypt(str.getBytes())));
			for(int i = 0; i < strTemp.length(); i++) {
				if(strTemp.charAt(i) != '\n' && strTemp.charAt(i) != '\r') {
					strResult = strResult + strTemp.charAt(i);
				}
			}
			strResult = java.net.URLEncoder.encode( strResult);
			return strResult;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	//Base64 + Seed 복호화
	public static String decrypt(String str, String key) {
		if (key.length() != 24) {
			return "";
		}


		try {
			//str = java.net.URLDecoder.decode( str );
			String strResult;
			String strTemp = "";
			strResult = "";
			BASE64Decoder decoder = new BASE64Decoder();
			SeedAlg seedAlg = new SeedAlg(key.getBytes());
			strTemp = new String(seedAlg.decrypt(decoder.decodeBuffer(str)));
			for (int i = 0; i < strTemp.length() && strTemp.charAt(i) != 0;) {
				if (strTemp.charAt(i) != '\n' && strTemp.charAt(i) != '\r') {
					strResult = strResult + strTemp.charAt(i);
					i++;
				}
			}

			return strResult;
		} catch (Exception ex) {
			return null;
		}
	}
*/	
	/**
	 * 
	 * 메일 보내기
	 * 
	 * @param sMap
	 * @param wMap
	 * @param oId
	 * @throws Exception
	 */
	public static void sendMail(String legacyid,
			String mailType,
			String toEmail,
			String toName,
			String fromEmail,
			String fromName,
			String mailTitle,
			String tag1,
			String tag2,
			String tag3,
			String tag4,
			String tag5,
			String tag6,
			String tag7,
			String tag8,
			String tag9 ) throws Exception {

		Connection conn = null;
		Statement stmt = null;

		try {
			StringBuffer query = new StringBuffer();

			query.append("insert /* jobpagoda.sendMail */ into automail_interface");
			query.append("	(legacyid,mailtype,email,name,insertdate,sendtime,sendyn,fromaddress,fromname , title, ");
			query.append("  tag1,tag2,tag3,tag4,tag5,tag6,tag7,tag8,tag9)");
			query.append(" values ");
			query.append("	('"+legacyid+"', '"+mailType+"', '"+toEmail+"', '"+toName+"', ");
			query.append(" getdate(), getdate(), 'N', '"+fromEmail+"', '"+fromName+"', '" + mailTitle + "', ");
			query.append(" '"+tag1 + "', '"+tag2 + "', '"+tag3 + "', '"+tag4 + "', '"+tag5 + "', '"+tag6 + "', '"+tag7 + "', ");
			query.append(" '"+tag8 + "', '"+tag9 + "')");
			
	    	Context initCtx = new InitialContext();
	        Context envCtx = (Context)initCtx.lookup("java:comp/env");
	        DataSource ds = (DataSource)envCtx.lookup("jdbc/pagoda_mail");
	        conn = ds.getConnection();


	        log.info("이메일 insert===>"+query.toString());
			stmt = conn.createStatement();

			stmt.executeUpdate(query.toString());
			
		} catch (Exception e) {
			//e.printStackTrace();
			log.info("이메일 Error===>"+e.toString());
		} finally {
			try {
				if(stmt != null) {
					stmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch(Exception ex) {}
		}
	}
	
}
