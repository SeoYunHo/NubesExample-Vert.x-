package com.yunho.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.routing.http.POST;
import com.yunho.database.DataBase;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

@Controller("/account")
public class AccountController {
	Statement statement;
	ResultSet resultSet;

	@POST("/login")
	public void login(RoutingContext ctx, Session session) {
		String id = ctx.request().getFormAttribute("id");
		String password = ctx.request().getFormAttribute("password");

		JsonObject jo = new JsonObject();

		try {
			statement = DataBase.DBconnection();
			resultSet = statement.executeQuery(
					"select count(*) from account where id= '" + id + "' and password='" + password + "';");
			if (resultSet.next() && resultSet.getInt(1) >= 0) {
				String sessionKey;
				int count = 1;
				do {
					UUID key = UUID.randomUUID();
					sessionKey = key.toString();
					resultSet = statement
							.executeQuery("select count(*) from session where sessionKey='" + sessionKey + "';");
					if (resultSet.next())
						count = resultSet.getInt(1);
				} while (count != 0);
				int result = statement.executeUpdate(
						"insert into session values('" + sessionKey + "','" + id + "','" + password + "');");
				if (result == 1) {
					session.put("sessionKey", sessionKey);
					ctx.setSession(session);
					jo.put("success", true);
					ctx.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(201)
							.end(jo.toString());
					ctx.response().close();
				} else {
					jo.put("success", false);
					ctx.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(400)
							.end(jo.toString());
					ctx.response().close();
				}
			} else {
				jo.put("success", false);
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

	@POST("/register")
	public void signUp(RoutingContext ctx) {
		String id = ctx.request().getFormAttribute("id");
		String password = ctx.request().getFormAttribute("password");

		JsonObject jo = new JsonObject();

		System.out.println(id + " " + password);
		try {
			statement = DataBase.DBconnection();
			System.out.println("DataBase connection");
			int result = statement.executeUpdate("insert into account values('" + id + "','" + password + "');");

			if (result == 1) {
				jo.put("success", true);
				ctx.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
						.end(jo.toString());
				ctx.response().close();
			} else {
				jo.put("success", false);
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
