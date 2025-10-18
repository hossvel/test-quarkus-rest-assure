package com.hossvel.resource;

import com.hossvel.model.Item;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    @GET
    public List<Item> getAll() {
        return Item.listAll();
    }

    @POST
    @Transactional
    public Response create(Item item) {
        item.persist();
        return Response.status(201).entity(item).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = Item.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(404).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, Item updated) {
        Item item = Item.findById(id);
        if (item == null) return Response.status(404).build();
        item.name = updated.name;
        item.price = updated.price;
        item.persist();
        return Response.ok(item).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        Item item = Item.findById(id);
        return item != null ? Response.ok(item).build() : Response.status(404).build();
    }
}