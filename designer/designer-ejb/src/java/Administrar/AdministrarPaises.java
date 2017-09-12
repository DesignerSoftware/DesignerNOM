/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Ciudades;
import Entidades.Departamentos;
import Entidades.Festivos;
import InterfaceAdministrar.AdministrarPaisesInterface;
import Entidades.Paises;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaDepartamentosInterface;
import InterfacePersistencia.PersistenciaFestivosInterface;
import InterfacePersistencia.PersistenciaPaisesInterface;
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
public class AdministrarPaises implements AdministrarPaisesInterface {

   private static Logger log = Logger.getLogger(AdministrarPaises.class);

   @EJB
   PersistenciaPaisesInterface persistenciaPaises;
   @EJB
   PersistenciaFestivosInterface persistenciaFestivos;
   @EJB
   PersistenciaCiudadesInterface persistenciaCiudades;
   @EJB
   PersistenciaDepartamentosInterface persistenciaDepartamentos;
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
   public void modificarPaises(List<Paises> listaPaises) {
      try {
         for (int i = 0; i < listaPaises.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaPaises.editar(getEm(), listaPaises.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarPaises() ERROR: " + e);
      }
   }

   @Override
   public void borrarPaises(List<Paises> listaPaises) {
      try {
         for (int i = 0; i < listaPaises.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaPaises.borrar(getEm(), listaPaises.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarPaises() ERROR: " + e);
      }
   }

   @Override
   public void crearPaises(List<Paises> listaPaises) {
      try {
         for (int i = 0; i < listaPaises.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaPaises.crear(getEm(), listaPaises.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearPaises() ERROR: " + e);
      }
   }

   public List<Paises> consultarPaises() {
      try {
         return persistenciaPaises.consultarPaises(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarPaises() ERROR: " + e);
         return null;
      }
   }

   @Override
   public Paises consultarPais(BigInteger secPaises) {
      try {
         return persistenciaPaises.consultarPais(getEm(), secPaises);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarPais() ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarDepartamentosPais(BigInteger secPaises) {
      try {
         return persistenciaPaises.contarDepartamentosPais(getEm(), secPaises);
      } catch (Exception e) {
         log.error("ERROR AdministrarPaises contarDepartamentosPais ERROR : " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarFestivosPais(BigInteger secPaises) {
      try {
         return persistenciaPaises.contarFestivosPais(getEm(), secPaises);
      } catch (Exception e) {
         log.error("ERROR AdministrarPaises contarFestivosPais ERROR : " + e);
         return null;
      }
   }

   @Override
   public List<Festivos> consultarFestivosPorPais(BigInteger secPais) {
      try {
         return persistenciaFestivos.consultarFestivosPais(getEm(), secPais);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarFestivosPorPais() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Ciudades> consultarCiudadesPorDepto(BigInteger secDepto) {
      try {
         return persistenciaCiudades.consultarCiudadesPorDepto(getEm(), secDepto);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarCiudadesPorDepto() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Departamentos> consultarDeptosPorPais(BigInteger secPais) {
      try {
         return persistenciaDepartamentos.consultarDepartamentosPorPais(getEm(), secPais);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarDeptosPorPais() ERROR: " + e);
         return null;
      }
   }
}
