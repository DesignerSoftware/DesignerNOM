/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.DetallesFormulas;
import Entidades.Parametros;
import Entidades.ParametrosEstructuras;
import Entidades.SolucionesNodos;
import InterfaceAdministrar.AdministrarComprobantesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaDetallesFormulasInterface;
import InterfacePersistencia.PersistenciaHistoriasformulasInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaParametrosInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
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
 * 'Comprobantes'.
 *
 * @author betelgeuse
 */


@Stateful
public class AdministrarComprobantes implements AdministrarComprobantesInterface {

   private static Logger log = Logger.getLogger(AdministrarComprobantes.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaSolucionesNodos'.
    */
   @EJB
   PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaParametros'.
    */
   @EJB
   PersistenciaParametrosInterface persistenciaParametros;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaActualUsuario'.
    */
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaParametrosEstructuras'.
    */
   @EJB
   PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaDetallesFormulas'.
    */
   @EJB
   PersistenciaDetallesFormulasInterface persistenciaDetallesFormulas;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaHistoriasformulas'.
    */
   @EJB
   PersistenciaHistoriasformulasInterface persistenciaHistoriasformulas;
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
   public List<Parametros> consultarParametrosComprobantesActualUsuario() {
      try {
         String usuarioBD;
         usuarioBD = persistenciaActualUsuario.actualAliasBD(getEm());
         log.warn("administrarcomprobantes consultarParametrosComprobantesActualUsuario()  actualUsuario: " + usuarioBD);
         return persistenciaParametros.parametrosComprobantes(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public ParametrosEstructuras consultarParametroEstructuraActualUsuario() {
      try {
         String usuarioBD;
         usuarioBD = persistenciaActualUsuario.actualAliasBD(getEm());
         return persistenciaParametrosEstructuras.buscarParametro(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<SolucionesNodos> consultarSolucionesNodosEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaSolucionesNodos.solucionNodoEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<SolucionesNodos> consultarSolucionesNodosEmpleador(BigInteger secEmpleado) {
      try {
         return persistenciaSolucionesNodos.solucionNodoEmpleador(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<DetallesFormulas> consultarDetallesFormulasEmpleado(BigInteger secEmpleado, String fechaDesde, String fechaHasta, BigInteger secProceso, BigInteger secHistoriaFormula) {
      try {
         return persistenciaDetallesFormulas.detallesFormula(getEm(), secEmpleado, fechaDesde, fechaHasta, secProceso, secHistoriaFormula);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger consultarHistoriaFormulaFormula(BigInteger secFormula, String fechaDesde) {
      try {
         return persistenciaHistoriasformulas.obtenerSecuenciaHistoriaFormula(getEm(), secFormula, fechaDesde);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}
