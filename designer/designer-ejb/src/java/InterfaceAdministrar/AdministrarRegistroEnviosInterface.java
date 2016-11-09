/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Inforeportes;
import Entidades.EnvioCorreos;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarRegistroEnviosInterface {
    
    public void obtenerConexion(String idSesion);

    public List<EnvioCorreos> consultarEnvioCorreos(BigInteger reporte);

    public Inforeportes consultarPorSecuencia(BigInteger envio);

//    public List<Empleados> consultarEmpleados(BigInteger reporte);
    public void editarEnvioCorreos(EnvioCorreos listaEC);

    public void modificarEC(List<EnvioCorreos> listECModificadas);

    public void borrarEnvioCorreos(EnvioCorreos listaEC);
}
