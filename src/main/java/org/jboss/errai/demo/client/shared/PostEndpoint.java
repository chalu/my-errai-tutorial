package org.jboss.errai.demo.client.shared;

import javax.ws.rs.Path;

@Path("/posts")
public interface PostEndpoint extends RESTService<Post> {

}
