/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Estructuras;
import InterfaceAdministrar.AdministrarReingresarEmpleadoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarReingresarEmpleado implements AdministrarReingresarEmpleadoInterface {

   private static Logger log = Logger.getLogger(AdministrarReingresarEmpleado.class);

   @EJB
   PersistenciaPersonasInterface persistenciaPersonas;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   @EJB
   AdministrarSesionesInterface administrarSesiones;
   private EntityManagerFactory emf;
   private EntityManager em;

   private EntityManager getEm() {
      try {
         if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }
   private List<Empleados> lovEmpleados;
   private List<Estructuras> lovEstructuras;
   private Date fechaDeRetiro;

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   public Date obtenerFechaRetiro(BigInteger secuenciaEmpleado) {
      try {
         fechaDeRetiro = persistenciaEmpleado.verificarFecha(getEm(), secuenciaEmpleado);
         return fechaDeRetiro;
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public void reintegrarEmpleado(BigDecimal codigoEmpleado, String centroCosto, Date fechaReingreso, short empresa, Date fechaFinal) {
      try {
         persistenciaEmpleado.reingresarEmpleado(getEm(), codigoEmpleado, centroCosto, fechaReingreso, empresa, fechaFinal);
      } catch (Exception e) {
         log.warn("ERROR - AdministrarReingresarEmpleado.reintegrarEmpleado() ERROR");
      }
   }

   public List<Empleados> listaEmpleados() {
      try {
         return persistenciaEmpleado.consultarEmpleadosReingreso(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Estructuras> listaEstructuras() {
      try {
         return persistenciaEstructuras.consultarEstructurasReingreso(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}
