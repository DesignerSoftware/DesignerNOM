/**
 * Documentación a cargo de Andres Pineda
 */
package Administrar;

import Entidades.DiasLaborables;
import Entidades.TiposContratos;
import Entidades.TiposDias;
import InterfaceAdministrar.AdministrarTiposContratosInterface;
import InterfacePersistencia.PersistenciaDiasLaborablesInterface;
import InterfacePersistencia.PersistenciaTiposContratosInterface;
import InterfacePersistencia.PersistenciaTiposDiasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br> Clase encargada de realizar las operaciones lógicas para
 * la pantalla 'TipoContrato'.
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarTiposContratos implements AdministrarTiposContratosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposContratos.class);

   //--------------------------------------------------------------------------    
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaTiposContratos'.
    */
   @EJB
   PersistenciaTiposContratosInterface persistenciaTiposContratos;
   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaDiasLaborables'.
    */
   @EJB
   PersistenciaDiasLaborablesInterface persistenciaDiasLaborables;
   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaTiposDias'.
    */
   @EJB
   PersistenciaTiposDiasInterface persistenciaTiposDias;
   /**
    * Enterprise JavaBean.<br> Atributo que representa todo lo referente a la
    * conexión del usuario que está usando el aplicativo.
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
   public List<TiposContratos> listaTiposContratos() {
      try {
         return persistenciaTiposContratos.tiposContratos(getEm());
      } catch (Exception e) {
         log.warn("Error listaTiposContratos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearTiposContratos(List<TiposContratos> listaTC) {
      try {
         for (int i = 0; i < listaTC.size(); i++) {
            persistenciaTiposContratos.crear(getEm(), listaTC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearTiposContratos Admi : " + e.toString());
      }
   }

   @Override
   public void editarTiposContratos(List<TiposContratos> listaTC) {
      try {
         for (int i = 0; i < listaTC.size(); i++) {
            persistenciaTiposContratos.editar(getEm(), listaTC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarTiposContratos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarTiposContratos(List<TiposContratos> listaTC) {
      try {
         for (int i = 0; i < listaTC.size(); i++) {
            persistenciaTiposContratos.borrar(getEm(), listaTC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarTiposContratos Admi : " + e.toString());
      }
   }

   @Override
   public List<DiasLaborables> listaDiasLaborablesParaTipoContrato(BigInteger secTipoContrato) {
      try {
         List<DiasLaborables> lista = persistenciaDiasLaborables.diasLaborablesParaSecuenciaTipoContrato(getEm(), secTipoContrato);
         return lista;
      } catch (Exception e) {
         log.warn("Error listaDiasLaborablesParaTipoContrato Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearDiasLaborables(List<DiasLaborables> listaDL) {
      try {
         for (int i = 0; i < listaDL.size(); i++) {
            persistenciaDiasLaborables.crear(getEm(), listaDL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearDiasLaborables Admi : " + e.toString());
      }
   }

   @Override
   public void editarDiasLaborables(List<DiasLaborables> listaDL) {
      try {
         for (int i = 0; i < listaDL.size(); i++) {
            persistenciaDiasLaborables.editar(getEm(), listaDL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarDiasLaborables Admi : " + e.toString());
      }
   }

   @Override
   public void borrarDiasLaborables(List<DiasLaborables> listaDL) {
      try {
         for (int i = 0; i < listaDL.size(); i++) {
            persistenciaDiasLaborables.borrar(getEm(), listaDL.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarDiasLaborables Admi : " + e.toString());
      }
   }

   @Override
   public List<TiposDias> lovTiposDias() {
      try {
         return persistenciaTiposDias.buscarTiposDias(getEm());
      } catch (Exception e) {
         log.warn("Error lovTiposDias Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void clonarTC(BigInteger secuenciaClonado, String nuevoNombre, Short nuevoCodigo) {
      try {
         persistenciaTiposContratos.clonarTipoContrato(secuenciaClonado, nuevoNombre, nuevoCodigo);
      } catch (Exception e) {
         log.warn("Error clonarTC Admi : " + e.toString());
      }
   }
}
