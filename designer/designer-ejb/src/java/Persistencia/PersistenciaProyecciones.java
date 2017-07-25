/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Proyecciones;
import InterfacePersistencia.PersistenciaProyeccionesInterface;
import java.math.BigInteger;
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
public class PersistenciaProyecciones implements PersistenciaProyeccionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaProyecciones.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    public void crear(EntityManager em, Proyecciones proyectos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(proyectos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaProyecciones.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, Proyecciones proyectos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(proyectos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaProyecciones.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, Proyecciones proyectos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(proyectos));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaProyecciones.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public List<Proyecciones> consultarProyecciones(EntityManager em, BigInteger secEmpleado) {
        List<Proyecciones> lista;
        try {
            em.clear();
            String sql = "SELECT * FROM proyecciones p WHERE p.empleado = NVL(?,p.empleado)";
            Query query = em.createNativeQuery(sql, Proyecciones.class);
            query.setParameter(1, secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            lista = query.getResultList();
        } catch (Exception e) {
            lista = null;
            log.error("PersistenciaProyecciones consultarProyecciones ERROR : " + e.getMessage());
        }
        return lista;
    }

}
