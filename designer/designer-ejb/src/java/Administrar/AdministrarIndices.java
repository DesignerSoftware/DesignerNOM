/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Indices;
import Entidades.TiposIndices;
import InterfaceAdministrar.AdministrarIndicesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaIndicesInterface;
import InterfacePersistencia.PersistenciaTiposIndicesInterface;
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
public class AdministrarIndices implements AdministrarIndicesInterface {

   private static Logger log = Logger.getLogger(AdministrarIndices.class);

    @EJB
    PersistenciaIndicesInterface persistenciaIndices;
    @EJB
    PersistenciaTiposIndicesInterface PersistenciaTiposIndices;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    public void modificarIndices(List<Indices> listaIndices) {
        for (int i = 0; i < listaIndices.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaIndices.editar(em, listaIndices.get(i));
        }
    }

    public void borrarIndices(List<Indices> listaIndices) {
        for (int i = 0; i < listaIndices.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaIndices.borrar(em, listaIndices.get(i));
        }
    }

    public void crearIndices(List<Indices> listaIndices) {
        for (int i = 0; i < listaIndices.size(); i++) {
            log.warn("Administrar crear...");
            persistenciaIndices.crear(em, listaIndices.get(i));
        }
    }

    public List<Indices> mostrarIndices() {
        List<Indices> listIndices;
        listIndices = persistenciaIndices.consultarIndices(em);
        return listIndices;
    }

    public List<TiposIndices> consultarLOVTiposIndices() {
        List<TiposIndices> lista;
        lista = PersistenciaTiposIndices.consultarTiposIndices(em);
        return lista;
    }

    public BigInteger contarParametrosIndicesIndice(BigInteger secuenciaIndices) {
        BigInteger verificadorIndicesPersonas = null;

        try {
            verificadorIndicesPersonas = persistenciaIndices.contarParametrosIndicesIndice(em, secuenciaIndices);
        } catch (Exception e) {
            log.error("ERROR AdmnistrarIndices contarParametrosIndicesIndice ERROR :" + e);
        } finally {
            return verificadorIndicesPersonas;
        }
    }

    public BigInteger contarResultadosIndicesDetallesIndice(BigInteger secuenciaIndices) {
        BigInteger verificadorIndicesPersonas = null;

        try {
            verificadorIndicesPersonas = persistenciaIndices.contarResultadosIndicesDetallesIndice(em, secuenciaIndices);
        } catch (Exception e) {
            log.error("ERROR AdmnistrarIndices contarResultadosIndicesDetallesIndice ERROR :" + e);
        } finally {
            return verificadorIndicesPersonas;
        }
    }

    public BigInteger contarResultadosIndicesIndice(BigInteger secuenciaIndices) {
        BigInteger verificadorIndicesPersonas = null;

        try {
            verificadorIndicesPersonas = persistenciaIndices.contarResultadosIndicesIndice(em, secuenciaIndices);
        } catch (Exception e) {
            log.error("ERROR AdmnistrarIndices contarResultadosIndicesIndice ERROR :" + e);
        } finally {
            return verificadorIndicesPersonas;
        }
    }

    public BigInteger contarResultadosIndicesSoportesIndice(BigInteger secuenciaIndices) {
        BigInteger verificadorIndicesPersonas = null;

        try {
            verificadorIndicesPersonas = persistenciaIndices.contarResultadosIndicesSoportesIndice(em, secuenciaIndices);
        } catch (Exception e) {
            log.error("ERROR AdmnistrarIndices contarResultadosIndicesSoportesIndice ERROR :" + e);
        } finally {
            return verificadorIndicesPersonas;
        }
    }

    public BigInteger contarUsuariosIndicesIndice(BigInteger secuenciaIndices) {
        BigInteger verificadorIndicesPersonas = null;

        try {
            verificadorIndicesPersonas = persistenciaIndices.contarUsuariosIndicesIndice(em, secuenciaIndices);
        } catch (Exception e) {
            log.error("ERROR AdmnistrarIndices contarUsuariosIndicesIndice ERROR :" + e);
        } finally {
            return verificadorIndicesPersonas;
        }
    }

    @Override
    public BigInteger contarCodigosRepetidosIndices(Short codigo) {
        BigInteger verificadorIndicesPersonas = null;

        try {
            verificadorIndicesPersonas = persistenciaIndices.contarCodigosRepetidosIndices(em, codigo);
        } catch (Exception e) {
            log.error("ERROR AdmnistrarIndices contarCodigosRepetidosIndices ERROR :" + e);
        } finally {
            return verificadorIndicesPersonas;
        }
    }
}
