/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.MotivosRetiros;
import Entidades.MotivosDefinitivas;
import Entidades.NovedadesSistema;
import Entidades.Vacaciones;
import InterfaceAdministrar.AdministrarNovedadesSistemaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaMotivosDefinitivasInterface;
import InterfacePersistencia.PersistenciaMotivosRetirosInterface;
import InterfacePersistencia.PersistenciaNovedadesSistemaInterface;
import InterfacePersistencia.PersistenciaVacacionesInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarNovedadesSistema implements AdministrarNovedadesSistemaInterface {

   private static Logger log = Logger.getLogger(AdministrarNovedadesSistema.class);

   @EJB
   PersistenciaNovedadesSistemaInterface persistenciaNovedades;
   @EJB
   PersistenciaMotivosDefinitivasInterface persistenciaMotivos;
   @EJB
   PersistenciaMotivosRetirosInterface persistenciaRetiros;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaVacacionesInterface persistenciaVacaciones;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private EntityManagerFactory emf;
   private EntityManager em; private String idSesionBck;

   private EntityManager getEm() {
      try {
         if (this.emf != null) { if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         } else {
            this.emf = administrarSesiones.obtenerConexionSesionEMF(idSesionBck);
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   //Trae las novedades del empleado cuya secuencia se envía como parametro//
   @Override
   public List<NovedadesSistema> novedadesEmpleado(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaNovedades.novedadesEmpleado(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error("Error AdministrarNovedadesEmpleados.novedadesEmpleado" + e);
         return null;
      }
   }

   @Override
   public void borrarNovedades(NovedadesSistema novedades) {
      try {
         persistenciaNovedades.borrar(getEm(), novedades);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarNovedades() ERROR: " + e);
      }
   }

   @Override
   public void crearNovedades(NovedadesSistema novedades) {
      try {
         persistenciaNovedades.crear(getEm(), novedades);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearNovedades() ERROR: " + e);
      }
   }

   @Override
   public void modificarNovedades(NovedadesSistema novedades) {
      try {
         persistenciaNovedades.editar(getEm(), novedades);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarNovedades() ERROR: " + e);
      }
   }

   @Override
   public List<Empleados> buscarEmpleados() {
      try {
         return persistenciaEmpleados.buscarEmpleadosActivosPensionados(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".buscarEmpleados() ERROR: " + e);
         return null;
      }
   }
//
//   public List<Empleados> lovEmpleados() {
//      try {
//         return persistenciaEmpleados.buscarEmpleadosActivosPensionados(getEm());
//      } catch (Exception e) {
//         log.error(this.getClass().getSimpleName() + ".lovEmpleados() ERROR: " + e);
//         return null;
//      }
//   }

   @Override
   public List<MotivosDefinitivas> lovMotivos() {
      try {
         return persistenciaMotivos.buscarMotivosDefinitivas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".lovMotivos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<MotivosRetiros> lovRetiros() {
      try {
         return persistenciaRetiros.consultarMotivosRetiros(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".lovRetiros() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<NovedadesSistema> vacacionesEmpleado(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaNovedades.novedadesEmpleadoVacaciones(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".vacacionesEmpleado() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<NovedadesSistema> cesantiasEmpleado(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaNovedades.novedadesEmpleadoCesantias(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".cesantiasEmpleado() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Vacaciones> periodosEmpleado(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaVacaciones.periodoVacaciones(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".periodosEmpleado() ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigDecimal valorCesantias(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaNovedades.valorCesantias(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".valorCesantias() ERROR: " + e);
         return null;
      }
   }

   @Override

   public BigDecimal valorIntCesantias(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaNovedades.valorIntCesantias(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".valorIntCesantias() ERROR: " + e);
         return null;
      }
   }

}
