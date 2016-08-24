/**
 * Documentación a cargo de Andres Pineda
 */
package Administrar;

import Entidades.DiasLaborables;
import Entidades.TiposContratos;
import Entidades.TiposDias;
import InterfaceAdministrar.AdministrarTiposContratosInterface;
import InterfacePersistencia.PersistenciaDiasLaborablesInterface;
import InterfacePersistencia.PersistenciaTiposContratosInterface;
import InterfacePersistencia.PersistenciaTiposDiasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br> Clase encargada de realizar las operaciones lógicas para
 * la pantalla 'TipoContrato'.
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarTiposContratos implements AdministrarTiposContratosInterface {

    //--------------------------------------------------------------------------    
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaTiposContratos'.
     */
    @EJB
    PersistenciaTiposContratosInterface persistenciaTiposContratos;
    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaDiasLaborables'.
     */
    @EJB
    PersistenciaDiasLaborablesInterface persistenciaDiasLaborables;
    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaTiposDias'.
     */
    @EJB
    PersistenciaTiposDiasInterface persistenciaTiposDias;
    /**
     * Enterprise JavaBean.<br> Atributo que representa todo lo referente a la
     * conexión del usuario que está usando el aplicativo.
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
    public List<TiposContratos> listaTiposContratos() {
        try {
            List<TiposContratos> lista = persistenciaTiposContratos.tiposContratos(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listaTiposContratos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearTiposContratos(List<TiposContratos> listaTC) {
        try {
            for (int i = 0; i < listaTC.size(); i++) {
                persistenciaTiposContratos.crear(em, listaTC.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearTiposContratos Admi : " + e.toString());
        }
    }

    @Override
    public void editarTiposContratos(List<TiposContratos> listaTC) {
        try {
            for (int i = 0; i < listaTC.size(); i++) {
                persistenciaTiposContratos.editar(em, listaTC.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarTiposContratos Admi : " + e.toString());
        }
    }

    @Override
    public void borrarTiposContratos(List<TiposContratos> listaTC) {
        try {
            for (int i = 0; i < listaTC.size(); i++) {
                persistenciaTiposContratos.borrar(em, listaTC.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarTiposContratos Admi : " + e.toString());
        }
    }

    @Override
    public List<DiasLaborables> listaDiasLaborablesParaTipoContrato(BigInteger secTipoContrato) {
        try {
            System.out.println("listaDiasLaborablesParaTipoContrato: em : " + em);
            List<DiasLaborables> lista = persistenciaDiasLaborables.diasLaborablesParaSecuenciaTipoContrato(em, secTipoContrato);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listaDiasLaborablesParaTipoContrato Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearDiasLaborables(List<DiasLaborables> listaDL) {
        try {
            for (int i = 0; i < listaDL.size(); i++) {
                persistenciaDiasLaborables.crear(em, listaDL.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearDiasLaborables Admi : " + e.toString());
        }
    }

    @Override
    public void editarDiasLaborables(List<DiasLaborables> listaDL) {
        try {
            for (int i = 0; i < listaDL.size(); i++) {
                persistenciaDiasLaborables.editar(em, listaDL.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarDiasLaborables Admi : " + e.toString());
        }
    }

    @Override
    public void borrarDiasLaborables(List<DiasLaborables> listaDL) {
        try {
            for (int i = 0; i < listaDL.size(); i++) {
                persistenciaDiasLaborables.borrar(em, listaDL.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarDiasLaborables Admi : " + e.toString());
        }
    }

    @Override
    public List<TiposDias> lovTiposDias() {
        try {
            List<TiposDias> lista = persistenciaTiposDias.buscarTiposDias(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error lovTiposDias Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void clonarTC(BigInteger secuenciaClonado, String nuevoNombre, Short nuevoCodigo) {
        try {
            persistenciaTiposContratos.clonarTipoContrato(secuenciaClonado, nuevoNombre, nuevoCodigo);
        } catch (Exception e) {
            System.out.println("Error clonarTC Admi : " + e.toString());
        }
    }
}
