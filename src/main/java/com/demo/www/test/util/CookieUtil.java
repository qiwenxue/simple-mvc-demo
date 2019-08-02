package com.demo.www.test.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.util.CommUtils;

public class CookieUtil {

	public static String getCookieValue(HttpServletRequest request, String parem) {
		String val = null;
		Cookie[] cookie = request.getCookies();
		if (cookie == null) {
			return null;
		}
		for (Cookie c : cookie) {
			if (parem.equals(c.getName())) {
				try {
					val = CommUtils.decode(c.getValue());
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
		return val;
	}

	public static void addCookie(HttpServletRequest request, HttpServletResponse response, String parameter, String val) {
		try {
			if (CommUtils.isNull(val)) {
				return;
			}
			Cookie c = new Cookie(parameter, val);
			//String domain = request.getHeader("host");
			//c.setDomain(domain);
			c.setMaxAge(3600*2);//2天过期
			c.setPath("/");
			c.setSecure(false);
			response.addHeader("Set-Cookie", parameter+"="+ val + ";HTTPOnly");
			response.addCookie(c);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
}
