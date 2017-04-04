/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposFunciones;
import InterfaceAdministrar.AdministrarTiposFuncionesInterface;
import InterfacePersistencia.PersistenciaTiposFuncionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposFunciones implements AdministrarTiposFuncionesInterface {

   @EJB
   PersistenciaTiposFuncionesInterface persistenciaTiposFunciones;
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
   public List<TiposFunciones> buscarTiposFunciones(BigInteger secuenciaOperando) {
      List<TiposFunciones> listaTiposFunciones;
      listaTiposFunciones = persistenciaTiposFunciones.tiposFunciones(em, secuenciaOperando);
      return listaTiposFunciones;
   }

   @Override
   public void borrarTiposFunciones(TiposFunciones tiposFunciones) {
      persistenciaTiposFunciones.borrar(em, tiposFunciones);
   }

   @Override
   public void crearTiposFunciones(TiposFunciones tiposFunciones) {
      persistenciaTiposFunciones.crear(em, tiposFunciones);
   }

   @Override
   public void modificarTiposFunciones(List<TiposFunciones> listaTiposFuncionesModificar) {
      for (int i = 0; i < listaTiposFuncionesModificar.size(); i++) {
         System.out.println("Modificando...");
         if (listaTiposFuncionesModificar.get(i).getFechafinal() == null) {
            listaTiposFuncionesModificar.get(i).setFechafinal(null);
         }
         if (listaTiposFuncionesModificar.get(i).getNombreobjeto() == null) {
            listaTiposFuncionesModificar.get(i).setNombreobjeto(null);
         }
         persistenciaTiposFunciones.editar(em, listaTiposFuncionesModificar.get(i));
      }
   }
}
