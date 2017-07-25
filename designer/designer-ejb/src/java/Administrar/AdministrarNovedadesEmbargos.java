/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.DetallesFormasDtos;
import Entidades.EersPrestamos;
import Entidades.EersPrestamosDtos;
import Entidades.Empleados;
import Entidades.FormasDtos;
import Entidades.Juzgados;
import Entidades.MotivosEmbargos;
import Entidades.Periodicidades;
import Entidades.Terceros;
import Entidades.TiposEmbargos;
import Entidades.VWPrestamoDtosRealizados;
import InterfaceAdministrar.AdministrarNovedadesEmbargosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaDetallesFormasDtosInterface;
import InterfacePersistencia.PersistenciaEersPrestamosDtosInterface;
import InterfacePersistencia.PersistenciaEersPrestamosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaFormasDtosInterface;
import InterfacePersistencia.PersistenciaJuzgadosInterface;
import InterfacePersistencia.PersistenciaMotivosEmbargosInterface;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTiposEmbargosInterface;
import InterfacePersistencia.PersistenciaVWPrestamoDtosRealizadosInterface;
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
public class AdministrarNovedadesEmbargos implements AdministrarNovedadesEmbargosInterface {

   private static Logger log = Logger.getLogger(AdministrarNovedadesEmbargos.class);

    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    @EJB
    PersistenciaJuzgadosInterface persistenciaJuzgados;
    @EJB
    PersistenciaTiposEmbargosInterface persistenciaTiposEmbargos;
    @EJB
    PersistenciaMotivosEmbargosInterface persistenciaMotivosEmbargos;
    @EJB
    PersistenciaTercerosInterface persistenciaTerceros;
    @EJB
    PersistenciaEersPrestamosInterface persistenciaEers;
    @EJB
    PersistenciaEersPrestamosDtosInterface persistenciaEersDtos;
    @EJB
    PersistenciaFormasDtosInterface persistenciaFormasDtos;
    @EJB
    PersistenciaPeriodicidadesInterface persistenciaPeriodicidades;
    @EJB
    PersistenciaDetallesFormasDtosInterface persistenciaDetallesFormasDtos;
    @EJB
    PersistenciaVWPrestamoDtosRealizadosInterface persistenciaVWPrestamo;
    public EersPrestamosDtos dE;
    public EersPrestamos e;
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
    public List<Empleados> listaEmpleados() {
        return persistenciaEmpleados.empleadosNovedadEmbargo(em);
    }

    @Override
    public List<Empleados> lovEmpleados() {
        return persistenciaEmpleados.empleadosNovedadEmbargo(em);
    }

    public List<TiposEmbargos> lovTiposEmbargos() {
        return persistenciaTiposEmbargos.buscarTiposEmbargos(em);
    }

    public List<Juzgados> lovJuzgados() {
        return persistenciaJuzgados.buscarJuzgados(em);
    }

    public List<MotivosEmbargos> lovMotivosEmbargos() {
        return persistenciaMotivosEmbargos.buscarMotivosEmbargos(em);
    }

    public List<Terceros> lovTerceros() {
        return persistenciaTerceros.buscarTerceros(em);
    }

    public List<Periodicidades> lovPeriodicidades() {
        return persistenciaPeriodicidades.consultarPeriodicidades(em);
    }

    public List<FormasDtos> lovFormasDtos(BigInteger tipoEmbargo) {
        return persistenciaFormasDtos.formasDescuentos(em, tipoEmbargo);
    }

    //Segunda Tabla
    public List<EersPrestamos> eersPrestamosEmpleado(BigInteger secuenciaEmpleado) {
        return persistenciaEers.eersPrestamosEmpleado(em, secuenciaEmpleado);
    }

    //LOV Formas de descuento segunda tabla
    public List<DetallesFormasDtos> lovDetallesFormasDescuentos(BigInteger formasDtos) {
        return persistenciaDetallesFormasDtos.detallesFormasDescuentos(em, formasDtos);
    }

    public List<FormasDtos> formasDescuentos(BigInteger tipoEmbargo) {
        return persistenciaFormasDtos.formasDescuentos(em, tipoEmbargo);
    }

    //Tercera Tabla
    public List<EersPrestamosDtos> eersPrestamosEmpleadoDtos(BigInteger secuenciaEersPrestamo) {
        return persistenciaEersDtos.eersPrestamosDtosEmpleado(em, secuenciaEersPrestamo);
    }

    public List<VWPrestamoDtosRealizados> prestamosRealizados(BigInteger secuencia) {
        return persistenciaVWPrestamo.buscarPrestamosDtos(em, secuencia);
    }

    //AGREGAR, BORRAR Y MODIFICAR DE LA TABLA DE ABAJO //
    @Override
    public void modificarDetalleEmbargo(List<EersPrestamosDtos> listaDetallesEmbargosModificar) {
        for (int i = 0; i < listaDetallesEmbargosModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaDetallesEmbargosModificar.get(i).getValor() == null) {
                listaDetallesEmbargosModificar.get(i).setValor(null);
            }
            if (listaDetallesEmbargosModificar.get(i).getPorcentaje() == null) {
                listaDetallesEmbargosModificar.get(i).setPorcentaje(null);
            }
            if (listaDetallesEmbargosModificar.get(i).getSaldoinicial() == null) {
                listaDetallesEmbargosModificar.get(i).setSaldoinicial(null);
            }

            persistenciaEersDtos.editar(em, dE);
        }
    }

    @Override
    public void borrarDetalleEmbargo(EersPrestamosDtos detallesEmbargos) {
        persistenciaEersDtos.borrar(em, detallesEmbargos);
    }

    @Override
    public void crearDetalleEmbargo(EersPrestamosDtos detallesEmbargos) {
        persistenciaEersDtos.crear(em, detallesEmbargos);
    }
    //AGREGAR, BORRAR Y MODIFICAR DE LA TABLA DE Arriba //

    @Override
    public void modificarEmbargo(List<EersPrestamos> listaEmbargosModificar) {
        for (int i = 0; i < listaEmbargosModificar.size(); i++) {
            log.warn("ListaEmbargosModificar " + listaEmbargosModificar.size());
            log.warn("Modificando...");
            if (listaEmbargosModificar.get(i).getTipoembargo() == null) {
                listaEmbargosModificar.get(i).setTipoembargo(null);
            }
            if (listaEmbargosModificar.get(i).getCancelaciondocumento() == null) {
                listaEmbargosModificar.get(i).setCancelaciondocumento(null);
            }
            if (listaEmbargosModificar.get(i).getCancelacionfechahasta() == null) {
                listaEmbargosModificar.get(i).setCancelacionfechahasta(null);
            }
            if (listaEmbargosModificar.get(i).getMotivoembargo() == null) {
                listaEmbargosModificar.get(i).setMotivoembargo(null);
            }
            if (listaEmbargosModificar.get(i).getJuzgado() == null) {
                listaEmbargosModificar.get(i).setJuzgado(null);
            }
            if (listaEmbargosModificar.get(i).getNumeroproceso() == null) {
                listaEmbargosModificar.get(i).setNumeroproceso(null);
            }

            persistenciaEers.editar(em, listaEmbargosModificar.get(i));
        }
    }

    @Override
    public void borrarEmbargo(EersPrestamos embargos) {
        persistenciaEers.borrar(em, embargos);
    }

    @Override
    public void crearEmbargo(EersPrestamos embargos) {
        persistenciaEers.crear(em, embargos);
    }
}
