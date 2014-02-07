package org.jboss.errai.demo.client.local;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.util.LogUtil;
import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.demo.client.shared.Post;
import org.jboss.errai.demo.client.shared.PostEndpoint;
import org.jboss.errai.demo.client.shared.Select;
import org.jboss.errai.enterprise.client.jaxrs.api.ResponseCallback;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.AutoBound;
import org.jboss.errai.ui.shared.api.annotations.Bound;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;

@Templated("posts.html#post")
@SuppressWarnings({"deprecation"})
public class PostItem extends Composite implements HasModel<Post> {
	
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
	
	@Inject @Select
	private Event<Post> selectEvt;

	@Override
	public Post getModel() {
		return binder.getModel();
	}

	@Override
	public void setModel(Post model) {
		binder.setModel(model);
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
	
	@EventHandler
	public void onClick(ClickEvent event){
		selectEvt.fire(binder.getModel());
		NodeList<Element> rows = this.getElement().getParentElement().getElementsByTagName("tr");
		for(int i = 0, len = rows.getLength(); i < len; i++){
			rows.getItem(i).removeClassName("success");
		}
		this.getElement().addClassName("success");
		LogUtil.log("fired seletion on " + binder.getModel());
	}

}
