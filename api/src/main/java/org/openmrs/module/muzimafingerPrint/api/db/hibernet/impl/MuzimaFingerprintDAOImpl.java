package org.openmrs.module.muzimafingerPrint.api.db.hibernet.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openmrs.module.muzimafingerPrint.MuzimaFingerprint;
import org.openmrs.module.muzimafingerPrint.api.db.hibernet.MuzimaFingerprintDAO;


import java.util.List;

/**
 * Created by vikas on 15/10/14.
 */
public class MuzimaFingerprintDAOImpl implements MuzimaFingerprintDAO {

    private SessionFactory factory;

    public MuzimaFingerprintDAOImpl(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<MuzimaFingerprint> getAll() {
        Criteria criteria = session().createCriteria(MuzimaFingerprint.class);
        //criteria.add(Restrictions.eq("voided", false));
        return criteria.list();
    }

    @Override
    public void saveMuzimaFingerprint(MuzimaFingerprint fingerprint) {
        try{
        session().saveOrUpdate(fingerprint);}
        catch (Exception e){
            session().save(fingerprint);
        }
    }

    @Override
    public MuzimaFingerprint findById(Integer id) {
        return (MuzimaFingerprint) session().get(MuzimaFingerprint.class, id);
    }

    private Session session() {
        return factory.getCurrentSession();
    }

    public MuzimaFingerprint findByUuid(String uuid) {
        return (MuzimaFingerprint) session().createQuery("from MuzimaFingerprint m where m.uuid = '" +uuid + "'").uniqueResult();
    }

    @Override
    public MuzimaFingerprint findByPatientUUID(String patientUUID) {

        return (MuzimaFingerprint)session().createQuery("from MuzimaFingerprint m where m.patientUUID = '" + patientUUID + "'").uniqueResult();
    }


}
