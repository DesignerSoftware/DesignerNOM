/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarMotivosPrestamosInterface;
import Entidades.MotivosPrestamos;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosPrestamosInterface;
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
public class AdministrarMotivosPrestamos implements AdministrarMotivosPrestamosInterface {

    @EJB
    PersistenciaMotivosPrestamosInterface persistenciaMotivosPrestamos;
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
    public void modificarMotivosPrestamos(List<MotivosPrestamos> listaMotivosPrestamos) {
        for (int i = 0; i < listaMotivosPrestamos.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaMotivosPrestamos.editar(em, listaMotivosPrestamos.get(i));
        }
    }

    @Override
    public void borrarMotivosPrestamos(List<MotivosPrestamos> listaMotivosPrestamos) {
        for (int i = 0; i < listaMotivosPrestamos.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaMotivosPrestamos.borrar(em, listaMotivosPrestamos.get(i));
        }
    }

    @Override
    public void crearMotivosPrestamos(List<MotivosPrestamos> listaMotivosPrestamos) {
        for (int i = 0; i < listaMotivosPrestamos.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaMotivosPrestamos.crear(em, listaMotivosPrestamos.get(i));
        }
    }

    @Override
    public List<MotivosPrestamos> mostrarMotivosPrestamos() {
        List<MotivosPrestamos> listMotivosPrestamos;
        listMotivosPrestamos = persistenciaMotivosPrestamos.buscarMotivosPrestamos(em);
        return listMotivosPrestamos;
    }

    @Override
    public MotivosPrestamos mostrarMotivoPrestamo(BigInteger secMotivoPrestamo) {
        MotivosPrestamos motivosPrestamos;
        motivosPrestamos = persistenciaMotivosPrestamos.buscarMotivoPrestamo(em, secMotivoPrestamo);
        return motivosPrestamos;
    }

    @Override
    public BigInteger verificarEersPrestamosMotivoPrestamo(BigInteger secuenciaMotivosPrestamos) {
        try {
            BigInteger verificarBorradoEersPrestamos = null;
            return verificarBorradoEersPrestamos = persistenciaMotivosPrestamos.contadorEersPrestamos(em, secuenciaMotivosPrestamos);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARMOTIVOSPRESTAMOS VERIFICARDIASLABORALES ERROR :" + e);
            return null;
        }
    }
}
