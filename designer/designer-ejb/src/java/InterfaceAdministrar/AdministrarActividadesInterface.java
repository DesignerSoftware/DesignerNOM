/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Actividades;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarActividadesInterface {

    public void modificarActividades(List<Actividades> listaActividades);

    public void borrarActividades(List<Actividades> listaActividades);

    public void crearActividades(List<Actividades> listaActividades);

    public List<Actividades> consultarActividades();

    public BigInteger contarBienNecesidadesActividad(BigInteger secuenciaActividades);

    public BigInteger contarParametrosInformesActividad(BigInteger secuenciaActividades);
    
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
