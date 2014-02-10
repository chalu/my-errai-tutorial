package org.jboss.errai.demo.client.local;

import org.jboss.errai.demo.client.shared.DomainEntity;
import org.jboss.errai.ui.client.widget.HasModel;

import com.google.gwt.user.client.ui.IsWidget;

public interface Presenter<M extends DomainEntity, W extends HasModel<M> & IsWidget, D, S> {
	
	void onViewShown(S evt);
	
	void onAdd(M added);
	
	void onEdit(M updated);
	
	void onDelete(D evt);
	
	void onSelect(M selection);

	void fetchData(boolean forceFetch);
		
	BoundDisplay<M> getBoundDisplay();
	
	FormEntryDisplay getFormEntryDisplay();
	
	ListWidgetDisplay<M, W> getListWidgetDisplay();
}
