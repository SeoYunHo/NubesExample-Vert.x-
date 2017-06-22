package com.yunho;

import com.google.common.net.HttpHeaders;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class RequestSecurePreprocessor {
	public static Handler<RoutingContext> create() {
		return new SecurePreprocessHandler();
	}

	private static class SecurePreprocessHandler implements Handler<RoutingContext> {
		@Override
		public void handle(RoutingContext ctx) {
			ctx.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
					"Cookie, Origin, X-Requested-With");
			ctx.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
					"POST, PUT, PATCH, GET, DELETE, OPTIONS, HEAD, CONNECT");
			ctx.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
			ctx.next();
		}
	}
}
