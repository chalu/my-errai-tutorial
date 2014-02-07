package org.jboss.errai.demo.client.local;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.common.client.util.LogUtil;
import org.jboss.errai.demo.client.shared.ClientSide;
import org.jboss.errai.demo.client.shared.DeletePost;
import org.jboss.errai.demo.client.shared.New;
import org.jboss.errai.demo.client.shared.Post;
import org.jboss.errai.demo.client.shared.PostEndpoint;
import org.jboss.errai.demo.client.shared.PostsViewShown;
import org.jboss.errai.demo.client.shared.Select;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasConstrainedValue;

@ApplicationScoped
public class PostsPresenter extends BasePresenter<Post, PostItem, DeletePost, PostsViewShown> {
	
	public interface PostsDisplay extends FullDisplay<Post, PostItem>{
		HasConstrainedValue<String> category();
	}
	
	@Inject
	private Caller<PostEndpoint> endpoint;
	
	@Inject
	private PostsView display;
	
	public PostsPresenter() {
		super(Post.class, "Post");
	}
	
	@PostConstruct
	private void ready(){
		display.resetBtn().addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				resetModel();
			}
		});
		
		display.submitBtn().addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				final Post p = display.binder().getModel();
				LogUtil.log("submit " + p + " @ " + p.getId());
				if(p != null && p.getId() != null){
					// handle update
					endpoint.call(new RemoteCallback<Post>() {
						@Override
						public void callback(Post updated) {
							LogUtil.log("back from editng " + updated + " @ " + updated.getId());
						}
					}).edit(p);
					LogUtil.log("updating " + p);
				}else if(p != null && p.getId() == null){
					// handle create
					endpoint.call(new RemoteCallback<Post>() {
						@Override
						public void callback(Post added) {
							LogUtil.log("back from adding " + added + " @ " + added.getId());
							resetModel();
						}
					}).add(p);
					LogUtil.log("adding " + p);
				}
			}
		});
	}
	
	@Override
	public void onViewShown(@Observes @ClientSide PostsViewShown evt) {
		super.onViewShown(evt);
	}
	
	public void onPostSelected(@Observes @Select Post p){
		display.binder().setModel(p);
	}
	
	public void onPostAdded(@Observes @New Post p){
		super.onAdd(p);
	}
	
	@Override
	public BoundDisplay<Post> getBoundDisplay() {
		return display;
	}
	
	@Override
	public ListWidgetDisplay<Post, PostItem> getListWidgetDisplay() {
		return display;
	}
	
	@Override
	public void fetchData(boolean forceFetch){
		LogUtil.log("fetching " + clzName + (forceFetch == true ? " forcefully" : " gracefully"));
		if(data.isEmpty() || forceFetch){
			endpoint.call(new RemoteCallback<List<Post>>() {
				@Override
				public void callback(List<Post> list) {
					if(list != null && !list.isEmpty()){
						data.clear();
						data.addAll(list);
						display.listWidget().getValue().clear();
						display.listWidget().getValue().addAll(data);
						LogUtil.log(clzName + " data is ready");
					}
				}
			}).fetchAll();
		}
	}

}
