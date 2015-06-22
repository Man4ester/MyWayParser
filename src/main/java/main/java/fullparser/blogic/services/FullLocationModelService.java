package main.java.fullparser.blogic.services;

import java.util.List;

import javax.transaction.Transactional;

import main.java.central.utils.HibernateUtils;
import main.java.fullparser.blogic.interfaces.IFullLocationModelService;
import main.java.fullparser.models.FullLocationModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;

@Transactional
public class FullLocationModelService implements IFullLocationModelService {
	
	private SessionFactory sessionFactory = HibernateUtils.getSessionfactory();

	private static final Logger logger = LogManager
			.getLogger(FullLocationModelService.class);

	@Override
	public FullLocationModel saveUpdate(FullLocationModel model)
			throws IllegalArgumentException {
		logger.info("saveUpdate");
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
	public FullLocationModel findById(long id) throws IllegalArgumentException {
		logger.info("findById: " + id);
		if (id == 0) {
			throw new IllegalArgumentException("id can't 0");
		}
		getSession().beginTransaction();
		try {
			return (FullLocationModel) getSession().get(FullLocationModel.class, id);
		} finally {
			getSession().close();
		}

	}

	@Override
	public List<FullLocationModel> finaAll() {
		logger.info("findAll");
		getSession().beginTransaction();
		try {
			return getSession().createQuery("from  FullLocationModel").list();
		} finally {
			getSession().close();
		}
	}

	@Override
	public List<FullLocationModel> finaByCriteria(DetachedCriteria cr, int from,
			int size) throws IllegalArgumentException {
		logger.info("finaByCriteria: From:" + from + " Size: " + size);
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
			List<FullLocationModel> res = (List<FullLocationModel>) cr
					.getExecutableCriteria(getSession()).setFirstResult(from)
					.setMaxResults(size).list();
			return res;
		} finally {
			getSession().close();
		}
	}

	@Override
	public void delete(long id) throws IllegalArgumentException,
			NullPointerException {
		logger.info("Delete by Id: " + id);
		if (id == 0) {
			throw new IllegalArgumentException("id can't be 0");
		}
		try {
			FullLocationModel dest = findById(id);
			if (dest == null) {
				throw new NullPointerException("Not exist FullLocationModel with id "
						+ id);
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

	@Override
	public void truncate() {
		logger.info("TRUNCATE");
		getSession().beginTransaction();
		try {
			String sql = "TRUNCATE FULL_LOCATION";
			getSession().createSQLQuery(sql).executeUpdate();
			getSession().getTransaction().commit();
		} finally {

		}
	}

}
