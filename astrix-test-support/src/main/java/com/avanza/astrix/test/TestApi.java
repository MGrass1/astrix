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
package com.avanza.astrix.test;

import java.util.stream.Stream;

/**
 * SPI to make test specific implementations of Astrix services ("service beans") available in the service registry managed by an {@link AstrixRule}. <p>
 * 
 * @author Elias Lindholm
 *
 */
public interface TestApi {

	/**
	 * Export all service beans provided by this TestApi. Note that it is NOT possible
	 * to export libraries from a TestApi.
	 * 
	 * TestApi specific methods (for instance methods for test-data population) should
	 * be placed directly in the given TestApi class, and NOT exported using this method.
	 * 
	 * @param testContext
	 */
	void exportServices(TestContext testContext);

	/**
	 * Returns a Stream of all TestApi's used by this TestApi. When {@link AstrixRule} loads a TestApi
	 * it will also load all its dependencies.
	 * 
	 * 
	 * Default implementation returns an empty stream indicating that a TestApi does not depend on any
	 * other TestApi's.
	 * 
	 * @return
	 */
	default Stream<Class<? extends TestApi>> getTestApiDependencies() {
		return Stream.empty();
	}

	interface TestContext {
		/**
		 * Registers a given Service in the associated TestContext. The service will be registered in the
		 * service registry associated with the given TestContext. <p>
		 * 
		 * @param serviceBean
		 * @param serviceImpl
		 */
		<T> void registerService(Class<T> serviceBean, T serviceImpl);

		/**
		 * See {@link #registerService(Class, Object)}
		 * @param serviceBean
		 * @param qualifier
		 * @param serviceImpl
		 */
		<T> void registerService(Class<T> serviceBean, String qualifier, T serviceImpl);

		/**
		 * Each TestContext has an AstrixContext (which is managed by the associated AstrixRule). The AstrixContext
		 * is shared between all TestApi instances. Beans might be pulled from the
		 * AstrixContext using this method. 
		 */
		<T> T getBean(Class<T> beanType);
		
		/**
		 * see {@link #getBean(Class)}
		 */
		<T> T getBean(Class<T> beanType, String qualifier);
		
		/**
		 * Returns a TestApi instance from the given TestContext. 
		 * 
		 * @param testApi
		 * @return
		 */
		<T extends TestApi> T getTestApi(Class<T> testApi);
	}


}
