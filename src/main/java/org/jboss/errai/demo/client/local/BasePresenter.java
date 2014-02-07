package org.jboss.errai.demo.client.local;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

import org.jboss.errai.common.client.util.LogUtil;
import org.jboss.errai.demo.client.shared.DeleteEvent;
import org.jboss.errai.demo.client.shared.DomainEntity;
import org.jboss.errai.demo.client.shared.Edit;
import org.jboss.errai.demo.client.shared.New;
import org.jboss.errai.demo.client.shared.Select;
import org.jboss.errai.ui.client.widget.HasModel;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.IsWidget;

@SuppressWarnings({"deprecation", "unused"})
@Dependent
public class BasePresenter<M extends DomainEntity, W extends HasModel<M> & IsWidget, D, S>
		implements Presenter<M, W, D, S> {

	protected List<M> data;

	protected Class<M> clz;

	protected String clzName;

	public BasePresenter() {
		super();
		data = new ArrayList<M>();
	}

	public BasePresenter(Class<M> clazz, String clazzName) {
		this();
		clz = clazz;
		clzName = clazzName;
	}

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

	@Override
	public BoundDisplay<M> getBoundDisplay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fetchData(boolean forceFetch) {
		// TODO Auto-generated method stub

	}

	@Override
	public ListWidgetDisplay<M, W> getListWidgetDisplay() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onViewShown(S evt) {
		LogUtil.log(clzName + " view is live, load data");
		fetchData(false);
	}

	@Override
	public void onAdd(M added) {
		LogUtil.log("recieved new " + clzName);
		data.add(added);
		LogUtil.log("added new " + clzName + " to data sotre");
		if(getListWidgetDisplay() != null){
			getListWidgetDisplay().listWidget().getValue().add(added);
			LogUtil.log("added new " + clzName + " to view");
		}		
		resetModel();
	}

	@Override
	public void onEdit(M updated) {
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
		resetModel();
	}

	@Override
	public void onDelete(D evt) {
		LogUtil.log(clzName + " has been deleted");
		DeleteEvent d = (DeleteEvent) evt;
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
	public void onModelSelected(M selection) {
		if(selection != null){
			LogUtil.log("got " + clzName + " seletion for " + selection);
			if(getBoundDisplay() != null){
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
