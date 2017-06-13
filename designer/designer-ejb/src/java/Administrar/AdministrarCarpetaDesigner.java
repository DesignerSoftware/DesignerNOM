/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Aficiones;
import Entidades.Empresas;
import Entidades.Modulos;
import Entidades.Pantallas;
import Entidades.Tablas;
import InterfaceAdministrar.AdministrarCarpetaDesignerInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaAficionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaModulosInterface;
import InterfacePersistencia.PersistenciaPantallasInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaTablasInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'CarpetaDesigner'.
 *
 * @author -Felipphe-
 */
@Stateful
public class AdministrarCarpetaDesigner implements AdministrarCarpetaDesignerInterface {

    @EJB
    PersistenciaModulosInterface persistenciaModulos;
    @EJB
    PersistenciaTablasInterface persistenciaTablas;
    @EJB
    PersistenciaPantallasInterface persistenciaPantallas;
    @EJB
    PersistenciaAficionesInterface persistenciaAficiones;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    @EJB
    PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;

    public List<Modulos> listModulos;
    public List<Tablas> listTablas;
    public Pantallas pantalla;
    public List<Aficiones> listAficiones;
    public Aficiones aficion;
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Modulos> consultarModulos() {
        try {
            return persistenciaModulos.buscarModulos(em);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Tablas> consultarTablas(BigInteger secuenciaMod) {
        try {
            return persistenciaTablas.buscarTablas(em, secuenciaMod);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Pantallas consultarPantalla(BigInteger secuenciaTab) {
        try {
            return persistenciaPantallas.buscarPantalla(em, secuenciaTab);
        } catch (Exception e) {
            return null;
        }
    }

    public String consultarNombrePantalla(BigInteger secuenciaTab) {
        try {
            Pantallas p = persistenciaPantallas.buscarPantalla(em, secuenciaTab);
            return p.getNombre();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Aficiones> consultarAficiones() {
        try {
            return persistenciaAficiones.buscarAficiones(em);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Aficiones consultarAficion(BigInteger secuencia) {
        aficion = persistenciaAficiones.buscarAficion(em, secuencia);
        return aficion;
    }

    @Override
    public void modificarAficiones(List<Aficiones> listAficiones) {
        for (int i = 0; i < listAficiones.size(); i++) {
            System.out.println("Modificando...");
            aficion = listAficiones.get(i);
            persistenciaAficiones.editar(em, aficion);
        }
    }

    @Override
    public Integer sugerirCodigoAficiones() {
        if (persistenciaAficiones == null) {
            System.out.println("Persistencia vacia.");
        }
        Integer max;
        Short respuesta;
        System.out.println("Hagalo!");
        respuesta = persistenciaAficiones.maximoCodigoAficiones(em);
        max = respuesta.intValue();
        max = max + 1;
        return max;
    }

    @Override
    public void crearAficion(Aficiones aficion) {
        persistenciaAficiones.crear(em, aficion);
    }

    @Override
    public void borrarAficion(Aficiones aficion) {
        persistenciaAficiones.borrar(em, aficion);
    }

    @Override
    public Aficiones consultarAficionCodigo(Short cod) {
        return persistenciaAficiones.buscarAficionCodigo(em, cod);
    }

    @Override
    public String consultarNombrePantallaPorEmpresa(Short codPantalla) throws Exception {
        List<Empresas> listaempresas;
        String nomPantalla = " ";
        BigInteger secEmpresa = BigInteger.ZERO;
        try {
            listaempresas = persistenciaEmpresas.consultarEmpresas(em);
        } catch (NoResultException nre) {
            System.err.println("Error: AdministrarCarpetaDesigner consultarNombrePantallaPorEmpresa " + nre.getMessage());
            //return " ";
            throw new Exception("No hay empresas disponibles para el usuario.");
        }
        if (listaempresas != null) {
            if (!listaempresas.isEmpty()) {
                if (listaempresas.size() == 1) {
                    secEmpresa = listaempresas.get(0).getSecuencia();
                } else {
                    try {
                        secEmpresa = persistenciaParametrosEstructuras.buscarEmpresaParametros(em);
                    } catch (NoResultException nre) {
                        throw new Exception("El usuario no tiene configurada la empresa.");
                    }
                }
            }
        }
        try {
            nomPantalla = persistenciaPantallas.buscarPantallaPorCodigoEmpresa(em, secEmpresa, codPantalla);
        } catch (NoResultException nre) {
            try {
                nomPantalla = persistenciaPantallas.buscarPantallaPorCodigo(em, codPantalla);
            } catch (NoResultException nre2) {
                throw new Exception("No hay pantalla configurada para el código " + codPantalla);
            }
        }
        return nomPantalla;
    }

}
