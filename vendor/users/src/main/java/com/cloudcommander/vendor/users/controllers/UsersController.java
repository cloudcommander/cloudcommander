package com.cloudcommander.vendor.users.controllers;

import com.cloudcommander.vendor.ddd.services.DddService;
import com.cloudcommander.vendor.users.ddd.aggregates.users.state.CreateUserCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

@Controller
@RequestMapping(value = "/users")
public class UsersController {

    @Resource(name = "usersDddService")
    private DddService dddService;

    @RequestMapping(method = RequestMethod.POST)
    public CompletionStage<ResponseEntity<Object>> create(){
        UUID uuid = UUID.randomUUID();
        CreateUserCommand createUserCommand = new CreateUserCommand(uuid);

        CompletionStage<Object> completionStage = dddService.dispatch(createUserCommand);

        return completionStage
                .thenApply(o -> {
                    return new ResponseEntity<>(HttpStatus.OK);
                })
                .exceptionally(throwable -> {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        });
    }
}
