/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.ConceptosJuridicos;
import Entidades.Empresas;
import InterfaceAdministrar.AdministrarConceptoJuridicoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosJuridicosInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'ConceptoJuridico'.
 *
 * @author betelgeuse
 */


@Stateless
public class AdministrarConceptoJuridico implements AdministrarConceptoJuridicoInterface {

   private static Logger log = Logger.getLogger(AdministrarConceptoJuridico.class);
   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaConceptosJuridicos'.
    */
   @EJB
   PersistenciaConceptosJuridicosInterface persistenciaConceptosJuridicos;
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
   public List<ConceptosJuridicos> consultarConceptosJuridicosEmpresa(BigInteger secuencia) {
      try {
         List<ConceptosJuridicos> conceptos = persistenciaConceptosJuridicos.buscarConceptosJuridicosEmpresa(getEm(), secuencia);
         return conceptos;
      } catch (Exception e) {
         log.warn("Error listConceptosJuridicosPorEmpresa Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearConceptosJuridicos(List<ConceptosJuridicos> listCJ) {
      try {
         for (int i = 0; i < listCJ.size(); i++) {
            persistenciaConceptosJuridicos.crear(getEm(), listCJ.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearConceptosJuridicos Admi : " + e.toString());
      }
   }

   @Override
   public void editarConceptosJuridicos(List<ConceptosJuridicos> listCJ) {
      try {
         for (int i = 0; i < listCJ.size(); i++) {
            persistenciaConceptosJuridicos.editar(getEm(), listCJ.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarConceptosJuridicos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarConceptosJuridicos(List<ConceptosJuridicos> listCJ) {
      try {
         for (int i = 0; i < listCJ.size(); i++) {
            persistenciaConceptosJuridicos.borrar(getEm(), listCJ.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarConceptosJuridicos Admi : " + e.toString());
      }
   }

   @Override
   public List<Empresas> consultarEmpresas() {
      try {
         List<Empresas> empresas = persistenciaEmpresas.consultarEmpresas(getEm());
         return empresas;
      } catch (Exception e) {
         log.warn("Error listEmpresas Admi : " + e.toString());
         return null;
      }
   }
}
