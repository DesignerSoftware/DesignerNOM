/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposJornadas;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarTiposJornadasInterface {

    public void obtenerConexion(String idSesion);

    public String modificarTipoJornada(TiposJornadas tiposJornadas);

    public String borrarTipoJornada(TiposJornadas tiposJornadas);

    public String crearTipoJornada(TiposJornadas tiposJornadas);

    public TiposJornadas consultarTipoJornada(BigInteger secTiposJornadas);

    public List<TiposJornadas> consultarTipoJornada();
}
