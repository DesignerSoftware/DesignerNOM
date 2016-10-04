/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.IndicesExternos;
import Entidades.ResultadosIndicesExternos;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarResultadosIndicesExternosInterface {
 public void obtenerConexion(String idSesion);
public void crearResultado(List<ResultadosIndicesExternos> listaCrear);
public void modificarResultado(List<ResultadosIndicesExternos> listaModificar);
public void borrarResultado(List<ResultadosIndicesExternos> listaBorrar);
public List<IndicesExternos> consultarIndicesExternos();
public List<ResultadosIndicesExternos> consultarResultadosIndicesExternos();
    
    
}
