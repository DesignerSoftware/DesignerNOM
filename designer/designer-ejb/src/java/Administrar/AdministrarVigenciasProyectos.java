/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Cargos;
import Entidades.Empleados;
import Entidades.Proyectos;
import Entidades.PryRoles;
import Entidades.VigenciasProyectos;
import InterfaceAdministrar.AdministrarVigenciasProyectosInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaProyectosInterface;
import InterfacePersistencia.PersistenciaPryRolesInterface;
import InterfacePersistencia.PersistenciaVigenciasProyectosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarVigenciasProyectos implements AdministrarVigenciasProyectosInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasProyectos.class);

   @EJB
   PersistenciaVigenciasProyectosInterface persistenciaVigenciasProyectos;
   @EJB
   PersistenciaPersonasInterface persistenciaPersonas;
   @EJB
   PersistenciaProyectosInterface persistenciaProyectos;
   @EJB
   PersistenciaPryRolesInterface persistenciaPryRoles;
   @EJB
   PersistenciaCargosInterface persistenciaCargos;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private VigenciasProyectos vP;
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

   @Override
   public List<VigenciasProyectos> vigenciasProyectosEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasProyectos.vigenciasProyectosEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error("Error AdministrarVigenciasProyectos.vigenciasProyectosPersona " + e);
         return null;
      }
   }

   public Empleados encontrarEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaEmpleados.buscarEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //Listas de Valores Educacion, Profesion, Instituciones, Adiestramiento
   @Override
   public List<Proyectos> lovProyectos() {
      try {
         return persistenciaProyectos.proyectos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<PryRoles> lovPryRoles() {
      try {
         return persistenciaPryRoles.pryroles(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Cargos> lovCargos() {
      try {
         return persistenciaCargos.consultarCargos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificarVigenciaProyecto(List<VigenciasProyectos> listaVigenciasProyectosModificar) {
      try {
         for (int i = 0; i < listaVigenciasProyectosModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaVigenciasProyectosModificar.get(i).getProyecto().getSecuencia() == null) {
               listaVigenciasProyectosModificar.get(i).setProyecto(null);
               vP = listaVigenciasProyectosModificar.get(i);
            } else {
               vP = listaVigenciasProyectosModificar.get(i);
            }
            if (listaVigenciasProyectosModificar.get(i).getPryRol().getSecuencia() == null) {
               listaVigenciasProyectosModificar.get(i).setPryRol(null);
               vP = listaVigenciasProyectosModificar.get(i);
            } else {
               vP = listaVigenciasProyectosModificar.get(i);
            }
            if (listaVigenciasProyectosModificar.get(i).getPryCargoproyecto().getSecuencia() == null) {
               listaVigenciasProyectosModificar.get(i).setPryCargoproyecto(null);
               vP = listaVigenciasProyectosModificar.get(i);
            } else {
               vP = listaVigenciasProyectosModificar.get(i);
            }
            persistenciaVigenciasProyectos.editar(getEm(), vP);
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarVigenciaProyecto(VigenciasProyectos vigenciasProyectos) {
      try {
         persistenciaVigenciasProyectos.borrar(getEm(), vigenciasProyectos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearVigenciaProyecto(VigenciasProyectos vigenciasProyectos) {
      try {
         persistenciaVigenciasProyectos.crear(getEm(), vigenciasProyectos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public Proyectos buscarProyectoPorNombreVigencia(String nombre) {
      try {
         return persistenciaProyectos.buscarProyectoNombre(getEm(), nombre);
      } catch (Exception e) {
         return null;
      }
   }
}
