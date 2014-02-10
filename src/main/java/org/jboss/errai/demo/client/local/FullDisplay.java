package org.jboss.errai.demo.client.local;

import org.jboss.errai.demo.client.shared.DomainEntity;
import org.jboss.errai.ui.client.widget.HasModel;

import com.google.gwt.user.client.ui.IsWidget;

public interface FullDisplay<M extends DomainEntity, W extends HasModel<M> & IsWidget> extends Display<M>, BoundDisplay<M>, ListWidgetDisplay<M, W>, FormEntryDisplay {

}
