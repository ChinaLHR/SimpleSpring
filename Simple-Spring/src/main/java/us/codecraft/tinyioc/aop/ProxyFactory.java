package us.codecraft.tinyioc.aop;

/**
 * @author yihua.huang@dianping.com
 * Proxy工厂：根据TargetSource的类型自动创建代理
 */
public class ProxyFactory extends AdvisedSupport implements AopProxy {

	@Override
	public Object getProxy() {
		return createAopProxy().getProxy();
	}

	protected final AopProxy createAopProxy() {
		return new Cglib2AopProxy(this);
	}
}
