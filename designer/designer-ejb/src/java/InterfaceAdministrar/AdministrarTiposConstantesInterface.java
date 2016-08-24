/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfaceAdministrar;

import Entidades.TiposConstantes;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarTiposConstantesInterface {
    
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public List<TiposConstantes> buscarTiposConstantes(BigInteger secuenciaOperando, String tipoOperando);

    public void borrarTiposConstantes(TiposConstantes tiposConstantes);

    public void crearTiposConstantes(TiposConstantes tiposConstantes);
      
    public void modificarTiposConstantes(TiposConstantes tiposConstantes);
    
}
