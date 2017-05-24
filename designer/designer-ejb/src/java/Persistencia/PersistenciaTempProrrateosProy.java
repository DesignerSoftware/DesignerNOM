/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TempProrrateosProy;
import InterfacePersistencia.PersistenciaTempProrrateosProyInterface;
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
public class PersistenciaTempProrrateosProy implements PersistenciaTempProrrateosProyInterface {

   @Override
   public void crear(EntityManager em, TempProrrateosProy tempAusentismos) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void editar(EntityManager em, TempProrrateosProy tempAusentismos) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void borrar(EntityManager em, TempProrrateosProy tempAusentismos) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void borrarRegistrosTempProrrateosProy(EntityManager em, String usuarioBD) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public List<TempProrrateosProy> obtenerTempProrrateosProy(EntityManager em, String usuarioBD) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   
   @Override
   public List<String> obtenerDocumentosSoporteCargados(EntityManager em, String usuarioBD) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT t.archivo FROM TempProrrateosProy t "
                 + "WHERE t.usuariobd = :usuarioBD AND t.estado = 'C'");
         query.setParameter("usuarioBD", usuarioBD);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<String> listDocumentosSoporte = query.getResultList();
         return listDocumentosSoporte;
      } catch (Exception e) {
         System.out.println("PersistenciaTempProrrateosProy.obtenerDocumentosSoporteCargados()" + e.getMessage());
         return null;
      }
   }

   @Override
   public void cargarTempProrrateosProy(EntityManager em, String fechaInicial, BigInteger secEmpresa) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         StoredProcedureQuery queryProcedure = em.createStoredProcedureQuery("TEMPPRORRATEOS_PKG.CARGAR_PRORRATEOSPROY");
         queryProcedure.execute();
         tx.commit();
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaTempProrrateosProy.cargarTempProrrateosProy()" + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void reversarTempProrrateosProy(EntityManager em, String documentoSoporte) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         Query query = em.createNativeQuery("SELECT MAX(trunc(FECHASISTEMA)) FROM TEMPPRORRATEOSPROY\n"
                 + " WHERE USUARIOBD=USER AND nvl(codigoempleadocargue,0)= NVL(0,0) AND ESTADO='C'\n"
                 + " and exists (select 'x' from empleados e where to_char(e.codigoempleado)=12138090)\n"
                 + " AND ARCHIVO = ?");
         query.setParameter(1, documentoSoporte);
         Date fecha = (Date) query.getSingleResult();
         System.out.println("PersistenciaTempProrrateosProy.reversarTempProrrateosProy() Paso1 Consulto fecha: " + fecha);
         if (fecha != null) {
            em.clear();
            StoredProcedureQuery queryProcedure = em.createStoredProcedureQuery("TEMPPRORRATEOS_PKG.REVERSAR_PRORRATEOSPROY");
            queryProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.INOUT);
            queryProcedure.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);

            queryProcedure.setParameter(1, documentoSoporte);
            queryProcedure.setParameter(2, fecha);

            queryProcedure.execute();
            queryProcedure.hasMoreResults();
            String strRetorno = (String) queryProcedure.getOutputParameterValue(1);
            System.out.println("PersistenciaTempProrrateosProy.reversarTempProrrateosProy() Paso2 Consulto strRetorno: " + strRetorno);
         }
         tx.commit();
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaTempProrrateosProy.reversarTempProrrateosProy()" + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

}
