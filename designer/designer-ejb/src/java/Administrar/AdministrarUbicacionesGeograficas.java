/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Ciudades;
import Entidades.Empresas;
import Entidades.SucursalesPila;
import Entidades.UbicacionesGeograficas;
import InterfaceAdministrar.AdministrarUbicacionesGeograficasInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaSucursalesPilaInterface;
import InterfacePersistencia.PersistenciaUbicacionesGeograficasInterface;
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
public class AdministrarUbicacionesGeograficas implements AdministrarUbicacionesGeograficasInterface {

   private static Logger log = Logger.getLogger(AdministrarUbicacionesGeograficas.class);

   //-------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaUbicacionesGeograficas'.
    */
   @EJB
   PersistenciaUbicacionesGeograficasInterface persistenciaUbicacionesGeograficas;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaCiudades'.
    */
   @EJB
   PersistenciaCiudadesInterface persistenciaCiudades;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaEmpresas'.
    */
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaSucursalesPila'.
    */
   @EJB
   PersistenciaSucursalesPilaInterface persistenciaSucursalesPila;
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

   //-------------------------------------------------------------------------------------
   @Override
   public List<Empresas> consultarEmpresas() {
      try {
         return persistenciaEmpresas.consultarEmpresas(getEm());
      } catch (Exception e) {
         log.warn("ADMINISTRARUBICACIONESGEOGRAFICAS: Falló al buscar las empresas /n" + e.getMessage());
         return null;
      }
   }

   public void modificarUbicacionesGeograficas(List<UbicacionesGeograficas> listaUbicacionesGeograficas) {
      try {
         for (int i = 0; i < listaUbicacionesGeograficas.size(); i++) {
            if (listaUbicacionesGeograficas.get(i).getCiudad().getSecuencia() == null) {
               listaUbicacionesGeograficas.get(i).setCiudad(null);
            }
         }
         for (int i = 0; i < listaUbicacionesGeograficas.size(); i++) {
            if (listaUbicacionesGeograficas.get(i).getSucursalPila().getSecuencia() == null) {
               listaUbicacionesGeograficas.get(i).setSucursalPila(null);
            }
         }
         for (int i = 0; i < listaUbicacionesGeograficas.size(); i++) {
            persistenciaUbicacionesGeograficas.editar(getEm(), listaUbicacionesGeograficas.get(i));
         }
      } catch (Exception e) {
         log.error("AdministrarUbicacionesGeograficas: Falló al editar el CentroCosto /n" + e.getMessage());
      }
   }

   public void borrarUbicacionesGeograficas(List<UbicacionesGeograficas> listaUbicacionesGeograficas) {
      try {
         for (int i = 0; i < listaUbicacionesGeograficas.size(); i++) {
            if (listaUbicacionesGeograficas.get(i).getCiudad().getSecuencia() == null) {
               listaUbicacionesGeograficas.get(i).setCiudad(null);
            }
         }
         for (int i = 0; i < listaUbicacionesGeograficas.size(); i++) {
            if (listaUbicacionesGeograficas.get(i).getSucursalPila().getSecuencia() == null) {
               listaUbicacionesGeograficas.get(i).setSucursalPila(null);
            }
         }
         for (int i = 0; i < listaUbicacionesGeograficas.size(); i++) {
            log.warn("Borando... sucursalpila : " + listaUbicacionesGeograficas.get(i).getSucursalPila().getSecuencia());
            persistenciaUbicacionesGeograficas.borrar(getEm(), listaUbicacionesGeograficas.get(i));
         }
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARUBICACIONESGEOGRAFICAS.borrarUbicacionesGeograficas ERROR=====" + e.getMessage());
      }
   }

   public void crearUbicacionesGeograficas(List<UbicacionesGeograficas> listaUbicacionesGeograficas) {
      try {
         for (int i = 0; i < listaUbicacionesGeograficas.size(); i++) {
            if (listaUbicacionesGeograficas.get(i).getCiudad().getSecuencia() == null) {
               listaUbicacionesGeograficas.get(i).setCiudad(null);
            }
            if (listaUbicacionesGeograficas.get(i).getSucursalPila().getSecuencia() == null) {
               listaUbicacionesGeograficas.get(i).setSucursalPila(null);
            }
            log.warn("ADMINISTRAR CREANDO...");
            persistenciaUbicacionesGeograficas.crear(getEm(), listaUbicacionesGeograficas.get(i));
         }
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARUBICACIONESGEOGRAFICAS CREARUBICACIONESGEOGRAFICAS : " + e);
      }
   }

   public List<UbicacionesGeograficas> consultarUbicacionesGeograficasPorEmpresa(BigInteger secEmpresa) {
      try {
         log.warn("ENTRE A ADMINISTRARUBICACIONESGEOGRAFICAS.buscarUbicacionesGeograficasPorEmpresa ");
         return persistenciaUbicacionesGeograficas.consultarUbicacionesGeograficasPorEmpresa(getEm(), secEmpresa);
      } catch (Exception e) {
         log.warn("ERROR ADMINISTRARUBICACIONESGEOGRAFICAS CONSULTARUBICACIONESGEOGRAFICASPOREMPRESA ERROR : " + e);
         return null;
      }
   }

   public List<Ciudades> lovCiudades() {
      try {
         return persistenciaCiudades.consultarCiudades(getEm());
      } catch (Exception e) {
         log.warn("\n ADMINISTRARUBICACIONESGEOGRAFICAS LOVCIUDADES ERROR : " + e);
         return null;
      }
   }

   public List<SucursalesPila> lovSucursalesPilaPorEmpresa(BigInteger secEmpresa) {
      try {
         log.warn("AdministrarUbicacionesGeograficas lovSucursalesPilaPorEmpresa : " + secEmpresa);
         return persistenciaSucursalesPila.consultarSucursalesPilaPorEmpresa(getEm(), secEmpresa);
      } catch (Exception e) {
         log.warn("\n ADMINISTRARUBICACIONESGEOGRAFICAS LOVSUCURSALESPILA ERROR : " + e);
         return null;
      }
   }

   public BigInteger contarAfiliacionesEntidadesUbicacionGeografica(BigInteger secuencia) {
      try {
         return persistenciaUbicacionesGeograficas.contarAfiliacionesEntidadesUbicacionGeografica(getEm(), secuencia);
      } catch (Exception e) {
         return null;
      }
   }

   public BigInteger contarInspeccionesUbicacionGeografica(BigInteger secuencia) {
      try {
         return persistenciaUbicacionesGeograficas.contarInspeccionesUbicacionGeografica(getEm(), secuencia);
      } catch (Exception e) {
         return null;
      }
   }

   public BigInteger contarParametrosInformesUbicacionGeografica(BigInteger secuencia) {
      try {
         return persistenciaUbicacionesGeograficas.contarParametrosInformesUbicacionGeografica(getEm(), secuencia);
      } catch (Exception e) {
         return null;
      }
   }

   public BigInteger contarRevisionesUbicacionGeografica(BigInteger secuencia) {
      try {
         return persistenciaUbicacionesGeograficas.contarRevisionesUbicacionGeografica(getEm(), secuencia);
      } catch (Exception e) {
         return null;
      }
   }

   public BigInteger contarVigenciasUbicacionesUbicacionGeografica(BigInteger secuencia) {
      try {
         return persistenciaUbicacionesGeograficas.contarVigenciasUbicacionesGeografica(getEm(), secuencia);
      } catch (Exception e) {
         return null;
      }
   }
}
