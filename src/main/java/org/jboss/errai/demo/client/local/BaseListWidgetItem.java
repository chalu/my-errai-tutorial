package org.jboss.errai.demo.client.local;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.util.LogUtil;
import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.demo.client.shared.DomainEntity;
import org.jboss.errai.demo.client.shared.RESTService;
import org.jboss.errai.demo.client.shared.Select;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Composite;

@Dependent
public abstract class BaseListWidgetItem<M extends DomainEntity, C extends RESTService<M>> extends Composite implements HasModel<M> {
	
	protected abstract DataBinder<M> binder();
	protected abstract Caller<C> restEndpoint();
	
	@Inject @Select
	private Event<M> selectEvt;

	@Override
	public M getModel() {
		return binder().getModel();
	}

	@Override
	public void setModel(M model) {
		if(model != null){
			binder().setModel(model);
		}		
	}
	
	@EventHandler
	public void onClick(ClickEvent event){
		M model = binder().getModel();
		selectEvt.fire(model);
		NodeList<Element> rows = this.getElement().getParentElement().getElementsByTagName("tr");
		for(int i = 0, len = rows.getLength(); i < len; i++){
			rows.getItem(i).removeClassName("success");
		}
		this.getElement().addClassName("success");
		LogUtil.log("fired seletion on " + model);
	}

}
