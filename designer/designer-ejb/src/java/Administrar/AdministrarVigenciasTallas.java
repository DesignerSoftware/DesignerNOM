/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.TiposTallas;
import Entidades.VigenciasTallas;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarVigenciasTallasInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaTiposTallasInterface;
import InterfacePersistencia.PersistenciaVigenciasTallasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarVigenciasTallas implements AdministrarVigenciasTallasInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasTallas.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaVigenciasTallasInterface persistenciaVigenciasTallas;
   @EJB
   PersistenciaTiposTallasInterface persistenciaTiposTallas;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   private EntityManager em;

   public void obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   public void modificarVigenciasTallas(List<VigenciasTallas> listaVigenciasTallas) {
      for (int i = 0; i < listaVigenciasTallas.size(); i++) {
         log.warn("Administrar Modificando...");
         persistenciaVigenciasTallas.editar(em, listaVigenciasTallas.get(i));
      }
   }

   public void borrarVigenciasTallas(List<VigenciasTallas> listaVigenciasTallas) {
      for (int i = 0; i < listaVigenciasTallas.size(); i++) {
         log.warn("Administrar Borrando...");
         persistenciaVigenciasTallas.borrar(em, listaVigenciasTallas.get(i));
      }
   }

   public void crearVigenciasTallas(List<VigenciasTallas> listaVigenciasTallas) {
      for (int i = 0; i < listaVigenciasTallas.size(); i++) {
         log.warn("Administrar Creando...");
         persistenciaVigenciasTallas.crear(em, listaVigenciasTallas.get(i));
      }
   }

   public List<VigenciasTallas> consultarVigenciasTallasPorEmpleado(BigInteger secPersona) {
      List<VigenciasTallas> listMotivosCambiosCargos;
      listMotivosCambiosCargos = persistenciaVigenciasTallas.consultarVigenciasTallasPorPersona(em, secPersona);
      return listMotivosCambiosCargos;
   }

   @Override
   public List<TiposTallas> consultarLOVTiposTallas() {
      List<TiposTallas> listTiposTallas = persistenciaTiposTallas.buscarTiposTallas(em);
      return listTiposTallas;
   }

   public Empleados consultarEmpleadoSecuenciaPersona(BigInteger secuencia) {
      Empleados persona;
      try {
         persona = persistenciaEmpleado.buscarEmpleadoSecuenciaPersona(em, secuencia);
         return persona;
      } catch (Exception e) {
         persona = null;
         log.warn("ERROR AdministrarHvReferencias  consultarEmpleadoSecuenciaPersona ERROR =====" + e);
         return persona;
      }
   }

}
