/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Bancos;
import Entidades.Ciudades;
import Entidades.Sucursales;
import InterfaceAdministrar.AdministrarSucursalesInterface;
import InterfacePersistencia.PersistenciaBancosInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaSucursalesInterface;
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
public class AdministrarSucursales implements AdministrarSucursalesInterface {

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaSucursales'.
     */
    @EJB
    PersistenciaSucursalesInterface persistenciaSucursales;
    @EJB
    PersistenciaBancosInterface persistenciaBancos;
    @EJB
    PersistenciaCiudadesInterface persistenciaCiudades;
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
    public void modificarSucursales(List<Sucursales> listaSucursales) {
        //for (int i = 0; i < listaSucursales.size(); i++) {
        for (Sucursales listaSucursale : listaSucursales) {
            System.out.println("Administrar Modificando...");
            //persistenciaSucursales.editar(em, listaSucursales.get(i));
            persistenciaSucursales.editar(em, listaSucursale);
        }
    }

    @Override
    public void borrarSucursales(List<Sucursales> listaSucursales) {
        for (int i = 0; i < listaSucursales.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaSucursales.borrar(em, listaSucursales.get(i));
        }
    }

    @Override
    public void crearSucursales(List<Sucursales> listaSucursales) {
        for (int i = 0; i < listaSucursales.size(); i++) {
            persistenciaSucursales.crear(em, listaSucursales.get(i));
        }
    }

    @Override
    public List<Sucursales> consultarSucursales() {
        List<Sucursales> listaSucursales;
        listaSucursales = persistenciaSucursales.consultarSucursales(em);
        return listaSucursales;
    }

    @Override
    public List<Bancos> consultarLOVBancos() {
        List<Bancos> listLOVBancos;
        listLOVBancos = persistenciaBancos.buscarBancos(em);
        return listLOVBancos;
    }

    @Override
    public List<Ciudades> consultarLOVCiudades() {
        List<Ciudades> listLOVCiudades;
        listLOVCiudades = persistenciaCiudades.consultarCiudades(em);
        return listLOVCiudades;
    }

    @Override
    public BigInteger contarVigenciasFormasPagosSucursal(BigInteger secuenciaSucursal) {
        BigInteger contarVigenciasFormasPagosSucursal;
        contarVigenciasFormasPagosSucursal = persistenciaSucursales.contarVigenciasFormasPagosSucursal(em, secuenciaSucursal);
        return contarVigenciasFormasPagosSucursal;
    }
}
