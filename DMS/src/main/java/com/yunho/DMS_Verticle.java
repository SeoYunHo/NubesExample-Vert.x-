package com.yunho;

import com.github.aesteve.vertx.nubes.VertxNubes;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;

public class DMS_Verticle extends AbstractVerticle {
	private HttpServer server;
	protected HttpServerOptions options;
	protected VertxNubes nubes;

	@Override
	public void init(Vertx vertx, Context context) {
		// TODO Auto-generated method stub
		JsonObject config = context.config();
		JsonArray arr = new JsonArray();
		arr.add("com.yunho.controller");
		config.put("controller-packages", arr);
		options = new HttpServerOptions();
		options.setHost(config.getString("host", "localhost"));
		options.setPort(config.getInteger("port", 8181));
		nubes = new VertxNubes(vertx, config);

		HttpServerOptions options = new HttpServerOptions();
		server = vertx.createHttpServer(options);
		System.out.println("this is init");
	}

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		nubes.bootstrap(new Handler<AsyncResult<Router>>() {
			@Override
			public void handle(AsyncResult<Router> event) {
				if (event.succeeded()) {
					Router router = event.result();
					router.route().handler(CookieHandler.create());
					router.route().handler(RequestSecurePreprocessor.create());
					// router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)).setNagHttps(false));
					router.route().handler(BodyHandler.create());
					server.requestHandler(router::accept);
					server.listen(options.getPort());
					System.out.println("Server ready : " + options.getPort());
					// LOG.info("Server listening on port : " +
					// options.getPort());
				}
			}
		});
	}

	@Override
	public void stop(Future<Void> future) throws Exception {
		// TODO Auto-generated method stub
		nubes.stop(nubesRes -> closeServer(future));
	}

	private void closeServer(Future<Void> future) {
		if (server != null) {
			server.close();
		} else {
			future.complete();
		}
	}
}
