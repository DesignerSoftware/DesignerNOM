/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.IndicesExternos;
import InterfaceAdministrar.AdministrarIndicesExternosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaIndicesExternosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarIndicesExternos implements AdministrarIndicesExternosInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaIndicesExternosInterface persitenciaIndices;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
         em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void crearIndice(List<IndicesExternos> listaCrear) {
        for (int i = 0; i < listaCrear.size(); i++) {
            persitenciaIndices.crear(em,listaCrear.get(i));
        }
    }

    @Override
    public void modificarIndice(List<IndicesExternos> listaModificar) {
        for (int i = 0; i < listaModificar.size(); i++) {
            persitenciaIndices.editar(em,listaModificar.get(i));
        }
    }

    @Override
    public void borrarIndice(List<IndicesExternos> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            persitenciaIndices.borrar(em,listaBorrar.get(i));
        }
    }

    @Override
    public List<IndicesExternos> consultarIndicesExternos() {
       List<IndicesExternos> listIndicesExternos = persitenciaIndices.buscarIndicesExternos(em);
        return listIndicesExternos;
    }
}
