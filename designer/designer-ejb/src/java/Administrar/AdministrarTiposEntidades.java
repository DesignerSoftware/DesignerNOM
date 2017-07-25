/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Grupostiposentidades;
import Entidades.TiposEntidades;
import InterfaceAdministrar.AdministrarTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaGruposTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaVigenciasAfiliacionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposEntidades implements AdministrarTiposEntidadesInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposEntidades.class);

   @EJB
   PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
   @EJB
   PersistenciaGruposTiposEntidadesInterface persistenciaGruposTiposEntidades;
   @EJB
   PersistenciaVigenciasAfiliacionesInterface persistenciaVigenciasAfiliaciones;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private EntityManager em;

   @Override
   public void obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   @Override
   public void modificarTipoEntidad(List<TiposEntidades> listTiposEntidadesModificadas) {
      for (int i = 0; i < listTiposEntidadesModificadas.size(); i++) {
         log.warn("Administrar Modificando...");
         persistenciaTiposEntidades.editar(em, listTiposEntidadesModificadas.get(i));
      }
   }

   @Override
   public void borrarTipoEntidad(List<TiposEntidades> listTiposEntidadesModificadas) {
      for (int i = 0; i < listTiposEntidadesModificadas.size(); i++) {
         log.warn("Administrar Borrando...");
         persistenciaTiposEntidades.borrar(em, listTiposEntidadesModificadas.get(i));
      }
   }

   @Override
   public void crearTipoEntidad(List<TiposEntidades> listTiposEntidadesModificadas) {
      for (int i = 0; i < listTiposEntidadesModificadas.size(); i++) {
         log.warn("Administrar Creando...");
         persistenciaTiposEntidades.crear(em, listTiposEntidadesModificadas.get(i));
      }
   }

   @Override
   public List<TiposEntidades> consultarTiposEntidades() {
      List<TiposEntidades> listTiposEntidades;
      listTiposEntidades = persistenciaTiposEntidades.buscarTiposEntidades(em);
      return listTiposEntidades;
   }

   @Override
   public TiposEntidades consultarTipoEntidad(BigInteger secTipoEntidad) {
      TiposEntidades tipoEntidad;
      tipoEntidad = persistenciaTiposEntidades.buscarTiposEntidadesSecuencia(em, secTipoEntidad);
      return tipoEntidad;
   }

   @Override
   public List<Grupostiposentidades> consultarLOVGruposTiposEntidades() {
      List<Grupostiposentidades> listGruposTiposEntidades;
      listGruposTiposEntidades = persistenciaGruposTiposEntidades.consultarGruposTiposEntidades(em);
      return listGruposTiposEntidades;
   }

   @Override
   public BigInteger contarVigenciasAfiliacionesTipoEntidad(BigInteger secuenciaTipoEntidad) {
      BigInteger verificador;

      try {
         return verificador = persistenciaTiposEntidades.verificarBorrado(em, secuenciaTipoEntidad);
      } catch (Exception e) {
         log.error("ERROR AdministrarTipoEntidad verificarBorrado ERROR :" + e);

         return null;
      }
   }

   @Override
   public BigInteger contarFormulasContratosEntidadesTipoEntidad(BigInteger secuenciaTipoEntidad) {
      BigInteger verificador;

      try {
         return verificador = persistenciaTiposEntidades.verificarBorradoFCE(em, secuenciaTipoEntidad);
      } catch (Exception e) {
         log.error("ERROR AdministrarTipoEntidad verificarBorradoFCE ERROR :" + e);

         return null;
      }
   }
}
