/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposTallasInterface;
import Entidades.TiposTallas;
import InterfacePersistencia.PersistenciaTiposTallasInterface;
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
public class AdministrarTiposTallas implements AdministrarTiposTallasInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposTallas.class);

    @EJB
    PersistenciaTiposTallasInterface persistenciaTiposTallas;
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
    public void modificarTiposTallas(List<TiposTallas> listTiposTallas) {
        for (int i = 0; i < listTiposTallas.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposTallas.editar(em, listTiposTallas.get(i));
        }
    }

    @Override
    public void borrarTiposTallas(List<TiposTallas> listTiposTallas) {
        for (int i = 0; i < listTiposTallas.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposTallas.borrar(em, listTiposTallas.get(i));
        }
    }

    @Override
    public void crearTiposTallas(List<TiposTallas> listTiposTallas) {
        for (int i = 0; i < listTiposTallas.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposTallas.crear(em, listTiposTallas.get(i));
        }
    }

    @Override
    public List<TiposTallas> consultarTiposTallas() {
        List<TiposTallas> listTiposTallas;
        listTiposTallas = persistenciaTiposTallas.buscarTiposTallas(em);
        return listTiposTallas;
    }

    @Override
    public TiposTallas consultarTipoTalla(BigInteger secTipoEmpresa) {
        TiposTallas tiposTallas;
        tiposTallas = persistenciaTiposTallas.buscarTipoTalla(em, secTipoEmpresa);
        return tiposTallas;
    }

    @Override
    public BigInteger contarElementosTipoTalla(BigInteger secuenciaElementos) {
        try {
            BigInteger verificadorElementos;
            log.error("Secuencia Borrado Elementos" + secuenciaElementos);
            return verificadorElementos = persistenciaTiposTallas.contadorElementos(em, secuenciaElementos);
        } catch (Exception e) {
            log.error("ERROR AdministrarTiposTallas verificarBorradoElementos ERROR :" + e);
            return null;
        }
    }

    @Override
    public BigInteger contarVigenciasTallasTipoTalla(BigInteger secuenciaVigenciasTallas) {
        try {
            BigInteger verificadorVigenciasTallas;
            log.error("Secuencia Borrado Vigencias Tallas" + secuenciaVigenciasTallas);
            return verificadorVigenciasTallas = persistenciaTiposTallas.contadorVigenciasTallas(em, secuenciaVigenciasTallas);
        } catch (Exception e) {
            log.error("ERROR AdministrarTiposTallas verificarBorradoVigenciasTallas ERROR :" + e);
            return null;
        }
    }
}
