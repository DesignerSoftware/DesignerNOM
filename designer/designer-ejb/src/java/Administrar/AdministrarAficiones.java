/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Aficiones;
import InterfaceAdministrar.AdministrarAficionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaAficionesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarAficiones implements AdministrarAficionesInterface {
    
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaAficionesInterface persistenciaAficiones;
    
    private EntityManager em;
    
    @Override
    public void obtenerConexion(String idSesion) {
         em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void crearAficiones(List<Aficiones> listaCrear) {
        for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaAficiones.crear(em,listaCrear.get(i));
        }
    }

    @Override
    public void modificarAficiones(List<Aficiones> listaModificar) {
       for (int i = 0; i < listaModificar.size(); i++) {
            Aficiones aficion = listaModificar.get(i);
            persistenciaAficiones.editar(em,aficion);
        }
    }

    @Override
    public void borrarAficiones(List<Aficiones> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaAficiones.borrar(em,listaBorrar.get(i));
        }
    }

    @Override
    public List<Aficiones> consultarAficiones() {
        List<Aficiones> listAficiones = persistenciaAficiones.buscarAficiones(em);
        return listAficiones;
    }


}
