package com.data.provider.config;

import com.data.provider.core.LicenseInstall;
import com.data.provider.core.LicenseVerify;
import com.data.provider.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/*
 * @Author: tianyong
 * @Date: 2020/7/21 16:14
 * @Description: 拦截器处理
 */
public class VerifyInterceptor implements HandlerInterceptor {


    @Autowired
    private LicenseInstall listener;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            VerifyConfig annotation = method.getAnnotation(VerifyConfig.class);
            if (annotation != null) {
                LicenseVerify licenseVerify = new LicenseVerify();
                // 校验证书是否有效
                boolean verifyResult = licenseVerify.verify(listener.getSubjectVerify());
                if (verifyResult) {
                    return true;
                } else {
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("text/html;charset=utf-8");
                    Map<String, String> result = new HashMap<>(1);
                    result.put("result", "您的证书无效，请核查服务器是否取得授权或重新申请证书！");
                    response.getWriter().write(result == null ? "" : Utils.objectToJson(result));
                    return false;
                }
            }
        }
        return true;
    }
}
