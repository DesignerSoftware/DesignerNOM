/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.SoActosInseguros;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarSoActosInsegurosInterface;
import InterfacePersistencia.PersistenciaSoActosInsegurosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author John Pineda
 */
@Stateful
public class AdministrarSoActosInseguros implements AdministrarSoActosInsegurosInterface {

   private static Logger log = Logger.getLogger(AdministrarSoActosInseguros.class);

    @EJB
    PersistenciaSoActosInsegurosInterface persistenciaSoActosInseguros;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    
    private List<SoActosInseguros> listSoActosInseguros;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    @Override
    public void modificarSoActosInseguros(List<SoActosInseguros> listSoActosInseguros) {
        for (int i = 0; i < listSoActosInseguros.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaSoActosInseguros.editar(em, listSoActosInseguros.get(i));
        }
    }

    @Override
    public void borrarSoActosInseguros(List<SoActosInseguros> listSoActosInseguros) {
        for (int i = 0; i < listSoActosInseguros.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaSoActosInseguros.borrar(em, listSoActosInseguros.get(i));
        }
    }

    @Override
    public void crearSoActosInseguros(List<SoActosInseguros> listSoActosInseguros) {
        for (int i = 0; i < listSoActosInseguros.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaSoActosInseguros.crear(em, listSoActosInseguros.get(i));
        }
    }

    @Override
    public List<SoActosInseguros> consultarSoActosInseguros() {
        listSoActosInseguros = persistenciaSoActosInseguros.buscarSoActosInseguros(em);
        return listSoActosInseguros;
    }

    @Override
    public SoActosInseguros consultarSoActoInseguro(BigInteger secSoCondicionesAmbientalesP) {
        SoActosInseguros soActosInseguros;
        soActosInseguros = persistenciaSoActosInseguros.buscarSoActoInseguro(em, secSoCondicionesAmbientalesP);
        return soActosInseguros;
    }

    @Override
    public BigInteger verificarSoAccidentesMedicos(BigInteger secuenciaElementos) {
        BigInteger verificarSoAccidtenesMedicos;
        try {
            log.error("Secuencia Borrado Elementos" + secuenciaElementos);
            return verificarSoAccidtenesMedicos = persistenciaSoActosInseguros.contadorSoAccidentesMedicos(em, secuenciaElementos);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARSOACTOSINSEGUROS verificarSoAccidtenesMedicos ERROR :" + e);
            return null;
        }
    }
}
