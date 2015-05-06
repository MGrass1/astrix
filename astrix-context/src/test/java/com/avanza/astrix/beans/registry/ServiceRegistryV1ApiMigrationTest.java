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
package com.avanza.astrix.beans.registry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.avanza.astrix.beans.service.AstrixServiceProperties;
import com.avanza.astrix.context.AstrixContextImpl;
import com.avanza.astrix.context.AstrixVersioningPlugin;
import com.avanza.astrix.context.TestAstrixConfigurer;
import com.avanza.astrix.core.AstrixObjectSerializer;
import com.avanza.astrix.provider.versioning.ServiceVersioningContext;


public class ServiceRegistryV1ApiMigrationTest {
	
	@Test
	public void generatesApplicationInstanceIdFromQualifierAndApiTypeFor_AstrixServiceRegistryEntry_onVersion1() throws Exception {
		AstrixContextImpl context = (AstrixContextImpl) new TestAstrixConfigurer().enableVersioning(true).configure();
		AstrixVersioningPlugin versioningPlugin = context.getPlugin(AstrixVersioningPlugin.class);
		AstrixObjectSerializer objectSerializer = versioningPlugin.create(ServiceVersioningContext.versionedService(2, ServiceRegistryObjectSerializerConfigurer.class));

		
		AstrixServiceRegistryEntry v1Entry = new AstrixServiceRegistryEntry();
		v1Entry.getServiceProperties().put(AstrixServiceProperties.QUALIFIER, "my-qualifier");
		v1Entry.getServiceProperties().put(AstrixServiceProperties.API, "foo.MyService");
		
		Object serializedV1 = objectSerializer.serialize(v1Entry, 1); //
		
		AstrixServiceRegistryEntry deserializedToV2 = objectSerializer.deserialize(serializedV1, AstrixServiceRegistryEntry.class, 1);
		assertEquals("foo.MyService_my-qualifier", deserializedToV2.getServiceProperties().get(AstrixServiceProperties.APPLICATION_INSTANCE_ID));
		
		AstrixServiceRegistryEntry v2Entry = new AstrixServiceRegistryEntry();
		v2Entry.getServiceProperties().put(AstrixServiceProperties.QUALIFIER, "my-qualifier");
		v2Entry.getServiceProperties().put(AstrixServiceProperties.API, "foo.MyService");
		v2Entry.getServiceProperties().put(AstrixServiceProperties.APPLICATION_INSTANCE_ID, "my-instance-id");

		Object serializedV2 = objectSerializer.serialize(v2Entry, 2); //
		
		deserializedToV2 = objectSerializer.deserialize(serializedV2, AstrixServiceRegistryEntry.class, 2);
		assertEquals("my-instance-id", deserializedToV2.getServiceProperties().get(AstrixServiceProperties.APPLICATION_INSTANCE_ID));
	}
	
}