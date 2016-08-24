/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.DetallesTiposCotizantes;
import Entidades.TiposEntidades;
import InterfaceAdministrar.AdministrarDetallesTiposCotizantesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaDetallesTiposCotizantesInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarDetallesTiposCotizantes implements AdministrarDetallesTiposCotizantesInterface {

    @EJB
    PersistenciaDetallesTiposCotizantesInterface persistenciaDetallesTiposCotizantes;
    @EJB
    PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    public List<DetallesTiposCotizantes> detallesTiposCotizantes(BigInteger secuenciaTipoCotizante) {
        List<DetallesTiposCotizantes> listaDetallesTiposCotizantes;
        listaDetallesTiposCotizantes = persistenciaDetallesTiposCotizantes.detallesTiposCotizantes(em,secuenciaTipoCotizante);
        return listaDetallesTiposCotizantes;
    }

    @Override
    public void borrarDetalleTipoCotizante(DetallesTiposCotizantes detallesTiposCotizantes) {
        persistenciaDetallesTiposCotizantes.borrar(em,detallesTiposCotizantes);
    }

    @Override
    public void crearDetalleTipoCotizante(DetallesTiposCotizantes detallesTiposCotizantes) {
        persistenciaDetallesTiposCotizantes.crear(em,detallesTiposCotizantes);
    }

    @Override
    public void modificarDetalleTipoCotizante(List<DetallesTiposCotizantes> listaDetallesTiposCotizantesModificar) {
        for (int i = 0; i < listaDetallesTiposCotizantesModificar.size(); i++) {
            System.out.println("Modificando...");
            if (listaDetallesTiposCotizantesModificar.get(i).getTipocotizante().getSecuencia() == null) {
                listaDetallesTiposCotizantesModificar.get(i).setTipocotizante(null);
            }
            if (listaDetallesTiposCotizantesModificar.get(i).getTipoentidad().getSecuencia() == null) {
                listaDetallesTiposCotizantesModificar.get(i).setTipoentidad(null);
            }
            if (listaDetallesTiposCotizantesModificar.get(i).getMinimosml() == null) {
                listaDetallesTiposCotizantesModificar.get(i).setMinimosml(null);
            }
            if (listaDetallesTiposCotizantesModificar.get(i).getMaximosml() == null) {
                listaDetallesTiposCotizantesModificar.get(i).setMaximosml(null);
            }
            persistenciaDetallesTiposCotizantes.editar(em,listaDetallesTiposCotizantesModificar.get(i));
        }
    }

    @Override
    public List<TiposEntidades> lovTiposEntidades() {
        List<TiposEntidades> listaTiposEntidades;
        listaTiposEntidades = persistenciaTiposEntidades.buscarTiposEntidades(em);
        return listaTiposEntidades;
    }
}
