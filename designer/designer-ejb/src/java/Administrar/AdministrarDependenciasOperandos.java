/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.DependenciasOperandos;
import Entidades.Operandos;
import InterfaceAdministrar.AdministrarDependenciasOperandosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaDependenciasOperandosInterface;
import InterfacePersistencia.PersistenciaOperandosInterface;
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
public class AdministrarDependenciasOperandos implements AdministrarDependenciasOperandosInterface {

    @EJB
    PersistenciaDependenciasOperandosInterface persistenciaDependenciasOperandos;
    @EJB
    PersistenciaOperandosInterface persistenciaOperandos;
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
    public List<DependenciasOperandos> buscarDependenciasOperandos(BigInteger secuenciaOperando) {
        List<DependenciasOperandos> listaDependenciasOperandos;
        listaDependenciasOperandos = persistenciaDependenciasOperandos.dependenciasOperandos(em,secuenciaOperando);
        return listaDependenciasOperandos;
    }

    @Override
    public void borrarDependenciasOperandos(DependenciasOperandos tiposConstantes) {
        persistenciaDependenciasOperandos.borrar(em,tiposConstantes);
    }

    @Override
    public void crearDependenciasOperandos(DependenciasOperandos tiposConstantes) {
        persistenciaDependenciasOperandos.crear(em,tiposConstantes);
    }

    @Override
    public void modificarDependenciasOperandos(DependenciasOperandos tiposConstantes) {
        persistenciaDependenciasOperandos.editar(em,tiposConstantes);

    }

    @Override
    public List<Operandos> buscarOperandos() {
        List<Operandos> listaOperandos;
        listaOperandos = persistenciaOperandos.buscarOperandos(em);
        return listaOperandos;
    }

    @Override
    public String nombreOperandos(int codigo) {
        String nombre;
        nombre = persistenciaDependenciasOperandos.nombreOperandos(em,codigo);
        return nombre;
    }
}
