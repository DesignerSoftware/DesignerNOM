/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Cargos;
import Entidades.Empresas;
import Entidades.FirmasReportes;
import Entidades.Personas;
import InterfaceAdministrar.AdministrarFirmasReportesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaFirmasReportesInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
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
public class AdministrarFirmasReportes implements AdministrarFirmasReportesInterface {

   private static Logger log = Logger.getLogger(AdministrarFirmasReportes.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaFirmasReportes'.
    */
   @EJB
   PersistenciaFirmasReportesInterface persistenciaFirmasReportes;
   @EJB
   PersistenciaCargosInterface persistenciaCargos;
   @EJB
   PersistenciaPersonasInterface persistenciaPersonas;
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

   public void modificarFirmasReportes(List<FirmasReportes> listaFirmasReportes) {
      try {
         for (int i = 0; i < listaFirmasReportes.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaFirmasReportes.editar(getEm(), listaFirmasReportes.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarFirmasReportes(List<FirmasReportes> listaFirmasReportes) {
      try {
         for (int i = 0; i < listaFirmasReportes.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaFirmasReportes.borrar(getEm(), listaFirmasReportes.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearFirmasReportes(List<FirmasReportes> listaFirmasReportes) {
      try {
         for (int i = 0; i < listaFirmasReportes.size(); i++) {
            log.warn("Administrar Creando...");
            log.warn("--------------DUPLICAR------------------------");
            log.warn("CODIGO : " + listaFirmasReportes.get(i).getCodigo());
            log.warn("NOMBRE: " + listaFirmasReportes.get(i).getDescripcion());
            log.warn("EMPRESA: " + listaFirmasReportes.get(i).getEmpresa().getNombre());
            log.warn("SUBTITULO : " + listaFirmasReportes.get(i).getSubtitulofirma());
            log.warn("PERSONA : " + listaFirmasReportes.get(i).getPersonafirma().getNombre());
            log.warn("CARGO : " + listaFirmasReportes.get(i).getCargofirma().getNombre());
            log.warn("--------------DUPLICAR------------------------");
            persistenciaFirmasReportes.crear(getEm(), listaFirmasReportes.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<FirmasReportes> consultarFirmasReportes() {
      try {
         return persistenciaFirmasReportes.consultarFirmasReportes(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public FirmasReportes consultarTipoIndicador(BigInteger secMotivoDemanda) {
      try {
         return persistenciaFirmasReportes.consultarFirmaReporte(getEm(), secMotivoDemanda);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Cargos> consultarLOVCargos() {
      try {
         return persistenciaCargos.consultarCargos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Personas> consultarLOVPersonas() {
      try {
         return persistenciaPersonas.consultarPersonas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Empresas> consultarLOVEmpresas() {
      try {
         return persistenciaEmpresas.consultarEmpresas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}
