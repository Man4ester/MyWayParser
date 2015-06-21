package main.java.central.blogic.services;

import java.util.List;

import javax.transaction.Transactional;

import main.java.central.blogic.interfaces.IDestinationService;
import main.java.central.model.Destination;
import main.java.central.utils.HibernateUtils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;

@Transactional
public class DestinationService implements IDestinationService {

	private SessionFactory sessionFactory = HibernateUtils.getSessionfactory();

	@Override
	public Destination saveUpdate(Destination model) throws IllegalArgumentException{
		if (model == null) {
			throw new IllegalArgumentException(
					"Destination can't be NULL for saving");
		}
		getSession().beginTransaction();
		try {
			if (model.getId() == null) {
				getSession().persist(model);
			} else {
				getSession().merge(model);
			}
			getSession().getTransaction().commit();
		} finally {
			getSession().close();
		}
		return model;

	}

	@Override
	public Destination findById(int id) throws IllegalArgumentException {
		if (id == 0) {
			throw new IllegalArgumentException("id can't 0");
		}
		getSession().beginTransaction();
		try {
			return (Destination) getSession().get(Destination.class, id);
		} finally {
			getSession().close();
		}

	}

	@Override
	public List<Destination> finaAll() {
		getSession().beginTransaction();
		try {
			return getSession().createQuery("from  Destination").list();
		} finally {
			getSession().close();
		}
	}

	@Override
	public List<Destination> finaByCriteria(DetachedCriteria cr, int from,
			int size) throws IllegalArgumentException {
		try {
			getSession().beginTransaction();
			if (cr == null) {
				throw new IllegalArgumentException("criteria can't be null");
			}
			if (from < 0) {
				throw new IllegalArgumentException("firstResult can't be < 0");
			}

			if (size < 0) {
				throw new IllegalArgumentException("maxResults can't be < 0");
			}
			List<Destination> res = (List<Destination>) cr
					.getExecutableCriteria(getSession()).setFirstResult(from)
					.setMaxResults(size).list();
			return res;
		} finally {
			getSession().close();
		}
	}

	@Override
	public void delete(int id) throws IllegalArgumentException, NullPointerException{
		if (id == 0) {
			throw new IllegalArgumentException("id can't be 0");
		}
		try {
			Destination dest = findById(id);
			if(dest ==null){
				throw new NullPointerException("Not exist Destination with id " + id);
			}
			getSession().beginTransaction();
			getSession().delete(dest);
			getSession().getTransaction().commit();
		} finally {
			getSession().close();
		}

	}

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

}
