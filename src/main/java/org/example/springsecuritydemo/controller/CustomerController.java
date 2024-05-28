package org.example.springsecuritydemo.controller;

import org.example.springsecuritydemo.entity.user.CustomUserDetails;
import org.example.springsecuritydemo.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("v1")
public class CustomerController {

    @Autowired
    SessionRegistry sessionRegistry;

    @GetMapping("index")
    public String index(){
        return "Hello World LIDER";
    }

    @GetMapping("index2")
    public String index2(){
        return "Hello World JUNIOR";
    }

    @GetMapping("forbidden")
    public String forbidden(){
        return "forbidden";
    }

    @GetMapping("session")
    public ResponseEntity<?> getSessions(){

        List<String> sessionIds = new ArrayList<>();
        List<CustomUserDetails> users = new ArrayList<>();

        List<Object> sessions = sessionRegistry.getAllPrincipals();

        sessions.forEach(session -> {
            if (session instanceof CustomUserDetails) {
                ((CustomUserDetails) session).eraseCredentials();
                users.add((CustomUserDetails) session);
            }

            List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(session,false);

            sessionInformations.forEach(sessionInformation -> {
                sessionIds.add(sessionInformation.getSessionId());
            });
        });

        Map<String,Object> response = Map.of(
                "users",users,
                "sessionIds",sessionIds
        );

        return ResponseEntity.ok(response);
    }
}
