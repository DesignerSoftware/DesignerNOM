/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.HistoricosUsuarios;
import Entidades.Perfiles;
import Entidades.Personas;
import Entidades.Usuarios;
import InterfaceAdministrar.AdministrarHistoricosUsuariosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaHistoricosUsuariosInterface;
import InterfacePersistencia.PersistenciaPerfilesInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaUsuariosInterface;
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
public class AdministrarHistoricosUsuarios implements AdministrarHistoricosUsuariosInterface{

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaHistoricosUsuariosInterface persistenciaHistoricosUsuarios;
    @EJB
    PersistenciaPersonasInterface persistenciaPersonas;
    @EJB
    PersistenciaUsuariosInterface persistenciaUsuarios;
    @EJB
    PersistenciaPerfilesInterface persistenciaPerfiles;
            
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<HistoricosUsuarios> consultarHistoricosUsuarios(BigInteger secUsuario) {
        List<HistoricosUsuarios> listaHistoricos =  persistenciaHistoricosUsuarios.buscarHistoricosUsuarios(em, secUsuario);
        return listaHistoricos;
    }

    @Override
    public void crearHistoricosUsuarios(List<HistoricosUsuarios> listaCrear) {
        for(int i = 0; i< listaCrear.size();i++){
            if(listaCrear.get(i).getPerfil().getSecuencia() == null){
                listaCrear.get(i).setPerfil(new Perfiles());
            }
            if(listaCrear.get(i).getUsuario().getSecuencia() == null){
                listaCrear.get(i).setUsuario(new Usuarios());
            }
            if(listaCrear.get(i).getPersona().getSecuencia() == null){
                listaCrear.get(i).setPersona(new Personas());
            }
            persistenciaHistoricosUsuarios.crear(em, listaCrear.get(i));
        }
    }

    @Override
    public void modificarHistoricosUsuarios(List<HistoricosUsuarios> listaModificar) {
         for(int i = 0; i< listaModificar.size();i++){
             if(listaModificar.get(i).getPerfil().getSecuencia() == null){
                listaModificar.get(i).setPerfil(new Perfiles());
            }
            if(listaModificar.get(i).getUsuario().getSecuencia() == null){
                listaModificar.get(i).setUsuario(new Usuarios());
            }
            if(listaModificar.get(i).getPersona().getSecuencia() == null){
                listaModificar.get(i).setPersona(new Personas());
            }
            persistenciaHistoricosUsuarios.editar(em, listaModificar.get(i));
        }
    }

    @Override
    public void borrarHistoricosUsuarios(List<HistoricosUsuarios> listaBorrar) {
         for(int i = 0; i< listaBorrar.size();i++){
             if(listaBorrar.get(i).getPerfil().getSecuencia() == null){
                listaBorrar.get(i).setPerfil(new Perfiles());
            }
            if(listaBorrar.get(i).getUsuario().getSecuencia() == null){
                listaBorrar.get(i).setUsuario(new Usuarios());
            }
            if(listaBorrar.get(i).getPersona().getSecuencia() == null){
                listaBorrar.get(i).setPersona(new Personas());
            }
            persistenciaHistoricosUsuarios.borrar(em, listaBorrar.get(i));
        }
    }

    @Override
    public List<Personas> lovPersonas() {
        List<Personas> lovPersonas = persistenciaPersonas.consultarPersonas(em);
        return lovPersonas;
    }

    @Override
    public List<Perfiles> lovPerfiles() {
        List<Perfiles> lovPerfiles = persistenciaPerfiles.consultarPerfiles(em);
        return lovPerfiles;
    }

    @Override
    public List<Usuarios> lovUsuarios(BigInteger secUsuario) {
       List<Usuarios> lovUsuario = persistenciaUsuarios.buscarUsuariosXSecuencia(em, secUsuario);
       return lovUsuario;
    }
}
