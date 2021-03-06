package io.github.osvaldjr.unit.usecases;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import io.github.glytching.junit.extension.random.Random;
import io.github.osvaldjr.domains.StubbyRequest;
import io.github.osvaldjr.gateways.FileGateway;
import io.github.osvaldjr.gateways.mock.MockGateway;
import io.github.osvaldjr.unit.UnitTest;
import io.github.osvaldjr.usecases.CreateStubbyUseCase;

class CreateStubbyUseCaseTest extends UnitTest {

  @Mock private FileGateway fileGateway;
  @Mock private MockGateway mockGateway;
  @InjectMocks private CreateStubbyUseCase createStubbyUsecase;
  @Captor private ArgumentCaptor<StubbyRequest.RequestBody> stubbyRequestBodyArgumentCaptor;
  @Captor private ArgumentCaptor<StubbyRequest.ResponseBody> stubbyResponseBodyArgumentCaptor;

  @Test
  void shouldExecute(
      @Random String scenario,
      @Random String serviceName,
      @Random String mockName,
      @Random StubbyRequest.RequestBody stubbyRequestBody,
      @Random StubbyRequest.ResponseBody stubbyResponseBody,
      @Random String id)
      throws IOException {
    String mockRequestFile = "mocks/" + mockName + "-request.json";
    String mockResponseFile = "mocks/" + mockName + "-response.json";
    String url = stubbyRequestBody.getUrl();
    when(fileGateway.getObjectFromFile(scenario, mockRequestFile, StubbyRequest.RequestBody.class))
        .thenReturn(stubbyRequestBody);
    when(fileGateway.getObjectFromFile(
            scenario, mockResponseFile, StubbyRequest.ResponseBody.class))
        .thenReturn(stubbyResponseBody);
    when(mockGateway.createStubbyRequest(
            stubbyRequestBodyArgumentCaptor.capture(), stubbyResponseBodyArgumentCaptor.capture()))
        .thenReturn(id);

    Object stubbyId = createStubbyUsecase.execute(scenario, serviceName, mockName);

    assertThat(stubbyId, equalTo(id));
    StubbyRequest.RequestBody requestBody = stubbyRequestBodyArgumentCaptor.getValue();
    assertThat(requestBody.getUrl(), equalTo(serviceName + url));
    assertThat(requestBody.getHeaders(), equalTo(stubbyRequestBody.getHeaders()));
    assertThat(requestBody.getMethod(), equalTo(stubbyRequestBody.getMethod()));
    assertThat(requestBody.getQueryParams(), equalTo(stubbyRequestBody.getQueryParams()));
    assertThat(requestBody.getBody(), equalTo(stubbyRequestBody.getBody()));
    StubbyRequest.ResponseBody responseBody = stubbyResponseBodyArgumentCaptor.getValue();
    assertThat(responseBody, equalTo(stubbyResponseBody));
    verify(fileGateway, times(2)).getObjectFromFile(anyString(), anyString(), any());
    verify(mockGateway, times(1))
        .createStubbyRequest(
            any(StubbyRequest.RequestBody.class), any(StubbyRequest.ResponseBody.class));
  }

  @Test
  void shouldInvalidUrlRequest(
      @Random String scenario,
      @Random String serviceName,
      @Random String mockName,
      @Random StubbyRequest.RequestBody stubbyRequestBody,
      @Random StubbyRequest.ResponseBody stubbyResponseBody)
      throws IOException {
    stubbyRequestBody.setUrl(null);

    String mockRequestFile = "mocks/" + mockName + "-request.json";
    String mockResponseFile = "mocks/" + mockName + "-response.json";
    when(fileGateway.getObjectFromFile(scenario, mockRequestFile, StubbyRequest.RequestBody.class))
        .thenReturn(stubbyRequestBody);
    when(fileGateway.getObjectFromFile(
            scenario, mockResponseFile, StubbyRequest.ResponseBody.class))
        .thenReturn(stubbyResponseBody);

    AssertionError throwable =
        assertThrows(
            AssertionError.class,
            () -> createStubbyUsecase.execute(scenario, serviceName, mockName));

    assertThat(throwable.getMessage(), equalTo("url cannot be null in create mock"));
  }

  @Test
  void shouldInvalidMethodRequest(
      @Random String scenario,
      @Random String serviceName,
      @Random String mockName,
      @Random StubbyRequest.RequestBody stubbyRequestBody,
      @Random StubbyRequest.ResponseBody stubbyResponseBody)
      throws IOException {
    stubbyRequestBody.setMethod(null);

    String mockRequestFile = "mocks/" + mockName + "-request.json";
    String mockResponseFile = "mocks/" + mockName + "-response.json";
    when(fileGateway.getObjectFromFile(scenario, mockRequestFile, StubbyRequest.RequestBody.class))
        .thenReturn(stubbyRequestBody);
    when(fileGateway.getObjectFromFile(
            scenario, mockResponseFile, StubbyRequest.ResponseBody.class))
        .thenReturn(stubbyResponseBody);

    AssertionError throwable =
        assertThrows(
            AssertionError.class,
            () -> createStubbyUsecase.execute(scenario, serviceName, mockName));

    assertThat(throwable.getMessage(), equalTo("method cannot be null in create mock"));
  }
}
