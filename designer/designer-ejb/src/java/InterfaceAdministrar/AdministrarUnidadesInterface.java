/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposUnidades;
import Entidades.Unidades;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarUnidadesInterface {

    public void obtenerConexion(String idSesion);

    public List<Unidades> consultarUnidades();
    
    public List<TiposUnidades> consultarTiposUnidades();
    public void borrarUnidades(List<Unidades> listaUnidades);
    public void crearUnidades(List<Unidades> listaUnidades);
    public void modificarUnidades(List<Unidades> listaUnidades);
}
