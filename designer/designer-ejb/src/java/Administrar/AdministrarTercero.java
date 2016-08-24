package Administrar;

import Entidades.Ciudades;
import Entidades.Empresas;
import Entidades.Terceros;
import Entidades.TercerosSucursales;
import InterfaceAdministrar.AdministrarTerceroInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTercerosSucursalesInterface;
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
public class AdministrarTercero implements AdministrarTerceroInterface{

    @EJB
    PersistenciaTercerosInterface persistenciaTerceros;
    @EJB
    PersistenciaTercerosSucursalesInterface persistenciaTercerosSucursales;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    @EJB
    PersistenciaCiudadesInterface persistenciaCiudades;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    //

    private EntityManager em;
    
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Terceros> obtenerListTerceros(BigInteger secuencia) {
        try {
            List<Terceros> listTerceros = persistenciaTerceros.lovTerceros(em, secuencia);
            return listTerceros;
        } catch (Exception e) {
            System.out.println("Error en obtenerListTerceros Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void modificarTercero(Terceros t) {
        try {
            persistenciaTerceros.editar(em, t);
        } catch (Exception e) {
            System.out.println("Error en modificarTercero Admi : " + e.toString());
        }
    }

    @Override
    public void borrarTercero(Terceros t) {
        try {
            persistenciaTerceros.borrar(em, t);
        } catch (Exception e) {
            System.out.println("Error en borrarTercero Admi : " + e.toString());

        }
    }

    @Override
    public void crearTercero(Terceros t) {
        try {
            persistenciaTerceros.crear(em, t);
        } catch (Exception e) {
            System.out.println("Error en crearTercero Admi : " + e.toString());

        }
    }

    @Override
    public List<TercerosSucursales> obtenerListTercerosSucursales(BigInteger secuencia) {
        try {
            List<TercerosSucursales> listTercerosSurcursales = persistenciaTercerosSucursales.buscarTercerosSucursalesPorTerceroSecuencia(em, secuencia);
            return listTercerosSurcursales;
        } catch (Exception e) {
            System.out.println("Error obtenerListTercerosSucursales Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void modificarTerceroSucursales(TercerosSucursales t) {
        try {
            persistenciaTercerosSucursales.editar(em, t);
        } catch (Exception e) {
            System.out.println("Error en modificarTerceroSucursales Admi : " + e.toString());
        }
    }

    @Override
    public void borrarTerceroSucursales(TercerosSucursales t) {
        try {
            persistenciaTercerosSucursales.borrar(em, t);
        } catch (Exception e) {
            System.out.println("Error en borrarTerceroSucursales Admi : " + e.toString());
        }
    }

    @Override
    public void crearTerceroSucursales(TercerosSucursales t) {
        try {
            persistenciaTercerosSucursales.crear(em, t);
        } catch (Exception e) {
            System.out.println("Error en crearTerceroSucursales Admi : " + e.toString());
        }
    }

    @Override
    public List<Empresas> listEmpresas() {
        try {
            List<Empresas> listEmpresas = persistenciaEmpresas.consultarEmpresas(em);
            return listEmpresas;
        } catch (Exception e) {
            System.out.println("Error en listEmpresas Admi : "+e.toString());
            return null;
        }
    }
    
    @Override
    public List<Ciudades> listCiudades(){
        try{
            List<Ciudades> listCiudades = persistenciaCiudades.consultarCiudades(em);
            return listCiudades;
        }catch(Exception e){
            System.out.println("Error en listCiudades Admi : "+e.toString());
            return null;
        }
    }

}
