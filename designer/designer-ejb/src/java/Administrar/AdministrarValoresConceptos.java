/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.ValoresConceptos;
import InterfaceAdministrar.AdministrarValoresConceptosInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaValoresConceptosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarValoresConceptos implements AdministrarValoresConceptosInterface {

   private static Logger log = Logger.getLogger(AdministrarValoresConceptos.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaValoresConceptos'.
    */
   @EJB
   PersistenciaValoresConceptosInterface persistenciaValoresConceptos;
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

   @Override
   public void modificarValoresConceptos(List<ValoresConceptos> listaValoresConceptos) {
      try {
         for (int i = 0; i < listaValoresConceptos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaValoresConceptos.editar(getEm(), listaValoresConceptos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarValoresConceptos() ERROR: " + e);
      }
   }

   @Override
   public void borrarValoresConceptos(List<ValoresConceptos> listaValoresConceptos) {
      try {
         for (int i = 0; i < listaValoresConceptos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaValoresConceptos.borrar(getEm(), listaValoresConceptos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarValoresConceptos() ERROR: " + e);
      }
   }

   @Override
   public void crearValoresConceptos(List<ValoresConceptos> listaValoresConceptos) {
      try {
         for (int i = 0; i < listaValoresConceptos.size(); i++) {
            persistenciaValoresConceptos.crear(getEm(), listaValoresConceptos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearValoresConceptos() ERROR: " + e);
      }
   }

   @Override
   public List<ValoresConceptos> consultarValoresConceptos() {
      try {
         return persistenciaValoresConceptos.consultarValoresConceptos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarValoresConceptos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Conceptos> consultarLOVConceptos() {
      try {
         return persistenciaConceptos.buscarConceptos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLOVConceptos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarConceptoValorConcepto(BigInteger concepto) {
      try {
         return persistenciaValoresConceptos.consultarConceptoValorConcepto(getEm(), concepto);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARVALORESCONCEPTOS CONSULTARCONCEPTOVALORCONCEPTO ERROR : " + e);
         return null;
      }

   }
}
