/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.Indicadores;
import Entidades.TiposIndicadores;
import Entidades.VigenciasIndicadores;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarEmplVigenciaIndicadorInterface {

    public List<Indicadores> listIndicadores();

    public List<TiposIndicadores> listTiposIndicadores();

    public Empleados empleadoActual(BigInteger secuencia);

    public List<VigenciasIndicadores> listVigenciasIndicadoresEmpleado(BigInteger secuencia);

    public void crearVigenciasIndicadores(List<VigenciasIndicadores> listVI);

    public void editarVigenciasIndicadores(List<VigenciasIndicadores> listVI);

    public void borrarVigenciasIndicadores(List<VigenciasIndicadores> listVI);

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
