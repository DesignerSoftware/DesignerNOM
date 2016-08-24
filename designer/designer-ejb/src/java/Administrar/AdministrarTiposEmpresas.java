/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposEmpresasInterface;
import Entidades.TiposEmpresas;
import InterfacePersistencia.PersistenciaTiposEmpresasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposEmpresas implements AdministrarTiposEmpresasInterface {

    @EJB
    PersistenciaTiposEmpresasInterface persistenciaTiposEmpresas;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    
    private TiposEmpresas tiposEmpresasSeleccionada;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    @Override
    public void modificarTiposEmpresas(List<TiposEmpresas> listTiposEmpresas) {
        for (int i = 0; i < listTiposEmpresas.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaTiposEmpresas.editar(em, listTiposEmpresas.get(i));
        }
    }

    @Override
    public void borrarTiposEmpresas(List<TiposEmpresas> listTiposEmpresas) {
        for (int i = 0; i < listTiposEmpresas.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaTiposEmpresas.borrar(em, listTiposEmpresas.get(i));
        }
    }

    @Override
    public void crearTiposEmpresas(List<TiposEmpresas> listTiposEmpresas) {
        for (int i = 0; i < listTiposEmpresas.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaTiposEmpresas.crear(em, listTiposEmpresas.get(i));
        }
    }

    @Override
    public List<TiposEmpresas> consultarTiposEmpresas() {
        List<TiposEmpresas> listTiposEmpresas;
        listTiposEmpresas = persistenciaTiposEmpresas.buscarTiposEmpresas(em);
        return listTiposEmpresas;
    }

    @Override
    public TiposEmpresas consultarTipoEmpresa(BigInteger secTipoEmpresa) {
        TiposEmpresas tiposEmpresas;
        tiposEmpresas = persistenciaTiposEmpresas.buscarTipoEmpresa(em, secTipoEmpresa);
        return tiposEmpresas;
    }

    @Override
    public BigInteger contarSueldosMercadosTipoEmpresa(BigInteger secuenciaSueldosMercados) {
        BigInteger verificadorSueldoMercados = null;
        try {
            System.err.println("Secuencia Borrado Sueldos Proyectos" + secuenciaSueldosMercados);
            verificadorSueldoMercados = persistenciaTiposEmpresas.contadorSueldosMercados(em, secuenciaSueldosMercados);
        } catch (Exception e) {
            System.err.println("ERROR AministrarTiposEmpresas verificarBorradoSueldosMercados ERROR :" + e);
        } finally {
            return verificadorSueldoMercados;
        }
    }
}
