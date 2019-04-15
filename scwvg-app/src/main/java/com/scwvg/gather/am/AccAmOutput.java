package com.scwvg.gather.am;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scwvg.gather.config.Global;
import com.scwvg.gather.util.BucketPath;
import com.scwvg.gather.util.Output;

public class AccAmOutput extends Output {
	private static final Logger logger = LoggerFactory.getLogger(AccAmOutput.class);
	
	private boolean isExit = false;
	private boolean bShould = false;
	
	
	@Override
	public void run() {
		
		BulkRequestBuilder bulkRequest = Global.esClientUtil.getBulkRequestBuilder();
		
		Map<String, Object> map = null;
		int num = 0;
		long time = System.currentTimeMillis();
		String index = Global.context.getString("es.index");
		
		Map<String, String> headers = new HashMap<>();
		headers.put("timestamp", String.valueOf(System.currentTimeMillis() - Global.context.getLong("delay", 0L)));
		
		index = BucketPath.escapeString(index, headers);
		
		Integer batchSize = Global.context.getInteger("es.batch.size", 5000);
		
		while(!isExit || !Global.queue.isEmpty()) {
			String message = Global.queue.poll();
			if(message != null) {
				String[] split = message.substring(message.indexOf("<AlarmStart>") + 12, message.indexOf("<AlarmEnd>")).split("\\|\\^\\|");
				
				map = new HashMap<>();
				for (String string : split) {
					if(string.length() <= 0 ) {
						continue;
					}
					int indexOf = string.indexOf(":");
					String key = string.substring(0, indexOf);
					String value = string.substring(indexOf + 1);
					if(StringUtils.isBlank(value)) {
						map.put(key, null);
					} else {
						map.put(key, value);
					}
				}
				
				if(!map.isEmpty()) {
					bulkRequest.add(Global.esClientUtil.getIndexRequestBuilder(index, index, map));
					num++;
				}
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) { e.printStackTrace(); }
			}
			
			if(num > 0 && (bShould || num >= batchSize)) {
				BulkResponse response = bulkRequest.execute().actionGet();
				if (response.hasFailures()) {
					// �������
					logger.info(response.buildFailureMessage());
					logger.info("��������es����");
				} else {
					logger.info(String.format("��es:%s�������,��ʱ:%s����", bulkRequest.numberOfActions(), System.currentTimeMillis() - time));
				}
				bulkRequest = Global.esClientUtil.getBulkRequestBuilder();
				time = System.currentTimeMillis();
				num = 0;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) { e.printStackTrace(); }
			}
		}
		
		if(num > 0) {
			BulkResponse response = bulkRequest.execute().actionGet();
			if (response.hasFailures()) {
				// �������
				logger.info(response.buildFailureMessage());
				logger.info("��������es����");
			} else {
				logger.info(String.format("��es:%s�������,��ʱ:%s����", bulkRequest.numberOfActions(), System.currentTimeMillis() - time));
			}
		}
		
		logger.info("slastic run completed!");
	}
	
	public void scheduer() {
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				bShould = true;
			}
			
		}, Global.context.getLong("es.interval", 10000L), Global.context.getLong("es.interval", 10000L));
	}
	
	/**
	 * �˳��߳�
	 */
	@Override
	public void exit() {
		this.isExit = true;
	}
	
	public static void main(String[] args) {
		String s = "��ʼ2019-04-13 23:59:59[324192686-170.129.240.26/59092|136.192.50.33, 1.3.6.1.2.1.1.3.0=109 days, 5:39:15.26, 1.3.6.1.6.3.1.1.4.1.0=1.3.6.1.4.1.2011.2.115, 1.3.6.1.4.1.2011.2.115=0, 1.3.6.1.4.1.2011.2.115=3, 1.3.6.1.4.1.2011.2.115=2, 1.3.6.1.4.1.2011.2.115=170.129.240.26, 1.3.6.1.4.1.2011.2.115=772907019, 1.3.6.1.4.1.2011.2.115=877797, 1.3.6.1.4.1.2011.2.115=771751969, 1.3.6.1.4.1.2011.2.115=27, 1.3.6.1.4.1.2011.2.115=2019-04-13 16:00:28Z, 1.3.6.1.4.1.2011.2.115=0, 1.3.6.1.4.1.2011.2.115=12, 1.3.6.1.4.1.2011.2.115=4, 1.3.6.1.4.1.2011.2.115=37, 1.3.6.1.4.1.2011.2.115=ZXHN F650, 1.3.6.1.4.1.2011.2.115=ONT_NO_DESCRIPTION?alarm-policy_0, , moInfo=snmp_check=1|totalid=2086948|jf_name=�ϸ����ƶ��������������ú����������߻���|zd_name=�ƶ���������������|remark=1|city_name=������|vendor_name=��Ϊ|device_name=������_�ϸ�_���˴���_OLT_1_��Ϊ_MA5680T|spec_name=ACC|devicetype_name=OLT|devicemodel_name=MA5680T|branch_name=|orgin_ip=170.129.240.26|oss_dev_name=������_�ϸ�_���˴���_OLT_1_��Ϊ_MA5680T|olt_adapter=|reverse_olt_adapter=|devcemodel_name=MA5680T|device_id=1701, oidInfo=state=0|row=5|event_name=ONU��Դ����|perceivedseverity=1|event_adapter=POSITION=RACK:1,SHELF:$GetByLine(10),SLOT:$GetByLine(11),PORT:$GetByLine(12),ONUNUM:$GetByLine(13);�澯����=GPON ONT����(DGi);Ӳ���汾=$GetByLine(14);ONT������Ϣ(�����ܿɼ�)=$Substr(\"15\",\"start\",\"?\"), MAC=$Substr(\"15\",\"?2\",\"?4\"), LOID=$Substr(\"15\",\"?4\",\"?end\"), ONUTYPE=$GetByLine(14), SERVICELEVEL=$Substr(\"15\",\"?\",\"?2\"), ONUIP=$Substr(\"15\",\"?1\",\"?2\"),ONUNAME=$OnuNameByIp(gatherID,\"15\",\"?1\",\"?2\"),|event_oid=772907019|recovery_oid=772972555]<AlarmStart>|^|BRANCH_NAME:|^|ORGIN_IP:170.129.240.26|^|CLEARANCETIMESTAMP:|^|OSS_DEV_NAME:������_�ϸ�_���˴���_OLT_1_��Ϊ_MA5680T|^|SEND_TIME:2019-04-13 23:59:59|^|ONLY_ID:324192686|^|ALARMTEXT:devcemodel_name#MA5680T|^|EQUIPMENTNAME:������_�ϸ�_���˴���_OLT_1_��Ϊ_MA5680T|^|ALARMORIGIN:136.192.50.33|^|ADDITIONALTEXT:POSITION=RACK:1,SHELF:0,SLOT:12,PORT:4,ONUNUM:37;�澯����=GPON ONT����(DGi);Ӳ���汾=ZXHN F650;ONT������Ϣ(�����ܿɼ�)=ONT_NO_DESCRIPTION, MAC=, LOID=, ONUTYPE=ZXHN F650, SERVICELEVEL=alarm-policy_0, ONUIP=,ONUNAME=,|^|PERCEIVEDSEVERITY:1|^|EVENTTIME:2019-04-13 23:59:59|^|SPECIALTY:ACC|^|NECLASS:OLT|^|CLEARANCEREPORTFLAG:0|^|ALARMNAME:ONU��Դ����|^|LOCATIONINFO:��Ϊ|^|TOTALID:2086948|^|JF_NAME:�ϸ����ƶ��������������ú����������߻���|^|ZD_NAME:�ƶ���������������|^|SERIAL:1701/772907019(772972555)/1555171199|^|REGION:������|^|IPADDRESS:170.129.240.26|^|REPAIRACTIONADVICE:|^|EQUIPMENTNAME_ALIAS:������_�ϸ�_���˴���_OLT_1_��Ϊ_MA5680T|^|RENOVATE_FLAG:0|^|<AlarmEnd>����";
		String[] split = s.substring(s.indexOf("<AlarmStart>") + 12, s.indexOf("<AlarmEnd>")).split("\\|\\^\\|");
		for (String string : split) {
			if(string.length() <= 0 ) {
				continue;
			}
			int indexOf = string.indexOf(":");
			String key = string.substring(0, indexOf);
			String value = string.substring(indexOf + 1);
			System.out.println(String.format("%s\t%s", key, value));
		}
		
	}

}
