package org.jboss.errai.demo.client.local;

import java.util.List;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.demo.client.shared.ClientSide;
import org.jboss.errai.demo.client.shared.Post;
import org.jboss.errai.demo.client.shared.PostsViewShown;
import org.jboss.errai.ui.client.widget.ListWidget;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShown;
import org.jboss.errai.ui.shared.api.annotations.AutoBound;
import org.jboss.errai.ui.shared.api.annotations.Bound;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasConstrainedValue;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;

@Page(role = DefaultPage.class)
@Templated("posts.html#root") @Singleton
public class PostsView extends Composite implements PostsPresenter.PostsDisplay {
	
	@Inject @AutoBound
	private DataBinder<Post> binder;

	@Inject
	@Bound(property="title")
	@DataField(value="titleentry")
	private TextBox titleEntry;

	@Inject
	@Bound(property="body")
	@DataField(value="bodyentry")
	private TextArea bdyEntry;

	@Inject
	@DataField
	private Button reset;

	@Inject
	@DataField
	private Button submit;

	@Inject @ClientSide
	private Event<PostsViewShown> shownEvt;
	
	@DataField
	private ValueListBox<String> postcategory = new ValueListBox<String>(new AbstractRenderer<String>() {
		@Override
		public String render(String str) {
			return (str == null ? "" : str);
		}				
	});
	
	@DataField
	private final ListWidget<Post, PostItem> posts;

	@PageShown
	private void onPageShown() {
		shownEvt.fire(new PostsViewShown());
	}

	public PostsView() {
		super();
		posts = new PostsList("tbody");
	}

	@Override
	public DataBinder<Post> binder() {
		return binder;
	}

	@Override
	public ListWidget<Post, PostItem> listWidget() {
		return posts;
	}

	@Override
	public Button resetBtn() {
		return reset;
	}

	@Override
	public Button submitBtn() {
		return submit;
	}

	@Override
	public HasConstrainedValue<String> category() {
		return postcategory;
	}
	
	@Override
	public void setData(List<Post> data) {
		listWidget().getValue().clear();
		listWidget().getValue().addAll(data);
	}

}
