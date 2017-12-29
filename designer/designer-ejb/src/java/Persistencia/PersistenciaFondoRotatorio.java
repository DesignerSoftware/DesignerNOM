/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.FondosRotatorios;
import InterfacePersistencia.PersistenciaFondoRotatorioInterface;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaFondoRotatorio implements PersistenciaFondoRotatorioInterface {

    private static Logger log = Logger.getLogger(PersistenciaTiposAuxilios.class);

    @Override
    public List<FondosRotatorios> consultarFondoRotatorio(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT fr FROM FondosRotatorios fr ORDER BY fr.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<FondosRotatorios> listaFondo = query.getResultList();
            return listaFondo;
        } catch (Exception e) {
            log.error("PersistenciaTiposAuxilios.buscarTiposAuxilios():  ", e);
            return null;
        }
    }

    @Override
    public String crear(EntityManager em, FondosRotatorios fondoRotatorio) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(fondoRotatorio);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaFondoRotatorio.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al crear el Fondo Rotatorio";
            }
        }
    }

    @Override
    public String editar(EntityManager em, FondosRotatorios fondoRotatorio) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(fondoRotatorio);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposAuxilios.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al editar el Tipo Auxilio";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, FondosRotatorios fondoRotatorio) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(fondoRotatorio));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaTiposAuxilios.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al borrar el Tipo Auxilio";
            }
        }
    }

}
