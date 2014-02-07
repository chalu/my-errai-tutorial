package org.jboss.errai.demo.client.shared;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

public interface RESTService<T> {

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public T fetch(@PathParam("id") Long id);

	@GET
	@Produces("application/json")
	public List<T> fetchAll();

	@GET
	@Path("/count")
	@Produces("application/json")
	public Long count();
	
	@POST
	@Path("/add")
	@Consumes("application/json")
	@Produces("application/json")
	public T add(T entity);
	
	@POST
	@Path("/edit")
	@Consumes("application/json")
	@Produces("application/json")
	public T edit(T entity);
	
	@GET
	@Path("/delete/{id:[0-9][0-9]*}")
	//@Consumes("application/json")
	@Produces("application/json")
	public Response delete(@PathParam("id") Long id);

}
