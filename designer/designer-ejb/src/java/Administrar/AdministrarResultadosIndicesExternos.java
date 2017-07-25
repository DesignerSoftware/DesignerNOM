/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.IndicesExternos;
import Entidades.ResultadosIndicesExternos;
import InterfaceAdministrar.AdministrarResultadosIndicesExternosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaResultadosIndicesExternosInterface;
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
public class AdministrarResultadosIndicesExternos implements AdministrarResultadosIndicesExternosInterface  {
   
   private static Logger log = Logger.getLogger(AdministrarResultadosIndicesExternos.class);
    
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaResultadosIndicesExternosInterface persistenciaIndicesExternos;
    
     private EntityManager em;
    
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void crearResultado(List<ResultadosIndicesExternos> listaCrear) {
        for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaIndicesExternos.crear(em,listaCrear.get(i));
        }
    }

    @Override
    public void modificarResultado(List<ResultadosIndicesExternos> listaModificar) {
        for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaIndicesExternos.editar(em,listaModificar.get(i));
        }
    }

    @Override
    public void borrarResultado(List<ResultadosIndicesExternos> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaIndicesExternos.borrar(em,listaBorrar.get(i));
        }
    }

    @Override
    public List<IndicesExternos> consultarIndicesExternos() {
        List<IndicesExternos> listIndicesExternos = persistenciaIndicesExternos.buscarIndicesExternos(em);
        return listIndicesExternos;
    }

    @Override
    public List<ResultadosIndicesExternos> consultarResultadosIndicesExternos() {
        List<ResultadosIndicesExternos> listResultadosIndicesExternos = persistenciaIndicesExternos.buscarResultadosIndicesExternos(em);
        return listResultadosIndicesExternos;
    }
}
