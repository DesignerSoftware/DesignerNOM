/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Modulos;
import Entidades.ObjetosDB;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarObjetosDBInterface {

    public void obtenerConexion(String idSesion);

    public String modificarObjetosDB(ObjetosDB objeto);

    public String crearObjetosDB(ObjetosDB objeto);

    public String borrarObjetosDB(ObjetosDB objeto);

    public List<ObjetosDB> consultarObjetosDB();

    public List<Modulos> consultarModulos();

}
