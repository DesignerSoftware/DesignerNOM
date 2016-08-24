/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Demandas;
import Entidades.Empleados;
import Entidades.MotivosDemandas;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarEmplDemandaInterface {

    public Empleados actualEmpleado(BigInteger secuencia);

    public List<MotivosDemandas> listMotivosDemandas();

    public List<Demandas> listDemandasEmpleadoSecuencia(BigInteger secuencia);

    public void crearDemandas(List<Demandas> listD);

    public void editarDemandas(List<Demandas> listD);

    public void borrarDemandas(List<Demandas> listD);

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
