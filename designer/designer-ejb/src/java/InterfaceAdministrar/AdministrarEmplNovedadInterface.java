/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Conceptos;
import Entidades.Empleados;
import Entidades.Novedades;
import Entidades.Periodicidades;
import Entidades.Terceros;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarEmplNovedadInterface {

    public List<Novedades> listNovedadesEmpleado(BigInteger secuenciaE);

    public Empleados actualEmpleado(BigInteger secuencia);

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public List<Conceptos> lovConceptos();

    public List<Periodicidades> lovPeriodicidades();

    public List<Terceros> lovTerceros();
    
     public void editarNovedad(List<Novedades> listaModificar);

    public void borrarNovedad(List<Novedades> listaBorrar);
    
}
