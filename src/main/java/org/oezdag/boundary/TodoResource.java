package org.oezdag.boundary;

import java.util.List;

import org.oezdag.control.TodoService;
import org.oezdag.entity.Todo;

import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/todos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodoResource {
    @Inject
    TodoService service;


    @GET
    public List<Todo> list(){
        return service.list();
    }

    @POST
    public Response create(Todo todo){
        if (todo == null || todo.title == null || todo.title.isBlank()) {
            throw new BadRequestException("title gerekli");
        }
        Todo created = service.create(todo);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("{id}/done")
    public Todo markDone(@PathParam("id") long id,
                         @QueryParam("value") @DefaultValue("true") boolean value) {
        return service.setDone(id, value).orElseThrow(NotFoundException::new);
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") long id) {
        if (service.delete(id)) return Response.noContent().build();
        throw new NotFoundException();
    }
 


    
}
