/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarHvReferenciasInterface;
import Entidades.Empleados;
import Entidades.HVHojasDeVida;
import Entidades.HvReferencias;
import Entidades.Personas;
import Entidades.TiposFamiliares;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaHvReferenciasInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaTiposFamiliaresInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarHvReferencias implements AdministrarHvReferenciasInterface {

    @EJB
    PersistenciaHvReferenciasInterface persistenciaHvReferencias;
    @EJB
    PersistenciaTiposFamiliaresInterface persistenciaTiposFamiliares;
    @EJB
    PersistenciaPersonasInterface persistenciaPersonas;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    HvReferencias hvReferencias;
    List<HVHojasDeVida> hvHojasDeVida;
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
    public void borrarHvReferencias(List<HvReferencias> listaHvReferencias) {
        for (int i = 0; i < listaHvReferencias.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaHvReferencias.borrar(em, listaHvReferencias.get(i));
        }
    }

    @Override
    public void crearHvReferencias(List<HvReferencias> listaHvReferencias) {
        for (int i = 0; i < listaHvReferencias.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaHvReferencias.crear(em, listaHvReferencias.get(i));
        }
    }

    @Override
    public void modificarHvReferencias(List<HvReferencias> listaHvReferencias) {
        for (int i = 0; i < listaHvReferencias.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaHvReferencias.editar(em, listaHvReferencias.get(i));
        }
    }

    @Override
    public List<HvReferencias> consultarHvReferenciasPersonalesPorPersona(BigInteger secEmpleado) {
        List<HvReferencias> listHvReferencias;
        try {
            listHvReferencias = persistenciaHvReferencias.consultarHvReferenciasPersonalesPorPersona(em, secEmpleado);
            if (listHvReferencias != null) {
                System.out.println("AdministrarHvReferencias Tamaño listHvReferencias : " + listHvReferencias.size());
            }
        } catch (Exception e) {
            System.out.println("Error en AdministrarHvReferencias hvEntrevistasPorEmplado");
            listHvReferencias = null;
        }
        return listHvReferencias;
    }

    @Override
    public List<HvReferencias> consultarHvReferenciasFamiliaresPorPersona(BigInteger secEmpleado) {
        List<HvReferencias> listHvReferencias;
        try {
            listHvReferencias = persistenciaHvReferencias.consultarHvReferenciasFamiliarPorPersona(em, secEmpleado);
        } catch (Exception e) {
            System.out.println("Error en AdministrarHvReferencias hvEntrevistasPorEmplado");
            listHvReferencias = null;
        }
        return listHvReferencias;
    }

    @Override
    public HvReferencias consultarHvReferencia(BigInteger secHvEntrevista) {
        persistenciaHvReferencias.buscarHvReferencia(em, secHvEntrevista);
        return hvReferencias;
    }

    @Override
    public Personas consultarPersonas(BigInteger secuencia) {
        Personas persona;
        try {
            persona = persistenciaPersonas.buscarPersona(em, secuencia);
            return persona;
        } catch (Exception e) {
            persona = null;
            System.out.println("ERROR AdministrarHvReferencias  consultarPersonas ERROR =====" + e);
            return persona;
        }
    }

    @Override
    public List<HVHojasDeVida> consultarHvHojasDeVida(BigInteger secuencia) {
        try {
            hvHojasDeVida = persistenciaHvReferencias.consultarHvHojaDeVidaPorPersona(em, secuencia);
            return hvHojasDeVida;
        } catch (Exception e) {
            hvHojasDeVida = null;
            System.out.println("ERROR AdministrarHvReferencias  buscarHvHojasDeVida ERROR =====" + e);
            return hvHojasDeVida;
        }
    }

    @Override
    public List<TiposFamiliares> consultarLOVTiposFamiliares() {
        try {
            List<TiposFamiliares> listTiposFamiliares;
            listTiposFamiliares = persistenciaTiposFamiliares.buscarTiposFamiliares(em);
            return listTiposFamiliares;
        } catch (Exception e) {
            System.err.println("ERROR EN ADMINISTRAR HV REFERENCIAS 1 ERROR " + e);
            return null;
        }
    }

     @Override
    public Empleados empleadoActual(BigInteger secuenciaP){
        try{
        Empleados retorno = persistenciaEmpleado.buscarEmpleado(em, secuenciaP);
        return retorno;
        }catch(Exception  e){
            System.out.println("Error empleadoActual Admi : "+e.toString());
            return null;
        }
    }
    
}
