package com.syf.papermanager.config.shiro;

import com.alibaba.fastjson.JSONObject;
import com.syf.papermanager.bean.entity.ResponseEntity;
import com.syf.papermanager.bean.enums.ResponseEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.config.shiro
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/14 21:46
 */
@Slf4j
public class ShiroUserFilter extends FormAuthenticationFilter {
    /**
     * 这里会进行第一次拦截，若返回false会进入onAccessDenied
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean allowed = super.isAccessAllowed(request, response, mappedValue);
        if (!allowed) {
            // 判断请求是否是options请求，options请求直接放行解决跨域问题
            String method = WebUtils.toHttp(request).getMethod();
            if (method.equals("OPTIONS")) {
                log.info("来自" + request.getRemoteAddr() + ":" + request.getRemotePort() + "的OPTIONS请求被放行");
                return true;
            }
        }
        return allowed;
    }

    /**
     * 在这里进行第二次拦截
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //有由采取的是前后端分离的开发，所以shiro配置类中并没有配置登录url、跳转url这些，isLoginRequest恒为假
        if (isLoginRequest(request, response)) { // 判断是否是登录请求，shiro配置类中设置的loginUrl
            if (isLoginSubmission(request, response)) { // 判断是否为post访问
                return executeLogin(request, response);
            } else {
                // sessionID已经注册,但是并没有使用post方式提交
                return true;
            }
        } else {
            /*
             * 跨域访问有时会先发起一条不带token，不带cookie的访问。
             * 这就需要我们抓取这条访问，然后给他通过，否则只要是跨域的访问都会因为未登录或缺少权限而被拦截
             * （如果重写了isAccessAllowed，就无需下面的判断）
             */
//			if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
//				resp.setStatus(HttpStatus.OK.value());
//				return true;
//			}
            /*
             * 跨域的第二次请求就是普通情况的request了，在这对他进行拦截
             */
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
            resp.setHeader("Access-Control-Allow-Credentials", "true");
            resp.setContentType("application/json; charset=utf-8");
            resp.setCharacterEncoding("UTF-8");
            //设置未登录状态码，那么前端拿到的就是一个401错误
            //resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            PrintWriter out = resp.getWriter();
            ResponseEntity result = new ResponseEntity(ResponseEnums.UNAUTHORIZED.getCode(), ResponseEnums.UNAUTHORIZED.getMsg());
            out.println(JSONObject.toJSONString(result));
            out.flush();
            out.close();
        }
        return false;
    }
}
