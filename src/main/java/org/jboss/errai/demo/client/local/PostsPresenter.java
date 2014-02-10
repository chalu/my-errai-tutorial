package org.jboss.errai.demo.client.local;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.demo.client.shared.ClientSide;
import org.jboss.errai.demo.client.shared.DeletePost;
import org.jboss.errai.demo.client.shared.Post;
import org.jboss.errai.demo.client.shared.PostEndpoint;
import org.jboss.errai.demo.client.shared.PostsViewShown;

import com.google.gwt.user.client.ui.HasConstrainedValue;

@ApplicationScoped
public class PostsPresenter extends BasePresenter<Post, PostItem, DeletePost, PostsViewShown, PostEndpoint> {
	
	public interface PostsDisplay extends FullDisplay<Post, PostItem>{
		HasConstrainedValue<String> category();
	}
	
	@Inject
	private Caller<PostEndpoint> endpoint;
	
	@Inject
	private PostsView display;
	
	@Override
	public void onViewShown(@Observes @ClientSide PostsViewShown evt) {
		super.onViewShown(evt);
	}
	
	public void onPostDeleted(@Observes DeletePost evt){
		super.onDelete(evt);
	}
	
	@Override
	protected Class<Post> clazz() {
		return Post.class;
	}
	
	@Override
	protected Display<Post> getDisplay() {
		return display;
	}
	
	@Override
	public FormEntryDisplay getFormEntryDisplay() {
		return display;
	}
	
	@Override
	protected Caller<PostEndpoint> restEndpoint() {
		return endpoint;
	}
	
	@Override
	public BoundDisplay<Post> getBoundDisplay() {
		return display;
	}
	
	@Override
	public ListWidgetDisplay<Post, PostItem> getListWidgetDisplay() {
		return display;
	}

}
