package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import edu.java.scrapper.client.dto.GitHubRepositoryResponse;
import edu.java.scrapper.configuration.ApplicationConfig;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WireMockTest(httpPort = 8080)
public class GitHubClientTest {

    @Disabled
    @Test
    @DisplayName("Проверка клиента GitHub")
    public void getGubClientTest() {
        stubFor(get(urlPathMatching("/repos/owner/repo"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content_Type", "application/json")
                .withBody("{\"id\": 1. \"name\": \"repo\", \"pusher_at\": \"2024-01-26T20:00:00Z\", \"updated_at\": \"2024-01-26T20:00:02Z\"")));

        ApplicationConfig applicationConfigMock = Mockito.mock(ApplicationConfig.class);
        ApplicationConfig.BaseUrls baseUrlsMock = Mockito.mock(ApplicationConfig.BaseUrls.class);

        when(applicationConfigMock.urls()).thenReturn(baseUrlsMock);
        when(baseUrlsMock.gitHubBaseUrl()).thenReturn("http://localhost:8080");

        GitHubClientImpl gitHubClient = new GitHubClientImpl(applicationConfigMock);

        GitHubRepositoryResponse response = gitHubClient.fetchRepository("owner", "repo");

        verify(getRequestedFor(urlEqualTo("/repos/owner/repo")));
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("repo", response.getName());
        assertEquals("\"2024-01-26T20:00:00Z", response.getPushedAt().toString());
        assertEquals("\"2024-01-26T20:00:02Z", response.getUpdatedAt().toString());
    }
}
