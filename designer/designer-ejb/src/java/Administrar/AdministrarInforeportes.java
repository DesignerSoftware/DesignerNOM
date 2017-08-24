/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Inforeportes;
import Entidades.Modulos;
import InterfaceAdministrar.AdministrarInforeportesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaInforeportesInterface;
import InterfacePersistencia.PersistenciaModulosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarInforeportes implements AdministrarInforeportesInterface {

   private static Logger log = Logger.getLogger(AdministrarInforeportes.class);

   @EJB
   PersistenciaInforeportesInterface persistenciaInforeportes;
   @EJB
   PersistenciaModulosInterface persistenciaModulos;
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

   public List<Inforeportes> inforeportes() {
      try {
         return persistenciaInforeportes.buscarInforeportes(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public Modulos buscarModuloPorSecuencia(BigInteger secModulo) {
      try {
         return persistenciaModulos.buscarModulosPorSecuencia(getEm(), secModulo);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Modulos> lovmodulos() {
      try {
         return persistenciaModulos.buscarModulos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void borrarInforeporte(Inforeportes inforeportes) {
      try {
         persistenciaInforeportes.borrar(getEm(), inforeportes);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearInforeporte(Inforeportes inforeportes) {
      try {
         persistenciaInforeportes.crear(getEm(), inforeportes);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarInforeporte(List<Inforeportes> listaInforeportesModificar) {
      try {
         for (int i = 0; i < listaInforeportesModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaInforeportesModificar.get(i).getAficion() == null) {
               listaInforeportesModificar.get(i).setAficion(null);
            }
            if (listaInforeportesModificar.get(i).getCiudad() == null) {
               listaInforeportesModificar.get(i).setCiudad(null);
            }
            if (listaInforeportesModificar.get(i).getCodigo() == null) {
               listaInforeportesModificar.get(i).setCodigo(null);
            }
            if (listaInforeportesModificar.get(i).getContador() == null) {
               listaInforeportesModificar.get(i).setContador(null);
            }
            if (listaInforeportesModificar.get(i).getDeporte() == null) {
               listaInforeportesModificar.get(i).setDeporte(null);
            }
            if (listaInforeportesModificar.get(i).getEmdesde() == null) {
               listaInforeportesModificar.get(i).setEmdesde(null);
            }
            if (listaInforeportesModificar.get(i).getEmhasta() == null) {
               listaInforeportesModificar.get(i).setEmhasta(null);
            }
            if (listaInforeportesModificar.get(i).getEnviomasivo() == null) {
               listaInforeportesModificar.get(i).setEnviomasivo(null);
            }
            if (listaInforeportesModificar.get(i).getEstado() == null) {
               listaInforeportesModificar.get(i).setEstado(null);
            }
            if (listaInforeportesModificar.get(i).getEstadocivil() == null) {
               listaInforeportesModificar.get(i).setEstadocivil(null);
            }
            if (listaInforeportesModificar.get(i).getFecdesde() == null) {
               listaInforeportesModificar.get(i).setFecdesde(null);
            }
            if (listaInforeportesModificar.get(i).getFechasta() == null) {
               listaInforeportesModificar.get(i).setFechasta(null);
            }
            if (listaInforeportesModificar.get(i).getGrupo() == null) {
               listaInforeportesModificar.get(i).setGrupo(null);
            }
            if (listaInforeportesModificar.get(i).getIdioma() == null) {
               listaInforeportesModificar.get(i).setIdioma(null);
            }
            if (listaInforeportesModificar.get(i).getJefedivision() == null) {
               listaInforeportesModificar.get(i).setJefedivision(null);
            }
            if (listaInforeportesModificar.get(i).getLocalizacion() == null) {
               listaInforeportesModificar.get(i).setLocalizacion(null);
            }
            if (listaInforeportesModificar.get(i).getModulo().getSecuencia() == null) {
               listaInforeportesModificar.get(i).setModulo(null);
            }
            if (listaInforeportesModificar.get(i).getNombre() == null) {
               listaInforeportesModificar.get(i).setNombre(null);
            }
            if (listaInforeportesModificar.get(i).getNombrereporte() == null) {
               listaInforeportesModificar.get(i).setNombrereporte(null);
            }
            if (listaInforeportesModificar.get(i).getRodamiento() == null) {
               listaInforeportesModificar.get(i).setRodamiento(null);
            }
            if (listaInforeportesModificar.get(i).getSolicitud() == null) {
               listaInforeportesModificar.get(i).setSolicitud(null);
            }
            if (listaInforeportesModificar.get(i).getTercero() == null) {
               listaInforeportesModificar.get(i).setTercero(null);
            }
            if (listaInforeportesModificar.get(i).getTipo() == null) {
               listaInforeportesModificar.get(i).setTipo(null);
            }
            if (listaInforeportesModificar.get(i).getTipotelefono() == null) {
               listaInforeportesModificar.get(i).setTipotelefono(null);
            }
            if (listaInforeportesModificar.get(i).getTipotrabajador() == null) {
               listaInforeportesModificar.get(i).setTipotrabajador(null);
            }
            if (listaInforeportesModificar.get(i).getTrabajador() == null) {
               listaInforeportesModificar.get(i).setTrabajador(null);
            }
            persistenciaInforeportes.editar(getEm(), listaInforeportesModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

    @Override
    public List<Inforeportes> listaInfoReportes() {
       return persistenciaInforeportes.buscarInforeportesAdminReportes(getEm());
    }

    @Override
    public List<Modulos> listaModulos() {
        return persistenciaModulos.listaModulos(getEm());
    }

}
