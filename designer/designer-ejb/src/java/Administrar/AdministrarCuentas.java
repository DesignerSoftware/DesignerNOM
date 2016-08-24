/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Cuentas;
import Entidades.Empresas;
import Entidades.Rubrospresupuestales;
import InterfaceAdministrar.AdministrarCuentasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCuentasInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaRubrosPresupuestalesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'Cuentas'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarCuentas implements AdministrarCuentasInterface {

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaEmpresas'.
     */
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaCuentas'.
     */
    @EJB
    PersistenciaCuentasInterface persistenciaCuentas;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaRubrosPresupuestales'.
     */
    @EJB
    PersistenciaRubrosPresupuestalesInterface persistenciaRubrosPresupuestales;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
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
    public void crearCuentas(List<Cuentas> listCuentasCrear) {
        try {
            for (int i = 0; i < listCuentasCrear.size(); i++) {
                if (listCuentasCrear.get(i).getContracuentatesoreria().getSecuencia() == null) {
                    listCuentasCrear.get(i).setContracuentatesoreria(null);
                }
                if (listCuentasCrear.get(i).getRubropresupuestal().getSecuencia() == null) {
                    listCuentasCrear.get(i).setRubropresupuestal(null);
                }
                persistenciaCuentas.crear(em, listCuentasCrear.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error en crearCuentas Admi : " + e.toString());
        }
    }

    @Override
    public void modificarCuentas(List<Cuentas> listCuentasModificar) {
        try {
            for (int i = 0; i < listCuentasModificar.size(); i++) {
                persistenciaCuentas.editar(em, listCuentasModificar.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error en modificarCuentas Admi : " + e.toString());
        }
    }

    @Override
    public void borrarCuentas(List<Cuentas> listCuentasBorrar) {
        try {
            for (int i = 0; i < listCuentasBorrar.size(); i++) {
                persistenciaCuentas.borrar(em, listCuentasBorrar.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error en borrarCuentas Admi : " + e.toString());
        }
    }

    @Override
    public List<Cuentas> consultarCuentasEmpresa(BigInteger secuencia) {
        try {
            List<Cuentas> listCuentas = persistenciaCuentas.buscarCuentasSecuenciaEmpresa(em, secuencia);
            return listCuentas;
        } catch (Exception e) {
            System.out.println("Error en listCuentasEmpresa Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Empresas> consultarEmpresas() {
        try {
            List<Empresas> listEmpresas = persistenciaEmpresas.consultarEmpresas(em);
            return listEmpresas;
        } catch (Exception e) {
            System.out.println("Error en listEmpresas Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Rubrospresupuestales> consultarLOVRubros() {
        try {
            List<Rubrospresupuestales> listRubros = persistenciaRubrosPresupuestales.buscarRubros(em);
            return listRubros;
        } catch (Exception e) {
            System.out.println("Error en listRubros Admi : " + e.toString());
            return null;
        }
    }
}
