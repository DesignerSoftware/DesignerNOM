/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Inforeportes;
import Entidades.Modulos;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarInforeportesInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public List<Inforeportes> inforeportes();

    public List<Modulos> lovmodulos();

    public void crearInforeporte(Inforeportes inforeportes);

    public void borrarInforeporte(Inforeportes inforeportes);

    public void modificarInforeporte(List<Inforeportes> listaInforeportesModificar);

    public Modulos buscarModuloPorSecuencia(BigInteger secModulo);
    
    public List<Inforeportes> listaInfoReportes();
    
    public List<Modulos> listaModulos();

    }
