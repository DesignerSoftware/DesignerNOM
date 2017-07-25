/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Ciudades;
import Entidades.Pantallas;
import Entidades.Perfiles;
import Entidades.Personas;
import Entidades.TiposDocumentos;
import Entidades.Usuarios;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaPantallasInterface;
import InterfacePersistencia.PersistenciaPerfilesInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaTiposDocumentosInterface;
import InterfacePersistencia.PersistenciaUsuariosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrador
 */
@Stateful
public class AdministrarUsuarios implements AdministrarUsuariosInterface {

   private static Logger log = Logger.getLogger(AdministrarUsuarios.class);

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
    @EJB
    PersistenciaCiudadesInterface persistenciaCiudades;
    @EJB
    PersistenciaTiposDocumentosInterface persistenciaTipoDocumento;

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

    @Override
    public void crearUsuariosBD(String alias) {
        persistenciaUsuarios.crearUsuario(em, alias);
    }

    @Override
    public void CrearUsuarioPerfilBD(String alias, String perfil) {
        persistenciaUsuarios.crearUsuarioPerfil(em, alias, perfil);
    }

    @Override
    public Integer eliminarUsuariosBD(String alias) {
        Integer exeE = null;
        try {
            persistenciaUsuarios.borrarUsuario(em, alias);
            return exeE;
        } catch (Exception e) {
            log.warn("Error eliminarUsuariosBD Admi : " + e.toString());
            return null;
        }

    }

    @Override
    public Integer eliminarUsuarioTotalBD(String alias) {
        Integer exeE2 = null;
        try {
            exeE2 = persistenciaUsuarios.borrarUsuarioTotal(em, alias);
            return exeE2;
        } catch (Exception e) {
            log.warn("Error eliminarUsuarioTotalBD Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void clonarUsuariosBD(BigInteger usuarioOrigen, BigInteger usuarioDestino) {
        persistenciaUsuarios.clonarUsuario(em, usuarioOrigen, usuarioDestino);
    }

    @Override
    public void desbloquearUsuariosBD(String alias) {
        persistenciaUsuarios.desbloquearUsuario(em, alias);
    }

    @Override
    public void restaurarUsuariosBD(String alias, String fecha) {
        persistenciaUsuarios.restaurarUsuario(em, alias, fecha);
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
            log.warn("Borrando..Usuarios.");
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
            log.warn("Creando. Usuarios..");
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

    @Override
    public List<Ciudades> lovCiudades() {
        List<Ciudades> lovCiudades = persistenciaCiudades.lovCiudades(em);
        return lovCiudades;
    }

    @Override
    public List<TiposDocumentos> consultarTiposDocumentos() {
        List<TiposDocumentos> listTiposDocumentos;
        listTiposDocumentos = persistenciaTipoDocumento.consultarTiposDocumentos(em);
        return listTiposDocumentos;
    }

    @Override
    public void crearPersona(Personas persona) {
        persistenciaPersonas.crear(em, persona);
    }

}
