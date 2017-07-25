/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposCotizantes;
import InterfaceAdministrar.AdministrarTiposCotizantesInterface;
import InterfacePersistencia.PersistenciaTiposCotizantesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposCotizantes implements AdministrarTiposCotizantesInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposCotizantes.class);

    @EJB
    PersistenciaTiposCotizantesInterface persistenciaTiposCotizantes;
    /**
     * Enterprise JavaBean.<br> Atributo que representa todo lo referente a la
     * conexión del usuario que está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<TiposCotizantes> tiposCotizantes() {
        try {
            List<TiposCotizantes> listaTiposCotizantes;
            listaTiposCotizantes = persistenciaTiposCotizantes.lovTiposCotizantes(em);
            return listaTiposCotizantes;
        } catch (Exception e) {
            log.warn("error en AdminsitrarTiposCotizantes.tiposCotizantes : " + e.toString());
            return null;
        }
    }

    @Override
    public void borrarTipoCotizante(List<TiposCotizantes> listBorrar) {
        for (int i = 0; i < listBorrar.size(); i++) {
            persistenciaTiposCotizantes.borrar(em, listBorrar.get(i));
        }
    }

    @Override
    public void crearTipoCotizante(List<TiposCotizantes> listCrear) {
        for (int i = 0; i < listCrear.size(); i++) {
            persistenciaTiposCotizantes.crear(em, listCrear.get(i));
        }
    }

    @Override
    public void modificarTipoCotizante(List<TiposCotizantes> listEditar) {
        try {

            for (int i = 0; i < listEditar.size(); i++) {
                log.warn("Modificando...");
                persistenciaTiposCotizantes.editar(em, listEditar.get(i));
            }
        } catch (Exception e) {
            log.warn("error en modificarTipoCotizante : " + e.toString());
        }
    }

    @Override
    public BigInteger clonarTipoCotizante(BigInteger codOrigen, BigInteger codDestino, String descripcion, BigInteger secClonado) {
       try{
           BigInteger secuenciaClonado = persistenciaTiposCotizantes.clonarTipoCotizante(em, codOrigen, codDestino, descripcion, secClonado);
           log.warn("secuencia clonado en el administrar : " + secuenciaClonado);
           return secuenciaClonado;
       }catch(Exception e){
           log.warn("error AdministrarTiposCotizantes.clonarTipoCotizante : " + e.toString());
           return null;
       }
    }
}
