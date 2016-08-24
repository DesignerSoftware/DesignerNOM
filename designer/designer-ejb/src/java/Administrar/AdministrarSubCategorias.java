/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.SubCategorias;
import InterfaceAdministrar.AdministrarSubCategoriasInterface;
import InterfacePersistencia.PersistenciaSubCategoriasInterface;
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
public class AdministrarSubCategorias implements AdministrarSubCategoriasInterface {

    @EJB
    PersistenciaSubCategoriasInterface persistenciaSubCategorias;
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
    public void modificarSubCategorias(List<SubCategorias> listaSubCategorias) {
        for (int i = 0; i < listaSubCategorias.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaSubCategorias.editar(em, listaSubCategorias.get(i));
        }
    }

    @Override
    public void borrarSubCategorias(List<SubCategorias> listaSubCategorias) {
        for (int i = 0; i < listaSubCategorias.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaSubCategorias.borrar(em, listaSubCategorias.get(i));
        }
    }

    @Override
    public void crearSubCategorias(List<SubCategorias> listaSubCategorias) {
        for (int i = 0; i < listaSubCategorias.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaSubCategorias.crear(em, listaSubCategorias.get(i));
        }
    }

    @Override
    public List<SubCategorias> consultarSubCategorias() {
        List<SubCategorias> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaSubCategorias.consultarSubCategorias(em);
        return listMotivosCambiosCargos;
    }

    @Override
    public SubCategorias consultarSubCategoria(BigInteger secSubCategorias) {
        SubCategorias subCategoria;
        subCategoria = persistenciaSubCategorias.consultarSubCategoria(em, secSubCategorias);
        return subCategoria;
    }

    @Override
    public BigInteger contarEscalafones(BigInteger secSubCategorias) {
        BigInteger contarEscalafones = null;

        try {
            return contarEscalafones = persistenciaSubCategorias.contarEscalafones(em, secSubCategorias);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarSubCategorias contarEscalafones ERROR : " + e);
            return null;
        }
    }
}
