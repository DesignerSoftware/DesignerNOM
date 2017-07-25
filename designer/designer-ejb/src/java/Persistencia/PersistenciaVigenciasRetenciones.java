/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.VigenciasRetenciones;
import InterfacePersistencia.PersistenciaVigenciasRetencionesInterface;
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
public class PersistenciaVigenciasRetenciones implements PersistenciaVigenciasRetencionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasRetenciones.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public void crear(EntityManager em, VigenciasRetenciones vretenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vretenciones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasRetenciones.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasRetenciones vretenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vretenciones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasRetenciones.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasRetenciones vretenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vretenciones));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasRetenciones.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
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
            log.error("Persistencia.PersistenciaVigenciasRetenciones.buscarVigenciasRetenciones()" + e.getMessage());
            return null;
        }
    }
}
