package org.jboss.errai.demo.server;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

@Dependent
public abstract class CRUD<M> {
	
	@Inject
	protected Logger log;

	@Inject
	protected EntityManager em;

	@Inject
	private UserTransaction tnx;
	
	protected Class<M> clz;
	protected String clzName;
	
	public CRUD() {
		super();
		
		clz = getClazz();
		clzName = clz.getSimpleName(); 
	}
	
	protected abstract Class<M> getClazz();
	
	protected Long countEntities(){
		log.severe("call to count " + this.clzName);
		return Long.valueOf(fetchEntities().size());
	}
    
	protected M fetchEntity(Long id){
    	log.severe("call to fetch " + this.clzName + " @ " + id);
    	M t = em.find(this.clz, id);
    	log.severe(this.clzName + " @ " + id + (t == null ? " was not found" : " was found"));
    	return t;
    }
    
	protected List<M> fetchEntities(){
    	log.severe("call to fetch all " + this.clzName);
    	return em.createQuery("select i from " + this.clzName + " i order by i.id", this.clz).getResultList();
    }
	
	protected M createEntity(M entity){
		log.severe("call to create " + this.clzName);
		try {
			tnx.begin();
			entity = em.merge(entity);
			tnx.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
		log.severe("created - " + this.clzName + ", returning call ...");
		return entity;
	}
	
	protected M updateEntity(M entity){
		log.severe("call to update " + this.clzName);
		try {
			tnx.begin();
			entity = em.merge(entity);
			tnx.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
		log.severe("updated - " + this.clzName + ", returning call ...");
		return entity;
	}
	
	protected void deleteEntities(List<Long> losers){
		if(losers != null && !losers.isEmpty()){
			for (Long l : losers) {
				deleteEntity(l);
			}
		}
	}
	
	protected boolean deleteEntity(Long id){
		log.severe("call to delete " + this.clzName + " @ " + id);
		boolean status = false;
		try {
			tnx.begin();
			M entity = em.find(this.clz, id);
			if(entity != null){
				log.severe("found " + this.clzName + ", deleting it ... ");
				em.remove(entity);
			}
			tnx.commit();
			status = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}	
		
		String str = (status == true ? "deleted - " : "cold not delete - "); 
		log.severe( str + this.clzName + " @ " + id + ", returning call ...");
		return status;
	}
	
	protected void refreshEntity(M entity){
		log.severe("call to refresh " + this.clzName);
		if(entity != null){
			try {
				tnx.begin();
				em.refresh(entity);
				tnx.commit();
			} catch (Exception ex) {
				ex.printStackTrace();
			}			
		}
	}

}
