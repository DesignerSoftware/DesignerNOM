/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.OdisCabeceras;
import Entidades.OdisDetalles;
import Entidades.RelacionesIncapacidades;
import Entidades.SucursalesPila;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import InterfaceAdministrar.AdministrarOdiCabeceraInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaOdiCabeceraInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
@Local
public class AdministrarOdiCabecera implements AdministrarOdiCabeceraInterface {

   private static Logger log = Logger.getLogger(AdministrarOdiCabecera.class);

   @EJB
   PersistenciaOdiCabeceraInterface persistenciaOdicabecera;
//    @EJB
//    PersistenciaVWActualesTiposTrabajadoresInterface persistenciaVWActualesTiposTrabajadores;
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
   public void borrar(OdisCabeceras odicabecera) {
      try {
         persistenciaOdicabecera.borrar(getEm(), odicabecera);
      } catch (Exception e) {
         log.warn("Error AdministrarOdiCabecera.borrar : " + e.toString());
      }
   }

   @Override
   public void crear(OdisCabeceras odicabecera) {
      try {
         persistenciaOdicabecera.crear(getEm(), odicabecera);
      } catch (Exception e) {
         log.warn("Error AdministrarOdiCabecera.crear : " + e.toString());
      }
   }

   @Override
   public void editar(OdisCabeceras odicabecera) {
      try {
         persistenciaOdicabecera.editar(getEm(), odicabecera);
      } catch (Exception e) {
         log.warn("Error AdministrarOdiCabecera.editar : " + e.toString());
      }
   }

   @Override
   public List<Empleados> lovEmpleados() {
      try {
         return persistenciaOdicabecera.lovEmpleados(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".lovEmpleados() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Empresas> lovEmpresas() {
      try {
         return persistenciaOdicabecera.lovEmpresas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".lovEmpresas() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Terceros> lovTerceros(BigInteger anio, BigInteger mes) {
      try {
         return persistenciaOdicabecera.lovTerceros(getEm(), anio, mes);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".lovTerceros() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposEntidades> lovtiposEntidades(BigInteger anio, BigInteger mes) {
      try {
         return persistenciaOdicabecera.lovTiposEntidades(getEm(), anio, mes);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".lovtiposEntidades() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<RelacionesIncapacidades> lovRelacionesIncapacidades(BigInteger secuenciaEmpleado) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public List<SucursalesPila> lovSucursales(BigInteger secuenciaEmpresa) {
      try {
         return persistenciaOdicabecera.lovSucursalesPila(getEm(), secuenciaEmpresa);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".lovSucursales() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<OdisCabeceras> listaNovedades(BigInteger anio, BigInteger mes, BigInteger secuenciaEmpresa) {
      try {
         return persistenciaOdicabecera.listOdisCabeceras(getEm(), anio, mes, secuenciaEmpresa);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".listaNovedades() ERROR: " + e);
         return null;
      }
   }

   @Override
   public void borrarDetalle(OdisDetalles odidetalle) {
      try {
         persistenciaOdicabecera.borrarDetalle(getEm(), odidetalle);
      } catch (Exception e) {
         log.warn("Error AdministrarOdiCabecera.borrarDetalle : " + e.toString());
      }
   }

   @Override
   public void crearDetalle(OdisDetalles odidetalle) {
      try {
         persistenciaOdicabecera.crearDetalle(getEm(), odidetalle);
      } catch (Exception e) {
         log.warn("Error AdministrarOdiCabecera.crearDetalle : " + e.toString());
      }
   }

   @Override
   public void editarDetalle(OdisDetalles odidetalle) {
      try {
         persistenciaOdicabecera.editarDetalle(getEm(), odidetalle);
      } catch (Exception e) {
         log.warn("Error AdministrarOdiCabecera.editarDetalle : " + e.toString());
      }
   }

}
