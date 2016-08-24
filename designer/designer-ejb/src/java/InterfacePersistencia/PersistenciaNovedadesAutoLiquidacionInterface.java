/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.Empresas;
import Entidades.NovedadesAutoLiquidaciones;
import Entidades.SucursalesPila;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaNovedadesAutoLiquidacionInterface {
public void crear(EntityManager em, NovedadesAutoLiquidaciones novedades);   
public void editar(EntityManager em, NovedadesAutoLiquidaciones novedades);   
public void borrar(EntityManager em, NovedadesAutoLiquidaciones novedades);   
public List <SucursalesPila> listasucursalesPila(EntityManager em, BigInteger secuenciaEmpresa);
public List <Terceros> listaTerceros(EntityManager em);
public List <TiposEntidades> listaTiposEntidades(EntityManager em);
public List <Empresas> listaEmpresas(EntityManager em);
public List <NovedadesAutoLiquidaciones> listaNovedades(EntityManager em,BigInteger anio, BigInteger mes, BigInteger secuenciaEmpresa);
}
