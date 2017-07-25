/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Indicadores;
import Entidades.TiposIndicadores;
import InterfaceAdministrar.AdministrarIndicadoresInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaIndicadoresInterface;
import InterfacePersistencia.PersistenciaTiposIndicadoresInterface;
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
public class AdministrarIndicadores implements AdministrarIndicadoresInterface {

   private static Logger log = Logger.getLogger(AdministrarIndicadores.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaIndicadoresInterface persistenciaIndicadores;
    @EJB
    PersistenciaTiposIndicadoresInterface persistenciaTiposIndicadores;
    
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void crearIndicador(List<Indicadores> listaCrear) {
        for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaIndicadores.crear(em,listaCrear.get(i));
        }
    }

    @Override
    public void modificarIndicador(List<Indicadores> listaModificar) {
       for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaIndicadores.editar(em,listaModificar.get(i));
        }
    }

    @Override
    public void borrarIndicador(List<Indicadores> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaIndicadores.borrar(em,listaBorrar.get(i));
        }
    }

    @Override
    public List<Indicadores> consultarIndicadores() {
        List<Indicadores> listIndicadores = persistenciaIndicadores.buscarIndicadores(em);
        return listIndicadores;
    }

    @Override
    public List<TiposIndicadores> consultarTiposIndicadores() {
        List<TiposIndicadores> lovTiposIndicadores = persistenciaTiposIndicadores.buscarTiposIndicadores(em);
        return lovTiposIndicadores;
    }

}
