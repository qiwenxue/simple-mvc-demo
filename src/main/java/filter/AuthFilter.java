package filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demo.www.test.util.Cache;
import com.demo.www.test.util.Const;
import com.demo.www.test.util.CookieUtil;

import core.ReflectionCache;
import core.util.CommUtils;
import net.sf.json.JSONObject;
public class AuthFilter implements Filter {
  private String uri;
    
  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
      ServletException {
    HttpServletRequest req = (HttpServletRequest)request;
    HttpServletResponse resp = (HttpServletResponse)response;
    request.setCharacterEncoding("UTF-8");
    String url = req.getRequestURI();
    String contextPath = req.getContextPath();
    if ( !contextPath.equals("/") && !contextPath.equals("") ) {
      url = url.substring(contextPath.length());
    }
    if ( ReflectionCache.getClazz(url) == null ) {
      chain.doFilter(req, resp);
      return;
    }
    if ( (","+uri+",").contains((","+url+",")) ) {//不需要进行拦截的地址
      chain.doFilter(req, resp);
      return;
    }
    String token = CookieUtil.getCookieValue(req, Const.LOGIN.loginToken);
    if ( CommUtils.isNotNull(token) && !"undefined".equals(token) ) {
      try {
        String str = Cache.get(token);
        if ( CommUtils.isNull(str)) {
          JSONObject obj = new JSONObject();
          obj.put(core.util.Const.RESPONSE.STATE, 1);
          obj.put(core.util.Const.RESPONSE.ERRMSG, "您尚未登录,请重新登录");
          ajaxMethod( obj.toString(), resp );
          return;
        } else {
          Cache.set(token, str);//延长过期时间
        }
      } catch (Exception e) {
         e.printStackTrace();
         return;
      }
    } else {
      JSONObject obj = new JSONObject();
      obj.put(core.util.Const.RESPONSE.STATE, 1);
      obj.put(core.util.Const.RESPONSE.ERRMSG, "您尚未登录,请重新登录");
      ajaxMethod( obj.toString(), resp );
      return;
    }
    chain.doFilter(req, resp);
  }

  @Override
  public void init(FilterConfig config) throws ServletException {
    uri = CommUtils.getPropValByKey("action.request.url.noneed.auth", "conf");
  }
   
  
  /**
   * ajax方法
   * 
   * @param messages
   * @throws Exception
   */
  private void ajaxMethod( String messages, HttpServletResponse response ) {
    PrintWriter out = null;
    try {
      response.setContentType("text/html;charset=UTF-8");
      response.setHeader("Cache-Control", "no-cache");
      response.setCharacterEncoding("UTF-8");
      out = response.getWriter();
      out.print(String.valueOf(messages));
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (out != null) {
        out.flush();
        out.close();
      }
    }
  }
}
