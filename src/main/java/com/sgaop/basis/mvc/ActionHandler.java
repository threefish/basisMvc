package com.sgaop.basis.mvc;

import com.sgaop.basis.annotation.Aop;
import com.sgaop.basis.annotation.Inject;
import com.sgaop.basis.annotation.IocBean;
import com.sgaop.basis.annotation.Parameter;
import com.sgaop.basis.cache.CacheManager;
import com.sgaop.basis.constant.Constant;
import com.sgaop.basis.error.WebErrorMessage;
import com.sgaop.basis.ioc.IocBeanContext;
import com.sgaop.basis.util.ClassTool;
import com.sgaop.basis.util.ParameterConverter;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/5/9 0009
 * To change this template use File | Settings | File Templates.
 */
public class ActionHandler {

    private static final Logger logger = Logger.getRootLogger();

    /**
     * 执行action
     *
     * @param servletPath
     * @param methodType
     * @param request
     * @param response
     * @return
     */
    public static ActionResult invokeAction(String servletPath, String methodType, HttpServletRequest request, HttpServletResponse response) {
        WebErrorMessage webErrorMessage = new WebErrorMessage();
        webErrorMessage.setCode(200);
        ActionResult actionResult = new ActionResult();
        try {
            ActionMethod actionMethod = (ActionMethod) CacheManager.getUrlCache(servletPath);
            if (actionMethod != null) {
                actionResult.setResultType(actionMethod.getOK());
                if (methodType.equals(actionMethod.getMethod())) {
                    Class<?> actionClass = actionMethod.getActionClass();
                    Method handlerMethod = actionMethod.getActionMethod();
                    handlerMethod.setAccessible(true);
                    IocBean iocBean = actionClass.getAnnotation(IocBean.class);

                    Object beanInstance = null;
//                    Aop aop = handlerMethod.getAnnotation(Aop.class);
//                    if (aop != null && aop.value().length > 0) {
//                        List<Callback> proxys = new ArrayList();
//                        for (String str : aop.value()) {
//                            Callback proxy = (Callback) IocBeanContext.me().getBean(str);
//                            proxys.add(proxy);
//                        }
//                        Enhancer enhancer = new Enhancer();
//                        enhancer.setSuperclass(actionClass);
//                        enhancer.setUseCache(true);
//                        enhancer.setCallbacks(proxys.toArray(new Callback[0]));
//                        // 增强目标类
//                        beanInstance = enhancer.create();
//                    } else {
                        beanInstance = IocBeanContext.me().getBean(iocBean.value());
                        if (beanInstance == null) {
                            beanInstance = actionClass.newInstance();
                        }
//                    }
                    /**
                     * 自动注入参数
                     */
                    Field[] fields = actionClass.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        if (field.getGenericType().equals(HttpServletRequest.class)) {
                            field.set(beanInstance, request);
                        } else if (field.getGenericType().equals(HttpServletResponse.class)) {
                            field.set(beanInstance, response);
                        } else if (field.getGenericType().equals(HttpSession.class)) {
                            field.set(beanInstance, request.getSession());
                        } else if (field.getGenericType().equals(ServletContext.class)) {
                            field.set(beanInstance, request.getServletContext());
                        } else {
//                            Inject inject = field.getAnnotation(Inject.class);
//                            if (iocBean != null && inject != null) {
//                                String resName = inject.value().equals("") ? field.getName() : inject.value();
//                                IocBeanContext.me().injectBean(field,beanInstance,resName);
//                            }
                        }
                    }
                    Class<?>[] actionParamTypes = handlerMethod.getParameterTypes();
                    List<Object> actionParamList = new ArrayList<>();
                    Annotation[][] annotations = handlerMethod.getParameterAnnotations();
                    Map<String, ?> requestParameterMap = Mvcs.getReqMap();
                    for (int i = 0; i < annotations.length; i++) {
                        Annotation[] annotation = annotations[i];
                        Class anClass = actionParamTypes[i];
                        if (annotation.length > 0) {
                            for (Annotation anno : annotation) {
                                if (anno instanceof Parameter) {
                                    String webParamKeyName = ((Parameter) anno).value();
                                    if (webParamKeyName.endsWith(">>")) {
                                        webParamKeyName = webParamKeyName.replace(">>", "");
                                        actionParamList.add(ParameterConverter.bulid(anClass, webParamKeyName, requestParameterMap));
                                    } else {
                                        Object ParamValuesObject = requestParameterMap.get(webParamKeyName);
                                        Object val = ClassTool.ParamCast(anClass, ParamValuesObject);
                                        actionParamList.add(val);
                                    }
                                }
                            }
                        } else if (anClass.equals(HttpServletRequest.class)) {
                            actionParamList.add(request);
                        } else if (anClass.equals(HttpServletResponse.class)) {
                            actionParamList.add(response);
                        } else if (anClass.equals(HttpSession.class)) {
                            actionParamList.add(request.getSession());
                        } else if (anClass.equals(ServletContext.class)) {
                            actionParamList.add(request.getServletContext());
                        } else {
                            webErrorMessage.setCode(500);
                            webErrorMessage.setMessage("Action的参数,除HttpServletRequest,HttpServletResponse外必须使用@" + Parameter.class + "注解");
                            logger.warn(webErrorMessage.getMessage());
                        }
                    }
                    Object object = handlerMethod.invoke(beanInstance, actionParamList.toArray());
                    actionResult.setResultData(object);
                } else {
                    webErrorMessage.setCode(404);
                }
            } else if (servletPath.endsWith(Constant.PAGE_SUFFIX)) {
                /**
                 * 在没有找到注解的情况下，并且访问的是jsp文件
                 */
                webErrorMessage.setJsp(true);
            } else {
                webErrorMessage.setCode(404);
            }
        } catch (Exception e) {
            e.printStackTrace();
            webErrorMessage.setCode(500);
            webErrorMessage.setException(e);
            logger.debug(e.getMessage());
        }
        if (webErrorMessage.getCode() == 404) {
            webErrorMessage.setMessage("  [" + methodType + "] Not Found URI=" + servletPath);
            logger.debug(webErrorMessage.getMessage());
        }
        actionResult.setWebErrorMessage(webErrorMessage);
        return actionResult;
    }
}
