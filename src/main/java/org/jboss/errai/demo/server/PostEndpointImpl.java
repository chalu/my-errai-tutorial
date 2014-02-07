package org.jboss.errai.demo.server;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.errai.demo.client.shared.DeletePost;
import org.jboss.errai.demo.client.shared.New;
import org.jboss.errai.demo.client.shared.Post;
import org.jboss.errai.demo.client.shared.PostEndpoint;

@RequestScoped
public class PostEndpointImpl extends BaseEndpoint<Post, DeletePost> implements PostEndpoint {
	
//	@Inject @New
//	protected Event<Post> entityAdded;
	
	public PostEndpointImpl() {
		super(Post.class);
	}
	
//	@Override
//	public Post add(Post entity) {
//		Post p = super.add(entity);
//		entityAdded.fire(entity);
//		log.severe("added " + entity + " and fired CDI event, returning call from Impl");
//		return p;
//	}
	
	@Override
	protected DeletePost getDeleteEventObject() {
		return new DeletePost();
	}

}
