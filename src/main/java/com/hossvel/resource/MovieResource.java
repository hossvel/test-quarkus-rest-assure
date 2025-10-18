package com.hossvel.resource;


import com.hossvel.repository.MovieRepository;
import com.hossvel.model.Movie;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;


@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {

    @Inject
    MovieRepository movieRepository;

    @GET
    public Response getAll() {
        List<Movie> movies = movieRepository.listAll();
        return Response.ok(movies).build();
    }

    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") Long id) {
        return movieRepository
                .findByIdOptional(id)
                .map(movie -> Response.ok(movie).build())
                .orElse(Response.status(NOT_FOUND).build());
    }

    @GET
    @Path("title/{title}")
    public Response getByTitle(@PathParam("title") String title) {
        return movieRepository
                .find("title", title)
                .singleResultOptional()
                .map(movie -> Response.ok(movie).build())
                .orElse(Response.status(NOT_FOUND).build());
    }

    @GET
    @Path("country/{country}")
    public Response getByCountry(@PathParam("country") String country) {
        List<Movie> movies = movieRepository.findByCountry(country);
        return Response.ok(movies).build();
    }

    @POST
    @Transactional
    public Response create(Movie movie) {
        movieRepository.persist(movie);
        if (movieRepository.isPersistent(movie)) {
            return Response.created(URI.create("/movies/" + movie.getId())).build();
        }
        return Response.status(NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateById(@PathParam("id") Long id, Movie movie) {
        return movieRepository
                .findByIdOptional(id)
                .map(
                        m -> {
                            m.setTitle(movie.getTitle());
                            return Response.ok(m).build();
                        })
                .orElse(Response.status(NOT_FOUND).build());
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = movieRepository.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(NOT_FOUND).build();
    }
}