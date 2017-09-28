/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Papeles;
import InterfacePersistencia.PersistenciaPapelesInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaPapeles implements PersistenciaPapelesInterface {

   private static Logger log = Logger.getLogger(PersistenciaPapeles.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    public void crear(EntityManager em, Papeles papel) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(papel);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPapeles.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, Papeles papel) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(papel);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPapeles.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, Papeles papel) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(papel));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPapeles.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public List<Papeles> consultarPapeles(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Papeles p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Papeles> listaPapeles = (List<Papeles>) query.getResultList();
            return listaPapeles;
        } catch (Exception e) {
            log.error("ERROR PERSISTENCIAPAPELES AL CONSULTARPAPELES ERROR :  ", e);
            return null;
        }
    }

    public Papeles consultarPapel(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT papelillo FROM Papeles papelillo WHERE papelillo.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Papeles papel = (Papeles) query.getSingleResult();
            return papel;
        } catch (Exception e) {
            log.error("ERROR PERSISTENCIAPAPELES AL CONSULTARPAPEL ", e);
            Papeles papel = null;
            return papel;
        }
    }

    public List<Papeles> consultarPapelesEmpresa(EntityManager em, BigInteger secEmpresa) {
        try {
            em.clear();
            String sql="SELECT * FROM PAPELES WHERE EMPRESA = ?";
            Query query = em.createNativeQuery(sql, Papeles.class);
            query.setParameter(1, secEmpresa);
            List<Papeles> listPapeles = query.getResultList();
            return listPapeles;
        } catch (Exception e) {
            log.error("ERROR PERSISTENCIAPAPELES AL CONSULTARPAPELESEMPRESA ERROR  ", e);
            return null;
        }
    }

    public BigInteger contarVigenciasCargosPapel(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM vigenciascargos WHERE papel = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            return new BigInteger(query.getSingleResult().toString());

        } catch (Exception e) {
            log.error("ERROR PERSISTENCIAPAPELES CONTARVIGENCIASCARGOSPAPEL ERROR :  ", e);
            return null;
        }
    }
        
   @Override
   public void adicionaPapelCambiosMasivos(EntityManager em, BigInteger secPapel, Date fechaCambio) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         StoredProcedureQuery query = em.createStoredProcedureQuery("CAMBIOSMASIVOS_PKG.AdicionaPapel");
         query.registerStoredProcedureParameter(1, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);

         query.setParameter(1, secPapel);
         query.setParameter(2, fechaCambio);
         query.execute();
      } catch (Exception e) {
         log.error(this.getClass().getName() + ".adicionaPapelCambiosMasivos() ERROR:  ", e);
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
      } finally {
         tx.commit();
      }
   }
}
