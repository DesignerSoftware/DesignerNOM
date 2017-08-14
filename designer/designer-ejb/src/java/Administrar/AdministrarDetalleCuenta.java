/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Cuentas;
import Entidades.VigenciasCuentas;
import InterfaceAdministrar.AdministrarDetalleCuentaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCuentasInterface;
import InterfacePersistencia.PersistenciaVigenciasCuentasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'DetalleCuenta'.
 *
 * @author Betelgeuse.
 */
@Stateful
public class AdministrarDetalleCuenta implements AdministrarDetalleCuentaInterface {

   private static Logger log = Logger.getLogger(AdministrarDetalleCuenta.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaVigenciasCuentas'.
    */
   @EJB
   PersistenciaVigenciasCuentasInterface persistenciaVigenciasCuentas;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaCuentas'.
    */
   @EJB
   PersistenciaCuentasInterface persistenciaCuentas;
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

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------
   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<VigenciasCuentas> consultarListaVigenciasCuentasCredito(BigInteger secuenciaC) {
      try {
         return persistenciaVigenciasCuentas.buscarVigenciasCuentasPorCredito(getEm(), secuenciaC);
      } catch (Exception e) {
         log.warn("Error listVigenciasCuentasCredito Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<VigenciasCuentas> consultarListaVigenciasCuentasDebito(BigInteger secuenciaC) {
      try {
         return persistenciaVigenciasCuentas.buscarVigenciasCuentasPorDebito(getEm(), secuenciaC);
      } catch (Exception e) {
         log.warn("Error listVigenciasCuentasDebito Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Cuentas mostrarCuenta(BigInteger secuencia) {
      try {
         return persistenciaCuentas.buscarCuentasSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error cuentaActual Admi: " + e.toString());
         return null;
      }
   }
}
