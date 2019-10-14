package org.tinygroup.httpvisitor;

/**
 * 请求拦截器
 *
 * @author yancheng11334
 */
public interface RequestInterceptor {

    /**
     * 对模拟请求对象执行业务处理
     *
     * @param request
     * @throws Exception
     */
    void process(Request request) throws Exception;
}
