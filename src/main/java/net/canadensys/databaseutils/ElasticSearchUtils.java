package net.canadensys.databaseutils;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ElasticSearchUtils {
	
	/**
	 * Build single-host ElasticSearch client using client.transport.ignore_cluster_name=true
	 * @param host
	 * @param port
	 * @return
	 */
	public static TransportClient buildClient(String host, Integer port){
		Settings settings = ImmutableSettings.settingsBuilder()
		        .put("client.transport.ignore_cluster_name", true).build();

		TransportClient client = new TransportClient(settings)
			.addTransportAddress(new InetSocketTransportAddress(host, port));
		return client;
	}
	
	/**
	 * Build single-host ElasticSearch client using a specific cluster name
	 * @param host
	 * @param port
	 * @param clusterName
	 * @return
	 */
	public static TransportClient buildClient(String host, Integer port, String clusterName){
		Settings settings = ImmutableSettings.settingsBuilder()
		        .put("cluster.name", clusterName).build();
		TransportClient client = new TransportClient(settings)
		.addTransportAddress(new InetSocketTransportAddress(host, port));
		return client;
	}

}
