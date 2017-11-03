/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.BloquesPantallas;
import Entidades.ObjetosBloques;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarObjetosBloquesInterface {
   public void obtenerConexion(String idSesion);

    public String modificar(ObjetosBloques objeto);

    public String crear(ObjetosBloques objeto);

    public String borrar(ObjetosBloques objeto);

    public List<ObjetosBloques> consultarObjetosBloques();

    public List<BloquesPantallas> consultarBloquesPantallas(); 
}
