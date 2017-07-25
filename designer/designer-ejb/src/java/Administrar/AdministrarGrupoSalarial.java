/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.GruposSalariales;
import Entidades.VigenciasGruposSalariales;
import InterfaceAdministrar.AdministrarGrupoSalarialInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaGruposSalarialesInterface;
import InterfacePersistencia.PersistenciaVigenciasGruposSalarialesInterface;
import java.math.BigInteger;
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
public class AdministrarGrupoSalarial implements AdministrarGrupoSalarialInterface {

   private static Logger log = Logger.getLogger(AdministrarGrupoSalarial.class);

    @EJB
    PersistenciaGruposSalarialesInterface persistenciaGruposSalariales;
    @EJB
    PersistenciaVigenciasGruposSalarialesInterface persistenciaVigenciasGruposSalariales;
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
    public List<GruposSalariales> listGruposSalariales() {
        try {
            List<GruposSalariales> gruposSalariales = persistenciaGruposSalariales.buscarGruposSalariales(em);
            return gruposSalariales;
        } catch (Exception e) {
            log.warn("Error listGruposSalariales Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearGruposSalariales(List<GruposSalariales> listaCrear) {
        try {
            for (int i = 0; i < listaCrear.size(); i++) {
                persistenciaGruposSalariales.crear(em, listaCrear.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearGruposSalariales Admi : " + e.toString());
        }
    }

    @Override
    public void editarGruposSalariales(List<GruposSalariales> listaEditar) {
        try {
            for (int i = 0; i < listaEditar.size(); i++) {
                persistenciaGruposSalariales.editar(em, listaEditar.get(i));
            }
        } catch (Exception e) {
            log.warn("Error editarGruposSalariales Admi : " + e.toString());
        }
    }

    @Override
    public void borrarGruposSalariales(List<GruposSalariales> listaBorrar) {
        try {
            for (int i = 0; i < listaBorrar.size(); i++) {
                persistenciaGruposSalariales.borrar(em, listaBorrar.get(i));
            }
        } catch (Exception e) {
            log.warn("Error borrarGruposSalariales Admi : " + e.toString());
        }
    }

    @Override
    public List<VigenciasGruposSalariales> lisVigenciasGruposSalarialesSecuencia(BigInteger secuencia) {
        try {
            List<VigenciasGruposSalariales> VgruposSalariales = persistenciaVigenciasGruposSalariales.buscarVigenciaGrupoSalarialSecuenciaGrupoSal(em, secuencia);
            return VgruposSalariales;
        } catch (Exception e) {
            log.warn("Error lisVigenciasGruposSalarialesSecuencia Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearVigenciasGruposSalariales(List<VigenciasGruposSalariales> lista) {
        try {
            for (int i = 0; i < lista.size(); i++) {
                persistenciaVigenciasGruposSalariales.crear(em, lista.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearVigenciasGruposSalariales Admi : " + e.toString());
        }
    }

    @Override
    public void editarVigenciasGruposSalariales(List<VigenciasGruposSalariales> lista) {
        try {
            for (int i = 0; i < lista.size(); i++) {
                persistenciaVigenciasGruposSalariales.editar(em, lista.get(i));
            }
        } catch (Exception e) {
            log.warn("Error editarVigenciasGruposSalariales Admi : " + e.toString());
        }
    }

    @Override
    public void borrarVigenciasGruposSalariales(List<VigenciasGruposSalariales> lista) {
        try {
            for (int i = 0; i < lista.size(); i++) {
                persistenciaVigenciasGruposSalariales.borrar(em, lista.get(i));
            }
        } catch (Exception e) {
            log.warn("Error borrarVigenciasGruposSalariales Admi : " + e.toString());
        }
    }
}
