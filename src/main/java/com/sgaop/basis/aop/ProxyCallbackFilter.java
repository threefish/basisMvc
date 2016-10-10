package com.sgaop.basis.aop;

import com.sgaop.basis.annotation.Aop;
import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/10/10 0010
 * To change this template use File | Settings | File Templates.
 */
public class ProxyCallbackFilter implements CallbackFilter {
    @Override
    public int accept(Method method) {
        //TODO 目前只能一个方法对应一个切面，无法添加多个切面，寻求解决中。如果使用模拟aop就不存在这个问题了，比较纠结
        Aop aop = method.getAnnotation(Aop.class);
        if(aop!=null){
            return 1;
        } else{
            return 0;
        }
    }
}
