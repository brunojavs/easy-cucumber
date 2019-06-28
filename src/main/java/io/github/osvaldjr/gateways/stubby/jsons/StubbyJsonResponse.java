package io.github.osvaldjr.gateways.stubby.jsons;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StubbyJsonResponse implements Serializable {

  private static final long serialVersionUID = -8674647371416791444L;
  private Integer hits;
  private Integer id;
}
