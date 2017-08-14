/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarPapelesInterface;
import Entidades.Papeles;
import Entidades.Empresas;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaPapelesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaPantallasInterface;
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
public class AdministrarPapeles implements AdministrarPapelesInterface {

   private static Logger log = Logger.getLogger(AdministrarPapeles.class);

   @EJB
   PersistenciaPapelesInterface persistenciaPapeles;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaPantallasInterface persistenciaPantallas;

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
   public List<Empresas> consultarEmpresas() {
      try {
         List<Empresas> listaEmpresas = persistenciaEmpresas.consultarEmpresas(getEm());
         return listaEmpresas;
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARPAPELES CONSULTAREMPRESAS ERROR : " + e);
         return null;
      }
   }

   @Override
   public void modificarPapeles(List<Papeles> listaPapeles) {
      try {
         for (int i = 0; i < listaPapeles.size(); i++) {
            log.warn("Modificando...");
            persistenciaPapeles.editar(getEm(), listaPapeles.get(i));
         }
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARPAPELES MODIFICARPAPELES ERROR : " + e);
      }
   }

   @Override
   public void borrarPapeles(List<Papeles> listaPapeles) {
      try {
         for (int i = 0; i < listaPapeles.size(); i++) {
            log.warn("Borrando...");
            persistenciaPapeles.borrar(getEm(), listaPapeles.get(i));
         }
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARPAPELES BORRARPAPELES ERROR : " + e);
      }
   }

   @Override
   public void crearPapeles(List<Papeles> listaPapeles) {
      try {
         log.warn("Creando... tamaño " + listaPapeles.size());
         for (int i = 0; i < listaPapeles.size(); i++) {
            log.warn("Creando...");
            persistenciaPapeles.crear(getEm(), listaPapeles.get(i));
         }
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARPAPELES CREARPAPELES ERROR : " + e);
      }
   }

   @Override
   public List<Papeles> consultarPapelesPorEmpresa(BigInteger secEmpresa) {
      try {
         return persistenciaPapeles.consultarPapelesEmpresa(getEm(), secEmpresa);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARPAPELES CONSULTARPAPELESPOREMPRESA ERROR : " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarVigenciasCargosPapel(BigInteger secPapeles) {
      try {
         return persistenciaPapeles.contarVigenciasCargosPapel(getEm(), secPapeles);
      } catch (Exception e) {
         log.warn("ERROR ADMINISTRARPAPELES CONTARVIGENCIASCARGOSPAPEL ERRO : " + e);
         return null;
      }
   }

   @Override
   public String interfaceContable(BigInteger secEmpresa) {
      try {
         return persistenciaPantallas.buscarIntContable(getEm(), secEmpresa);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}
