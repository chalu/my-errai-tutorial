package org.jboss.errai.demo.server;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.jboss.errai.demo.client.shared.DeleteEvent;
import org.jboss.errai.demo.client.shared.Edit;
import org.jboss.errai.demo.client.shared.New;
import org.jboss.errai.demo.client.shared.Post;
import org.jboss.errai.demo.client.shared.RESTService;

@Dependent
public abstract class BaseEndpoint<M, D extends DeleteEvent> extends CRUD<M> implements RESTService<M> {
	
//	@Inject
//	protected Logger log;
	
	@Inject @New
	protected Event<M> entityAdded;
	
	@Inject @Edit
	protected Event<M> entityEdited;
	
	@Inject
	protected Event<D> entityDeleted;
	
	public BaseEndpoint() {
		super();
	}
	
	protected abstract D getDeleteEventObject();

	@Override
	public M fetch(Long id) {
		log.severe("lets fetch " + clzName + " @ " + id);
		return fetchEntity(id);
	}

	@Override
	public List<M> fetchAll() {
		log.severe("lets fetch all " + clzName);
		return fetchEntities();
	}

	@Override
	public Long count() {
		log.severe("lets count all " + clzName);
		return countEntities();
	}

	@Override
	public M add(M entity) {
		log.severe("lets add " + clzName + " - " + entity);
		entity = createEntity(entity);
		
		entityAdded.fire(entity);						
		log.severe("added " + entity + " and fired CDI event, returning call");
		return entity;
	}

	@Override
	public M edit(M entity) {
		log.severe("lets update " + clzName + " - " + entity);
		entity = updateEntity(entity);
		entityEdited.fire(entity);
		log.severe("updated " + entity + " and fired CDI event, returning call");
		return entity;
	}

	@Override
	public Response delete(Long id) {
		log.severe("lets delete " + clzName + " @ " + id);
		boolean status = deleteEntity(id);
		log.severe("deleted " + clzName + " @ " + id);
		if(status){
			D d = getDeleteEventObject();
			if(d != null){
				d.setEntityId(id);
				entityDeleted.fire(d);
				log.severe("fired delete CDI event for " + clzName + " @ " + id);
			}			
			return Response.ok().build();
		}		
		return Response.serverError().build();
	}

}
