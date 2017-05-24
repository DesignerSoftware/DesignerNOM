/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TempProrrateos;
import InterfacePersistencia.PersistenciaTempProrrateosInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class PersistenciaTempProrrateos implements PersistenciaTempProrrateosInterface {

   @Override
   public void crear(EntityManager em, TempProrrateos tempProrrateos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tempProrrateos);
         tx.commit();
      } catch (Exception e) {
         System.out.println("PersistenciaTempProrrateos.crear: ERROR: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TempProrrateos tempProrrateos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tempProrrateos);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaTempProrrateos.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TempProrrateos tempProrrateos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(tempProrrateos));
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaTempProrrateos.borrar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrarRegistrosTempProrrateos(EntityManager em, String usuarioBD) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         Query query = em.createQuery("DELETE FROM TempProrrateos t WHERE t.usuariobd =:usuarioBD AND t.estado = 'N'");
         query.setParameter("usuarioBD", usuarioBD);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         System.out.println("No se pudo borrar el registro (borrarRegistrosTempProrrateos) : " + e.toString());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<TempProrrateos> obtenerTempProrrateos(EntityManager em, String usuarioBD) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT t FROM TempProrrateos t "
                 + "WHERE t.usuariobd = :usuarioBD AND t.estado = 'N'");
         query.setParameter("usuarioBD", usuarioBD);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<TempProrrateos> listTProrrateo = query.getResultList();
         return listTProrrateo;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaTempProrrateos.obtenerTempProrrateos()" + e.getMessage());
         return null;
      }
   }

   @Override
   public List<String> obtenerDocumentosSoporteCargados(EntityManager em, String usuarioBD) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT t.archivo FROM TempProrrateos t "
                 + "WHERE t.usuariobd = :usuarioBD AND t.estado = 'C'");
         query.setParameter("usuarioBD", usuarioBD);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<String> listDocumentosSoporte = query.getResultList();
         return listDocumentosSoporte;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaTempProrrateos.obtenerDocumentosSoporteCargados()" + e.getMessage());
         return null;
      }
   }

   @Override
   public void cargarTempProrrateos(EntityManager em, String fechaInicial, BigInteger secEmpresa) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         StoredProcedureQuery queryProcedure = em.createStoredProcedureQuery("TEMPPRORRATEOS_PKG.CARGAR_PRORRATEOS");
         queryProcedure.execute();
         tx.commit();
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaTempProrrateos.cargarTempProrrateos()" + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void reversarTempProrrateos(EntityManager em, String documentoSoporte) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         Query query = em.createNativeQuery("SELECT MAX(trunc(FECHASISTEMA)) FROM TEMPPRORRATEOS\n"
                 + " WHERE USUARIOBD=USER AND nvl(codigoempleadocargue,0)= NVL(0,0) AND ESTADO='C'\n"
                 + " and exists (select 'x' from empleados e where to_char(e.codigoempleado)=12138090)\n"
                 + " AND ARCHIVO = ?");
         query.setParameter(1, documentoSoporte);
         Date fecha = (Date) query.getSingleResult();
         System.out.println("PersistenciaTempProrrateos.reversarTempProrrateos() Paso1 Consulto fecha: " + fecha);
         if (fecha != null) {
            em.clear();
            StoredProcedureQuery queryProcedure = em.createStoredProcedureQuery("TEMPPRORRATEOS_PKG.REVERSAR_PRORRATEOS");
            queryProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.INOUT);
            queryProcedure.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);

            queryProcedure.setParameter(1, documentoSoporte);
            queryProcedure.setParameter(2, fecha);

            queryProcedure.execute();
            queryProcedure.hasMoreResults();
            String strRetorno = (String) queryProcedure.getOutputParameterValue(1);
            System.out.println("PersistenciaTempProrrateos.reversarTempProrrateos() Paso2 Consulto strRetorno: " + strRetorno);
         }
         tx.commit();
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaTempProrrateos.reversarTempProrrateos()" + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

}
