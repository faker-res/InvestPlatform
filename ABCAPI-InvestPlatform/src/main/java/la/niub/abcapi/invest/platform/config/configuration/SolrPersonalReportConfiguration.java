//package la.niub.abcapi.invest.platform.config.configuration;
//
//import org.apache.solr.client.solrj.SolrClient;
//import org.apache.solr.client.solrj.impl.CloudSolrClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//public class SolrPersonalReportConfiguration {
//
//    @Value("${spring.data.solr.personalReport.zkHost}")
//    private String zkHost;
//
//    @Value("${spring.data.solr.personalReport.zkChroot}")
//    private String zkChroot;
//
//    @Bean
//    @Primary
//    public SolrClient encoder(){
//        List<String> zkHosts = Arrays.asList(zkHost.split(","));
//        return new CloudSolrClient(zkHosts,zkChroot);
//    }
//}
