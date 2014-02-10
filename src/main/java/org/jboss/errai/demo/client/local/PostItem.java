package org.jboss.errai.demo.client.local;

import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.util.LogUtil;
import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.demo.client.shared.Post;
import org.jboss.errai.demo.client.shared.PostEndpoint;
import org.jboss.errai.enterprise.client.jaxrs.api.ResponseCallback;
import org.jboss.errai.ui.shared.api.annotations.AutoBound;
import org.jboss.errai.ui.shared.api.annotations.Bound;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;

@Templated("posts.html#post")
@SuppressWarnings({"deprecation"})
public class PostItem extends BaseListWidgetItem<Post, PostEndpoint> {
	
	@Inject
	private Caller<PostEndpoint> endpoint;
	
	@Inject
	@AutoBound
	private DataBinder<Post> binder;	

	@Bound
	@DataField(value="posttitle")
	private final Element title = DOM.createTD();

	@Bound
	@DataField(value="postbody")
	private final Element body = DOM.createTD();
	
	@Inject @DataField
	private Button deletepost;
	
	@Override
	protected DataBinder<Post> binder() {
		return binder;
	}
	
	@Override
	protected Caller<PostEndpoint> restEndpoint() {
		return endpoint;
	}
	
	@EventHandler("deletepost")
	public void onDelete(ClickEvent event){
		LogUtil.log("delete " + binder.getModel().getTitle());
		boolean go = Window.confirm("please confirm delete action.");
		if(go){			
			Post model = binder.getModel();
			endpoint.call(new ResponseCallback() {				
				@Override
				public void callback(Response response) {
					LogUtil.log("executed delete on server");
				}
			}).delete(model.getId());
			LogUtil.log("deleting ...");
		}		
	}

}
