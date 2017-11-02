/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TempProrrateosProy;
import Entidades.TempProrrateosProyAux;
import InterfacePersistencia.PersistenciaTempProrrateosProyInterface;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
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
@LocalBean
public class PersistenciaTempProrrateosProy implements PersistenciaTempProrrateosProyInterface {

   private static Logger log = Logger.getLogger(PersistenciaTempProrrateosProy.class);

   @Override
   public void crear(EntityManager em, TempProrrateosProy tempAusentismos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tempAusentismos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTempProrrateosProy.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TempProrrateosProy tempAusentismos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tempAusentismos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTempProrrateosProy.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TempProrrateosProy tempAusentismos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(tempAusentismos));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTempProrrateosProy.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrarRegistrosTempProrrateosProy(EntityManager em, String usuarioBD) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sql = "DELETE FROM TEMPPRORRATEOSPROY WHERE USUARIOBD = ? AND ESTADO = 'N'";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, usuarioBD);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error("No se pudo borrar el registro en borrarRegistrosTempProrrateosProy() :  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<TempProrrateosProy> obtenerTempProrrateosProy(EntityManager em, String usuarioBD) {
      try {
         String sql = "SELECT * FROM TEMPPRORRATEOSPROY WHERE USUARIOBD = ? AND NVL(ESTADO,'N') <> 'C'";
         em.clear();
         Query query = em.createNativeQuery(sql, TempProrrateosProy.class);
         query.setParameter(1, usuarioBD);
         List<TempProrrateosProy> listTNovedades = query.getResultList();
         if (listTNovedades != null) {
            if (!listTNovedades.isEmpty()) {
               sql = "SELECT T.SECUENCIA,\n"
                       + " (SELECT P.NOMBRE||' '||P.PRIMERAPELLIDO||' '||P.SEGUNDOAPELLIDO FROM EMPLEADOS E, PERSONAS P\n"
                       + " WHERE E.CODIGOEMPLEADO = T.CODIGOEMPLEADO AND E.PERSONA = P.SECUENCIA AND ROWNUM < 2) NOMBREEMPLEADO,\n"
                       + " PROY.NOMBREPROYECTO NOMBREPROYECTO\n"
                       + " FROM TEMPPRORRATEOSPROY T, PROYECTOS PROY\n"
                       + " WHERE T.USUARIOBD = ? AND NVL(T.ESTADO,'N') <> 'C'\n"
                       + " AND T.PROYECTO = PROY.CODIGO";
               em.clear();
               Query query2 = em.createNativeQuery(sql, TempProrrateosProyAux.class);
               query2.setParameter(1, usuarioBD);
               List<TempProrrateosProyAux> listAux = query2.getResultList();
               if (listAux != null) {
                  if (!listAux.isEmpty()) {
                     for (TempProrrateosProyAux recAux : listAux) {
                        for (TempProrrateosProy recProrrateo : listTNovedades) {
                           if (recProrrateo.getSecuencia().equals(recAux.getSecuencia())) {
                              recProrrateo.setNombreEmpleado(recAux.getNombreEmpleado());
                              recProrrateo.setNombreProyecto(recAux.getNombreProyecto());
                           }
                        }
                     }
                  }
               }
            }
         }
         return listTNovedades;
      } catch (Exception e) {
         log.error("PersistenciaTempProrrateosProy.obtenerTempProrrateosProy():  ", e);
         return null;
      }
   }

   @Override
   public List<String> obtenerDocumentosSoporteCargados(EntityManager em) {
      try {
         em.clear();
         Query query = em.createNativeQuery("SELECT t.archivo FROM TempProrrateosProy t "
                 + "WHERE t.usuariobd = USER AND t.estado = 'C'");
//         query.setParameter("usuarioBD", usuarioBD);
//         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<String> listDocumentosSoporte = query.getResultList();
         return listDocumentosSoporte;
      } catch (Exception e) {
         log.error("PersistenciaTempProrrateosProy.obtenerDocumentosSoporteCargados():  ", e);
         return null;
      }
   }

   @Override
//   public void cargarTempProrrateosProy(EntityManager em, String fechaInicial, BigInteger secEmpresa) {
   public void cargarTempProrrateosProy(EntityManager em) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         StoredProcedureQuery queryProcedure = em.createStoredProcedureQuery("PRODUCCION.TEMPPRORRATEOS_PKG.CARGAR_PRORRATEOSPROY");
         queryProcedure.execute();
         tx.commit();
      } catch (Exception e) {
         log.error("PersistenciaTempProrrateosProy.cargarTempProrrateosProy():  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public int reversarTempProrrateosProy(EntityManager em, String usuario, String documentoSoporte) {
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
         log.warn("PersistenciaTempProrrateosProy.reversarTempProrrateosProy() Paso1 Consulto fecha: " + fecha);
         if (fecha != null) {
            em.clear();
            StoredProcedureQuery queryProcedure = em.createStoredProcedureQuery("PRODUCCION.TEMPPRORRATEOS_PKG.REVERSAR_PRORRATEOSPROY");
            queryProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.INOUT);
            queryProcedure.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);

            queryProcedure.setParameter(1, documentoSoporte);
            queryProcedure.setParameter(2, fecha);

            queryProcedure.execute();
            queryProcedure.hasMoreResults();
            String strRetorno = (String) queryProcedure.getOutputParameterValue(1);
            log.warn("PersistenciaTempProrrateosProy.reversarTempProrrateosProy() Paso2 Consulto strRetorno: " + strRetorno);
         }
         tx.commit();
         return 1;
      } catch (Exception e) {
         log.error("PersistenciaTempProrrateosProy.reversarTempProrrateosProy():  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
         return 0;
      }
   }

}
