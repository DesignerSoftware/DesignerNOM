/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Periodicidades;
import Entidades.Unidades;
import InterfaceAdministrar.AdministrarPeriodicidadesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import InterfacePersistencia.PersistenciaUnidadesInterface;
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
public class AdministrarPeriodicidades implements AdministrarPeriodicidadesInterface {

   private static Logger log = Logger.getLogger(AdministrarPeriodicidades.class);

   @EJB
   PersistenciaPeriodicidadesInterface persistenciaPeriodicidades;
   @EJB
   PersistenciaUnidadesInterface persistenciaUnidades;
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
   public void modificarPeriodicidades(List<Periodicidades> listaPeriodicidades) {
      try {
         for (int i = 0; i < listaPeriodicidades.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaPeriodicidades.editar(getEm(), listaPeriodicidades.get(i));
         }
      } catch (Exception e) {
         log.error("SE JODIDO ESTO ADMINISTRARPERIODICIDADES MODIFICARPERIODICIDADES ERROR : " + e);
      }
   }

   @Override
   public void borrarPeriodicidades(List<Periodicidades> listaPeriodicidades) {
      try {
         for (int i = 0; i < listaPeriodicidades.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaPeriodicidades.borrar(getEm(), listaPeriodicidades.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearPeriodicidades(List<Periodicidades> listaPeriodicidades) {
      try {
         for (int i = 0; i < listaPeriodicidades.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaPeriodicidades.crear(getEm(), listaPeriodicidades.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<Periodicidades> consultarPeriodicidades() {
      try {
         return persistenciaPeriodicidades.consultarPeriodicidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Periodicidades consultarPeriodicidad(BigInteger secPeriodicidad) {
      try {
         return persistenciaPeriodicidades.consultarPeriodicidad(getEm(), secPeriodicidad);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Unidades> consultarLOVUnidades() {
      try {
         return persistenciaUnidades.consultarUnidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarCPCompromisosPeriodicidad(BigInteger secuenciaPeriodicidades) {
      try {
         log.warn("Secuencia Periodicidades : " + secuenciaPeriodicidades);
         return persistenciaPeriodicidades.contarCPCompromisosPeriodicidad(getEm(), secuenciaPeriodicidades);
      } catch (Exception e) {
         log.error("ERROR AdministrarPeriodicidades contarCPCompromisosPeriodicidad ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarDetallesPeriodicidadesPeriodicidad(BigInteger secuenciaPeriodicidades) {
      try {
         log.warn("Secuencia Periodicidades : " + secuenciaPeriodicidades);
         return persistenciaPeriodicidades.contarDetallesPeriodicidadesPeriodicidad(getEm(), secuenciaPeriodicidades);
      } catch (Exception e) {
         log.error("ERROR AdministrarPeriodicidades contarDetallesPeriodicidadesPeriodicidad ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarEersPrestamosDtosPeriodicidad(BigInteger secuenciaPeriodicidades) {
      try {
         log.warn("Secuencia Periodicidades : " + secuenciaPeriodicidades);
         return persistenciaPeriodicidades.contarEersPrestamosDtosPeriodicidad(getEm(), secuenciaPeriodicidades);
      } catch (Exception e) {
         log.error("ERROR AdministrarPeriodicidades contarEersPrestamosDtosPeriodicidad ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarEmpresasPeriodicidad(BigInteger secuenciaPeriodicidades) {
      try {
         log.warn("Secuencia Periodicidades : " + secuenciaPeriodicidades);
         return persistenciaPeriodicidades.contarEmpresasPeriodicidad(getEm(), secuenciaPeriodicidades);
      } catch (Exception e) {
         log.error("ERROR AdministrarPeriodicidades contarEmpresasPeriodicidad ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarFormulasAseguradasPeriodicidad(BigInteger secuenciaPeriodicidades) {
      try {
         log.warn("Secuencia Periodicidades : " + secuenciaPeriodicidades);
         return persistenciaPeriodicidades.contarFormulasAseguradasPeriodicidad(getEm(), secuenciaPeriodicidades);
      } catch (Exception e) {
         log.error("ERROR AdministrarPeriodicidades contarFormulasAseguradasPeriodicidad ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarFormulasContratosPeriodicidad(BigInteger secuenciaPeriodicidades) {
      try {
         log.warn("Secuencia Periodicidades : " + secuenciaPeriodicidades);
         return persistenciaPeriodicidades.contarFormulasContratosPeriodicidad(getEm(), secuenciaPeriodicidades);
      } catch (Exception e) {
         log.error("ERROR AdministrarPeriodicidades contarFormulasContratosPeriodicidad ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarGruposProvisionesPeriodicidad(BigInteger secuenciaPeriodicidades) {
      try {
         log.warn("Secuencia Periodicidades : " + secuenciaPeriodicidades);
         return persistenciaPeriodicidades.contarGruposProvisionesPeriodicidad(getEm(), secuenciaPeriodicidades);
      } catch (Exception e) {
         log.error("ERROR AdministrarPeriodicidades contarGruposProvisionesPeriodicidad ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarNovedadesPeriodicidad(BigInteger secuenciaPeriodicidades) {
      try {
         log.warn("Secuencia Periodicidades : " + secuenciaPeriodicidades);
         return persistenciaPeriodicidades.contarNovedadesPeriodicidad(getEm(), secuenciaPeriodicidades);
      } catch (Exception e) {
         log.error("ERROR AdministrarPeriodicidades contarNovedadPeriodicidad ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarParametrosCambiosMasivosPeriodicidad(BigInteger secuenciaPeriodicidades) {
      try {
         log.warn("Secuencia Periodicidades : " + secuenciaPeriodicidades);
         return persistenciaPeriodicidades.contarParametrosCambiosMasivosPeriodicidad(getEm(), secuenciaPeriodicidades);
      } catch (Exception e) {
         log.error("ERROR AdministrarPeriodicidades contarParametrosCambiosMasivosPeriodicidad ERROR :" + e);
         return null;
      }
   }

   public BigInteger contarVigenciasFormasPagosPeriodicidad(BigInteger secuenciaPeriodicidades) {
      try {
         log.warn("Secuencia Periodicidades : " + secuenciaPeriodicidades);
         return persistenciaPeriodicidades.contarVigenciasFormasPagosPeriodicidad(getEm(), secuenciaPeriodicidades);
      } catch (Exception e) {
         log.error("ERROR AdministrarPeriodicidades contarVigenciasFormasPagosPeriodicidad ERROR :" + e);
         return null;
      }
   }
}
