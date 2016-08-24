/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Ciudades;
import Entidades.Empleados;
import Entidades.MotivosContratos;
import Entidades.TiposContratos;
import Entidades.VigenciasTiposContratos;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface AdministrarVigenciasTiposContratosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public List<VigenciasTiposContratos> vigenciasTiposContratosEmpleado(BigInteger secEmpleado);

    public void modificarVTC(List<VigenciasTiposContratos> listVTCModificadas);

    public void borrarVTC(VigenciasTiposContratos vigenciasTipoContrato);

    public void crearVTC(VigenciasTiposContratos vigenciasTipoContrato);

    public Empleados buscarEmpleado(BigInteger secuencia);

    public List<Ciudades> ciudades();

    public List<MotivosContratos> motivosContratos();

    public List<TiposContratos> tiposContratos();
}
