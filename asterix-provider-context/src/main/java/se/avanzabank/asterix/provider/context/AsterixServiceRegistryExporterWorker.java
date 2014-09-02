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
package se.avanzabank.asterix.provider.context;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.openspaces.core.GigaSpace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.avanzabank.asterix.bus.client.AsterixServiceRegistry;
import se.avanzabank.asterix.bus.client.AsterixServiceRegistryApiDescriptor;
import se.avanzabank.asterix.bus.client.AsterixServiceProperties;
import se.avanzabank.asterix.context.AsterixPlugins;
import se.avanzabank.asterix.context.AsterixVersioningPlugin;
import se.avanzabank.asterix.remoting.client.AsterixRemotingProxy;
import se.avanzabank.asterix.remoting.client.AsterixRemotingTransport;
import se.avanzabank.space.SpaceLocator;
/**
 * The service bus worker is a server-side component responsible for continiously publishing 
 * all exported services from the current application onto the service bus.
 * 
 * @author Elias Lindholm (elilin)
 * 
 */
public class AsterixServiceRegistryExporterWorker extends Thread {
	
	private List<ServiceRegistryExporter> serviceExporters = Collections.emptyList();
	private final AsterixServiceRegistry serviceRegistry;
	private final Logger log = LoggerFactory.getLogger(AsterixServiceRegistryExporterWorker.class);

	@Autowired
	public AsterixServiceRegistryExporterWorker(
			SpaceLocator sl, // External dependency
			AsterixPlugins asterixPlugins) { // Plugin dependency
		AsterixVersioningPlugin versioningPlugin = asterixPlugins.getPlugin(AsterixVersioningPlugin.class);
		// TODO: AsterixSerivceBus should be retreived from service-framework, not by hard-coding usage of remoting-framework here.
		GigaSpace serviceRegistrySpace = sl.createClusteredProxy("service-bus-space"); // TODO: fault tolerance, connection mannagment, etc.
		this.serviceRegistry = AsterixRemotingProxy.create(AsterixServiceRegistry.class, AsterixRemotingTransport.remoteSpace(serviceRegistrySpace), versioningPlugin.create(AsterixServiceRegistryApiDescriptor.class));
	}

	public AsterixServiceRegistryExporterWorker(List<ServiceRegistryExporter> serviceProvideres, AsterixServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
		this.serviceExporters = serviceProvideres;
	}

	@Autowired(required = false)
	public void setServiceExporters(List<ServiceRegistryExporter> serviceExporters) {
		this.serviceExporters = serviceExporters;
	}
	

	@PostConstruct
	public void startServiceExporter() {
		if (serviceExporters.isEmpty()) {
			log.info("No ServiceExporters configured. No services will be published on service bus");
		}
		start();
	}

	// TODO: proper logging and management of exporter thread
	// TODO: For pu's: only run on one primary instance (typically the one with id "1")
	@Override
	public void run() {
		while (!interrupted()) {
			exportProvidedServcies();
			try {
				sleep(60_000L);// TODO: intervall of lease renewal
			} catch (InterruptedException e) {
				interrupt();
			} 
		}
	}

	private void exportProvidedServcies() {
		for (ServiceRegistryExporter provider : serviceExporters) {
			for (AsterixServiceProperties serviceProperties : provider.getProvidedServices()) {
				log.debug("Exporting on service bus. service={} properties={}", serviceProperties.getApi().getName(), serviceProperties);
				serviceRegistry.register(serviceProperties.getApi(), serviceProperties);
			}
		}
	}
	

}
