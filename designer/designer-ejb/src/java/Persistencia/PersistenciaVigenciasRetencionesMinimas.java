/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.VigenciasRetencionesMinimas;
import InterfacePersistencia.PersistenciaVigenciasRetencionesMinimasInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaVigenciasRetencionesMinimas implements PersistenciaVigenciasRetencionesMinimasInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasRetencionesMinimas.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public void crear(EntityManager em, VigenciasRetencionesMinimas vretenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vretenciones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasRetencionesMinimas.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasRetencionesMinimas vretenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vretenciones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasRetencionesMinimas.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasRetencionesMinimas vretenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vretenciones));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasRetencionesMinimas.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<VigenciasRetencionesMinimas> buscarVigenciasRetencionesMinimas(EntityManager em) {
        try{
        em.clear();
        Query query = em.createQuery("SELECT v FROM VigenciasRetencionesMinimas v ORDER BY v.fechavigencia");
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        List<VigenciasRetencionesMinimas> setsLista = (List<VigenciasRetencionesMinimas>) query.getResultList();
        return setsLista;
        }catch(Exception e){
            log.error("PersistenciaVigenciasRetencionesMinimas.buscarVigenciasRetencionesMinimas():  ", e);
         return null;   
        }
    }
}
