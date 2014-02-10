package org.jboss.errai.demo.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class DeletePost extends DeleteEvent {
	
	public DeletePost() {
		super();
	}
	
	public DeletePost(Long id) {
		super(id);
	}

}
