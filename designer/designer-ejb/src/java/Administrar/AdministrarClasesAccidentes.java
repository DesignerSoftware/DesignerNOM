/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.ClasesAccidentes;
import InterfaceAdministrar.AdministrarClasesAccidentesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaClasesAccidentesInterface;
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
 * 'ClasesAccidentes'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarClasesAccidentes implements AdministrarClasesAccidentesInterface {

   private static Logger log = Logger.getLogger(AdministrarClasesAccidentes.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaClasesAccidentes'.
    */
   @EJB
   PersistenciaClasesAccidentesInterface persistenciaClasesAccidentes;

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
   public void modificarClasesAccidentes(List<ClasesAccidentes> listClasesAccidentesModificada) {
      try {
         ClasesAccidentes clasesAccidentesSeleccionada;
         for (int i = 0; i < listClasesAccidentesModificada.size(); i++) {
            log.warn("Administrar Modificando...");
            clasesAccidentesSeleccionada = listClasesAccidentesModificada.get(i);
            persistenciaClasesAccidentes.editar(getEm(), clasesAccidentesSeleccionada);
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarClasesAccidentes(List<ClasesAccidentes> listaClasesAccidentes) {
      try {
         for (int i = 0; i < listaClasesAccidentes.size(); i++) {
            log.warn("Borrando...");
            persistenciaClasesAccidentes.borrar(getEm(), listaClasesAccidentes.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearClasesAccidentes(List<ClasesAccidentes> listaClasesAccidentes) {
      try {
         for (int i = 0; i < listaClasesAccidentes.size(); i++) {
            log.warn("Creando...");
            persistenciaClasesAccidentes.crear(getEm(), listaClasesAccidentes.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<ClasesAccidentes> consultarClasesAccidentes() {
      try {
         return persistenciaClasesAccidentes.buscarClasesAccidentes(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public ClasesAccidentes consultarClaseAccidente(BigInteger secClasesAccidentes) {
      try {
         return persistenciaClasesAccidentes.buscarClaseAccidente(getEm(), secClasesAccidentes);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarSoAccidentesMedicosClaseAccidente(BigInteger secuenciaElementos) {
      try {
         log.error("Secuencia Borrado Elementos" + secuenciaElementos);
         return persistenciaClasesAccidentes.contadorSoAccidentesMedicos(getEm(), secuenciaElementos);
      } catch (Exception e) {
         log.error("ERROR AdministrarClasesAccidentes verificarSoAccidtenesMedicos ERROR :" + e);
         return null;
      }
   }
}
