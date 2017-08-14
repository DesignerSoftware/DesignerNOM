/**
 * Documentación a cargo de Andres Pineda
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Novedades;
import Entidades.SolucionesFormulas;
import InterfaceAdministrar.AdministrarSolucionesFormulasInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaNovedadesInterface;
import InterfacePersistencia.PersistenciaSolucionesFormulasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'SolucionFormula'.
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarSolucionesFormulas implements AdministrarSolucionesFormulasInterface {

   private static Logger log = Logger.getLogger(AdministrarSolucionesFormulas.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaNovedades'.
    */
   @EJB
   PersistenciaNovedadesInterface persistenciaNovedades;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaEmpleado'.
    */
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaSolucionesFormulas'.
    */
   @EJB
   PersistenciaSolucionesFormulasInterface persistenciaSolucionesFormulas;
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
   public List<SolucionesFormulas> listaSolucionesFormulaParaEmpleadoYNovedad(BigInteger secEmpleado, BigInteger secNovedad) {
      try {
         return persistenciaSolucionesFormulas.listaSolucionesFormulasParaEmpleadoYNovedad(getEm(), secEmpleado, secNovedad);
      } catch (Exception e) {
         log.warn("Error listaSolucionesFormulaParaEmpleadoYNovedad Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Empleados empleadoActual(BigInteger codEmpleado) {
      try {
         return persistenciaEmpleado.buscarEmpleadoTipo(getEm(), codEmpleado);
      } catch (Exception e) {
         log.warn("Error empleadoActual Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Novedades novedadActual(BigInteger secNovedad) {
      try {
         return persistenciaNovedades.buscarNovedad(getEm(), secNovedad);
      } catch (Exception e) {
         log.warn("Error novedadActual Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void borrarSolucionesFormulas(List<SolucionesFormulas> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaSolucionesFormulas.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

}
