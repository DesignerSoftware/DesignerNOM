/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empresas;
import Entidades.Monedas;
import Entidades.Proyectos;
import Entidades.PryClientes;
import Entidades.PryPlataformas;
import InterfaceAdministrar.AdministrarProyectosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaMonedasInterface;
import InterfacePersistencia.PersistenciaProyectosInterface;
import InterfacePersistencia.PersistenciaPryClientesInterface;
import InterfacePersistencia.PersistenciaPryPlataformasInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Viktor
 */
@Stateful
public class AdministrarProyectos implements AdministrarProyectosInterface {

   private static Logger log = Logger.getLogger(AdministrarProyectos.class);

   @EJB
   PersistenciaProyectosInterface persistenciaProyectos;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   @EJB
   PersistenciaPryClientesInterface persistenciaPryCliente;
   @EJB
   PersistenciaMonedasInterface persistenciaMonedas;
   @EJB
   PersistenciaPryPlataformasInterface persistenciaPryPlataformas;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private EntityManagerFactory emf;
   private EntityManager em;

   private EntityManager getEm() {
      try {
         if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<Proyectos> Proyectos() {
      try {
         return persistenciaProyectos.proyectos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Proyectos> lovProyectos() {
      try {
         return persistenciaProyectos.proyectos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearProyectos(List<Proyectos> crearList) {
      try {
         for (int i = 0; i < crearList.size(); i++) {
            if (crearList.get(i).getEmpresa().getSecuencia() == null) {
               crearList.get(i).setEmpresa(null);
            }
            if (crearList.get(i).getPryCliente().getSecuencia() == null) {
               crearList.get(i).setPryCliente(null);
            }
            if (crearList.get(i).getPryPlataforma().getSecuencia() == null) {
               crearList.get(i).setPryPlataforma(null);
            }
            if (crearList.get(i).getTipomoneda().getSecuencia() == null) {
               crearList.get(i).setTipomoneda(null);
            }
            persistenciaProyectos.crear(getEm(), crearList.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearProyectos Admi : " + e.toString());
      }
   }

   @Override
   public void editarProyectos(List<Proyectos> editarList) {
      try {
         for (int i = 0; i < editarList.size(); i++) {
            if (editarList.get(i).getEmpresa().getSecuencia() == null) {
               editarList.get(i).setEmpresa(null);
            }
            if (editarList.get(i).getPryCliente().getSecuencia() == null) {
               editarList.get(i).setPryCliente(null);
            }
            if (editarList.get(i).getPryPlataforma().getSecuencia() == null) {
               editarList.get(i).setPryPlataforma(null);
            }
            if (editarList.get(i).getTipomoneda().getSecuencia() == null) {
               editarList.get(i).setTipomoneda(null);
            }
            persistenciaProyectos.editar(getEm(), editarList.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarProyectos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarProyectos(List<Proyectos> borrarList) {
      try {
         for (int i = 0; i < borrarList.size(); i++) {
            if (borrarList.get(i).getEmpresa().getSecuencia() == null) {
               borrarList.get(i).setEmpresa(null);
            }
            if (borrarList.get(i).getPryCliente().getSecuencia() == null) {
               borrarList.get(i).setPryCliente(null);
            }
            if (borrarList.get(i).getPryPlataforma().getSecuencia() == null) {
               borrarList.get(i).setPryPlataforma(null);
            }
            if (borrarList.get(i).getTipomoneda().getSecuencia() == null) {
               borrarList.get(i).setTipomoneda(null);
            }
            persistenciaProyectos.borrar(getEm(), borrarList.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarProyectos Admi : " + e.toString());
      }
   }

   @Override
   public List<PryClientes> listPryClientes() {
      try {
         return persistenciaPryCliente.buscarPryClientes(getEm());
      } catch (Exception e) {
         log.warn("Error listPryClientes Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<PryPlataformas> listPryPlataformas() {
      try {
         return persistenciaPryPlataformas.buscarPryPlataformas(getEm());
      } catch (Exception e) {
         log.warn("Error listPryPlataformas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empresas> listEmpresas() {
      try {
         return persistenciaEmpresas.consultarEmpresas(getEm());
      } catch (Exception e) {
         log.warn("Error listEmpresas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Monedas> listMonedas() {
      try {
         return persistenciaMonedas.consultarMonedas(getEm());
      } catch (Exception e) {
         log.warn("Error listMonedas Admi : " + e.toString());
         return null;
      }
   }
}
