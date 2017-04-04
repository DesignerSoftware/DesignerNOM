/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposBloques;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarTiposBloquesInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public List<TiposBloques> buscarTiposBloques(BigInteger secuenciaOperando);

    public void borrarTiposBloques(TiposBloques tiposBloques);

    public void crearTiposBloques(TiposBloques tiposBloques);

    public void modificarTiposBloques(TiposBloques tiposBloques);

}
