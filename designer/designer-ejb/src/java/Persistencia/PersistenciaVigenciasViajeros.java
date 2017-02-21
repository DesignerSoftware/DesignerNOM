/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.VigenciasViajeros;
import InterfacePersistencia.PersistenciaVigenciasViajerosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaVigenciasViajeros implements PersistenciaVigenciasViajerosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    public void crear(EntityManager em, VigenciasViajeros vigenciaViajero) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciaViajero);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaVigenciasViajeros.crear()" + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, VigenciasViajeros vigenciaViajero) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciaViajero);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaVigenciasViajeros.editar()" + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, VigenciasViajeros vigenciaViajero) {

        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciaViajero));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaVigenciasViajeros.borrar()" + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }

    }

    public VigenciasViajeros consultarTipoExamen(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(VigenciasViajeros.class, secuencia);
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaVigenciasViajeros.consultarTipoExamen()" + e.getMessage());
            return null;
        }
    }

    public List<VigenciasViajeros> consultarVigenciasViajerosPorEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vrl FROM VigenciasViajeros vrl WHERE vrl.empleado.secuencia = :secuenciaEmpl ORDER BY vrl.fechavigencia DESC");
            query.setParameter("secuenciaEmpl", secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasViajeros> vigenciasViajeros = (List<VigenciasViajeros>) query.getResultList();
            if (vigenciasViajeros != null) {
                System.out.println("TIPO VIAJERO: " + vigenciasViajeros.get(0).getTipoViajero().getNombre());
            }
            return vigenciasViajeros;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaVigenciasViajeros.consultarVigenciasViajerosPorEmpleado()" + e.getMessage());
            return null;
        }
    }

    public List<VigenciasViajeros> consultarVigenciasViajeros(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT te FROM VigenciasViajeros te ORDER BY te.fechavigencia ASC ");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasViajeros> listMotivosDemandas = query.getResultList();
            return listMotivosDemandas;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaVigenciasViajeros.consultarVigenciasViajeros()" + e.getMessage());
            return null;
        }
    }
}
