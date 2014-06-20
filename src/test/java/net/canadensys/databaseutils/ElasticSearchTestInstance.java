package net.canadensys.databaseutils;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;

/**
 * Helper class to manage local(embedded) instance of ElasticSearch for testing purpose.
 * @author canadensys
 *
 */
public class ElasticSearchTestInstance {

	//the actual node
    private Node node;
    
	public void startElasticSearch(){
        node = nodeBuilder().clusterName("testCluster").local(true).node();
        node.start();
        node.client().admin().cluster().prepareHealth().setWaitForGreenStatus().execute().actionGet();
    }

    public void stopElasticSearch(){
    	if(node !=null){
    		node.close();
    	}
    }
    
    /**
     * Build a client to connect to a running local(embedded) ElacticSearch node.
     * @return
     */
	public Client buildLocalClient(){
		return node.client();
	}
}
