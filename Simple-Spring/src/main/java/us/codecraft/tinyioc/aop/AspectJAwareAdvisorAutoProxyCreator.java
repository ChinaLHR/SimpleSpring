package us.codecraft.tinyioc.aop;

import org.aopalliance.intercept.MethodInterceptor;
import us.codecraft.tinyioc.beans.BeanPostProcessor;
import us.codecraft.tinyioc.beans.factory.AbstractBeanFactory;
import us.codecraft.tinyioc.beans.factory.BeanFactory;

import java.util.List;

/**
 * @author yihua.huang@dianping.com
 * AOP实现的核心：
 * 将AOP融入Bean的创建过程
 * AspectJ方式织入的核心，是一个BeanPostProcess（会扫描所有的Pointcut并对Bean进行织入）
 */
public class AspectJAwareAdvisorAutoProxyCreator implements BeanPostProcessor, BeanFactoryAware {

	private AbstractBeanFactory beanFactory;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
		return bean;
	}


//在Bean创建之后对Bean进行AOP
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
		if (bean instanceof AspectJExpressionPointcutAdvisor) {
			return bean;
		}
        if (bean instanceof MethodInterceptor) {
            return bean;
        }

        //获取所有Pointcut，进行切面处理，返回完成Aop的Proxy
		List<AspectJExpressionPointcutAdvisor> advisors = beanFactory
				.getBeansForType(AspectJExpressionPointcutAdvisor.class);
		for (AspectJExpressionPointcutAdvisor advisor : advisors) {
			if (advisor.getPointcut().getClassFilter().matches(bean.getClass())) {
				AdvisedSupport advisedSupport = new AdvisedSupport();
				advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
				advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());

				TargetSource targetSource = new TargetSource(bean, bean.getClass().getInterfaces());
				advisedSupport.setTargetSource(targetSource);

				return new JdkDynamicAopProxy(advisedSupport).getProxy();
			}
		}
		return bean;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws Exception {
		this.beanFactory = (AbstractBeanFactory) beanFactory;
	}
}
