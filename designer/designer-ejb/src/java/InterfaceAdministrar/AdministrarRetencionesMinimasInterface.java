/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.RetencionesMinimas;
import Entidades.VigenciasRetencionesMinimas;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarRetencionesMinimasInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public void borrarVigenciaRetencion(VigenciasRetencionesMinimas vretenciones);

    public void crearVigenciaRetencion(VigenciasRetencionesMinimas vretenciones);

    public void modificarVigenciaRetencion(List<VigenciasRetencionesMinimas> listaVigenciasRetencionesModificar);

    public List<VigenciasRetencionesMinimas> consultarVigenciasRetenciones();

    public void borrarRetencion(RetencionesMinimas retenciones);

    public void crearRetencion(RetencionesMinimas retenciones);

    public void modificarRetencion(List<RetencionesMinimas> listaRetencionesModificar);

    public List<RetencionesMinimas> consultarRetenciones(BigInteger secRetencion);

}
