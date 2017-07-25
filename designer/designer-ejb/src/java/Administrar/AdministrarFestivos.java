/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Festivos;
import Entidades.Paises;
import InterfaceAdministrar.AdministrarFestivosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaFestivosInterface;
import InterfacePersistencia.PersistenciaPaisesInterface;
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
public class AdministrarFestivos implements AdministrarFestivosInterface {

   private static Logger log = Logger.getLogger(AdministrarFestivos.class);

    @EJB
    PersistenciaPaisesInterface persistenciaPaises;
    @EJB
    PersistenciaFestivosInterface persistenciaFestivos;
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

    public void modificarFestivos(List<Festivos> listaFestivos) {
        for (int i = 0; i < listaFestivos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaFestivos.editar(em,listaFestivos.get(i));
        }
    }

    public void borrarFestivos(List<Festivos> listaFestivos) {
        for (int i = 0; i < listaFestivos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaFestivos.borrar(em,listaFestivos.get(i));
        }
    }

    public void crearFestivos(List<Festivos> listaFestivos) {
        for (int i = 0; i < listaFestivos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaFestivos.crear(em,listaFestivos.get(i));
        }
    }

    public List<Festivos> consultarFestivosPais(BigInteger secPais) {
        List<Festivos> listFestivos;
        listFestivos = persistenciaFestivos.consultarFestivosPais(em,secPais);
        return listFestivos;
    }

    public List<Paises> consultarLOVPaises() {
        List<Paises> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaPaises.consultarPaises(em);
        return listMotivosCambiosCargos;
    }

}
