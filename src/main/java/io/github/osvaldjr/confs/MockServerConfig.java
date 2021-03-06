package io.github.osvaldjr.confs;

import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty("dependencies.mockserver.port")
public class MockServerConfig {

  @Value("${dependencies.mockserver.port:}")
  Integer mockServerPort;

  @Value("${dependencies.mockserver.host:localhost}")
  String mockServerHost;

  @Bean
  public MockServerClient mockServerClient() {
    return new MockServerClient(mockServerHost, mockServerPort);
  }
}
