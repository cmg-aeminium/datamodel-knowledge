/**
 * Copyright (c) 2020 Carlos Gonçalves (https://www.linkedin.com/in/carlosmogoncalves/)
 * Likely open-source, so copy at will, bugs will be yours as well.
 */
package pt.cmg.aeminium.knowledge.dao;

import java.util.Collection;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.jboss.weld.interceptor.util.proxy.TargetInstanceProxy;

/**
 * @author Carlos Manuel
 */
public abstract class JPACrudDAO<T> {

    private final Class<T> entityClass;

    public JPACrudDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @PersistenceContext(unitName = "knowledge-data")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Persists an entity into the system.
     */
    public void create(T entity) {
        if (entity != null) {
            create(entity, false);
        }
    }

    /**
     * Persists an entity into the system with the possibility to force or not the changes into the
     * persistence context
     *
     * @param entity
     * @param flush true if forcing is needed, false otherwise
     */
    public void create(T entity, boolean flush) {
        if (entity != null) {
            getEntityManager().persist(entity);
            if (flush) {
                getEntityManager().flush();
            }
        }
    }

    /**
     * Edits an Entity
     */
    public void edit(T entity) {
        if (entity != null) {
            // If this happens to be an Injected entity, then saving requires an extra step. This is rare, but most User types are injected via authentication, so...
            if (entity instanceof TargetInstanceProxy<?> proxy) {
                getEntityManager().merge(proxy.weld_getTargetInstance());
            } else {
                getEntityManager().merge(entity);
            }
        }
    }

    /**
     * Removes an entity from the database
     */
    public void remove(T entity) {
        if (entity != null) {
            getEntityManager().remove(getEntityManager().merge(entity));
        }
    }

    /**
     * Retrieves and entity by its numeric ID.
     *
     * @param id
     * @return the found entity instance or null if the entity does not exist
     */
    public T findById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }

        return getEntityManager().find(entityClass, id);
    }

    /**
     * Retrieves the full list of Entities of its type with no restrictions.
     *
     * @return a list of the results (empty if none was found)
     */
    public List<T> findAll() {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        Root<T> root = cq.from(entityClass);

        cq.select(cq.from(entityClass));
        cq.orderBy(cb.asc(root.get("id")));

        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findInIds(Collection<Long> ids) {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);

        Root<T> root = cq.from(entityClass);

        cq.select(cq.from(entityClass));
        cq.where(root.get("id").in(ids));

        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Retrieves the total number of Entities of its type with no restrictions.
     *
     * @return the total number of entities found
     */
    public Long count() {
        CriteriaQuery<Long> cq = getEntityManager().getCriteriaBuilder().createQuery(Long.class);
        Root<T> rt = cq.from(entityClass);

        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        TypedQuery<Long> q = getEntityManager().createQuery(cq);

        return q.getSingleResult();
    }

}
