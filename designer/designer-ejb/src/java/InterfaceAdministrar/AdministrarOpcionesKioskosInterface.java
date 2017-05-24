/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.OpcionesKioskos;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarOpcionesKioskosInterface {

    public void obtenerConexion(String idSesion);

    public List<OpcionesKioskos> consultarOpcionesKioskos();
}
