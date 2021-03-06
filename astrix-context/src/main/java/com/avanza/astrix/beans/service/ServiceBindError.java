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
package com.avanza.astrix.beans.service;

import com.avanza.astrix.core.ServiceUnavailableException;
/**
 * Thrown when service binding fails.
 * 
 * This means that service discovery was successful, but that
 * binding fails using the {@link ServiceComponent} defined
 * in the service properties returned from service discovery.
 * 
 * See the associated cause for more details about the failure.
 * 
 * 
 * 
 * @author Elias Lindholm
 *
 */
final class ServiceBindError extends ServiceUnavailableException {
	private static final long serialVersionUID = 1L;
	public ServiceBindError(String msg) {
		super(msg);
	}
}