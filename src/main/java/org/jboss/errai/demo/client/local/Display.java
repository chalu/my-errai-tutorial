package org.jboss.errai.demo.client.local;

import java.util.List;

import org.jboss.errai.demo.client.shared.DomainEntity;

public interface Display<M extends DomainEntity> {
	
	void setData(List<M> data);

}
