package com.scwvg.gather.util;

import java.net.InetAddress;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * es�ͻ��˹�����
 * @author tangyonglin
 *
 */
public class ESClientUtil {
	
	private final static Logger log = LoggerFactory.getLogger(ESClientUtil.class);
	
	/**����*/
	private String hosts;
	/**es��Ⱥ����*/
	private String clusterName;
	/**�Ƿ�������̽*/
	private boolean sniff = false;
	/**�Ƿ�����*/
	private boolean connectFlag = false;
	
	/**es�ͻ���*/
	private TransportClient client;
	
	public static void main(String[] args) {
		Settings settings = Settings.builder()
				.put("cluster.name", "aaa")
				.put("client.transport.sniff", false).build();
		System.out.println(settings);
	}
	
	/**
	 * ��ʼ��es�ͻ���
	 * @throws Exception
	 */
	public void init() throws Exception {
		Settings settings = Settings.builder()
				.put("cluster.name", clusterName)
				.put("client.transport.sniff", this.sniff)
				.put("client.transport.ping_timeout", "300s").build();
		if (!StringUtils.isEmpty(hosts)) {
			String[] ipAarry = hosts.split(",");
			client = new PreBuiltTransportClient(settings);
			for (String str : ipAarry) {
				if (str.contains(":")) {
					TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(str.substring(0, str.indexOf(":"))), Integer.parseInt(str.substring(str.indexOf(":") + 1)));
					client.addTransportAddress(transportAddress);
				}
			}
			connectFlag = true;
			log.info(String.format("%s %s", hosts, "es��ʼ�����"));

		} else {
			log.info("es��������Ϊ��!");
		}
	}
	
	/**
	 * ����Index
	 * @param index
	 * @param type
	 * @param settings
	 * @param json
	 * @return
	 */
	public boolean createIndex(String index, Settings settings, Map<String, String> mapping) {
		if(!connectFlag) {
			throw new RuntimeException("esδ��ʼ����");
		}
		
		IndicesAdminClient indices = this.client.admin().indices();
		//�ж�index�Ƿ����
		if(!exists(new String[]{index})) {//index������, ����index������mapping
			CreateIndexRequestBuilder requestBuilder = indices.prepareCreate(index).setSettings(settings);
			for(Map.Entry<String, String> entry : mapping.entrySet()) {
				
				String _type = entry.getKey();
				String _value = entry.getValue();
				
				JSONObject json = new JSONObject();
				json.put(_type, _value);
				
				requestBuilder.addMapping(_type, json.toString());
			}
			
			return requestBuilder.execute().actionGet().isAcknowledged();
		} 
		log.info("Index: {}�Ѵ��ڣ�", index);
		return false;
	}
	
	/**
	 * ����mapping
	 * @param index
	 * @param type
	 * @param mapping
	 * @return
	 */
	public boolean createMapping(String index, String type, String mapping) {
		if(!connectFlag) {
			throw new RuntimeException("esδ��ʼ����");
		}
		IndicesAdminClient indices = this.client.admin().indices();
		PutMappingRequest mappingRequest = Requests.putMappingRequest(index);
		JSONObject json = new JSONObject();
		json.put(type, mapping);
		mappingRequest.type(type).source(json.toString());
		return indices.putMapping(mappingRequest).actionGet().isAcknowledged();
	}
	
	/**
	 * ɾ������
	 * @param index
	 * @return
	 */
	public boolean delIndex(String index) {
		IndicesAdminClient indices = this.client.admin().indices();
		return indices.prepareDelete(index).execute().actionGet().isAcknowledged();
	}
	
	/**
	 * index�Ƿ����
	 * @param indexs
	 * @return
	 */
	public boolean exists(String[] indexs) {
		if(!connectFlag) {
			throw new RuntimeException("esδ��ʼ����");
		}
		return this.client.admin().indices().prepareExists(indexs).execute().actionGet().isExists();
	}
	
	/**
	 * �ж�ָ���������������Ƿ����
	 * @param indexName ������
	 * @param indexType ��������
	 * @return  ���ڣ�true; �����ڣ�false;
	 */
	public boolean exists(String indexName,String indexType) {
		if(!connectFlag) {
			throw new RuntimeException("esδ��ʼ����");
		}
		IndicesAdminClient indices = this.client.admin().indices();
		TypesExistsResponse  response = indices.typesExists(new TypesExistsRequest(new String[]{indexName}, indexType)).actionGet();
	    return response.isExists();
	}
	
	/**
	 * ��ȡBulk
	 * @return
	 */
	public BulkRequestBuilder getBulkRequestBuilder() {
		return this.client.prepareBulk();
	}
	
	/**
	 * ����IndexRequestBuilder����
	 * @param index	����
	 * @param type	mapping
	 * @param id	����
	 * @param json	����
	 * @return
	 */
	public IndexRequestBuilder getIndexRequestBuilder(String index, String type, String id, String json) {
		return getIndexRequestBuilder(index, type, id, json, null);
	}
	
	/**
	 * ����IndexRequestBuilder����
	 * @param index	����
	 * @param type	mapping
	 * @param json	����
	 * @return
	 */
	public IndexRequestBuilder getIndexRequestBuilder(String index, String type, String json) {
		return getIndexRequestBuilder(index, type, null, json, null);
	}
	
	/**
	 * ����IndexRequestBuilder����
	 * @param index	����
	 * @param type	mapping
	 * @param id	����
	 * @param map	����
	 * @return
	 */
	public IndexRequestBuilder getIndexRequestBuilder(String index, String type, String id, Map<String, Object> map) {
		return getIndexRequestBuilder(index, type, id, map, null);
	}
	
	/**
	 * ����IndexRequestBuilder����
	 * @param index	����
	 * @param type	mapping
	 * @param map	����
	 * @return
	 */
	public IndexRequestBuilder getIndexRequestBuilder(String index, String type, Map<String, Object> map) {
		return getIndexRequestBuilder(index, type, null, map, null);
	}
	
	/**
	 * ����IndexRequestBuilder����
	 * @param index	����
	 * @param type	mapping
	 * @param map	����
	 * @param routings	·��
	 * @return
	 */
	public IndexRequestBuilder getIndexRequestBuilder(String index, String type, Map<String, Object> map, String[] routings) {
		return getIndexRequestBuilder(index, type, null, map, routings);
	}
	
	/**
	 * ����IndexRequestBuilder����
	 * @param index	����
	 * @param type	mapping
	 * @param id	����
	 * @param json	����
	 * @param routings	·��
	 * @return
	 */
	public IndexRequestBuilder getIndexRequestBuilder(String index, String type, String id, String json, String[] routings) {
		
		if(StringUtils.isEmpty(id)) {//���IDΪ�գ�ʹ��uuid
			id = UUID.randomUUID().toString();
		}
		
		IndexRequestBuilder builder = client.prepareIndex(index, type, id);
		
		if(routings!=null && routings.length > 0) {
			for(String routing : routings) {//ָ��·��
				builder.setRouting(routing);
			}
		}
		
		builder.setSource(json);
		return builder;
	}
	
	/**
	 * ����IndexRequestBuilder����
	 * @param index	����
	 * @param type	mapping
	 * @param id	����
	 * @param map	����
	 * @param routings	·��
	 * @return
	 */
	public IndexRequestBuilder getIndexRequestBuilder(String index, String type, String id, Map<String, Object> map, String[] routings) {
		
		if(StringUtils.isEmpty(id)) {//���IDΪ�գ�ʹ��uuid
			id = UUID.randomUUID().toString();
		}
		
		IndexRequestBuilder builder = client.prepareIndex(index, type, id);
		
		if(routings!=null && routings.length > 0) {
			for(String routing : routings) {//ָ��·��
				builder.setRouting(routing);
			}
		}
		
		builder.setSource(map);
		return builder;
	}
	
	public void setHosts(String hosts) {
		this.hosts = hosts;
	}
	
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	
	public void setSniff(boolean sniff) {
		this.sniff = sniff;
	}

}
