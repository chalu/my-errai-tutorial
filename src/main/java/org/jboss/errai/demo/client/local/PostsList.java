package org.jboss.errai.demo.client.local;

import org.jboss.errai.demo.client.shared.Post;
import org.jboss.errai.ui.client.widget.ListWidget;

import com.google.gwt.user.client.ui.HTMLPanel;

public class PostsList extends ListWidget<Post, PostItem> {
	
	public PostsList(String type) {
		super(new HTMLPanel(type, ""));
	}

	@Override
	protected Class<PostItem> getItemWidgetType() {
		return PostItem.class;
	}

}
