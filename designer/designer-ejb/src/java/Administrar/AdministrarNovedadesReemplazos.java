/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Cargos;
import Entidades.Empleados;
import Entidades.Encargaturas;
import Entidades.Estructuras;
import Entidades.MotivosReemplazos;
import Entidades.TiposReemplazos;
import InterfaceAdministrar.AdministrarNovedadesReemplazosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEncargaturasInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaMotivosReemplazosInterface;
import InterfacePersistencia.PersistenciaTiposReemplazosInterface;
import java.math.BigInteger;
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
public class AdministrarNovedadesReemplazos implements AdministrarNovedadesReemplazosInterface {

   private static Logger log = Logger.getLogger(AdministrarNovedadesReemplazos.class);

   @EJB
   PersistenciaEncargaturasInterface persistenciaEncargaturas;
   @EJB
   PersistenciaMotivosReemplazosInterface persistenciaMotivosReemplazos;
   @EJB
   PersistenciaTiposReemplazosInterface persistenciaTiposReemplazos;
   @EJB
   PersistenciaCargosInterface persistenciaCargos;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
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

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   //Trae las encargaturas del empleado cuya secuencia se envía como parametro//
   @Override
   public List<Encargaturas> encargaturasEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaEncargaturas.encargaturasEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error("Error AdministrarNovedadesReemplazos.encargaturasEmpleado" + e);
         return null;
      }
   }

   @Override
   public Empleados encontrarEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaEmpleados.buscarEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //Listas de Tipos Reemplazos, Motivos Reemplazos, Estructuras, Cargos
   @Override
   public List<Empleados> lovEmpleados() {
      try {
         return persistenciaEmpleados.buscarEmpleados(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposReemplazos> lovTiposReemplazos() {
      try {
         return persistenciaTiposReemplazos.buscarTiposReemplazos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<MotivosReemplazos> lovMotivosReemplazos() {
      try {
         return persistenciaMotivosReemplazos.motivosReemplazos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Estructuras> lovEstructuras() {
      try {
         return persistenciaEstructuras.estructuras(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Cargos> lovCargos() {
      try {
         return persistenciaCargos.cargosSalario(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   /*Toca Arreglarlo con el Native Query
     @Override
     public List<Cargos> lovCargos() {
     return persistenciaCargos.cargos();
     }*/
   @Override
   public void modificarEncargatura(List<Encargaturas> listaEncargaturasModificar) {
      try {
         for (int i = 0; i < listaEncargaturasModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaEncargaturasModificar.get(i).getCargo().getSecuencia() == null) {
               listaEncargaturasModificar.get(i).setCargo(null);
            }
            if (listaEncargaturasModificar.get(i).getMotivoreemplazo().getSecuencia() == null) {
               listaEncargaturasModificar.get(i).setMotivoreemplazo(null);
            }
            if (listaEncargaturasModificar.get(i).getReemplazado().getSecuencia() == null) {
               listaEncargaturasModificar.get(i).setReemplazado(null);
            }
            if (listaEncargaturasModificar.get(i).getEstructura().getSecuencia() == null) {
               listaEncargaturasModificar.get(i).setEstructura(null);
            }
            persistenciaEncargaturas.editar(getEm(), listaEncargaturasModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarEncargaturas(Encargaturas encargaturas) {
      try {
         persistenciaEncargaturas.borrar(getEm(), encargaturas);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearEncargaturas(Encargaturas encargaturas) {
      try {
         persistenciaEncargaturas.crear(getEm(), encargaturas);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<Empleados> buscarEmpleadoReemplazosHV(BigInteger secEmpleado) {
      try {
         return persistenciaEmpleados.empleadosReemplazosHV(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> buscarEmpleados() {
      try {
         return persistenciaEmpleados.buscarEmpleados(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}
