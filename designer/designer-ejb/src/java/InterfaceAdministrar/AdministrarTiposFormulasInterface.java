/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Formulas;
import Entidades.TiposFormulas;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarTiposFormulasInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public List<TiposFormulas> buscarTiposFormulas(BigInteger secuenciaOperando);

    public void borrarTiposFormulas(TiposFormulas tiposFormulas);

    public void crearTiposFormulas(TiposFormulas tiposFormulas);
      
    public void modificarTiposFormulas(TiposFormulas tiposFormulas);
    
    public List<Formulas> lovFormulas();
    
}
