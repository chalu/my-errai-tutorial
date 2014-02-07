package org.jboss.errai.demo.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class DeleteEvent {

	protected Long entityId;
	
	public DeleteEvent() {
		super();
	}
	
	public DeleteEvent(Long id) {
		this();
		setEntityId(id);
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
}
