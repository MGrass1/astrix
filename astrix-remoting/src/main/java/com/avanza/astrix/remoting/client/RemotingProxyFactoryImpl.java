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
package com.avanza.astrix.remoting.client;

import java.util.Objects;

import com.avanza.astrix.beans.core.ReactiveTypeConverter;
import com.avanza.astrix.beans.service.ServiceDefinition;
import com.avanza.astrix.beans.service.ServiceProperties;
import com.avanza.astrix.beans.tracing.AstrixTraceProvider;
import com.avanza.astrix.beans.tracing.DefaultTraceProvider;
import com.avanza.astrix.core.remoting.RoutingStrategy;
import com.avanza.astrix.core.util.ReflectionUtil;
import com.avanza.astrix.modules.AstrixInject;
import com.avanza.astrix.versioning.core.AstrixObjectSerializer;
import com.avanza.astrix.versioning.core.ObjectSerializerFactory;

public class RemotingProxyFactoryImpl implements RemotingProxyFactory {
	
	private final ObjectSerializerFactory objectSerializerFactory;
	private final ReactiveTypeConverter reactiveTypeConverter;
	private final AstrixTraceProvider astrixTraceProvider;

	/**
	 * @deprecated please use {@link #RemotingProxyFactoryImpl(ObjectSerializerFactory, ReactiveTypeConverter, AstrixTraceProvider)}
	 */
	@Deprecated
	public RemotingProxyFactoryImpl(ObjectSerializerFactory objectSerializerFactory, ReactiveTypeConverter reactiveTypeConverter) {
		this(objectSerializerFactory, reactiveTypeConverter, new DefaultTraceProvider());
	}

	@AstrixInject
	public RemotingProxyFactoryImpl(
			ObjectSerializerFactory objectSerializerFactory,
			ReactiveTypeConverter reactiveTypeConverter,
			AstrixTraceProvider astrixTraceProvider
	) {
		this.objectSerializerFactory = objectSerializerFactory;
		this.reactiveTypeConverter = reactiveTypeConverter;
		this.astrixTraceProvider = Objects.requireNonNull(astrixTraceProvider);
	}

	@Override
	public <T> T create(ServiceDefinition<T> serviceDefinition, ServiceProperties serviceProperties,
			RemotingTransportSpi remotingTransportSpi, RoutingStrategy routingStrategy) {
		AstrixObjectSerializer objectSerializer = objectSerializerFactory.create(serviceDefinition.getObjectSerializerDefinition());
		RemotingTransport remotingTransport = RemotingTransport.create(remotingTransportSpi);
		return RemotingProxy.create(
				serviceDefinition.getServiceType(),
				ReflectionUtil.classForName(serviceProperties.getProperty(ServiceProperties.API)),
				remotingTransport,
				objectSerializer,
				routingStrategy,
				reactiveTypeConverter,
				astrixTraceProvider
		);
	}

}
