package com.academy.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.academy.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Slf4j
public class kickOutFilter extends AccessControlFilter {

    private String kickOutUrl; //踢出后到的地址

    private boolean kickOutAfter = false; //踢出之前登录的/之后登录的用户 默认踢出之前登录的用户

    private int maxSession = 1; //同一个帐号最大会话数 默认1

    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied (ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if(!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }

        Session session = subject.getSession();
        SysUser user = (SysUser) subject.getPrincipal();
        String username = user.getLoginName();
        Serializable sessionId = session.getId();
        //读取缓存   没有就存入
        Deque<Serializable> deque = cache.get(username);
        //如果此用户没有session队列，也就是还没有登录过，缓存中没有
        //就new一个空队列，不然deque对象为空，会报空指针
        if(deque == null){
            deque = new LinkedList<Serializable>();
        }

        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if(!deque.contains(sessionId) && session.getAttribute("kickOut") == null) {
            //将sessionId存入队列
            deque.push(sessionId);
            //将用户的sessionId队列缓存
            cache.put(username, deque);
        }

        //如果队列里的sessionId数超出最大会话数，开始踢人
        while(deque.size() > maxSession) {
            Serializable kickOutSessionId = null;
            if(kickOutAfter) { //如果踢出后者
                kickOutSessionId = deque.removeFirst();
                //踢出后再更新下缓存队列
                cache.put(username, deque);
            } else { //否则踢出前者
                kickOutSessionId = deque.removeLast();
                //踢出后再更新下缓存队列
                cache.put(username, deque);
            }

            try {
                //获取被踢出的sessionId的session对象
                Session kickOutSession = sessionManager.getSession(new DefaultSessionKey(kickOutSessionId));
                if(kickOutSession != null) {
                    //设置会话的kickOut属性表示踢出了
                    kickOutSession.setAttribute("kickOut", true);
                }
            } catch (Exception e) {//ignore exception
                e.printStackTrace();
            }
        }

        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickOut") != null) {
            //会话被踢出了
            try {
                //退出登录
                subject.logout();
            } catch (Exception e) { //ignore
                e.printStackTrace();
            }
            saveRequest(request);

            Map<String, String> resultMap = new HashMap<String, String>();
            //判断是不是Ajax请求
            if ("XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {

                resultMap.put("user_status", "300");
                resultMap.put("message", "您已经在其他地方登录，请重新登录！");
                //输出json串
                out(response, resultMap);
            }else{
                //重定向
                WebUtils.issueRedirect(request, response, kickOutUrl);
            }
            return false;
        }
        return true;
    }

    private void out(ServletResponse response, Map<String, String> resultMap) throws IOException {
        try {
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.println(JSON.toJSONString(resultMap));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("KickOutSessionFilter 输出JSON异常，可以忽略");
        }
    }

    public String getKickOutUrl() {
        return kickOutUrl;
    }

    public void setKickOutUrl(String kickOutUrl) {
        this.kickOutUrl = kickOutUrl;
    }

    public boolean isKickOutAfter() {
        return kickOutAfter;
    }

    public void setKickOutAfter(boolean kickOutAfter) {
        this.kickOutAfter = kickOutAfter;
    }

    public int getMaxSession() {
        return maxSession;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public Cache<String, Deque<Serializable>> getCache() {
        return cache;
    }

    public void setCache(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro_redis_cache");
    }
}
