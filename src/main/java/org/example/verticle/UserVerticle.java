package org.example.verticle;

import io.vertx.core.AbstractVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.example.exception.UserNotFoundException;
import org.example.model.User;
import org.example.service.UserService;

public class UserVerticle extends AbstractVerticle {

    private final UserService userService;

    public UserVerticle(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        // Routes
        router.post("/users").handler(this::handleCreateUser);
        router.get("/users/:id").handler(this::handleGetUserById);
        router.put("/users/:id/email").handler(this::handleUpdateEmail);
        router.delete("/users/:id").handler(this::handleDeleteUser);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8888, http -> {
                    if (http.succeeded()) {
                        System.out.println("HTTP server started on port 8888");
                        startPromise.complete();
                    } else {
                        startPromise.fail(http.cause());
                    }
                });
    }

    // ------------------------------------------------------------------------------------
    // POST /users  -> Create User
    // ------------------------------------------------------------------------------------
    private void handleCreateUser(RoutingContext ctx) {
        JsonObject body = ctx.body().asJsonObject();
        System.out.println(body);

        if (body == null || !body.containsKey("name") || !body.containsKey("email")) {
            ctx.response().setStatusCode(400).end("Missing name or email");
            return;
        }

        String name = body.getString("name");
        String email = body.getString("email");

        if (name.isBlank() || email.isBlank()) {
            ctx.response().setStatusCode(400).end("name/email cannot be empty");
            return;
        }

        User user = userService.createUser(name, email);

        ctx.response()
                .setStatusCode(201)
                .putHeader("Content-Type", "application/json")
                .end(JsonObject.mapFrom(user).encode());
    }

    // ------------------------------------------------------------------------------------
    // GET /users/:id
    // ------------------------------------------------------------------------------------
    private void handleGetUserById(RoutingContext ctx) {
        long id = parseId(ctx);
        if (id == -1) return;

        try {
            User user = userService.getUser(id);

            ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .end(JsonObject.mapFrom(user).encode());

        } catch (UserNotFoundException e) {
            ctx.response().setStatusCode(404).end(e.getMessage());
        }
    }

    // ------------------------------------------------------------------------------------
    // PUT /users/:id/email
    // ------------------------------------------------------------------------------------
    private void handleUpdateEmail(RoutingContext ctx) {
        long id = parseId(ctx);
        if (id == -1) return;

        JsonObject body = ctx.body().asJsonObject();

        if (body == null || !body.containsKey("email")) {
            ctx.response().setStatusCode(400).end("Missing email");
            return;
        }

        String email = body.getString("email");

        if (email.isBlank()) {
            ctx.response().setStatusCode(400).end("email cannot be empty");
            return;
        }

        try {
            User updated = userService.updateUser(id, null, email); // only updating email
            ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .end(JsonObject.mapFrom(updated).encode());
        } catch (UserNotFoundException e) {
            ctx.response().setStatusCode(404).end(e.getMessage());
        }
    }

    // ------------------------------------------------------------------------------------
    // DELETE /users/:id
    // ------------------------------------------------------------------------------------
    private void handleDeleteUser(RoutingContext ctx) {
        long id = parseId(ctx);
        if (id == -1) return;

        userService.deleteUser(id);
        ctx.response().setStatusCode(204).end();
    }

    // ------------------------------------------------------------------------------------
    // Helper: parse path param safely
    // ------------------------------------------------------------------------------------
    private long parseId(RoutingContext ctx) {
        try {
            return Long.parseLong(ctx.pathParam("id"));
        } catch (NumberFormatException ex) {
            ctx.response().setStatusCode(400).end("Invalid ID format");
            return -1;
        }
    }
}

