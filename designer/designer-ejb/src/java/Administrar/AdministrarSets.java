package Administrar;

import Entidades.Empleados;
import Entidades.Sets;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarSetsInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaSetsInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarSets implements AdministrarSetsInterface {

   private static Logger log = Logger.getLogger(AdministrarSets.class);

   @EJB
   PersistenciaSetsInterface persistenciaSets;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
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

   @Override
   public List<Sets> SetsEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaSets.buscarSetsEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error en Administrar Vigencias Contratos (VigenciasContratosEmpleado)");
         return null;
      }
   }

   @Override
   public void modificarSets(List<Sets> listSetsModificadas) {
      try {
         for (int i = 0; i < listSetsModificadas.size(); i++) {
            log.warn("Modificando...");
            persistenciaSets.editar(getEm(), listSetsModificadas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarSets(Sets sets) {
      try {
         persistenciaSets.borrar(getEm(), sets);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearSets(Sets sets) {
      try {
         persistenciaSets.crear(getEm(), sets);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public Empleados buscarEmpleado(BigInteger secuencia) {
      try {
         return persistenciaEmpleados.buscarEmpleadoSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         return null;
      }
   }

}
