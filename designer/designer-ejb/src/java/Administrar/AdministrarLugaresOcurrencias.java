/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Administrar;

import Entidades.LugaresOcurrencias;
import InterfaceAdministrar.AdministrarLugaresOcurrenciasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaLugaresOcurrenciasInterface;
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
public class AdministrarLugaresOcurrencias implements AdministrarLugaresOcurrenciasInterface {

   private static Logger log = Logger.getLogger(AdministrarLugaresOcurrencias.class);

  @EJB
    PersistenciaLugaresOcurrenciasInterface persistenciaLugaresOcurrencias;
    private LugaresOcurrencias lugarOcurrenciaSeleccionada;
    private LugaresOcurrencias lugarOcurrencia;
    private List<LugaresOcurrencias> listLugarOcurrencia;
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
    public void modificarLugarOcurrencia(List<LugaresOcurrencias> listaLugaresOcurrencias) {
        for (int i = 0; i < listaLugaresOcurrencias.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaLugaresOcurrencias.editar(em, listaLugaresOcurrencias.get(i));
        }
    }

  @Override
    public void borrarLugarOcurrencia(List<LugaresOcurrencias> listaLugaresOcurrencias) {
        for (int i = 0; i < listaLugaresOcurrencias.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaLugaresOcurrencias.borrar(em, listaLugaresOcurrencias.get(i));
        }
    }

  @Override
    public void crearLugarOcurrencia(List<LugaresOcurrencias> listaLugaresOcurrencias) {
        for (int i = 0; i < listaLugaresOcurrencias.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaLugaresOcurrencias.crear(em, listaLugaresOcurrencias.get(i));
        }
    }

  @Override
    public List<LugaresOcurrencias> consultarLugaresOcurrencias() {
        listLugarOcurrencia = persistenciaLugaresOcurrencias.buscarLugaresOcurrencias(em);
        return listLugarOcurrencia;
    }

  @Override
    public LugaresOcurrencias consultarLugarOcurrencia(BigInteger secLugarOcurrencia) {
        lugarOcurrencia = persistenciaLugaresOcurrencias.buscarLugaresOcurrencias(em, secLugarOcurrencia);
        return lugarOcurrencia;
    }

  @Override
    public BigInteger verificarSoAccidentesLugarOcurrencia(BigInteger secuenciaLugaresOcurrencias) {
        BigInteger verificarSoAccidentes;
        try {
            return verificarSoAccidentes = persistenciaLugaresOcurrencias.contadorSoAccidentes(em, secuenciaLugaresOcurrencias);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARLUGARESOCURRENCIAS VERIFICAR SO ACCIDENTES ERROR : " + e);
            return null;
        }
    }
}
