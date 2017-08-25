package Administrar;

import Entidades.Conceptos;
import Entidades.Formulas;
import Entidades.FormulasConceptos;
import InterfaceAdministrar.AdministrarFormulaConceptoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaFormulasConceptosInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
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
public class AdministrarFormulaConcepto implements AdministrarFormulaConceptoInterface {

   private static Logger log = Logger.getLogger(AdministrarFormulaConcepto.class);

   @EJB
   PersistenciaFormulasConceptosInterface persistenciaFormulasConceptos;
   @EJB
   PersistenciaFormulasInterface persistenciaFormulas;
   @EJB
   PersistenciaConceptosInterface persistenciaConceptos;
   /**
    * Enterprise JavaBean.<br> Atributo que representa todo lo referente a la
    * conexión del usuario que está usando el aplicativo.
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
   public List<FormulasConceptos> formulasConceptosParaFormula(BigInteger secuencia) {
      try {
         log.warn("Administrar.AdministrarFormulaConcepto.formulasConceptosParaFormula() secuencia : " + secuencia);
         return persistenciaFormulasConceptos.formulasConceptosParaFormulaSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error formulasConceptosParaFormula Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearFormulasConceptos(List<FormulasConceptos> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            persistenciaFormulasConceptos.crear(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearFormulasConceptos Admi : " + e.toString());
      }
   }

   @Override
   public void editarFormulasConceptos(List<FormulasConceptos> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            persistenciaFormulasConceptos.editar(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarFormulasConceptos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarFormulasConceptos(List<FormulasConceptos> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            persistenciaFormulasConceptos.borrar(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarFormulasConceptos Admi : " + e.toString());
      }
   }

   @Override
   public List<FormulasConceptos> listFormulasConceptos() {
      try {
         return persistenciaFormulasConceptos.buscarFormulasConceptos(getEm());
      } catch (Exception e) {
         log.warn("Error listFormulasConceptos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Conceptos> listConceptos() {
      try {
         return persistenciaConceptos.buscarConceptos(getEm());
      } catch (Exception e) {
         log.warn("Error listConceptos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Formulas formulaActual(BigInteger secuencia) {
      try {
         return persistenciaFormulas.buscarFormula(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error formulaActual Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public FormulasConceptos cargarFormulasConcepto(BigInteger secConcepto) {
      try {
         List<FormulasConceptos> lista = persistenciaFormulasConceptos.formulasConceptosXSecConcepto(getEm(), secConcepto);
         if (lista.isEmpty() || lista == null) {
            log.warn("Error cargarFormulasConcepto: formulasConcepto trae lista vacia o nula Admi");
            return null;
         } else {
            return lista.get(0);
         }
      } catch (Exception e) {
         log.warn("Error cargarFormulasConcepto Admi : " + e.toString());
         return null;
      }
   }
   /*
     * @Override public boolean
     * verificarExistenciaConceptoFormulasConcepto(EntityManager em, BigInteger
     * secConcepto) { try { boolean lista =
     * persistenciaFormulasConceptos.verificarExistenciaConceptoFormulasConcepto(getEm(),
     * secConcepto); return lista; } catch (Exception e) {
     * log.warn("Error verificarExistenciaConceptoFormulasConcepto
     * Admi : " + e.toString()); return false; } }
     *
     * @Override public boolean cargarFormulasConcepto(EntityManager em,
     * BigInteger secuencia, BigInteger secFormula) { try { boolean lista =
     * persistenciaFormulasConceptos.verificarFormulaCargue_Concepto(getEm(),
     * secuencia, secFormula); return lista; } catch (Exception e) {
     * log.warn("Error cargarFormulaConcepto Admi : " + e.toString());
     * return false; }
    }
    */
}
