package io.github.osvaldjr.stepdefinitions;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.github.osvaldjr.utils.CreateStubby;
import io.github.osvaldjr.utils.GetMockHits;
import io.github.osvaldjr.utils.Mock;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class MockSteps extends Steps {

  @Autowired(required = false)
  private CreateStubby createStubbyUsecase;

  @Autowired(required = false)
  private GetMockHits getMockHitsUsecase;

  @Autowired(required = false)
  private Mock mock;

  private String scenarioName;
  private Map<String, Object> stubbyIdMap;

  @Before
  public void before(Scenario scenario) {
    scenarioName = FilenameUtils.getBaseName(scenario.getUri());
  }

  @Before("@CleanStubby")
  public void cleanupStubby() {
    mock.deleteAllServices();
  }

  @Then("I have a mock ([^\"]*) for dependency ([^\"]*)")
  public void aHaveAMockForDependency(String mockName, String serviceName) throws IOException {
    Object stubbyId = createStubbyUsecase.execute(scenarioName, serviceName, mockName);
    stubbyIdMap.put(getStubbyKey(scenarioName, serviceName, mockName), stubbyId);
  }

  @Then("I expect mock ([^\"]*) for dependency ([^\"]*) to have been called (\\d+) times")
  public void iExpectMockForDependencyToHaveBeenCalledTimes(
      String mockName, String serviceName, Integer times) {
    String mapKey = getStubbyKey(scenarioName, serviceName, mockName);
    Object stubbyId = stubbyIdMap.get(mapKey);

    assertThat(getMockHitsUsecase.execute(stubbyId), equalTo(times));
  }

  @Given("I clear all mocks")
  public void iClearAllMocks() {
    mock.deleteAllServices();
  }

  private String getStubbyKey(String scenario, String serviceName, String mockName) {
    return scenario + serviceName + mockName;
  }
}