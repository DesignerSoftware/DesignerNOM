/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.HVHojasDeVida;
import Entidades.HvReferencias;
import Entidades.Personas;
import Entidades.TiposFamiliares;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarHvReferencias1Interface {

    public void obtenerConexion(String idSesion);
    public void modificarHvReferencias(List<HvReferencias> listaHvReferencias);
    public void borrarHvReferencias(List<HvReferencias> listaHvReferencias);
    public void crearHvReferencias(List<HvReferencias> listaHvReferencias);
    public HvReferencias consultarHvReferencia(BigInteger secHvReferencias);
    public List<HvReferencias> consultarHvReferenciasFamiliaresPorPersona(BigInteger secEmpleado);
    public List<HVHojasDeVida> consultarHvHojasDeVida(BigInteger secEmpleado);
    public List<TiposFamiliares> consultarLOVTiposFamiliares();
    public Personas consultarPersonas(BigInteger secEmpleado);
    public Empleados empleadoActual(BigInteger secuencia);   
}
