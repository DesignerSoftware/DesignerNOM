/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.VigenciasRetenciones;
import InterfacePersistencia.PersistenciaVigenciasRetencionesInterface;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaVigenciasRetenciones implements PersistenciaVigenciasRetencionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasRetenciones.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public String crear(EntityManager em, VigenciasRetenciones vretenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vretenciones);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaVigenciasRetenciones.crear:  ", e);
                return e.getMessage();
            } else {
                return "Ha ocurrido un error al crear la Vigencia Retención";
            }
        }
    }

    @Override
    public String editar(EntityManager em, VigenciasRetenciones vretenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vretenciones);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaVigenciasRetenciones.editar:  ", e);
                return e.getMessage();
            } else {
                return "Ha ocurrido un error al editar la Vigencia Retención";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, VigenciasRetenciones vretenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vretenciones));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
            log.error("Error PersistenciaVigenciasRetenciones.borrar:  ", e);
                return e.getMessage();
            } else {
                return "Ha ocurrido un error al borrar la Vigencia Retención";
            }
        }
    }

    @Override
    public List<VigenciasRetenciones> buscarVigenciasRetenciones(EntityManager em) {
        try{
        em.clear();
        Query query = em.createQuery("SELECT v FROM VigenciasRetenciones v ORDER BY v.fechavigencia ASC");
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        List<VigenciasRetenciones> setsLista = (List<VigenciasRetenciones>) query.getResultList();
        return setsLista;
        }catch(Exception e){
            log.error("PersistenciaVigenciasRetenciones.buscarVigenciasRetenciones():  ", e);
            return null;
        }
    }
}
