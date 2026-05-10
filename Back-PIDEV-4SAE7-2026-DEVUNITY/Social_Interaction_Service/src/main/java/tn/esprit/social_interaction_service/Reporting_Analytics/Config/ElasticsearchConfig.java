package tn.esprit.social_interaction_service.Reporting_Analytics.Config;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

import javax.net.ssl.SSLContext;

//@Configuration
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris}")
    private String elasticsearchUri;

    @Value("${spring.elasticsearch.username}")
    private String username;

    @Value("${spring.elasticsearch.password}")
    private String password;

    @Override
    public ClientConfiguration clientConfiguration() {
        try {
            SSLContext sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial((chain, authType) -> true)
                    .build();

            // Extract host and port from URI (e.g., localhost:9200)
            String hostAndPort = elasticsearchUri.replace("https://", "").replace("http://", "");

            return ClientConfiguration.builder()
                    .connectedTo(hostAndPort)
                    .usingSsl(sslContext, NoopHostnameVerifier.INSTANCE)
                    .withBasicAuth(username, password)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to configure Elasticsearch client SSL", e);
        }
    }
}
