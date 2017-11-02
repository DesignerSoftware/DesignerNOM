/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposIndices;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarTiposIndicesInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public String modificarTiposIndices(TiposIndices tiposIndice);

    public String borrarTiposIndices(TiposIndices tiposIndice);

    public String crearTiposIndices(TiposIndices tiposIndice);

    public List<TiposIndices> consultarTiposIndices();

    public TiposIndices consultarTipoIndice(BigInteger secTiposIndices);

    public BigInteger contarIndicesTipoIndice(BigInteger secTiposIndices);
}
