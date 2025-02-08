/* Licensed under Apache-2.0 2024. */
package github.benslabbert.vertxdageruilib.component;

import github.benslabbert.vertxdaggercommons.config.Config;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentVerticle extends AbstractVerticle {

  private static final Logger log = LoggerFactory.getLogger(ComponentVerticle.class);
  private HttpServer server = null;

  @Override
  public void start(Promise<Void> startPromise) {
    JsonObject cfg = config();
    Config config = Config.fromJson(cfg);
    Objects.requireNonNull(config);
    Objects.requireNonNull(config.httpConfig());

    Router router = Router.router(vertx);
    router.route("/*").handler(ctx -> StaticHandler.create("svelte").handle(ctx));

    server =
        vertx
            .createHttpServer(
                new HttpServerOptions().setPort(config.httpConfig().port()).setHost("0.0.0.0"))
            .requestHandler(router)
            .listen(
                res -> {
                  if (res.succeeded()) {
                    log.info("started http server");
                    startPromise.complete();
                  } else {
                    log.error("failed to start verticle", res.cause());
                    startPromise.fail(res.cause());
                  }
                });
  }

  public int getPort() {
    return server.actualPort();
  }

  @Override
  public void stop(Promise<Void> stopPromise) {
    server.close(stopPromise);
  }
}
