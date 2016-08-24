/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Pantallas;
import Entidades.Perfiles;
import Entidades.Personas;
import Entidades.Usuarios;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosInterface;
import InterfacePersistencia.PersistenciaPantallasInterface;
import InterfacePersistencia.PersistenciaPerfilesInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaUsuariosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
@Stateful
public class AdministrarUsuarios implements AdministrarUsuariosInterface {

    @EJB
    PersistenciaUsuariosInterface persistenciaUsuarios;
    @EJB
    PersistenciaPersonasInterface persistenciaPersonas;
    @EJB
    PersistenciaPerfilesInterface persistenciaPerfiles;
    @EJB
    PersistenciaPantallasInterface persistenciaPantallas;
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    // Metodos
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    public List<Usuarios> consultarUsuarios() {
        List<Usuarios> listaUsuarios;
        listaUsuarios = persistenciaUsuarios.buscarUsuarios(em);
        return listaUsuarios;
    }

    public Integer crearUsuariosBD(String alias) {
        Integer exeC = null;
        try {
            exeC = persistenciaUsuarios.crearUsuario(em, alias);
            return exeC;
        } catch (Exception e) {
            System.out.println("Error crearUsuarioDB Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Integer CrearUsuarioPerfilBD(String alias, String perfil) {
        Integer exeC2 = null;
        try {
        exeC2 = persistenciaUsuarios.crearUsuarioPerfil(em, alias, perfil);
        return exeC2;
        } catch (Exception e){
            System.out.println("Error crearUsuarioPerfilDB Admi : " + e.toString());
            return null;
        } 
    }

    @Override
    public Integer eliminarUsuariosBD(String alias) {
        Integer exeE = null;
        try {
        persistenciaUsuarios.borrarUsuario(em, alias);
        return exeE;
        } catch (Exception e){
            System.out.println("Error eliminarUsuariosBD Admi : " + e.toString());
            return null;
        }      
        
    }
    
    @Override 
    public Integer eliminarUsuarioTotalBD(String alias){
        Integer exeE2 = null;
        try {
        exeE2 = persistenciaUsuarios.borrarUsuarioTotal(em, alias);
        return exeE2;
        } catch (Exception e){
            System.out.println("Error eliminarUsuarioTotalBD Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Integer clonarUsuariosBD(String alias, String aliasclonado, BigInteger secuencia) {
        Integer exeA = null;
        try {
        exeA = persistenciaUsuarios.clonarUsuario(em, alias, aliasclonado, secuencia);
        System.out.println("está haciendo algo de clonar");
        return exeA;
        } catch (Exception e){
            System.out.println("Error clonarUsuariosBD Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Integer desbloquearUsuariosBD(String alias) {
        Integer exeD = null;
        try {
        exeD = persistenciaUsuarios.desbloquearUsuario(em, alias);
        System.out.println("está haciendo algo de desbloquear");
        return exeD;
        } catch (Exception e){
            System.out.println("Error desbloquearUsuariosBD Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Integer restaurarUsuariosBD(String alias, String fecha) {
        Integer exeR = null;
        try {
        exeR = persistenciaUsuarios.restaurarUsuario(em, alias, fecha);
        System.out.println("está haciendo algo de restaurar");
        return exeR;
        } catch (Exception e){
            System.out.println("Error restaurarUsuariosBD Admi : " + e.toString());
            return null;
        }
    }

    public List<Personas> consultarPersonas() {
        List<Personas> listaPersonas;
        listaPersonas = persistenciaPersonas.consultarPersonas(em);
        return listaPersonas;
    }

    public List<Perfiles> consultarPerfiles() {
        List<Perfiles> listaPerfiles;
        listaPerfiles = persistenciaPerfiles.consultarPerfiles(em);
        return listaPerfiles;
    }

    public List<Pantallas> consultarPantallas() {
        List<Pantallas> listaPantallas;
        listaPantallas = persistenciaPantallas.buscarPantallas(em);
        return listaPantallas;
    }

    @Override
    public void modificarUsuarios(List<Usuarios> listaUsuarios) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getAlias().equals(null)) {
                listaUsuarios.get(i).setAlias(null);
                persistenciaUsuarios.editar(em, listaUsuarios.get(i));
            } else if (listaUsuarios.get(i).getPersona().getSecuencia() == null) {
                listaUsuarios.get(i).setPersona(null);
            } else if (listaUsuarios.get(i).getPerfil().getSecuencia() == null) {
                listaUsuarios.get(i).setPerfil(null);
            } else if (listaUsuarios.get(i).getPantallainicio().getSecuencia() == null) {
                listaUsuarios.get(i).setPantallainicio(null);
            } else {
                persistenciaUsuarios.editar(em, listaUsuarios.get(i));
            }
        }
    }

    @Override
    public void borrarUsuarios(List<Usuarios> listaUsuarios) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            System.out.println("Borrando..Usuarios.");
            if (listaUsuarios.get(i).getAlias().equals(null)) {
                listaUsuarios.get(i).setAlias(null);
                persistenciaUsuarios.borrar(em, listaUsuarios.get(i));
            } else if (listaUsuarios.get(i).getPersona().getSecuencia() == null) {
                listaUsuarios.get(i).setPersona(null);
            } else if (listaUsuarios.get(i).getPerfil().getSecuencia() == null) {
                listaUsuarios.get(i).setPerfil(null);
            } else if (listaUsuarios.get(i).getPantallainicio().getSecuencia() == null) {
                listaUsuarios.get(i).setPantallainicio(null);
            } else {
                persistenciaUsuarios.borrar(em, listaUsuarios.get(i));
            }
        }
    }

    @Override
    public void crearUsuarios(List<Usuarios> listaUsuarios) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            System.out.println("Creando. Usuarios..");
            if (listaUsuarios.get(i).getAlias().equals(null)) {
                listaUsuarios.get(i).setAlias(null);
                persistenciaUsuarios.crear(em, listaUsuarios.get(i));
            } else if (listaUsuarios.get(i).getPersona().getSecuencia() == null) {
                listaUsuarios.get(i).setPersona(null);
            } else if (listaUsuarios.get(i).getPerfil().getSecuencia() == null) {
                listaUsuarios.get(i).setPerfil(null);
            } else if (listaUsuarios.get(i).getPantallainicio().getSecuencia() == null) {
                listaUsuarios.get(i).setPantallainicio(null);
            } else {
                persistenciaUsuarios.crear(em, listaUsuarios.get(i));
            }
        }
    }

}
