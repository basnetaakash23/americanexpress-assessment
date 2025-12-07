package org.example;

import io.vertx.core.Vertx;
import org.example.exception.UserNotFoundException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.repository.impl.InMemoryUserRepository;
import org.example.service.UserService;
import org.example.service.impl.UserServiceImpl;
import org.example.verticle.UserVerticle;

public class Main {
    public static void main(String[] args)  {

        UserRepository repository = new InMemoryUserRepository();
        UserService service = new UserServiceImpl(repository);

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new UserVerticle(service));
    }
}