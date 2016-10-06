/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Empleados;
import Entidades.HVHojasDeVida;
import Entidades.HvReferencias;
import Entidades.Personas;
import Entidades.TiposFamiliares;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaHvReferencias1Interface {

    public List<HvReferencias> referenciasPersonalesPersona(EntityManager em, BigInteger secuenciaHV);

    public List<HvReferencias> contarReferenciasFamiliaresPersona(EntityManager em, BigInteger secuenciaHV);

    public void crear(EntityManager em, HvReferencias hvReferencias);

    public void editar(EntityManager em, HvReferencias hvReferencias);

    public void borrar(EntityManager em, HvReferencias hvReferencias);

    public HvReferencias buscarHvReferencia(EntityManager em, BigInteger secuenciaHvReferencias);

    public List<HvReferencias> buscarHvReferencias(EntityManager em);

    public List<HvReferencias> consultarHvReferenciasFamiliarPorPersona(EntityManager em, BigInteger secEmpleado);

    public List<HVHojasDeVida> consultarHvHojaDeVidaPorPersona(EntityManager em, BigInteger secEmpleado);
}
