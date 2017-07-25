/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposEducaciones;
import InterfaceAdministrar.AdministrarTiposEducacionesInterface;
import InterfacePersistencia.PersistenciaTiposEducacionesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposEducaciones implements AdministrarTiposEducacionesInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposEducaciones.class);

    @EJB
    PersistenciaTiposEducacionesInterface persistenciaTiposEducaciones;
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

    @Override
    public List<TiposEducaciones> TiposEducaciones() {
        List<TiposEducaciones> listaTiposEducaciones;
        listaTiposEducaciones = persistenciaTiposEducaciones.tiposEducaciones(em);
        return listaTiposEducaciones;
    }

    @Override
    public List<TiposEducaciones> lovTiposEducaciones() {
        return persistenciaTiposEducaciones.tiposEducaciones(em);
    }

    @Override
    public void crear(List<TiposEducaciones> listaCrear) {
        try {
            for (int i = 0; i < listaCrear.size(); i++) {
                persistenciaTiposEducaciones.crear(em, listaCrear.get(i));
            }
        } catch (Exception e) {
            log.warn("Error en AdministrarTiposEducaciones.crear : " + e.toString());
        }
    }

    @Override
    public void editar(List<TiposEducaciones> listaEditar) {
        try {
            for (int i = 0; i < listaEditar.size(); i++) {
                persistenciaTiposEducaciones.editar(em, listaEditar.get(i));
            }
        } catch (Exception e) {
            log.warn("Error en AdministrarTiposEducaciones.editar : " + e.toString());
        }
    }

    @Override
    public void borrar(List<TiposEducaciones> listaBorrar) {
        try {
            for (int i = 0; i < listaBorrar.size(); i++) {
                persistenciaTiposEducaciones.borrar(em, listaBorrar.get(i));
            }
        } catch (Exception e) {
            log.warn("Error en AdministrarTiposEducaciones.borrar : " + e.toString());
        }
    }
}
