package com.example.resource;

import com.example.Roles;
import com.example.dto.request.LoginRequest;
import com.example.dto.response.ErrorResponse;
import com.example.dto.response.TokenResponse;
import com.example.model.User;
import com.example.service.AuthService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.Optional;

@Path("/auth")
public class AuthResource {

    @Inject
    AuthService authService;
    @Context
    SecurityContext securityContext;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest loginRequest) {
        try{
            Optional<String> token = authService.authenticate(loginRequest.username, loginRequest.password);

            if (token.isPresent()) {
                return Response.ok(new TokenResponse("bearer", token.get())).header("Authorization", "Bearer " + token.get()).build();
            }

            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(true,"Username atau Password Salah")).build();
        }catch (Exception e) {
            if (e.getMessage().equals("Maximum login attempts exceeded")){
                return Response.status(Response.Status.FORBIDDEN).entity("Maximum login attempts exceeded").build();
            }else{
                return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
            }
        }

    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(User user) {
        if (User.find("username", user.username).firstResultOptional().isPresent() ||
                User.find("email", user.email).firstResultOptional().isPresent()) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        User newUser = authService.register(user);
        return Response.ok(newUser).status(Response.Status.CREATED).build();
    }


    @GET
    @Path("/me")
    @RolesAllowed({Roles.USER, Roles.SERVICE})
    public User me() {
        return User.find("email", securityContext.getUserPrincipal().getName()).firstResult();
    }

}
