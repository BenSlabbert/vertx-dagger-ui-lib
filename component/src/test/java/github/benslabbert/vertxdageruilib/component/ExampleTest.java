/* Licensed under Apache-2.0 2024. */
package github.benslabbert.vertxdageruilib.component;

import static github.benslabbert.vertxdaggercommons.FreePortUtility.getPort;
import static org.assertj.core.api.Assertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import github.benslabbert.vertxdaggercommons.ConfigEncoder;
import github.benslabbert.vertxdaggercommons.config.Config;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class ExampleTest {

  private static Playwright playwright;
  private static Browser browser;
  private Config config;

  @BeforeAll
  static void launchBrowser() {
    playwright = Playwright.create();
    browser = playwright.chromium().launch();
  }

  @AfterAll
  static void closeBrowser() {
    playwright.close();
  }

  // New instance for each test method.
  BrowserContext context;
  Page page;

  @BeforeEach
  void createContextAndPage(Vertx vertx, VertxTestContext testContext) {
    context = browser.newContext();
    page = context.newPage();
    config =
        Config.builder()
            .profile(Config.Profile.PROD)
            .httpConfig(Config.HttpConfig.builder().port(getPort()).build())
            .build();
    JsonObject cfg = ConfigEncoder.encode(config);
    vertx.deployVerticle(
        new ComponentVerticle(),
        new DeploymentOptions().setConfig(cfg),
        testContext.succeedingThenComplete());
  }

  @AfterEach
  void closeContext() {
    context.close();
  }

  @Test
  void test() {
    page.navigate("http://127.0.0.1:" + config.httpConfig().port());
    page.waitForCondition(page.querySelector("div")::isVisible);

    String footer = page.getByTestId("footer").first().innerText();
    assertThat(footer).isEqualTo("this is the footer");
  }
}
