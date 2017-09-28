/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.ConexionesKioskos;
import Entidades.Empleados;
import Entidades.EmpleadosAux;
import InterfacePersistencia.PersistenciaKioAdminInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaKioAdmin implements PersistenciaKioAdminInterface {

   private static Logger log = Logger.getLogger(PersistenciaKioAdmin.class);

   @Override
   public ConexionesKioskos conexionesKioskos(EntityManager em, BigInteger secEmpleado) {
      try {
         em.clear();
         String sqlQuery = "SELECT * FROM CONEXIONESKIOSKOS CK WHERE EMPLEADO = ? ";
         Query query = em.createNativeQuery(sqlQuery, ConexionesKioskos.class);
         query.setParameter(1, secEmpleado);
         ConexionesKioskos listaCK;
         listaCK = (ConexionesKioskos) query.getSingleResult();
         return listaCK;
      } catch (NoResultException e) {
         log.error(this.getClass().getName() + " error en conexionesKioskos():  ", e);
         return null;
      }
   }

   @Override
   public List<Empleados> consultarEmpleadosCK(EntityManager em) {
      try {
         em.clear();
         String sqlQuery = "SELECT * FROM EMPLEADOS E\n"
                 + " WHERE EXISTS (SELECT 'X' FROM CONEXIONESKIOSKOS CK WHERE CK.EMPLEADO=E.SECUENCIA)";
         Query query = em.createNativeQuery(sqlQuery, Empleados.class
         );
         List<Empleados> listaEmpleados;
         listaEmpleados = query.getResultList();
         if (listaEmpleados != null) {
            if (!listaEmpleados.isEmpty()) {
               em.clear();
               Query query2 = em.createNativeQuery("SELECT E.SECUENCIA, P.PATHFOTO, P.NOMBRE NOMBREPERSONA, P.PRIMERAPELLIDO, P.SEGUNDOAPELLIDO,\n"
                       + " P.EMAIL, P.NUMERODOCUMENTO, EM.NOMBRE NOMBREEMPRESA, EM.RETENCIONYSEGSOCXPERSONA RETENCIONYSEGSOCXPERSONA\n"
                       + " FROM EMPLEADOS E, EMPRESAS EM, PERSONAS P\n"
                       + " WHERE E.PERSONA = P.SECUENCIA AND E.EMPRESA = EM.SECUENCIA AND EXISTS (SELECT 'X' FROM CONEXIONESKIOSKOS CK WHERE CK.EMPLEADO = E.SECUENCIA)", EmpleadosAux.class);
               List<EmpleadosAux> listaEmpleadosAux = query2.getResultList();
               log.warn("PersistenciaKioAdmin.consultarEmpleadosCK() Ya consulo Transients");
               if (listaEmpleadosAux != null) {
                  if (!listaEmpleadosAux.isEmpty()) {
                     log.warn("PersistenciaKioAdmin.consultarEmpleadosCK() listaEmpleadosAux.size(): " + listaEmpleadosAux.size());
                     for (EmpleadosAux recAux : listaEmpleadosAux) {
                        for (Empleados recEmpleado : listaEmpleados) {
                           if (recAux.getSecuencia().equals(recEmpleado.getSecuencia())) {
                              recEmpleado.llenarTransients(recAux);
                           }
                        }
                     }
                  }
               }
            }
         }
         return listaEmpleados;
      } catch (Exception e) {
         log.error(this.getClass().getName() + " error en consultarEmpleadosCK()");
         e.printStackTrace();
         return null;
      }
   }

   @Override
   public void modificarck(EntityManager em, ConexionesKioskos ck) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(ck);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaKioAdmin.modificarck:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void resetUsuario(EntityManager em, BigInteger secEmpleado) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sqlQuery = "DELETE FROM CONEXIONESKIOSKOS WHERE EMPLEADO = ? ";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secEmpleado);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error(this.getClass().getName() + " error en resetUsuario()");
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void unlockUsuario(EntityManager em, BigInteger secEmpleado) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         String sqlQuery = "UPDATE CONEXIONESKIOSKOS SET ACTIVO='S' WHERE EMPLEADO = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secEmpleado);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         log.error(this.getClass().getName() + " error en unlockUsuario()");
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

}
