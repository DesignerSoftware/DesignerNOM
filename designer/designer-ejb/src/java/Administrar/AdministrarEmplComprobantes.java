/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.CentrosCostos;
import Entidades.Comprobantes;
import Entidades.CortesProcesos;
import Entidades.Cuentas;
import Entidades.DetallesFormulas;
import Entidades.Empleados;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.Terceros;
import InterfaceAdministrar.AdministrarEmplComprobantesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCentrosCostosInterface;
import InterfacePersistencia.PersistenciaComprobantesInterface;
import InterfacePersistencia.PersistenciaCortesProcesosInterface;
import InterfacePersistencia.PersistenciaCuentasInterface;
import InterfacePersistencia.PersistenciaDetallesFormulasInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaHistoriasformulasInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'EmplComprobantes'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarEmplComprobantes implements AdministrarEmplComprobantesInterface {

   private static Logger log = Logger.getLogger(AdministrarEmplComprobantes.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaComprobantes'.
    */
   @EJB
   PersistenciaComprobantesInterface persistenciaComprobantes;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaCortesProcesos'.
    */
   @EJB
   PersistenciaCortesProcesosInterface persistenciaCortesProcesos;
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
    * 'persistenciaEmpleado'.
    */
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaProcesos'.
    */
   @EJB
   PersistenciaProcesosInterface persistenciaProcesos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTerceros'.
    */
   @EJB
   PersistenciaTercerosInterface persistenciaTerceros;
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
   @EJB
   PersistenciaCuentasInterface persistenciaCuentas;
   @EJB
   PersistenciaCentrosCostosInterface persistenciaCentrosCostos;
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

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------
   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public Empleados consultarEmpleado(BigInteger secuencia) {
      Empleados empleado;
      try {
         empleado = persistenciaEmpleado.buscarEmpleadoSecuencia(getEm(), secuencia);
         return empleado;
      } catch (Exception e) {
         empleado = null;
         return empleado;
      }
   }

   @Override
   public BigInteger consultarMaximoNumeroComprobante() {
      try {
         return persistenciaComprobantes.numeroMaximoComprobante(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Comprobantes> consultarComprobantesEmpleado(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaComprobantes.comprobantesEmpleado(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<CortesProcesos> consultarCortesProcesosComprobante(BigInteger secuenciaComprobante) {
      try {
         return persistenciaCortesProcesos.cortesProcesosComprobante(getEm(), secuenciaComprobante);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<SolucionesNodos> consultarSolucionesNodosEmpleado(BigInteger secuenciaCorteProceso, BigInteger secuenciaEmpleado) {
      try {
         return persistenciaSolucionesNodos.solucionNodoCorteProcesoEmpleado(getEm(), secuenciaCorteProceso, secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<SolucionesNodos> consultarSolucionesNodosEmpleador(BigInteger secuenciaCorteProceso, BigInteger secuenciaEmpleado) {
      try {
         return persistenciaSolucionesNodos.solucionNodoCorteProcesoEmpleador(getEm(), secuenciaCorteProceso, secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Procesos> consultarLOVProcesos() {
      try {
         return persistenciaProcesos.lovProcesos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Terceros> consultarLOVTerceros(BigInteger secEmpresa) {
      try {
         return persistenciaTerceros.lovTerceros(getEm(), secEmpresa);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificarComprobantes(List<Comprobantes> listComprobantes) {
      try {
         for (int i = 0; i < listComprobantes.size(); i++) {
            log.warn("Modificando Comprobantes...");
            persistenciaComprobantes.editar(getEm(), listComprobantes.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarComprobantes(Comprobantes comprobante) {
      try {
         persistenciaComprobantes.borrar(getEm(), comprobante);
      } catch (Exception e) {
         log.warn("Error borrarComprobantes" + e);
      }
   }

   @Override
   public void crearComprobante(Comprobantes comprobantes) {
      try {
         persistenciaComprobantes.crear(getEm(), comprobantes);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarCortesProcesos(List<CortesProcesos> listaCortesProcesos) {
      try {
         for (int i = 0; i < listaCortesProcesos.size(); i++) {
            log.warn("Modificando Cortes procesos...");
            if (listaCortesProcesos.get(i).getProceso().getSecuencia() == null) {
               listaCortesProcesos.get(i).setProceso(null);
               persistenciaCortesProcesos.editar(getEm(), listaCortesProcesos.get(i));
            } else {
               persistenciaCortesProcesos.editar(getEm(), listaCortesProcesos.get(i));
            }
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarCortesProcesos(CortesProcesos corteProceso) {
      try {
         persistenciaCortesProcesos.borrar(getEm(), corteProceso);
      } catch (Exception e) {
         log.warn("Error borrarCortesProcesos" + e);
      }
   }

   @Override
   public void crearCorteProceso(CortesProcesos corteProceso) {
      try {
         persistenciaCortesProcesos.crear(getEm(), corteProceso);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarSolucionesNodosEmpleado(List<SolucionesNodos> listaSolucionesNodosEmpleado) {
      try {
         for (int i = 0; i < listaSolucionesNodosEmpleado.size(); i++) {
            log.warn("Modificando Soluciones Nodo Empleado...");
            if (listaSolucionesNodosEmpleado.get(i).getNittercero() == null) {
               listaSolucionesNodosEmpleado.get(i).setNittercero(null);
               persistenciaSolucionesNodos.editar(getEm(), listaSolucionesNodosEmpleado.get(i));
            } else {
               persistenciaSolucionesNodos.editar(getEm(), listaSolucionesNodosEmpleado.get(i));
            }
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<DetallesFormulas> consultarDetallesFormula(BigInteger secEmpleado, String fechaDesde, String fechaHasta, BigInteger secProceso, BigInteger secHistoriaFormula) {
      try {
         return persistenciaDetallesFormulas.detallesFormula(getEm(), secEmpleado, fechaDesde, fechaHasta, secProceso, secHistoriaFormula);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger consultarHistoriaFormula(BigInteger secFormula, String fechaDesde) {
      try {
         return persistenciaHistoriasformulas.obtenerSecuenciaHistoriaFormula(getEm(), secFormula, fechaDesde);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Cuentas> lovCuentas() {
      try {
         return persistenciaCuentas.buscarCuentas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<CentrosCostos> lovCentrosCostos() {
      try {
         return persistenciaCentrosCostos.buscarCentrosCostos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public boolean eliminarCPconUndoCierre(BigInteger secProceso, BigInteger secEmpleado, Date fechaCorte) {
      try {
         log.warn("Entro en eliminarCPconUndoCierre()");
         return persistenciaCortesProcesos.eliminarCPconUndoCierre(getEm(), secProceso, secEmpleado, fechaCorte);
      } catch (Exception e) {
         log.warn(this.getClass().getName() + " Error eliminarCPconUndoCierre : " + e);
         return false;
      }
   }
}
