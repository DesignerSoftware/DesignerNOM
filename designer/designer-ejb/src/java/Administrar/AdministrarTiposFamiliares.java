/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposFamiliares;
import InterfacePersistencia.PersistenciaTiposFamiliaresInterface;
import InterfaceAdministrar.AdministrarTiposFamiliaresInterface;
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
public class AdministrarTiposFamiliares implements AdministrarTiposFamiliaresInterface {

    @EJB
    PersistenciaTiposFamiliaresInterface persistenciaTiposFamiliares;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    
    private TiposFamiliares tiposFamiliaresSeleccionada;
    private TiposFamiliares tiposFamiliares;
    private List<TiposFamiliares> listTiposFamiliares;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    @Override
    public void modificarTiposFamiliares(List<TiposFamiliares> listTiposFamiliares) {
        for (int i = 0; i < listTiposFamiliares.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaTiposFamiliares.editar(em, listTiposFamiliares.get(i));
        }
    }
   @Override
    public void borrarTiposFamiliares(List<TiposFamiliares> listTiposFamiliares) {
        for (int i = 0; i < listTiposFamiliares.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaTiposFamiliares.borrar(em, listTiposFamiliares.get(i));
        }
    }
   @Override
    public void crearTiposFamiliares(List<TiposFamiliares> listTiposFamiliares) {
        for (int i = 0; i < listTiposFamiliares.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaTiposFamiliares.crear(em, listTiposFamiliares.get(i));
        }
    }

    @Override
    public List<TiposFamiliares> consultarTiposFamiliares() {
        listTiposFamiliares = persistenciaTiposFamiliares.buscarTiposFamiliares(em);
        return listTiposFamiliares;
    }
   @Override
    public TiposFamiliares consultarTipoExamen(BigInteger secTipoEmpresa) {
        tiposFamiliares = persistenciaTiposFamiliares.buscarTiposFamiliares(em, secTipoEmpresa);
        return tiposFamiliares;
    }
   @Override
    public BigInteger contarHvReferenciasTipoFamiliar(BigInteger secuenciaTiposFamiliares) {
        BigInteger verificadorHvReferencias = null;

        try {
            System.err.println("Secuencia Borrado Elementos" + secuenciaTiposFamiliares);
            verificadorHvReferencias = persistenciaTiposFamiliares.contadorHvReferencias(em, secuenciaTiposFamiliares);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarTiposFamiliares verificarBorradoElementos ERROR :" + e);
        } finally {
            return verificadorHvReferencias;
        }
    }

}
