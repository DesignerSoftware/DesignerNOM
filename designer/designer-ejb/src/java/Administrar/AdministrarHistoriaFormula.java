package Administrar;

import Entidades.EstructurasFormulas;
import Entidades.Formulas;
import Entidades.Historiasformulas;
import Entidades.Nodos;
import Entidades.Operadores;
import Entidades.Operandos;
import InterfaceAdministrar.AdministrarHistoriaFormulaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEstructurasFormulasInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaHistoriasformulasInterface;
import InterfacePersistencia.PersistenciaNodosInterface;
import InterfacePersistencia.PersistenciaOperadoresInterface;
import InterfacePersistencia.PersistenciaOperandosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author PROYECTO01
 */
@Stateless
public class AdministrarHistoriaFormula implements AdministrarHistoriaFormulaInterface {

   private static Logger log = Logger.getLogger(AdministrarHistoriaFormula.class);

   @EJB
   PersistenciaHistoriasformulasInterface persistenciaHistoriasFormulas;
   @EJB
   PersistenciaNodosInterface persistenciaNodos;
   @EJB
   PersistenciaFormulasInterface persistenciaFormulas;
   @EJB
   PersistenciaOperandosInterface persistenciaOperandos;
   @EJB
   PersistenciaOperadoresInterface persistenciaOperadores;
   @EJB
   PersistenciaEstructurasFormulasInterface persistenciaEstructurasFormulas;
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

   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<Historiasformulas> listHistoriasFormulasParaFormula(BigInteger secuencia) {
      try {
         return persistenciaHistoriasFormulas.historiasFormulasParaFormulaSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error listHistoriasFormulasParaFormula Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearHistoriasFormulas(List<Historiasformulas> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            String aux = lista.get(i).getObservaciones().toUpperCase();
            lista.get(i).setObservaciones(aux);
            persistenciaHistoriasFormulas.crear(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearHistoriasFormulas Admi : " + e.toString());
      }
   }

   @Override
   public void editarHistoriasFormulas(List<Historiasformulas> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            String aux = lista.get(i).getObservaciones().toUpperCase();
            lista.get(i).setObservaciones(aux);
            persistenciaHistoriasFormulas.editar(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarHistoriasFormulas Admi : " + e.toString());
      }
   }

   @Override
   public void borrarHistoriasFormulas(List<Historiasformulas> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            persistenciaHistoriasFormulas.borrar(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarHistoriasFormulas Admi : " + e.toString());
      }
   }

   @Override
   public List<Nodos> listNodosDeHistoriaFormula(BigInteger secuencia) {
      try {
         return persistenciaNodos.buscarNodosPorSecuenciaHistoriaFormula(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error listNodosDeHistoriaFormula Admi : " + e.toString());
         return null;
      }
   }

   public void crearNodos(List<Nodos> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            log.warn("Nivel : " + lista.get(i).getPosicion());
         }
         for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getOperador().getSecuencia() == null) {
               lista.get(i).setOperador(null);
            }
            if (lista.get(i).getOperando().getSecuencia() == null) {
               lista.get(i).setOperando(null);
            }
            log.warn("lista.get(i) Crear : " + lista.get(i).getSecuencia());
            persistenciaNodos.crear(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearNodos Admi : " + e.toString());
      }
   }

   public void borrarNodos(List<Nodos> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            log.warn("Nivel : " + lista.get(i).getPosicion());
         }
         for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getOperador().getSecuencia() == null) {
               lista.get(i).setOperador(null);
            }
            if (lista.get(i).getOperando().getSecuencia() == null) {
               lista.get(i).setOperando(null);
            }
            persistenciaNodos.borrar(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarNodos Admi : " + e.toString());
      }
   }

   public void editarNodos(List<Nodos> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getOperador().getSecuencia() == null) {
               lista.get(i).setOperador(null);
            }
            if (lista.get(i).getOperando().getSecuencia() == null) {
               lista.get(i).setOperando(null);
            }
            persistenciaNodos.editar(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarNodos Admi : " + e.toString());
      }
   }

   @Override
   public Formulas actualFormula(BigInteger secuencia) {
      try {
         return persistenciaFormulas.buscarFormula(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error actualFormula Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Operadores> listOperadores() {
      try {
         return persistenciaOperadores.buscarOperadores(getEm());
      } catch (Exception e) {
         log.warn("Error listOperadores  Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Operandos> listOperandos() {
      try {
         return persistenciaOperandos.buscarOperandos(getEm());
      } catch (Exception e) {
         log.warn("Error listOperandos  Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<EstructurasFormulas> listEstructurasFormulasParaHistoriaFormula(BigInteger secuencia) {
      try {
         return persistenciaEstructurasFormulas.estructurasFormulasParaHistoriaFormula(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error listEstructurasFormulasParaHistoriaFormula Admi : " + e.toString());
         return null;
      }
   }

}
