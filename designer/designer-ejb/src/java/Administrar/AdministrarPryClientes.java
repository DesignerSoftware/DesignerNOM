/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.PryClientes;
import InterfaceAdministrar.AdministrarPryClientesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaPryClientesInterface;
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
public class AdministrarPryClientes implements AdministrarPryClientesInterface {

   private static Logger log = Logger.getLogger(AdministrarPryClientes.class);

   @EJB
   PersistenciaPryClientesInterface persistenciaPryClientes;
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
   public void modificarPryClientes(List<PryClientes> listaPryClientes) {
      try {
         for (int i = 0; i < listaPryClientes.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaPryClientes.editar(getEm(), listaPryClientes.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarPryClientes() ERROR: " + e);
      }
   }

   @Override
   public void borrarPryClientes(List<PryClientes> listaPryClientes) {
      try {
         for (int i = 0; i < listaPryClientes.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaPryClientes.borrar(getEm(), listaPryClientes.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarPryClientes() ERROR: " + e);
      }
   }

   @Override
   public void crearPryClientes(List<PryClientes> listaPryClientes) {
      try {
         for (int i = 0; i < listaPryClientes.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaPryClientes.crear(getEm(), listaPryClientes.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearPryClientes() ERROR: " + e);
      }
   }

   @Override
   public List<PryClientes> consultarPryClientes() {
      try {
         return persistenciaPryClientes.buscarPryClientes(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarPryClientes() ERROR: " + e);
         return null;
      }
   }

   @Override
   public PryClientes consultarPryCliente(BigInteger secPryClientes) {
      try {
         return persistenciaPryClientes.buscarPryCliente(getEm(), secPryClientes);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarPryCliente() ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarProyectosPryCliente(BigInteger secuenciaProyectos) {
      try {
         log.error("Secuencia Borrado Competencias Cargos" + secuenciaProyectos);
         return persistenciaPryClientes.contadorProyectos(getEm(), secuenciaProyectos);
      } catch (Exception e) {
         log.error("ERROR AdministrarPryClientes verificarBorradoProyecto ERROR :" + e);
         return null;
      }
   }
}
