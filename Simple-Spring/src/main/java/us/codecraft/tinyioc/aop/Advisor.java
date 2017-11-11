package us.codecraft.tinyioc.aop;

import org.aopalliance.aop.Advice;

/**
 * @author yihua.huang@dianping.com
 * 通知器对象可以获取一个通知对象 Advice 。
 * 就是用于实现 具体的方法拦截，需要使用者编写，也就对应了 Spring 中的前置通知、后置通知、环切通知等。
 */
public interface Advisor {

    Advice getAdvice();
}
