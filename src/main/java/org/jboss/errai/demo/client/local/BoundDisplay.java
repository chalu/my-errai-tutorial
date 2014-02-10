package org.jboss.errai.demo.client.local;

import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.demo.client.shared.DomainEntity;

public interface BoundDisplay<M extends DomainEntity> {
	DataBinder<M> binder();
}
