package org.openmrs.module.muzimabiometrics.api.db.hibernate;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openmrs.module.muzimabiometrics.MuzimaFingerprint;
import org.openmrs.module.muzimabiometrics.api.db.MuzimaFingerprintDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by vikas on 15/10/14.
 */
public class HibernateMuzimaFingerprintDAO implements MuzimaFingerprintDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<MuzimaFingerprint> getAll() {
        Criteria criteria = session().createCriteria(MuzimaFingerprint.class);
        //criteria.add(Restrictions.eq("voided", false));
        return criteria.list();
    }

    @Override
    public MuzimaFingerprint saveMuzimaFingerprint(MuzimaFingerprint fingerprint) {
        try{
            session().saveOrUpdate(fingerprint);
            return fingerprint;
        } catch (Exception e){
            e.printStackTrace();
        }
        return fingerprint;
    }

    @Override
    public MuzimaFingerprint findById(Integer id) {
        return (MuzimaFingerprint) session().get(MuzimaFingerprint.class, id);
    }

    private Session session() {
        getSessionFactory().getCurrentSession().setCacheMode(CacheMode.IGNORE);
        return getSessionFactory().getCurrentSession();
    }

    public MuzimaFingerprint findByUuid(String uuid) {
        return (MuzimaFingerprint) session().createQuery("from MuzimaFingerprint m where m.uuid = '" +uuid + "'").uniqueResult();
    }

    @Override
    public MuzimaFingerprint findByPatientUUID(String patientUUID) {
        return (MuzimaFingerprint)session().createQuery("from MuzimaFingerprint m where m.patientUUID = '" + patientUUID + "'").uniqueResult();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

}
