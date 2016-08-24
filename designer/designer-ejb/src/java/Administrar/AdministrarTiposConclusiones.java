/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Administrar;

import Entidades.TiposConclusiones;
import InterfaceAdministrar.AdministrarTiposConclusionesInterface;
import InterfacePersistencia.PersistenciaTiposConclusionesInterface;
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
public class AdministrarTiposConclusiones implements AdministrarTiposConclusionesInterface {

   @EJB
    PersistenciaTiposConclusionesInterface persistenciaTiposConclusiones;
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
    public void modificarTiposConclusiones(List<TiposConclusiones> listaTiposConclusiones) {
        for (int i = 0; i < listaTiposConclusiones.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaTiposConclusiones.editar(em, listaTiposConclusiones.get(i));
        }
    }

    @Override
    public void borrarTiposConclusiones(List<TiposConclusiones> listaTiposConclusiones) {
        for (int i = 0; i < listaTiposConclusiones.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaTiposConclusiones.borrar(em, listaTiposConclusiones.get(i));
        }
    }

    @Override
    public void crearTiposConclusiones(List<TiposConclusiones> listaTiposConclusiones) {
        for (int i = 0; i < listaTiposConclusiones.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaTiposConclusiones.crear(em, listaTiposConclusiones.get(i));
        }
    }

   @Override
    public List<TiposConclusiones> consultarTiposConclusiones() {
        List<TiposConclusiones> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaTiposConclusiones.consultarTiposConclusiones(em);
        return listMotivosCambiosCargos;
    }

    @Override
    public TiposConclusiones consultarTipoConclusion(BigInteger secTiposConclusiones) {
        TiposConclusiones subCategoria;
        subCategoria = persistenciaTiposConclusiones.consultarTipoConclusion(em, secTiposConclusiones);
        return subCategoria;
    }

    @Override
    public BigInteger contarProcesosTipoConclusion(BigInteger secTiposConclusiones) {
        BigInteger contarProcesosTipoConclusion = null;

        try {
            return contarProcesosTipoConclusion = persistenciaTiposConclusiones.contarChequeosMedicosTipoConclusion(em, secTiposConclusiones);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarTiposConclusiones contarChequeosMedicosTipoConclusion ERROR : " + e);
            return null;
        }
    }
}
