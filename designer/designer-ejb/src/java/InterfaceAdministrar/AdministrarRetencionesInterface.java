/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Retenciones;
import Entidades.VigenciasRetenciones;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarRetencionesInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    public String borrarVigenciaRetencion(VigenciasRetenciones vretenciones);

    public String crearVigenciaRetencion(VigenciasRetenciones vretenciones);

    public String modificarVigenciaRetencion(VigenciasRetenciones vretenciones);

    public List<VigenciasRetenciones> consultarVigenciasRetenciones();

    public String borrarRetencion(Retenciones retenciones);

    public String crearRetencion(Retenciones retenciones);

    public String modificarRetencion(Retenciones retenciones);

    public List<Retenciones> consultarRetenciones(BigInteger secRetencion);

}
