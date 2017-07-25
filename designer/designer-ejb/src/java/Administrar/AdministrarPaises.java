/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Ciudades;
import Entidades.Departamentos;
import Entidades.Festivos;
import InterfaceAdministrar.AdministrarPaisesInterface;
import Entidades.Paises;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaDepartamentosInterface;
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
public class AdministrarPaises implements AdministrarPaisesInterface {

   private static Logger log = Logger.getLogger(AdministrarPaises.class);

    @EJB
    PersistenciaPaisesInterface persistenciaPaises;
    @EJB
    PersistenciaFestivosInterface persistenciaFestivos;
    @EJB
    PersistenciaCiudadesInterface persistenciaCiudades;
    @EJB
    PersistenciaDepartamentosInterface persistenciaDepartamentos;
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
    public void modificarPaises(List<Paises> listaPaises) {
        for (int i = 0; i < listaPaises.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaPaises.editar(em, listaPaises.get(i));
        }
    }

    @Override
    public void borrarPaises(List<Paises> listaPaises) {
        for (int i = 0; i < listaPaises.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaPaises.borrar(em, listaPaises.get(i));
        }
    }

    @Override
    public void crearPaises(List<Paises> listaPaises) {
        for (int i = 0; i < listaPaises.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaPaises.crear(em, listaPaises.get(i));
        }
    }

    public List<Paises> consultarPaises() {
        List<Paises> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaPaises.consultarPaises(em);
        return listMotivosCambiosCargos;
    }

    @Override
    public Paises consultarPais(BigInteger secPaises) {
        Paises subCategoria;
        subCategoria = persistenciaPaises.consultarPais(em, secPaises);
        return subCategoria;
    }

    @Override
    public BigInteger contarDepartamentosPais(BigInteger secPaises) {
        BigInteger contarDepartamentosPais = null;

        try {
            return contarDepartamentosPais = persistenciaPaises.contarDepartamentosPais(em, secPaises);
        } catch (Exception e) {
            log.error("ERROR AdministrarPaises contarDepartamentosPais ERROR : " + e);
            return null;
        }
    }

    @Override
    public BigInteger contarFestivosPais(BigInteger secPaises) {
        BigInteger contarFestivosPais = null;

        try {
            return contarFestivosPais = persistenciaPaises.contarFestivosPais(em, secPaises);
        } catch (Exception e) {
            log.error("ERROR AdministrarPaises contarFestivosPais ERROR : " + e);
            return null;
        }
    }

    @Override
    public List<Festivos> consultarFestivosPorPais(BigInteger secPais) {
        List<Festivos> festivos = persistenciaFestivos.consultarFestivosPais(em, secPais);
        return festivos;
    }

    @Override
    public List<Ciudades> consultarCiudadesPorDepto(BigInteger secDepto) {
        List<Ciudades> ciudades = persistenciaCiudades.consultarCiudadesPorDepto(em, secDepto);
        return ciudades;
    }

    @Override
    public List<Departamentos> consultarDeptosPorPais(BigInteger secPais) {
        List<Departamentos> deptos = persistenciaDepartamentos.consultarDepartamentosPorPais(em, secPais);
        return deptos;
    }
}
