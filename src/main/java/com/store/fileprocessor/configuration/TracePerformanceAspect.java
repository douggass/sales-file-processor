package com.store.fileprocessor.configuration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class TracePerformanceAspect {

	/**
	 * Method that will demonstrate the runtime of batch processing
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Around("execution(* com.store.fileprocessor.configuration.batch..*.*(..)))")
	public Object logTracePerformanceAspect(ProceedingJoinPoint joinPoint) throws Throwable {

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

		// Get intercepted method details
		String className = methodSignature.getDeclaringType().getSimpleName();
		String methodName = methodSignature.getName();

		long start = System.currentTimeMillis();

		Object result = joinPoint.proceed();
		long end = System.currentTimeMillis();

		// Log method execution time
		log.info("Execution time of {} . {} :: {} ms", className, methodName, (end - start));

		return result;
	}
}
