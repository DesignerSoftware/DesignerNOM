/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TempProrrateos;
import Entidades.TempProrrateosAux;
import InterfacePersistencia.PersistenciaTempProrrateosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
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
public class PersistenciaTempProrrateos implements PersistenciaTempProrrateosInterface {

   private static Logger log = Logger.getLogger(PersistenciaTempProrrateos.class);

   @Override
   public void crear(EntityManager em, TempProrrateos tempAusentismos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tempAusentismos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTempProrrateos.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TempProrrateos tempAusentismos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tempAusentismos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTempProrrateos.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TempProrrateos tempAusentismos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(tempAusentismos));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTempProrrateos.borrar:  ", e);
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
         String sql = "DELETE FROM TEMPPRORRATEOS WHERE USUARIOBD = ? AND ESTADO = 'N'";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, usuarioBD);
         query.executeUpdate();
         log.warn("PersistenciaTempProrrateos.borrarRegistrosTempProrrateos() Ya ejecuto");
         tx.commit();
      } catch (Exception e) {
         log.error("No se pudo borrar el registro en borrarRegistrosTempProrrateos() :  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<TempProrrateos> obtenerTempProrrateos(EntityManager em, String usuarioBD) {
      try {
         String sql = "SELECT * FROM TEMPPRORRATEOS WHERE USUARIOBD = ? AND NVL(ESTADO,'N') <> 'C'";
         em.clear();
         Query query = em.createNativeQuery(sql, TempProrrateos.class);
         query.setParameter(1, usuarioBD);
         List<TempProrrateos> listTNovedades = query.getResultList();
         if (listTNovedades != null) {
            if (!listTNovedades.isEmpty()) {
               sql = "SELECT T.SECUENCIA, \n"
                       + "(SELECT P.NOMBRE||' '||P.PRIMERAPELLIDO||' '||P.SEGUNDOAPELLIDO FROM EMPLEADOS E, PERSONAS P \n"
                       + "WHERE E.CODIGOEMPLEADO = T.CODIGOEMPLEADO AND E.PERSONA = P.SECUENCIA AND ROWNUM < 2) NOMBREEMPLEADO, \n"
                       + "(SELECT CC.NOMBRE FROM CENTROSCOSTOS CC \n"
                       + "WHERE CC.CODIGO = T.CENTROCOSTO AND ROWNUM < 2) NOMBRECENTROCOSTO, \n"
                       + "PROY.NOMBREPROYECTO NOMBREPROYECTO \n"
                       + "FROM TEMPPRORRATEOS T, PROYECTOS PROY \n"
                       + "WHERE USUARIOBD = ? AND NVL(T.ESTADO,'N') <> 'C' \n"
                       + "AND T.PROYECTO = PROY.CODIGO(+)";
               em.clear();
               Query query2 = em.createNativeQuery(sql, TempProrrateosAux.class);
               query2.setParameter(1, usuarioBD);
               List<TempProrrateosAux> listAux = query2.getResultList();
               if (listAux != null) {
                  if (!listAux.isEmpty()) {
                     for (TempProrrateosAux recAux : listAux) {
                        for (TempProrrateos recProrrateo : listTNovedades) {
                           if (recProrrateo.getSecuencia().equals(recAux.getSecuencia())) {
                              recProrrateo.setNombreEmpleado(recAux.getNombreEmpleado());
                              recProrrateo.setNombreProyecto(recAux.getNombreProyecto());
                              recProrrateo.setNombreCentrocosto(recAux.getNombreCentrocosto());
                           }
                        }
                     }
                  }
               }
            }
         }
         return listTNovedades;
      } catch (Exception e) {
         log.error("PersistenciaTempProrrateos.obtenerTempProrrateos():  ", e);
         return null;
      }
   }

   @Override
   public List<String> obtenerDocumentosSoporteCargados(EntityManager em) {
      try {
         em.clear();
         Query query = em.createNativeQuery("SELECT t.archivo FROM TempProrrateos t "
                 + "WHERE t.usuariobd = USER AND t.estado = 'C'");
//         query.setParameter("usuarioBD", usuarioBD);
//         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<String> listDocumentosSoporte = query.getResultList();
         return listDocumentosSoporte;
      } catch (Exception e) {
         log.error("PersistenciaTempProrrateos.obtenerDocumentosSoporteCargados():  ", e);
         return null;
      }
   }

   @Override
//   public void cargarTempProrrateos(EntityManager em, String fechaInicial, BigInteger secEmpresa) {
   public void cargarTempProrrateos(EntityManager em) {
      log.info("PersistenciaTempProrrateos.cargarTempProrrateos()");
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         StoredProcedureQuery queryProcedure = em.createStoredProcedureQuery("PRODUCCION.TEMPPRORRATEOS_PKG.CARGAR_PRORRATEOS");
         queryProcedure.execute();
         log.info("PersistenciaTempProrrateos.cargarTempProrrateos() Ya ejecuto PRODUCCION.TEMPPRORRATEOS_PKG.CARGAR_PRORRATEOS");
         tx.commit();
      } catch (Exception e) {
         log.error("PersistenciaTempProrrateos.cargarTempProrrateos():  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public int reversarTempProrrateos(EntityManager em, String usuario, String documentoSoporte) {
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
         log.warn("PersistenciaTempProrrateos.reversarTempProrrateos() Paso1 Consulto fecha: " + fecha);
         if (fecha != null) {
            em.clear();
            StoredProcedureQuery queryProcedure = em.createStoredProcedureQuery("PRODUCCION.TEMPPRORRATEOS_PKG.REVERSAR_PRORRATEOS");
            queryProcedure.registerStoredProcedureParameter(1, String.class, ParameterMode.INOUT);
            queryProcedure.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);

            queryProcedure.setParameter(1, documentoSoporte);
            queryProcedure.setParameter(2, fecha);

            queryProcedure.execute();
            queryProcedure.hasMoreResults();
            String strRetorno = (String) queryProcedure.getOutputParameterValue(1);
            log.warn("PersistenciaTempProrrateos.reversarTempProrrateos() Paso2 Consulto strRetorno: " + strRetorno);
         }
         tx.commit();
         return 1;
      } catch (Exception e) {
         log.error("PersistenciaTempProrrateos.reversarTempProrrateos():  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
         return 0;
      }
   }

   @Override
   public boolean verificarCodigoCentroCosto_Empresa(EntityManager em, BigInteger codigoCC, BigInteger secEmpresa) {
      try {
         em.clear();
         Query query = em.createNativeQuery("SELECT COUNT(*) FROM CENTROSCOSTOS C WHERE C.codigo = ? AND C.empresa = ?");
         query.setParameter(1, codigoCC);
         query.setParameter(2, secEmpresa);
         BigDecimal n = (BigDecimal) query.getSingleResult();
         return n.intValue() > 0;
      } catch (Exception e) {
         log.error("PersistenciaEmpleados.verificarCodigoCentroCosto_Empresa() e:  ", e);
         return false;
      }
   }

   @Override
   public boolean verificarCodigoProyecto_Empresa(EntityManager em, String codigoProy, BigInteger secEmpresa) {
      try {
         em.clear();
         Query query = em.createNativeQuery("SELECT COUNT(*) FROM PROYECTOS C WHERE C.codigo = ? AND C.empresa = ?");
         query.setParameter(1, codigoProy);
         query.setParameter(2, secEmpresa);
         BigDecimal n = (BigDecimal) query.getSingleResult();
         return n.intValue() > 0;
      } catch (Exception e) {
         log.error("PersistenciaEmpleados.verificarCodigoProyecto_Empresa() e:  ", e);
         return false;
      }
   }

}
