package com.zlw.commons.validation;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;

import com.zlw.commons.validation.annotation.Validation;

public class ValidationAdvisor extends AbstractPointcutAdvisor {
	
	private static final long serialVersionUID = 1L;
	
	
	@Autowired
	private ValidationInterceptor advice;
	
	private StaticMethodMatcherPointcut pointcut = new StaticMethodMatcherPointcut() {
		@Override
		public boolean matches(Method method, Class<?> targetClass) {
			return method.isAnnotationPresent(Validation.class);
		}
	};
	


	@Override
	public Pointcut getPointcut() {
		return pointcut;
	}

	@Override
	public Advice getAdvice() {
		return advice;
	}
}
