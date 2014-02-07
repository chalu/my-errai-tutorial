package org.jboss.errai.demo.client.local;

import org.jboss.errai.databinding.client.api.DataBinder;

public interface BoundDisplay<M> {
	DataBinder<M> binder();
}
