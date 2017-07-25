/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Operadores;
import InterfaceAdministrar.AdministrarOperadoresInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaOperadoresInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarOperadores implements AdministrarOperadoresInterface {

   private static Logger log = Logger.getLogger(AdministrarOperadores.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaOperadoresInterface persistenciaOperadores;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Operadores> listOperadores() {
        List<Operadores> lista = persistenciaOperadores.buscarOperadores(em);
        return lista;
    }

}
