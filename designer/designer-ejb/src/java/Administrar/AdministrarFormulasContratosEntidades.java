/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.FormulasContratosEntidades;
import Entidades.Formulascontratos;
import Entidades.TiposEntidades;
import InterfaceAdministrar.AdministrarFormulasContratosEntidadesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaFormulasContratosEntidadesInterface;
import InterfacePersistencia.PersistenciaFormulasContratosInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarFormulasContratosEntidades implements AdministrarFormulasContratosEntidadesInterface {

   private static Logger log = Logger.getLogger(AdministrarFormulasContratosEntidades.class);
//ATRIBUTOS
   //--------------------------------------------------------------------------    

   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaFormulasContratosEntidades'.
    */
   @EJB
   PersistenciaFormulasContratosEntidadesInterface persistenciaFormulasContratosEntidades;
   @EJB
   PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
   @EJB
   PersistenciaFormulasContratosInterface persistenciaFormulas;
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

   public void modificarFormulasContratosEntidades(List<FormulasContratosEntidades> listaFormulasContratosEntidades) {
      try {
         for (int i = 0; i < listaFormulasContratosEntidades.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaFormulasContratosEntidades.editar(getEm(), listaFormulasContratosEntidades.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarFormulasContratosEntidades() ERROR: " + e);
      }
   }

   public void borrarFormulasContratosEntidades(List<FormulasContratosEntidades> listaFormulasContratosEntidades) {
      try {
         for (int i = 0; i < listaFormulasContratosEntidades.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaFormulasContratosEntidades.borrar(getEm(), listaFormulasContratosEntidades.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarFormulasContratosEntidades() ERROR: " + e);
      }
   }

   public void crearFormulasContratosEntidades(List<FormulasContratosEntidades> listaFormulasContratosEntidades) {
      try {
         for (int i = 0; i < listaFormulasContratosEntidades.size(); i++) {
            persistenciaFormulasContratosEntidades.crear(getEm(), listaFormulasContratosEntidades.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearFormulasContratosEntidades() ERROR: " + e);
      }
   }

   @Override
   public List<FormulasContratosEntidades> consultarFormulasContratosEntidades() {
      try {
         return persistenciaFormulasContratosEntidades.consultarFormulasContratosEntidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarFormulasContratosEntidades() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<FormulasContratosEntidades> consultarFormulasContratosEntidadesPorFormulaContrato(BigInteger secFormulaContrato) {
      try {
         return persistenciaFormulasContratosEntidades.consultarFormulasContratosEntidadesPorFormulaContrato(getEm(), secFormulaContrato);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarFormulasContratosEntidadesPorFormulaContrato() ERROR: " + e);
         return null;
      }
   }

   public List<TiposEntidades> consultarLOVTiposEntidades() {
      try {
         return persistenciaTiposEntidades.buscarTiposEntidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLOVTiposEntidades() ERROR: " + e);
         return null;
      }
   }

   @Override
   public Formulascontratos consultarFormulaDeFormulasContratosEntidades(BigInteger secFormulaContrato) {
      try {
         return persistenciaFormulas.formulasContratosParaContratoFormulasContratosEntidades(getEm(), secFormulaContrato);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarFormulaDeFormulasContratosEntidades() ERROR: " + e);
         return null;
      }
   }

}
