package org.jboss.errai.demo.client.local;

import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.client.widget.ListWidget;

import com.google.gwt.user.client.ui.IsWidget;

public interface ListWidgetDisplay<M, W extends HasModel<M> & IsWidget> {
	ListWidget<M, W> listWidget();
}
