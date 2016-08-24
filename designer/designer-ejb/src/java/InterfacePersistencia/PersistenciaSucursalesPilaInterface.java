/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.SucursalesPila;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaSucursalesPilaInterface {

    public void crear(EntityManager em, SucursalesPila sucursalesPilas);

    public void editar(EntityManager em, SucursalesPila sucursalesPilas);

    public void borrar(EntityManager em, SucursalesPila sucursalesPilas);

    public List<SucursalesPila> consultarSucursalesPila(EntityManager em);

    public List<SucursalesPila> consultarSucursalesPilaPorEmpresa(EntityManager em, BigInteger secEmpresa);

    public BigInteger contarUbicacionesGeograficasSucursal_Pila(EntityManager em, BigInteger secuencia);

    public BigInteger contarParametrosInformesSucursal_Pila(EntityManager em, BigInteger secuencia);

    public BigInteger contarOdiscorReaccionesCabSucursal_Pila(EntityManager em, BigInteger secuencia);

    public BigInteger contarOdisCabecerasSucursal_Pila(EntityManager em, BigInteger secuencia);

    public BigInteger contarNovedadesCorreccionesAutolSucursal_Pila(EntityManager em, BigInteger secuencia);

    public BigInteger contarNovedadesAutoLiquidacionesSucursal_Pila(EntityManager em, BigInteger secuencia);
}
