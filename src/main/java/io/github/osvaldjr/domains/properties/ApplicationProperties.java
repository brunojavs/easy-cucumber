package io.github.osvaldjr.domains.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties
public class ApplicationProperties {

  private TargetProperties target;

  private DependencyProperties dependencies = new DependencyProperties();
}
