/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.OdisCabeceras;
import Entidades.OdisDetalles;
import Entidades.RelacionesIncapacidades;
import Entidades.SucursalesPila;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaOdiCabeceraInterface {

    public void crear(EntityManager em, OdisCabeceras odicabecera);

    public void editar(EntityManager em, OdisCabeceras odicabecera);

    public void borrar(EntityManager em, OdisCabeceras odicabecera);

    public void crearDetalle(EntityManager em, OdisDetalles odidetalle);

    public void editarDetalle(EntityManager em, OdisDetalles odidetalle);

    public void borrarDetalle(EntityManager em, OdisDetalles odidetalle);

    public List<Terceros> lovTerceros(EntityManager em, BigInteger anio, BigInteger mes);

    public List<TiposEntidades> lovTiposEntidades(EntityManager em, BigInteger anio, BigInteger mes);

    public List<Empresas> lovEmpresas(EntityManager em);

    public List<Empleados> lovEmpleados(EntityManager em);

    public List<SucursalesPila> lovSucursalesPila(EntityManager em, BigInteger secuenciaEmpresa);

    public List<RelacionesIncapacidades> lovRelacionesIncapacidades(EntityManager em, BigInteger secuenciaEmpleado);

    public List<OdisCabeceras> listOdisCabeceras(EntityManager em, BigInteger anio, BigInteger mes, BigInteger secuenciaEmpresa);
}
