package com.yunho.controller;

import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.routing.http.GET;

import io.vertx.ext.web.RoutingContext;

@Controller("/resource")
public class ResourcesController {
	@GET("/image")
	public void sendImage(RoutingContext ctx) {
		ctx.response().sendFile("C:/Users/YunHo/workspace/DMS/src/main/java/com/yunho/image/example.jpg");
		ctx.response().close();
	}
}
