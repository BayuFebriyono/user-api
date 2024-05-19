package com.example.resource;

import com.example.Roles;
import com.example.dto.response.ErrorResponse;
import com.example.model.User;
import com.example.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;


@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({Roles.ADMIN})

public class UserResource {
    @Inject
    UserService userService;

    @POST
    public Response createUser(@Valid User user) {
        try{
            User createdUser = userService.createUser(user);
            return Response.ok(createdUser).status(Response.Status.CREATED).build();
        } catch (ValidationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(true, e.getMessage())).build();
        }
    }

    @GET
    public List<User> listAllUsers(){
        return userService.listAllUsers();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        Optional<User> userOptional = userService.getUserById(id);
        return userOptional.map(user -> Response.ok(user).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, @Valid User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            if (updatedUser != null) {
                return Response.ok(updatedUser).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
         } catch (ValidationException e){
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(true, e.getMessage())).build();
    }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
