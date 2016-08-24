/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposCertificadosInterface;
import Entidades.TiposCertificados;
import InterfacePersistencia.PersistenciaTiposCertificadosInterface;
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
public class AdministrarTiposCertificados implements AdministrarTiposCertificadosInterface {

    @EJB
    PersistenciaTiposCertificadosInterface persistenciaTiposCertificados;
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
    public void modificarTiposCertificados(List<TiposCertificados> listaTiposCertificados) {
        for (int i = 0; i < listaTiposCertificados.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaTiposCertificados.editar(em, listaTiposCertificados.get(i));
        }
    }

    @Override
    public void borrarTiposCertificados(List<TiposCertificados> listaTiposCertificados) {
        for (int i = 0; i < listaTiposCertificados.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaTiposCertificados.borrar(em, listaTiposCertificados.get(i));
        }
    }

    @Override
    public void crearTiposCertificados(List<TiposCertificados> listaTiposCertificados) {
        for (int i = 0; i < listaTiposCertificados.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaTiposCertificados.crear(em, listaTiposCertificados.get(i));
        }
    }

    @Override
    public List<TiposCertificados> consultarTiposCertificados() {
        List<TiposCertificados> listTipoCertificado;
        listTipoCertificado = persistenciaTiposCertificados.buscarTiposCertificados(em);
        return listTipoCertificado;
    }

    @Override
    public TiposCertificados consultarTipoCertificado(BigInteger secTipoCertificado) {
        TiposCertificados tipoCertificado;
        tipoCertificado = persistenciaTiposCertificados.buscarTipoCertificado(em, secTipoCertificado);
        return tipoCertificado;
    }
}
