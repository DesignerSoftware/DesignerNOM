/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposDotaciones;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarTiposDotacionesInterface {

    public void obtenerConexion(String idSesion);

    public String modificarTiposDotaciones(TiposDotaciones tipoDotacion);

    public String borrarTiposDotaciones(TiposDotaciones tipoDotacion);

    public String crearTiposDotaciones(TiposDotaciones tipoDotacion);

    public List<TiposDotaciones> consultarTiposDotaciones();
}
