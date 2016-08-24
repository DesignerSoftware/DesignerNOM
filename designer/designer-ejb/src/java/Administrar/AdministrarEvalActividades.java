/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.EvalActividades;
import InterfaceAdministrar.AdministrarEvalActividadesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEvalActividadesInterface;
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
public class AdministrarEvalActividades implements AdministrarEvalActividadesInterface {

    @EJB
    PersistenciaEvalActividadesInterface persistenciaEvalActividades;
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

    public void modificarEvalActividades(List<EvalActividades> listaEvalActividades) {
        for (int i = 0; i < listaEvalActividades.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaEvalActividades.editar(em,listaEvalActividades.get(i));
        }
    }

    public void borrarEvalActividades(List<EvalActividades> listaEvalActividades) {
        for (int i = 0; i < listaEvalActividades.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaEvalActividades.borrar(em,listaEvalActividades.get(i));
        }
    }

    public void crearEvalActividades(List<EvalActividades> listaEvalActividades) {
        for (int i = 0; i < listaEvalActividades.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaEvalActividades.crear(em,listaEvalActividades.get(i));
        }
    }

    public List<EvalActividades> consultarEvalActividades() {
        List<EvalActividades> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaEvalActividades.consultarEvalActividades(em);
        return listMotivosCambiosCargos;
    }

    public EvalActividades consultarEvalActividad(BigInteger secEvalActividades) {
        EvalActividades subCategoria;
        subCategoria = persistenciaEvalActividades.consultarEvalActividad(em,secEvalActividades);
        return subCategoria;
    }

    public BigInteger contarCapBuzonesEvalActividad(BigInteger secEvalActividades) {
        BigInteger contarCapBuzonesEvalActividad = null;

        try {
            return contarCapBuzonesEvalActividad = persistenciaEvalActividades.contarCapBuzonesEvalActividad(em,secEvalActividades);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarEvalActividades contarCapBuzonesEvalActividad ERROR : " + e);
            return null;
        }
    }

    public BigInteger contarCapNecesidadesEvalActividad(BigInteger secEvalActividades) {
        BigInteger contarCapNecesidadesEvalActividad = null;

        try {
            return contarCapNecesidadesEvalActividad = persistenciaEvalActividades.contarCapNecesidadesEvalActividad(em,secEvalActividades);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarEvalActividades contarCapNecesidadesEvalActividad ERROR : " + e);
            return null;
        }
    }

    public BigInteger contarEvalPlanesDesarrollosEvalActividad(BigInteger secEvalActividades) {
        BigInteger contarEvalPlanesDesarrollosEvalActividad = null;

        try {
            return contarEvalPlanesDesarrollosEvalActividad = persistenciaEvalActividades.contarEvalPlanesDesarrollosEvalActividad(em,secEvalActividades);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarEvalActividades contarEvalPlanesDesarrollosEvalActividad ERROR : " + e);
            return null;
        }
    }

}
