/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposBloques;
import InterfaceAdministrar.AdministrarTiposBloquesInterface;
import InterfacePersistencia.PersistenciaTiposBloquesInterface;
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
public class AdministrarTiposBloques implements AdministrarTiposBloquesInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposBloques.class);

   @EJB
   PersistenciaTiposBloquesInterface persistenciaTiposBloques;
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
   public List<TiposBloques> buscarTiposBloques(BigInteger secuenciaOperando) {
      List<TiposBloques> listaTiposBloques = persistenciaTiposBloques.tiposBloques(em, secuenciaOperando);
//      if (listaTiposBloques != null) {
//         if (!listaTiposBloques.isEmpty()) {
//            log.warn("AdministrarTiposBloques.buscarTiposBloques() pos(0) : " + listaTiposBloques.get(0).getBloqueplsql());
//         }
//      }
      return listaTiposBloques;
   }

   @Override
   public void borrarTiposBloques(TiposBloques tiposConstantes) {
      persistenciaTiposBloques.borrar(em, tiposConstantes);
   }

   @Override
   public void crearTiposBloques(TiposBloques tiposConstantes) {
      persistenciaTiposBloques.crear(em, tiposConstantes);
   }

   @Override
   public void modificarTiposBloques(TiposBloques tiposConstantes) {
      persistenciaTiposBloques.editar(em, tiposConstantes);

   }

}
