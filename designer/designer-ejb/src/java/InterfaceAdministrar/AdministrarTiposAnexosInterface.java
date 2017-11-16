/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposAnexos;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */

public interface AdministrarTiposAnexosInterface {
    
    public void obtenerConexion(String idSesion);

    public String modificarTiposAnexos(TiposAnexos tiposAnexos);

    public String borrarTiposAnexos(TiposAnexos tiposAnexos);

    public String crearTiposAnexos(TiposAnexos tiposAnexos);

    public TiposAnexos consultarTiposAnexo(BigInteger secTiposAnexos);

    public List<TiposAnexos> consultarTiposAnexos();

    public BigInteger verificarTiposAnexos(BigInteger secTiposAnexos);

}
