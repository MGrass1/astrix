/*
 * Copyright 2014 Avanza Bank AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.avanza.astrix.ft.hystrix;

import java.util.Optional;

import com.avanza.astrix.beans.config.BeanConfigurations;
import com.avanza.astrix.beans.core.AstrixBeanKey;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;

final class BeanConfigurationPropertiesStrategy extends HystrixPropertiesStrategy {
	
	private final BeanConfigurations beanConfigurations;
	private final BeanMapping beanMapping;
	
	public BeanConfigurationPropertiesStrategy(BeanConfigurations beanConfigurations, BeanMapping beanMapping) {
		this.beanConfigurations = beanConfigurations;
		this.beanMapping = beanMapping;
	}

	@Override
	public HystrixCommandProperties getCommandProperties(HystrixCommandKey commandKey,
			com.netflix.hystrix.HystrixCommandProperties.Setter builder) {
		return this.beanMapping.getBeanKey(commandKey)
							   .flatMap(beanKey -> createCommandProperties(beanKey, commandKey, builder))
							   .orElse(super.getCommandProperties(commandKey, builder));
	}
	
	private Optional<HystrixCommandProperties> createCommandProperties(AstrixBeanKey<?> beanKey, HystrixCommandKey commandKey,
																	com.netflix.hystrix.HystrixCommandProperties.Setter builder) {
		return Optional.ofNullable(beanConfigurations.getBeanConfiguration(beanKey))
					   .map(beanConfiguration -> new AstrixCommandProperties(beanConfiguration, commandKey, builder));
	}
	
	@Override
	public HystrixThreadPoolProperties getThreadPoolProperties(HystrixThreadPoolKey threadPoolKey, 
									com.netflix.hystrix.HystrixThreadPoolProperties.Setter builder) {
		return this.beanMapping.getBeanKey(threadPoolKey)
							   .flatMap(beanKey -> createThreadPoolProperties(beanKey, threadPoolKey, builder))
							   .orElse(super.getThreadPoolProperties(threadPoolKey, builder));
	}
	
	private Optional<HystrixThreadPoolProperties> createThreadPoolProperties(AstrixBeanKey<?> beanKey, HystrixThreadPoolKey threadPoolKey, 
								com.netflix.hystrix.HystrixThreadPoolProperties.Setter builder) {
		return Optional.ofNullable(beanConfigurations.getBeanConfiguration(beanKey))
					    .map(beanConfiguration -> new AstrixThreadPoolProperties(beanConfiguration, threadPoolKey, builder));
	}
	
	@Override
	public HystrixCollapserProperties getCollapserProperties(HystrixCollapserKey collapserKey,
			com.netflix.hystrix.HystrixCollapserProperties.Setter builder) {
		return super.getCollapserProperties(collapserKey, builder);
	}
	
	

}