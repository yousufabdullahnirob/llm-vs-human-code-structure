package com.example.metadata.resources;

import com.example.metadata.EntityRegistry.EntityRepository;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Generic REST resource base class for exposing CRUD operations on a single
 * entity type. Concrete resources (e.g. a UserResource) extend this and
 * supply the entity type and repository; this class handles the common
 * request/response wiring: authorization check -> repository call -> response.
 *
 * This mirrors a common pattern in larger metadata-catalog systems where
 * dozens of near-identical REST resources share one generic base class.
 */
public abstract class EntityResource<T> {

  protected final EntityRepository<T> repository;
  protected final Authorizer authorizer;
  protected final String entityType;

  protected EntityResource(String entityType, EntityRepository<T> repository, Authorizer authorizer) {
    this.entityType = entityType;
    this.repository = repository;
    this.authorizer = authorizer;
  }

  @GET
  @Path("/{id}")
  public Response get(@Context SecurityContext securityContext, @PathParam("id") String id) {
    authorizer.authorizeRead(securityContext, entityType, id);
    T entity = repository.getById(id);
    if (entity == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(entity).build();
  }

  @GET
  public Response list(
      @Context SecurityContext securityContext,
      @QueryParam("limit") @jakarta.ws.rs.DefaultValue("10") int limit,
      @QueryParam("after") String after) {
    authorizer.authorizeReadAll(securityContext, entityType);
    List<T> entities = repository.list(limit, after);
    return Response.ok(entities).build();
  }

  @POST
  public Response create(@Context SecurityContext securityContext, T entity) {
    authorizer.authorizeCreate(securityContext, entityType);
    repository.save(entity);
    return Response.status(Response.Status.CREATED).entity(entity).build();
  }

  @PUT
  @Path("/{id}")
  public Response update(
      @Context SecurityContext securityContext, @PathParam("id") String id, T entity) {
    authorizer.authorizeUpdate(securityContext, entityType, id);
    repository.save(entity);
    return Response.ok(entity).build();
  }

  @DELETE
  @Path("/{id}")
  public Response delete(@Context SecurityContext securityContext, @PathParam("id") String id) {
    authorizer.authorizeDelete(securityContext, entityType, id);
    repository.delete(id);
    return Response.noContent().build();
  }

  /** Minimal authorization contract, standing in for a real policy engine. */
  public interface Authorizer {
    void authorizeRead(SecurityContext ctx, String entityType, String id);

    void authorizeReadAll(SecurityContext ctx, String entityType);

    void authorizeCreate(SecurityContext ctx, String entityType);

    void authorizeUpdate(SecurityContext ctx, String entityType, String id);

    void authorizeDelete(SecurityContext ctx, String entityType, String id);
  }
}
