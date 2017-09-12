/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.ConceptosSoportes;
import Entidades.Operandos;
import InterfaceAdministrar.AdministrarConceptosSoportesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaConceptosSoportesInterface;
import InterfacePersistencia.PersistenciaOperandosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */


@Stateful
public class AdministrarConceptosSoportes implements AdministrarConceptosSoportesInterface {

   private static Logger log = Logger.getLogger(AdministrarConceptosSoportes.class);
//--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    

   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaConceptosSoportes'.
    */
   @EJB
   PersistenciaConceptosSoportesInterface persistenciaConceptosSoportes;
   @EJB
   PersistenciaOperandosInterface persistenciaOperandos;
   @EJB
   PersistenciaConceptosInterface persistenciaConceptos;
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

   public void modificarConceptosSoportes(List<ConceptosSoportes> listaConceptosSoportes) {
      try {
         for (int i = 0; i < listaConceptosSoportes.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaConceptosSoportes.editar(getEm(), listaConceptosSoportes.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarConceptosSoportes() ERROR: " + e);
      }
   }

   public void borrarConceptosSoportes(List<ConceptosSoportes> listaConceptosSoportes) {
      try {
         for (int i = 0; i < listaConceptosSoportes.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaConceptosSoportes.borrar(getEm(), listaConceptosSoportes.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarConceptosSoportes() ERROR: " + e);
      }
   }

   public void crearConceptosSoportes(List<ConceptosSoportes> listaConceptosSoportes) {
      try {
         for (int i = 0; i < listaConceptosSoportes.size(); i++) {
            persistenciaConceptosSoportes.crear(getEm(), listaConceptosSoportes.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearConceptosSoportes() ERROR: " + e);
      }
   }

   @Override
   public List<ConceptosSoportes> consultarConceptosSoportes() {
      try {
         List<ConceptosSoportes> listaConceptosSoportes;
         listaConceptosSoportes = persistenciaConceptosSoportes.consultarConceptosSoportes(getEm());
         return listaConceptosSoportes;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarConceptosSoportes() ERROR: " + e);
         return null;
      }
   }

   public List<Operandos> consultarLOVOperandos() {
      try {
         List<Operandos> listLOVOperandos;
         listLOVOperandos = persistenciaOperandos.buscarOperandos(getEm());
         return listLOVOperandos;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLOVOperandos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Operandos> consultarLOVOperandosPorConcepto(BigInteger secConceptoSoporte) {
      try {
         List<Operandos> listLOVOperandos;
         listLOVOperandos = persistenciaOperandos.operandoPorConceptoSoporte(getEm(), secConceptoSoporte);
         return listLOVOperandos;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLOVOperandosPorConcepto() ERROR: " + e);
         return null;
      }
   }

   public List<Conceptos> consultarLOVConceptos() {
      try {
         List<Conceptos> listLOVConceptos;
         listLOVConceptos = persistenciaConceptos.buscarConceptos(getEm());
         return listLOVConceptos;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLOVConceptos() ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarConceptosOperandos(BigInteger concepto, BigInteger operando) {
      try {
         BigInteger contarConceptosOperandos = persistenciaConceptosSoportes.consultarConceptoSoporteConceptoOperador(getEm(), concepto, operando);
         return contarConceptosOperandos;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".contarConceptosOperandos() ERROR: " + e);
         return null;
      }
   }
}
