package org.jboss.errai.demo.client.local;

import org.jboss.errai.ui.client.widget.HasModel;

import com.google.gwt.user.client.ui.IsWidget;

public interface FullDisplay<M, W extends HasModel<M> & IsWidget> extends BoundDisplay<M>, ListWidgetDisplay<M, W>, FormEntryDisplay {

}
