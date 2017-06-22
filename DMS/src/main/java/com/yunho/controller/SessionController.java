package com.yunho.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.routing.http.POST;
import com.yunho.database.DataBase;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

@Controller("/session")
public class SessionController {
	String sessionKey;
	Statement statement;
	ResultSet resultSet;

	@POST("/check")
	public void sessionCheck(RoutingContext ctx, Session session) {
		JsonObject jo = new JsonObject();
		String sessionKey = session.get("sessionKey").toString();

		try {
			statement = DataBase.DBconnection();
			resultSet = statement.executeQuery("select count(*) from session where sessionKey='" + sessionKey + "';");

			if (resultSet.next() && resultSet.getInt(1) > 0) {
				jo.put("session", true);
				ctx.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
						.end(jo.toString());
				ctx.response().close();
			} else {
				jo.put("session", false);
				ctx.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(400)
						.end(jo.toString());
				ctx.response().close();
			}
		} catch (SQLException e) {
			jo.put("success", false);
			ctx.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(500)
					.end(jo.toString());
			ctx.response().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
