package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.client.dto.GitHubCommitResponse;
import edu.java.scrapper.client.dto.GitHubRepositoryResponse;
import edu.java.scrapper.configuration.ApplicationConfig;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@WireMockTest(httpPort = 8080)
public class GitHubClientTest {

    @Test
    public void gitHubClientRepositoryTest() {

        stubFor(get(urlPathMatching("/repos/owner/repo"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(
                    "{\"id\": 1, \"name\": \"repo\", \"pushed_at\": \"2024-01-26T19:16:51Z\", \"updated_at\": \"2024-01-26T19:18:01Z\"}")));

        ApplicationConfig applicationConfigMock = Mockito.mock(ApplicationConfig.class);
        ApplicationConfig.BaseUrls baseUrlsMock = Mockito.mock(ApplicationConfig.BaseUrls.class);
        when(applicationConfigMock.urls()).thenReturn(baseUrlsMock);
        when(baseUrlsMock.gitHubBaseUrl()).thenReturn("http://localhost:8080/");

        GitHubClientImpl gitHubClient = new GitHubClientImpl(applicationConfigMock);

        GitHubRepositoryResponse response = gitHubClient.fetchRepository("owner", "repo");

        verify(getRequestedFor(urlEqualTo("/repos/owner/repo")));
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("repo", response.name());
        assertEquals("2024-01-26T19:16:51Z", response.pushedAt().toString());
        assertEquals("2024-01-26T19:18:01Z", response.updatedAt().toString());
    }

    @Test
    public void gitHubClientCommitTest() {

        stubFor(get(urlPathMatching("/repos/owner/repo/commits"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    [
                      {
                        "sha": "b6bbc5890aab5f926bfeaaf2ca55cf5f94955651",
                        "commit": {
                          "message": "Commit №2"
                          }
                      },
                      {
                        "sha": "dab38bdeec3c19718964cb7525ef79f9cd9c3289",
                        "commit": {
                          "message": "Commit №1"
                        }
                      }]""")));

        ApplicationConfig applicationConfigMock = Mockito.mock(ApplicationConfig.class);
        ApplicationConfig.BaseUrls baseUrlsMock = Mockito.mock(ApplicationConfig.BaseUrls.class);
        when(applicationConfigMock.urls()).thenReturn(baseUrlsMock);
        when(baseUrlsMock.gitHubBaseUrl()).thenReturn("http://localhost:8080/");

        GitHubClientImpl gitHubClient = new GitHubClientImpl(applicationConfigMock);

        List<GitHubCommitResponse> response = gitHubClient.fetchCommit("owner", "repo");

        verify(getRequestedFor(urlEqualTo("/repos/owner/repo/commits")));
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("b6bbc5890aab5f926bfeaaf2ca55cf5f94955651", response.getFirst().sha());
        assertEquals("Commit №2", response.getFirst().commit().message());
        assertEquals("dab38bdeec3c19718964cb7525ef79f9cd9c3289", response.getLast().sha());
        assertEquals("Commit №1", response.getLast().commit().message());
    }

    @Test
    public void gitHubClientNoCommitTest() {

        stubFor(get(urlPathMatching("/repos/owner/repo/commits"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                      "message": "Git Repository is empty.",
                      "documentation_url": "https://docs.github.com/rest/commits/commits#list-commits"
                    }""")));

        ApplicationConfig applicationConfigMock = Mockito.mock(ApplicationConfig.class);
        ApplicationConfig.BaseUrls baseUrlsMock = Mockito.mock(ApplicationConfig.BaseUrls.class);
        when(applicationConfigMock.urls()).thenReturn(baseUrlsMock);
        when(baseUrlsMock.gitHubBaseUrl()).thenReturn("http://localhost:8080/");

        GitHubClientImpl gitHubClient = new GitHubClientImpl(applicationConfigMock);

        List<GitHubCommitResponse> response = gitHubClient.fetchCommit("owner", "repo");

        verify(getRequestedFor(urlEqualTo("/repos/owner/repo/commits")));
        assertNotNull(response);
        assertEquals(0, response.size());
    }

}
