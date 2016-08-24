/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Persistencia;

import Entidades.DetallesTiposCotizantes;
import InterfacePersistencia.PersistenciaDetallesTiposCotizantesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'DetallesTiposCotizantes' de la
 * base de datos.
 *
 * @author Victor Algarin
 */
@Stateless
public class PersistenciaDetallesTiposCotizantes implements PersistenciaDetallesTiposCotizantesInterface{

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
   /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/
    
    @Override
    public void crear(EntityManager em,DetallesTiposCotizantes detallesTiposCotizantes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(detallesTiposCotizantes);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaDetallesTiposCotizantes.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em,DetallesTiposCotizantes detallesTiposCotizantes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(detallesTiposCotizantes);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaDetallesTiposCotizantes.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em,DetallesTiposCotizantes detallesTiposCotizantes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(detallesTiposCotizantes));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaDetallesTiposCotizantes.borrar: " + e);
            }
        }
    }
    
    @Override
    public List<DetallesTiposCotizantes> detallesTiposCotizantes(EntityManager em,BigInteger tipoCotizante) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT d FROM DetallesTiposCotizantes d WHERE d.tipocotizante.secuencia = :tipoCotizante");
            query.setParameter("tipoCotizante", tipoCotizante);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<DetallesTiposCotizantes> listaDetalles = query.getResultList();
            return listaDetalles;
        } catch (Exception e) {
            System.out.println("Exepcion en PersistenciaDetallesTiposCotizantes.detallesTiposCotizantes" + e);
            return null;
        }
    }

    
    
    
}
