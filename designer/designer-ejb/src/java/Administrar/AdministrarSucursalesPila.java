/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empresas;
import Entidades.SucursalesPila;
import InterfaceAdministrar.AdministrarSucursalesPilaInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaSucursalesPilaInterface;
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
public class AdministrarSucursalesPila implements AdministrarSucursalesPilaInterface {

   private static Logger log = Logger.getLogger(AdministrarSucursalesPila.class);

   @EJB
   PersistenciaSucursalesPilaInterface persistenciaSucursalesPila;
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

   //-------------------------------------------------------------------------------------
   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<Empresas> buscarEmpresas() {
      try {
         List<Empresas> listaEmpresas = persistenciaEmpresas.consultarEmpresas(getEm());
         return listaEmpresas;
      } catch (Exception e) {
         log.warn("AdministrarCentroCostos: Falló al buscar las empresas /n" + e.getMessage());
         return null;
      }
   }

   public void modificarSucursalesPila(List<SucursalesPila> listaSucursalesPila) {
      try {
         for (int i = 0; i < listaSucursalesPila.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaSucursalesPila.editar(getEm(), listaSucursalesPila.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarSucursalesPila() ERROR: " + e);
      }
   }

   public void borrarSucursalesPila(List<SucursalesPila> listaSucursalesPila) {
      try {
         for (int i = 0; i < listaSucursalesPila.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaSucursalesPila.borrar(getEm(), listaSucursalesPila.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarSucursalesPila() ERROR: " + e);
      }
   }

   public void crearSucursalesPila(List<SucursalesPila> listaSucursalesPila) {
      try {
         for (int i = 0; i < listaSucursalesPila.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaSucursalesPila.crear(getEm(), listaSucursalesPila.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearSucursalesPila() ERROR: " + e);
      }
   }

   public List<SucursalesPila> consultarSucursalesPila() {
      try {
         return persistenciaSucursalesPila.consultarSucursalesPila(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarSucursalesPila() ERROR: " + e);
         return null;
      }
   }

   public List<SucursalesPila> consultarSucursalPila(BigInteger secSucursalesPila) {
      try {
         return persistenciaSucursalesPila.consultarSucursalesPilaPorEmpresa(getEm(), secSucursalesPila);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarSucursalPila() ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarNovedadesAutoLiquidacionesSucursal_Pila(BigInteger secSucursalesPila) {
      try {
         return persistenciaSucursalesPila.contarNovedadesAutoLiquidacionesSucursal_Pila(getEm(), secSucursalesPila);
      } catch (Exception e) {
         log.error("ERROR AdministrarSucursalesPila contarNovedadesAutoLiquidacionesSucursal_Pila ERROR : " + e);
         return null;
      }
   }

   public BigInteger contarNovedadesCorreccionesAutolSucursal_Pila(BigInteger secSucursalesPila) {
      try {
         return persistenciaSucursalesPila.contarNovedadesCorreccionesAutolSucursal_Pila(getEm(), secSucursalesPila);
      } catch (Exception e) {
         log.error("ERROR AdministrarSucursalesPila contarNovedadesCorreccionesAutolSucursal_Pila ERROR : " + e);
         return null;
      }
   }

   public BigInteger contarOdisCabecerasSucursal_Pila(BigInteger secSucursalesPila) {
      try {
         return persistenciaSucursalesPila.contarOdisCabecerasSucursal_Pila(getEm(), secSucursalesPila);
      } catch (Exception e) {
         log.error("ERROR AdministrarSucursalesPila contarOdisCabecerasSucursal_Pila ERROR : " + e);
         return null;
      }
   }

   public BigInteger contarOdiscorReaccionesCabSucursal_Pila(BigInteger secSucursalesPila) {
      try {
         return persistenciaSucursalesPila.contarOdiscorReaccionesCabSucursal_Pila(getEm(), secSucursalesPila);
      } catch (Exception e) {
         log.error("ERROR AdministrarSucursalesPila contarOdiscorReaccionesCabSucursal_Pila ERROR : " + e);
         return null;
      }
   }

   public BigInteger contarParametrosInformesSucursal_Pila(BigInteger secSucursalesPila) {
      try {
         return persistenciaSucursalesPila.contarParametrosInformesSucursal_Pila(getEm(), secSucursalesPila);
      } catch (Exception e) {
         log.error("ERROR AdministrarSucursalesPila contarParametrosInformesSucursal_Pila ERROR : " + e);
         return null;
      }
   }

   public BigInteger contarUbicacionesGeograficasSucursal_Pila(BigInteger secSucursalesPila) {
      try {
         return persistenciaSucursalesPila.contarUbicacionesGeograficasSucursal_Pila(getEm(), secSucursalesPila);
      } catch (Exception e) {
         log.error("ERROR AdministrarSucursalesPila contarUbicacionesGeograficasSucursal_Pila ERROR : " + e);
         return null;
      }
   }

}
