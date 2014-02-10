package org.jboss.errai.demo.server;

import javax.enterprise.context.RequestScoped;

import org.jboss.errai.demo.client.shared.DeletePost;
import org.jboss.errai.demo.client.shared.Post;
import org.jboss.errai.demo.client.shared.PostEndpoint;

@RequestScoped
public class PostEndpointImpl extends BaseEndpoint<Post, DeletePost> implements PostEndpoint {

	@Override
	protected Class<Post> getClazz() {
		return Post.class;
	}

	@Override
	protected DeletePost getDeleteEventObject() {
		return new DeletePost();
	}

}
