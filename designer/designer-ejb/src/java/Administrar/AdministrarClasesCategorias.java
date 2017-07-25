/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Administrar;

import Entidades.ClasesCategorias;
import InterfaceAdministrar.AdministrarClasesCategoriasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaClasesCategoriasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarClasesCategorias implements AdministrarClasesCategoriasInterface {

   private static Logger log = Logger.getLogger(AdministrarClasesCategorias.class);

   @EJB
    PersistenciaClasesCategoriasInterface persistenciaClasesCategorias;
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
    public void modificarClasesCategorias(List<ClasesCategorias> listaClasesCategorias) {
        for (int i = 0; i < listaClasesCategorias.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaClasesCategorias.editar(em,listaClasesCategorias.get(i));
        }
    }

    @Override
    public void borrarClasesCategorias(List<ClasesCategorias> listaClasesCategorias) {
        for (int i = 0; i < listaClasesCategorias.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaClasesCategorias.borrar(em,listaClasesCategorias.get(i));
        }
    }

    @Override
    public void crearClasesCategorias(List<ClasesCategorias> listaClasesCategorias) {
        for (int i = 0; i < listaClasesCategorias.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaClasesCategorias.crear(em,listaClasesCategorias.get(i));
        }
    }

    public List<ClasesCategorias> consultarClasesCategorias() {
        List<ClasesCategorias> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaClasesCategorias.consultarClasesCategorias(em);
        return listMotivosCambiosCargos;
    }

    @Override
    public ClasesCategorias consultarClaseCategoria(BigInteger secClasesCategorias) {
        ClasesCategorias subCategoria;
        subCategoria = persistenciaClasesCategorias.consultarClaseCategoria(em,secClasesCategorias);
        return subCategoria;
    }

    @Override
    public BigInteger contarCategoriaClaseCategoria(BigInteger secClasesCategorias) {
        BigInteger contarCategoriaClaseCategoria = null;

        try {
            return contarCategoriaClaseCategoria = persistenciaClasesCategorias.contarCategoriasClaseCategoria(em,secClasesCategorias);
        } catch (Exception e) {
            log.error("ERROR AdministrarClasesCategorias contarCategoriaClaseCategoria ERROR : " + e);
            return null;
        }
    }
}
