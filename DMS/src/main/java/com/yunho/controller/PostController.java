package com.yunho.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.routing.http.DELETE;
import com.github.aesteve.vertx.nubes.annotations.routing.http.GET;
import com.github.aesteve.vertx.nubes.annotations.routing.http.POST;
import com.github.aesteve.vertx.nubes.annotations.routing.http.PUT;
import com.yunho.database.DataBase;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

@Controller("/post")
public class PostController {
	Statement statement;
	ResultSet resultSet;

	@POST("/free/list")
	public void list(RoutingContext ctx) {
		JsonArray jsonArray = new JsonArray();
		JsonObject jo;

		try {
			statement = DataBase.DBconnection();
			resultSet = statement.executeQuery("select * from post;");

			while (resultSet.next()) {
				jo = new JsonObject();

				jo.put("no", resultSet.getString("no"));
				jo.put("title", resultSet.getString("title"));
				jo.put("content", resultSet.getString("content"));

				jsonArray.add(jo);
				System.out.println(jo.toString());
			}
			ctx.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
					.end(jsonArray.toString());
			ctx.response().close();
		} catch (SQLException e) {
			ctx.response().setStatusCode(500).end();
			ctx.response().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@GET("/free/:no")
	public void get(RoutingContext ctx) {
		JsonObject jo = new JsonObject();
		String no = ctx.request().getParam("no");

		try {

			statement = DataBase.DBconnection();
			resultSet = statement.executeQuery("select * from post where no='" + no + "';");

			if (resultSet.next()) {
				jo.put("no", resultSet.getString("no"));
				jo.put("title", resultSet.getString("title"));
				jo.put("content", resultSet.getString("content"));
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
			ctx.response().setStatusCode(500).end(jo.toString());
			ctx.response().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@POST("/free")
	public void write(RoutingContext ctx) {
		String title = ctx.request().getFormAttribute("title");
		String content = ctx.request().getFormAttribute("content");
		JsonObject jo = new JsonObject();

		try {
			statement = DataBase.DBconnection();
			int result = statement
					.executeUpdate("insert into post (title,content) values('" + title + "','" + content + "');");

			if (result == 1) {
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
		} catch (SQLException e) {
			jo.put("success", false);
			ctx.response().setStatusCode(500).end(jo.toString());
			ctx.response().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@DELETE("/free/:no")
	public void delete(RoutingContext ctx) {
		String no = ctx.request().getParam("no");
		JsonObject jo = new JsonObject();

		try {
			statement = DataBase.DBconnection();
			int result = statement.executeUpdate("delete from post where no=" + no + ";");

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
			ctx.response().setStatusCode(500).end(jo.toString());
			ctx.response().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@PUT("/free/:no")
	public void update(RoutingContext ctx) {
		String no = ctx.request().getParam("no");
		String title = ctx.request().getFormAttribute("title");
		String content = ctx.request().getFormAttribute("content");
		JsonObject jo = new JsonObject();

		try {
			statement = DataBase.DBconnection();
			int titleResult = 0;
			int contentResult = 0;
			if (title != null) {
				titleResult = statement.executeUpdate("update post set title='" + title + "' where no=" + no + ";");
			}

			if (content != null) {
				contentResult = statement
						.executeUpdate("update post set content='" + content + "' where no=" + no + ";");
			}

			if (titleResult == 1 || contentResult == 1) {
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
		} catch (SQLException e) {
			jo.put("success", false);
			ctx.response().setStatusCode(500).end(jo.toString());
			ctx.response().close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}
