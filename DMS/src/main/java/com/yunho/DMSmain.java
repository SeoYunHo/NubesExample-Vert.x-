package com.yunho;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class DMSmain {
	public static void main(String[] args) {
		VertxOptions options = new VertxOptions();
		options.setMaxEventLoopExecuteTime(Long.MAX_VALUE);
		Vertx vertx = Vertx.vertx(options);
		vertx.deployVerticle(new com.yunho.DMS_Verticle(), StringAsyncResult -> {
			System.out.println("DMS_Verticle deployment complete");
		});
	}
}
