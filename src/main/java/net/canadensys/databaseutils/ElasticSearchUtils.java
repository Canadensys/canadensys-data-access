package net.canadensys.databaseutils;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ElasticSearchUtils {
	
	/**
	 * Build single-host ElasticSearch client
	 * @param host
	 * @param port
	 * @return
	 */
	public static TransportClient buildClient(String host, Integer port){
		TransportClient client = new TransportClient()
			.addTransportAddress(new InetSocketTransportAddress(host, port));
		return client;
	}

}
