/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposFunciones;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarTiposFuncionesInterface {

   /**
    * Método encargado de obtener el Entity Manager el cual tiene asociado la
    * sesion del usuario que utiliza el aplicativo.
    *
    * @param idSesion Identificador se la sesion.
    */
   public void obtenerConexion(String idSesion);

   public List<TiposFunciones> buscarTiposFunciones(BigInteger secuenciaOperando);

   public void borrarTiposFunciones(TiposFunciones tiposFunciones);

   public void crearTiposFunciones(TiposFunciones tiposFunciones);

   public void modificarTiposFunciones(List<TiposFunciones> listaTiposFuncionesModificar);

}
