package org.jboss.errai.demo.client.local;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.common.client.util.LogUtil;
import org.jboss.errai.demo.client.shared.DeleteEvent;
import org.jboss.errai.demo.client.shared.DomainEntity;
import org.jboss.errai.demo.client.shared.Edit;
import org.jboss.errai.demo.client.shared.New;
import org.jboss.errai.demo.client.shared.RESTService;
import org.jboss.errai.demo.client.shared.Select;
import org.jboss.errai.ui.client.widget.HasModel;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;

@Dependent
public abstract class BasePresenter<M extends DomainEntity, W extends HasModel<M> & IsWidget, D, S, C extends RESTService<M>>
		implements Presenter<M, W, D, S> {

	protected List<M> data;

	protected Class<M> clz;

	protected String clzName;

	public BasePresenter() {
		super();
		
		clz = clazz();
		data = new ArrayList<M>();
		
		String qualifiedName = clz.getName();
		clzName = qualifiedName.substring(qualifiedName.lastIndexOf(".")+1);
	}
	
	protected abstract Class<M> clazz();
	protected abstract Caller<C> restEndpoint();
	
	protected abstract Display<M> getDisplay();
	

	public List<M> getData() {
		return data;
	}

	/*
	 * Within the collection represented by the data parameter,
	 * look for the object whose ID is id, then return the index
	 * if found or -1 otherwise 
	 */
	protected int indexOfModelWithId(Long id, List<M> data) {
		int pos = -1;
		for (DomainEntity de : data) {
			if (de.getId().equals(id)) {
				pos = data.indexOf(de);
			}
		}
		return pos;
	}

	protected int indexOfModelWithId(Long id) {
		return indexOfModelWithId(id, getData());
	}

	protected int indexOfModelWithIdOf(DomainEntity src, List<M> data) {
		int pos = -1;
		for (DomainEntity de : data) {
			if (de.getId().equals(src.getId())) {
				pos = data.indexOf(de);
			}
		}
		return pos;
	}

	protected int indexOfModelWithIdOf(DomainEntity src) {
		return indexOfModelWithIdOf(src, getData());
	}
	
	@PostConstruct
	private void ready(){
		if(getFormEntryDisplay() != null && getFormEntryDisplay().resetBtn() != null){
			getFormEntryDisplay().resetBtn().addClickHandler(new ClickHandler() {			
				@Override
				public void onClick(ClickEvent event) {
					resetModel();
				}
			});
		}
		
		if(getFormEntryDisplay() != null && getFormEntryDisplay().submitBtn() != null){
			getFormEntryDisplay().submitBtn().addClickHandler(new ClickHandler() {			
				@Override
				public void onClick(ClickEvent event) {
					if(getBoundDisplay() != null && getBoundDisplay().binder() != null){						
						M model = getBoundDisplay().binder().getModel();
						LogUtil.log("submit " + model + " @ " + model.getId());
						if(model != null && model.getId() != null){
							// handle update
							restEndpoint().call(new RemoteCallback<M>() {
								@Override
								public void callback(M updated) {
									LogUtil.log("back from editng " + updated + " @ " + updated.getId());
									resetModel();
								}
							}).edit(model);
							LogUtil.log("updating " + model);
						}else if(model != null && model.getId() == null){
							// handle create
							restEndpoint().call(new RemoteCallback<M>() {
								@Override
								public void callback(M added) {
									LogUtil.log("back from adding " + added + " @ " + added.getId());
									resetModel();
								}
							}).add(model);
							LogUtil.log("adding " + model);
						}
					}					
				}
			});
		}
	}

	@Override
	public BoundDisplay<M> getBoundDisplay() {
		return null;
	}

	@Override
	public ListWidgetDisplay<M, W> getListWidgetDisplay() {
		return null;
	}
	
	@Override
	public FormEntryDisplay getFormEntryDisplay() {
		return null;
	}

	@Override
	public void fetchData(boolean forceFetch) {
		LogUtil.log("fetching " + clzName + (forceFetch == true ? " forcefully" : " gracefully"));
		if(data.isEmpty() || forceFetch){
			restEndpoint().call(new RemoteCallback<List<M>>() {
				@Override
				public void callback(List<M> list) {
					if(list != null && !list.isEmpty()){
						data.clear();
						data.addAll(list);
						getDisplay().setData(list);						
						LogUtil.log(clzName + " data is ready");
					}
				}
			}).fetchAll();
		}
	}
	
	@Override
	public void onViewShown(S shownEvt) {
		LogUtil.log(clzName + " view is live, load data");
		fetchData(false);
	}
	
	/*
	 * The @Observes qualifyer causes compilation errors
	 */
//	public void whenViewIsShown(@Observes S evt){}
	

	@Override
	public void onAdd(@Observes @New M added) {
		LogUtil.log("recieved new " + clzName);
		data.add(added);
		LogUtil.log("added new " + clzName + " to data sotre");
		if(getListWidgetDisplay() != null){
			getListWidgetDisplay().listWidget().getValue().add(added);
			LogUtil.log("added new " + clzName + " to view");
		}				
	}

	@Override
	public void onEdit(@Observes @Edit M updated) {
		LogUtil.log("recieved updated " + clzName);
		DomainEntity e = (DomainEntity) updated;
		int pos = indexOfModelWithIdOf(e);
		LogUtil.log("update was for " + clzName + " indexed @ " + pos);
		if (pos >= 0) {
			data.set(pos, updated);
			LogUtil.log("updated " + clzName + " in data sotre");
			if(getListWidgetDisplay() != null){
				getListWidgetDisplay().listWidget().getValue().set(pos, updated);
				LogUtil.log("updated " + clzName + " in view");
			}
		}
	}

	@Override
	public void onDelete(D deleteEvt) {
		LogUtil.log(clzName + " has been deleted");
		DeleteEvent d = (DeleteEvent) deleteEvt;
		int pos = indexOfModelWithId(d.getEntityId());
		LogUtil.log("delete was for " + clzName + " indexed @ " + pos);
		if (pos >= 0) {
			data.remove(pos);
			LogUtil.log("deleted " + clzName + " in data sotre");
			if(getListWidgetDisplay() != null){
				getListWidgetDisplay().listWidget().getValue().remove(pos);
				LogUtil.log("deleted " + clzName + " in view");
			}
		}
		resetModel();
	}

	@Override
	public void onSelect(@Observes @Select M selection) {
		if(selection != null){
			LogUtil.log("got " + clzName + " seletion for " + selection);
			if(getBoundDisplay() != null && getBoundDisplay().binder() != null){
				getBoundDisplay().binder().setModel(selection);
				LogUtil.log(clzName + " UI should reflect binding for " + selection);
			}
		}	
	}
	
	protected void resetModel(){
		if(getBoundDisplay() != null){
			getBoundDisplay().binder().setModel( (M) GWT.create(clz));
		}
	}

}
