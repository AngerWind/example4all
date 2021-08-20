package com.tiger.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import java.util.Properties;

public class Producer {

    static Logger log = Logger.getLogger(Producer.class);

    private static final String TOPIC = "etl.kudu_analysis.loan.loan.1";
    private static final String BROKER_LIST = "10.2.0.211:9092,10.2.0.212:9092,10.2.0.214:9092";
    private static KafkaProducer<String, String> producer = null;

    /*
    初始化生产者
     */
    static {
        Properties configs = initConfig();
        producer = new KafkaProducer<String, String>(configs);
    }

    /*
    初始化配置
     */
    private static Properties initConfig() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    public static void main(String[] args) {
        //消息实体
        ProducerRecord<String, String> record = null;

            String message = "{\"before\":null,\"data\":{\"app_version\":null,\"industry_code\":null," +
                    "\"primary_column_md5\":\"0d6149c474b33a4128ec3e47ca43ec5b\",\"merchant_partner_code\":null," +
                    "\"work_start_date\":null,\"graduated_at\":null,\"push_type\":null," +
                    "\"resident_address_province_code\":null,\"applied_tenor\":\"\",\"x_origin\":1550748907279466846," +
                    "\"applied_amount\":\"10000\",\"school\":null,\"record_insert_time\":1572434785000,\"id\":20906," +
                    "\"merchant_no\":null,\"record_update_time\":1572438800000,\"device_id\":null," +
                    "\"merchant_bank_card\":null,\"company_telephone\":null,\"approval_type\":\"normal\",\"ip\":null," +
                    "\"biz_type\":\"\",\"degree\":null,\"apply_time\":1564407793000," +
                    "\"resident_address_city_code\":null,\"invitation_number\":\"0\"," +
                    "\"name\":\"AjxyABYNdljDKNo4MPFpJQ==\",\"x_loan_id\":-4729268342846945802,\"apply_type\":\"\"," +
                    "\"position_id\":\"1000\",\"device_model\":null,\"origin\":\"WLY_STD_FRE\"," +
                    "\"user_register_time\":null,\"merchant_phone\":null,\"platform\":\"H5\",\"mac\":null," +
                    "\"income_month\":10000,\"marriage\":\"\",\"merchant_bank_card_account\":null," +
                    "\"loan_id\":\"test-071304\",\"product\":\"WLY_STD_FRE\",\"enrolled_at\":null," +
                    "\"ums_update_time_\":1621580064176,\"family_city_code\":null," +
                    "\"cnid\":\"51142319940415-26pGbx5z/HYjj5ThZGLEuA==-9-NRvpdGZxWBNaYZUZapCn7A==\"," +
                    "\"log_time\":1564407794000,\"resident_address_district_code\":null,\"work_unit\":null," +
                    "\"account\":\"1568479-WowY9qzZGFdmTQYHCUhPkA==\",\"contacts\":null},\"isMergeTable\":false," +
                    "\"isMongoArray\":false,\"isPrimaryTable\":false,\"isSnapshot\":true,\"isSnapshotFinish\":false," +
                    "\"op\":\"INSERT\",\"primaryKey\":\"loan_id\",\"sourceDbGlobalUniqStr\":\"x_anti_fraud\"," +
                    "\"sourceDbName\":\"x_anti_fraud\",\"sourceTableName\":\"loan\",\"tableType\":\"DIMENSION\"," +
                    "\"targetDbGlobalUniqStr\":\"kudu_analysis\",\"targetDbName\":\"kudu_analysis\"," +
                    "\"targetTableName\":\"loan\",\"umsId\":-1,\"umsUpdateTime\":1621580064176}";
            record = new ProducerRecord<String, String>(TOPIC, String.valueOf(1), message);
            producer.send(record);
            System.out.println(message);
            // 发送消息并异步接收消息确认
//            producer.send(record, new Callback() {
//                @Override public void onCompletion(RecordMetadata recordMetadata, Exception e) {
//                    if (null != e) {
//                        log.info("send error" + e.getMessage());
//                    } else {
//                        System.out.println(String
//                            .format("offset:%s,partition:%s", recordMetadata.offset(), recordMetadata.partition()));
//                    }
//                }
//            });

        producer.close();
    }
}
