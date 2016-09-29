/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Indicadores;
import Entidades.TiposIndicadores;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarIndicadoresInterface {

public void obtenerConexion(String idSesion);
public void crearIndicador(List<Indicadores> listaCrear);
public void modificarIndicador(List<Indicadores> listaModificar);
public void borrarIndicador(List<Indicadores> listaBorrar);
public List<Indicadores> consultarIndicadores();
public List<TiposIndicadores> consultarTiposIndicadores();
    
}
