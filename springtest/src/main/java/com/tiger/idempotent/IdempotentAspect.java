/**
 * @Title: MethodIdempotentInterceptor.java
 * @Copyright: © 2018 我来贷
 * @Company: 深圳卫盈智信科技有限公司
 */

package com.tiger.idempotent;

import java.lang.reflect.Method;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class IdempotentAspect {

    private static final Logger logger = LoggerFactory.getLogger(IdempotentAspect.class);

    private ParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    private ExpressionParser parser = new SpelExpressionParser();

    private static final String MOBILE = "X-User-Mobile";

    private static final String TOKEN = "X-User-Token";

    private static final String ACCESS_TOKEN = "X-Access-Token";
    /**
     * 命名空间
     */
    private static final String NAMESPACE = "thirdparty.open.idempotent";

    @Autowired
    private RedisChecker redisChecker;

    @Around(value = "@annotation(idempotent)", argNames = "joinPoint, idempotent")
    public Object invoke(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        // 从方法处理器中获取出要调用的方法
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        // 方法全名
        String methodPath = method.getDeclaringClass().getName() + "." + method.getName();
        // 参数名
        String parameterName = getParameterName(method);
        // 获取参数
        String parm = generateParm(joinPoint.getArgs(), method, idempotent);
        // 拼装key文本
        String keyStr = NAMESPACE + "." + methodPath + parameterName + parm;
        // 生成key
        String key = DigestUtils.sha1Hex(keyStr);

        // 获取超时时间
        int expired = idempotent.expired();
        // 获取可调用次数
        int times = idempotent.times();

        List result;
        // 校验flag
        boolean check = false;
        try {
            // 检查是否超过次数,过期时间单位转化为毫秒
            result = redisChecker.check(key, expired, times);
            if ((Long)result.get(0) == 1) {
                check = true;
            }
        } catch (Exception e) {
            // redis异常时放行
            logger.error("IdempotentInterceptor redis Exception:", e);
            return joinPoint.proceed();
        }
        if (check) {
            // 获取锁成功时放行
            logger.info(methodPath + "防重校验成功，参数：" + parm + ",key=" + key);
            return joinPoint.proceed();
        }
        // 超过请求次数时抛出异常
        Long retryTime = (Long)result.get(1) / 1000;
        String errorMessage = methodPath + "超过" + expired + "秒内" + times + "次的请求的限制,请" + retryTime + "秒后重试,参数:" + parm;
        logger.warn(errorMessage);
        throw new Exception(errorMessage);
    }

    /**
     * 
     * 获取参数
     *
     * @param args
     * @param method
     * @param idempotent
     * @return
     */
    private String generateParm(Object[] args, Method method, Idempotent idempotent) {
        // 没有参数时
        if (args == null || args.length <= 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        String[] keyTargets = idempotent.value();
        // value为空时全量作为key,
        if (null == keyTargets || keyTargets.length <= 0) {
            // for (int i = 0; i < args.length; i++) {
            // // 过滤HttpServletRequest和HttpServletResponse
            // Object object = args[i];
            // // 将HttpServletRequest中的参数加入幂等计算
            // if (object instanceof HttpServletRequest) {
            // HttpServletRequest request = (HttpServletRequest) object;
            // //组装参数
            // appendHttpServletRequest(result, request);
            // }
            // }
        }
        // value有值时取值作为key
        else {
            StandardEvaluationContext context = new StandardEvaluationContext(args);
            // 获取参数名称
            String[] parametersName = discoverer.getParameterNames(method);
            for (int i = 0; i < args.length; i++) {
                Object object = args[i];
                // 将HttpServletRequest中的参数加入幂等计算
                if (object instanceof HttpServletRequest) {
                    HttpServletRequest request = (HttpServletRequest)object;
                    // 组装参数
                    appendHttpServletRequest(result, request);
                }
                // 设置参数名称和对应的值
                context.setVariable(parametersName[i], object);
            }
            for (int i = 0; i < keyTargets.length; i++) {
                // 解析SpEL表达式
                Expression exp = parser.parseExpression(keyTargets[i]);
                // 获取SpEL表达式的值
                Object value = exp.getValue(context, Object.class);
                // 组装参数
                append(result, keyTargets[i] + "=" + value.toString());
            }

        }
        return result.toString();
    }

    /**
     * 
     * 将HttpServletRequest中的参数加入幂等计算
     *
     * @param result
     */
    private void appendHttpServletRequest(StringBuilder result, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        String mobile = request.getHeader(MOBILE);
        String token = request.getHeader(TOKEN);
        String accessToken = request.getHeader(ACCESS_TOKEN);
        append(result, "#requestURI=" + requestURI);
        append(result, "#requestMethod=" + requestMethod);
        append(result, "#mobile=" + mobile);
        append(result, "#token=" + token);
        append(result, "#accessToken=" + accessToken);

    }

    /**
     * 
     * 获取参数名
     *
     * @param method
     * @return
     */
    private String getParameterName(Method method) {
        StringBuilder parameterName = new StringBuilder("(");
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> class1 : parameterTypes) {
            parameterName.append(class1.getSimpleName());
            parameterName.append(",");
        }
        if (parameterName.length() > 1) {
            parameterName.replace(parameterName.length() - 1, parameterName.length(), ")");
        } else {
            parameterName.append(")");
        }
        return parameterName.toString();
    }

    /**
     * 拼接字符串
     * 
     * @param src
     * @param str
     */
    private void append(StringBuilder src, String str) {
        if (!StringUtils.isBlank(str)) {
            src.append(str);
        }
    }

}
