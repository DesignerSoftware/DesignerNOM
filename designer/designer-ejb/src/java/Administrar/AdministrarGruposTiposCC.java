/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.GruposTiposCC;
import InterfaceAdministrar.AdministrarGruposTiposCCInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaGruposTiposCCInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarGruposTiposCC implements AdministrarGruposTiposCCInterface {

    @EJB
    PersistenciaGruposTiposCCInterface persistenciaGrupos;
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void crearGrupo(List<GruposTiposCC> listaCrear) {
        try {
            for (int i = 0; i < listaCrear.size(); i++) {
                persistenciaGrupos.crear(em, listaCrear.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error Administrar.AdministrarGruposTiposCC.crearGrupo() : " + e.toString());
        }

    }

    @Override
    public void editarGrupo(List<GruposTiposCC> listaEditar) {
        try {
            for (int i = 0; i < listaEditar.size(); i++) {
                persistenciaGrupos.editar(em, listaEditar.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error Administrar.AdministrarGruposTiposCC.editarGrupo() : " + e.toString());
        }
    }

    @Override
    public void borrarGrupo(List<GruposTiposCC> listaBorrar) {
        try {
            for (int i = 0; i < listaBorrar.size(); i++) {
                persistenciaGrupos.borrar(em, listaBorrar.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error Administrar.AdministrarGruposTiposCC.borrarGrupo() : " + e.toString());
        }
    }

    @Override
    public List<GruposTiposCC> consultarGrupos() {
        List<GruposTiposCC> listaGrupos = persistenciaGrupos.buscarGruposTiposCC(em);
        return listaGrupos;
    }

}
