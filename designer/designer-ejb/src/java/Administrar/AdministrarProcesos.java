/**
 * Documentación a cargo de Andres Pineda
 */
package Administrar;

import Entidades.Formulas;
import Entidades.FormulasProcesos;
import Entidades.Operandos;
import Entidades.OperandosLogs;
import Entidades.Procesos;
import Entidades.Tipospagos;
import InterfaceAdministrar.AdministrarProcesosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaFormulasProcesosInterface;
import InterfacePersistencia.PersistenciaOperandosInterface;
import InterfacePersistencia.PersistenciaOperandosLogsInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaTiposPagosInterface;
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
 * 'Proceso'.
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarProcesos implements AdministrarProcesosInterface {

   private static Logger log = Logger.getLogger(AdministrarProcesos.class);
   //------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    

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
    * 'persistenciaOperandosLogs'.
    */
   @EJB
   PersistenciaOperandosLogsInterface persistenciaOperandosLogs;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaFormulasProcesos'.
    */
   @EJB
   PersistenciaFormulasProcesosInterface persistenciaFormulasProcesos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTiposPagos'.
    */
   @EJB
   PersistenciaTiposPagosInterface persistenciaTiposPagos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaFormulas'.
    */
   @EJB
   PersistenciaFormulasInterface persistenciaFormulas;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaOperandos'.
    */
   @EJB
   PersistenciaOperandosInterface persistenciaOperandos;
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
   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------    
   ///// ---- PROCESOS ---- /////

   @Override
   public List<Procesos> listaProcesos() {
      try {
         return persistenciaProcesos.buscarProcesos(getEm());
      } catch (Exception e) {
         log.warn("Error listaProcesos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearProcesos(List<Procesos> listaP) {
      try {
         for (int i = 0; i < listaP.size(); i++) {
            String textM = listaP.get(i).getDescripcion().toUpperCase();
            listaP.get(i).setDescripcion(textM);
            String textC = listaP.get(i).getComentarios().toUpperCase();
            listaP.get(i).setComentarios(textC);
            persistenciaProcesos.crear(getEm(), listaP.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearProcesos Admi : " + e.toString()
         );
      }
   }

   @Override
   public void editarProcesos(List<Procesos> listaP) {
      try {
         for (int i = 0; i < listaP.size(); i++) {
            String textM = listaP.get(i).getDescripcion().toUpperCase();
            listaP.get(i).setDescripcion(textM);
            String textC = listaP.get(i).getComentarios().toUpperCase();
            listaP.get(i).setComentarios(textC);
            persistenciaProcesos.editar(getEm(), listaP.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarProcesos Admi : " + e.toString()
         );
      }
   }

   @Override
   public void borrarProcesos(List<Procesos> listaP) {
      try {
         for (int i = 0; i < listaP.size(); i++) {
            persistenciaProcesos.borrar(getEm(), listaP.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarProcesos Admi : " + e.toString()
         );
      }
   }

   @Override
   public List<Tipospagos> lovTiposPagos() {
      try {
         return persistenciaTiposPagos.consultarTiposPagos(getEm());
      } catch (Exception e) {
         log.warn("Error lovTiposPagos Admi : " + e.toString());
         return null;
      }
   }
   ///// ---- PROCESOS ---- /////

   ///// ---- FORMULASPROCESOS ---- /////
   @Override
   public List<FormulasProcesos> listaFormulasProcesosParaProcesoSecuencia(BigInteger secuencia) {
      try {
         return persistenciaFormulasProcesos.formulasProcesosParaProcesoSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error listaFormulasProcesosParaProcesoSecuencia Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearFormulasProcesos(List<FormulasProcesos> listaFP) {
      try {
         for (int i = 0; i < listaFP.size(); i++) {
            persistenciaFormulasProcesos.crear(getEm(), listaFP.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearFormulasProcesos Admi : " + e.toString()
         );
      }
   }

   @Override
   public void editarFormulasProcesos(List<FormulasProcesos> listaFP) {
      try {
         for (int i = 0; i < listaFP.size(); i++) {
            persistenciaFormulasProcesos.editar(getEm(), listaFP.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarFormulasProcesos Admi : " + e.toString()
         );
      }
   }

   @Override
   public void borrarFormulasProcesos(List<FormulasProcesos> listaFP) {
      try {
         for (int i = 0; i < listaFP.size(); i++) {
            persistenciaFormulasProcesos.borrar(getEm(), listaFP.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarFormulasProcesos Admi : " + e.toString()
         );
      }
   }

   @Override
   public List<Formulas> lovFormulas() {
      try {
         return persistenciaFormulas.buscarFormulas(getEm());
      } catch (Exception e) {
         log.warn("Error lovFormulas Admi : " + e.toString());
         return null;
      }
   }
   ///// ---- FORMULASPROCESOS ---- /////

   ///// ---- OPERANDOSLOGS ---- /////
   @Override
   public List<OperandosLogs> listaOperandosLogsParaProcesoSecuencia(BigInteger secuencia) {
      try {
         return persistenciaOperandosLogs.buscarOperandosLogsParaProcesoSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error listaOperandosLogsParaProcesoSecuencia Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearOperandosLogs(List<OperandosLogs> listaOL) {
      try {
         for (int i = 0; i < listaOL.size(); i++) {
            listaOL.get(i).setDescripcion(String.valueOf(listaOL.get(i).getOperando().getCodigo()));
            persistenciaOperandosLogs.crear(getEm(), listaOL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearOperandosLogs Admi : " + e.toString()
         );
      }
   }

   @Override
   public void editarOperandosLogs(List<OperandosLogs> listaOL) {
      try {
         for (int i = 0; i < listaOL.size(); i++) {
            persistenciaOperandosLogs.editar(getEm(), listaOL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarOperandosLogs Admi : " + e.toString()
         );
      }
   }

   @Override
   public void borrarOperandosLogs(List<OperandosLogs> listaOL) {
      try {
         for (int i = 0; i < listaOL.size(); i++) {
            persistenciaOperandosLogs.borrar(getEm(), listaOL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarOperandosLogs Admi : " + e.toString()
         );
      }
   }

   @Override
   public List<Operandos> lovOperandos() {
      try {
         return persistenciaOperandos.buscarOperandos(getEm());
      } catch (Exception e) {
         log.warn("Error lovOperandos Admi : " + e.toString());
         return null;
      }
   }

   public String clonarProceso(String descripcionN, short codigoN, short codigoO) {
      try {
         return persistenciaProcesos.clonarProceso(getEm(), descripcionN, codigoN, codigoO);
      } catch (Exception e) {
         log.warn("Administrar.AdministrarProcesos.clonarProceso() Error : " + e.toString());
         return "ERROR EJECUTANDO LA TRANSACCION DESDE EL SISTEMA";
      }
   }
   ///// ---- OPERANDOSLOGS ---- /////

}
