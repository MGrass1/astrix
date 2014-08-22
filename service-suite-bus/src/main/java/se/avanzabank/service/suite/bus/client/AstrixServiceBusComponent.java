/*
 * Copyright 2014-2015 Avanza Bank AB
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
package se.avanzabank.service.suite.bus.client;

import java.util.List;

import se.avanzabank.service.suite.context.AstrixContext;


public interface AstrixServiceBusComponent {
	
	/**
	 * Check whether this component exports any services on a given descriptor.
	 * 
	 * @param possibleDescriptorHolder
	 * @return
	 */
	List<Class<?>> getExportedServices(Class<?> possibleDescriptorHolder);
	
	
	<T> T createService(Class<?> descriptorHolder, Class<T> type, AstrixServiceProperties serviceProperties, AstrixContext context);
	
}
