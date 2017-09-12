/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.GruposConceptos;
import Entidades.Operandos;
import Entidades.OperandosGruposConceptos;
import Entidades.Procesos;
import InterfaceAdministrar.AdministrarOperandosGruposConceptosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaGruposConceptosInterface;
import InterfacePersistencia.PersistenciaOperandosGruposConceptosInterface;
import InterfacePersistencia.PersistenciaOperandosInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Victor Algarin
 */
@Stateful
public class AdministrarOperandosGruposConceptos implements AdministrarOperandosGruposConceptosInterface {

   private static Logger log = Logger.getLogger(AdministrarOperandosGruposConceptos.class);

   @EJB
   PersistenciaOperandosGruposConceptosInterface persistenciaOperandosGruposConceptos;
   @EJB
   PersistenciaGruposConceptosInterface persistenciaGruposConceptos;
   @EJB
   PersistenciaOperandosInterface persistenciaOperandos;
   @EJB
   PersistenciaProcesosInterface persistenciaProcesos;
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

//Procesos
   @Override
   public void borrarProcesos(Procesos procesos) {
      try {
         persistenciaProcesos.borrar(getEm(), procesos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarProcesos() ERROR: " + e);
      }
   }

   @Override
   public void crearProcesos(Procesos procesos) {
      try {
         persistenciaProcesos.crear(getEm(), procesos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearProcesos() ERROR: " + e);
      }
   }

   @Override
   public void modificarProcesos(List<Procesos> listaVigenciasRetencionesModificar) {
      try {
         for (int i = 0; i < listaVigenciasRetencionesModificar.size(); i++) {
            persistenciaProcesos.editar(getEm(), listaVigenciasRetencionesModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarProcesos() ERROR: " + e);
      }
   }

   public List<Procesos> consultarProcesos() {
      try {
         return persistenciaProcesos.buscarProcesos(getEm());
      } catch (Exception e) {
         log.warn("Error consultarVigenciasRetenciones: " + e.toString());
         return null;
      }
   }

   public List<Operandos> consultarOperandos() {
      try {
         return persistenciaOperandos.buscarOperandos(getEm());
      } catch (Exception e) {
         log.warn("Error consultarVigenciasRetenciones: " + e.toString());
         return null;
      }
   }

   public List<GruposConceptos> consultarGrupos() {
      try {
         return persistenciaGruposConceptos.buscarGruposConceptos(getEm());
      } catch (Exception e) {
         log.warn("Error consultarVigenciasRetenciones: " + e.toString());
         return null;
      }
   }

//OperandosGruposConceptos
   @Override
   public void borrarOperandosGrupos(OperandosGruposConceptos operandos) {
      try {
         persistenciaOperandosGruposConceptos.borrar(getEm(), operandos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarOperandosGrupos() ERROR: " + e);
      }
   }

   @Override
   public void crearOperandosGrupos(OperandosGruposConceptos operandos) {
      try {
         persistenciaOperandosGruposConceptos.crear(getEm(), operandos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearOperandosGrupos() ERROR: " + e);
      }
   }

   @Override
   public void modificarOperandosGrupos(List<OperandosGruposConceptos> listaOperandosGruposConceptosModificar) {
      try {
         for (int i = 0; i < listaOperandosGruposConceptosModificar.size(); i++) {
            log.warn("Modificando...");
            persistenciaOperandosGruposConceptos.editar(getEm(), listaOperandosGruposConceptosModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarOperandosGrupos() ERROR: " + e);
      }
   }

   @Override
   public List<OperandosGruposConceptos> consultarOperandosGrupos(BigInteger secProceso) {
      try {
         return persistenciaOperandosGruposConceptos.buscarOperandosGruposConceptosPorProcesoSecuencia(getEm(), secProceso);
      } catch (Exception e) {
         log.warn("Error conceptoActual Admi : " + e.toString());
         return null;
      }
   }

}
