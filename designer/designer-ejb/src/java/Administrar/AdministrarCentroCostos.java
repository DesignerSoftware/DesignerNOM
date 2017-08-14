/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.CentrosCostos;
import Entidades.Empresas;
import Entidades.TiposCentrosCostos;
import InterfaceAdministrar.AdministrarCentroCostosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCentrosCostosInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaTiposCentrosCostosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'CentroCostos'.
 *
 * @author betelgeuse
 */


@Stateful
@LocalBean
public class AdministrarCentroCostos implements AdministrarCentroCostosInterface {

   private static Logger log = Logger.getLogger(AdministrarCentroCostos.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaCentrosCostos'.
    */
   @EJB
   PersistenciaCentrosCostosInterface persistenciaCentrosCostos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTiposCentrosCostos'.
    */
   @EJB
   PersistenciaTiposCentrosCostosInterface persistenciaTiposCentrosCostos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaEmpresas'.
    */
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;

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

   //-------------------------------------------------------------------------------------
   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<Empresas> buscarEmpresas() {
      try {
         return persistenciaEmpresas.consultarEmpresas(getEm());
      } catch (Exception e) {
         log.warn("AdministrarCentroCostos: Falló al buscar las empresas /n" + e.getMessage());
         return null;
      }
   }

   @Override
   public void modificarCentroCostos(List<CentrosCostos> listaCentrosCostos) {
      try {
         for (int i = 0; i < listaCentrosCostos.size(); i++) {
            log.warn("Modificando...");
            persistenciaCentrosCostos.editar(getEm(), listaCentrosCostos.get(i));
         }
      } catch (Exception e) {
         log.error("AdministrarCentrosCostos: Falló al editar el CentroCosto /n" + e.getMessage());
      }
   }

   @Override
   public void borrarCentroCostos(List<CentrosCostos> listaCentrosCostos) {
      log.warn("ENTRE A AdministrarCentroCostos.borrarCentroCostos ");
      try {
         for (int i = 0; i < listaCentrosCostos.size(); i++) {
            log.warn("Borrando...");
            persistenciaCentrosCostos.borrar(getEm(), listaCentrosCostos.get(i));
         }
      } catch (Exception e) {
         log.error("ERROR AdministrarCentroCostos.borrarCentroCostos ERROR=====" + e.getMessage());
      }
   }

   @Override
   public void crearCentroCostos(List<CentrosCostos> listaCentrosCostos) {
      log.warn("ENTRE A AdministrarCentroCostos.crearCentroCostos ");
      try {
         for (int i = 0; i < listaCentrosCostos.size(); i++) {
            log.warn("Creando...");
            persistenciaCentrosCostos.crear(getEm(), listaCentrosCostos.get(i));
         }
      } catch (Exception e) {
         log.error("ERROR AdministrarCentroCostos.crearCentroCostos ERROR======" + e.getMessage());
      }
   }

   @Override
   public List<CentrosCostos> consultarCentrosCostosPorEmpresa(BigInteger secEmpresa) {
      try {
         log.warn("ENTRE A AdministrarCentroCostos.consultarCentrosCostosPorEmpresa ");
         return persistenciaCentrosCostos.buscarCentrosCostosEmpr(getEm(), secEmpresa);
      } catch (Exception e) {
         log.warn("Error en Administrar CentrosCostos (centrosCostosPorEmpresa)");
         return null;
      }
   }

   @Override
   public List<TiposCentrosCostos> lovTiposCentrosCostos() {
      try {
         return persistenciaTiposCentrosCostos.buscarTiposCentrosCostos(getEm());
      } catch (Exception e) {
         log.warn("\n AdministrarCentroCostos error en buscarTiposCentroCostos \n" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorComprobantesContables(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorComprobantesContables(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorComprobantesContables ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorDetallesCCConsolidador(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorDetallesCCConsolidador(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorDetallesCCConsolidador ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorDetalleContable(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorDetallesCCDetalle(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorDetalleContable ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorEmpresas(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorEmpresas(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorEmpresas ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorEstructuras(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorEstructuras(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorEstructuras ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorInterConCondor(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorInterconCondor(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorInterconCondor ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorInterConDynamics(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorInterconDynamics(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorInterconDynamics ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorInterConGeneral(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorInterconGeneral(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorInterconGeneral ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorInterConHelisa(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorInterconHelisa(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorInterconHelisa ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorInterConSapbo(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorInterconSapbo(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorInterconSapbo ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorInterConSiigo(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorInterconSiigo(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorInterconSiigo ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorInterConTotal(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorInterconTotal(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorInterconTotal ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorNovedadesD(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorNovedadesD(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorNovedadesD ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorNovedadesC(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorNovedadesC(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorNovedadesC ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorProcesosProductivos(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorProcesosProductivos(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorProcesosProductivos ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorProyecciones(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorProyecciones(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorProyecciones ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorSolucionesNodosC(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorSolucionesNodosC(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorSolucionesNodosC ERROR===" + e.getMessage());

         return null;
      }
   }

   @Override
   public BigInteger contadorSolucionesNodosD(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorSolucionesNodosD(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorSolucionesNodosD ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorSoPanoramas(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorSoPanoramas(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorSoPanoramas ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorTerceros(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorTerceros(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorTerceros ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorUnidadesRegistradas(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorUnidadesRegistradas(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorUnidadesRegistradas ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorVigenciasCuentasC(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorVigenciasCuentasC(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorVigenciasCuentasC ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorVigenciasCuentasD(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorVigenciasCuentasD(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorVigenciasCuentasD ERROR===" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorVigenciasProrrateos(BigInteger secCentroCosto) {
      try {
         return persistenciaCentrosCostos.contadorVigenciasProrrateos(getEm(), secCentroCosto);
      } catch (Exception e) {
         log.warn("ERROR administrarCentrosCostos.contadorVigenciasProrrateos ERROR===" + e.getMessage());
         return null;
      }
   }

}
