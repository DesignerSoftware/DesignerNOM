/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Inforeportes;
import Entidades.Modulos;
import InterfaceAdministrar.AdministrarInforeportesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaInforeportesInterface;
import InterfacePersistencia.PersistenciaModulosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

@Stateful
public class AdministrarInforeportes implements AdministrarInforeportesInterface {

    @EJB
    PersistenciaInforeportesInterface persistenciaInforeportes;
    @EJB
    PersistenciaModulosInterface persistenciaModulos;

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
    
    public List<Inforeportes> inforeportes() {
        List<Inforeportes> listaInforeportes;
        listaInforeportes = persistenciaInforeportes.buscarInforeportes(em);
        return listaInforeportes;
    }
    
    public Modulos buscarModuloPorSecuencia(BigInteger secModulo) {
        Modulos modu;
        modu = persistenciaModulos.buscarModulosPorSecuencia(em, secModulo);
        return modu;
    }

    public List<Modulos> lovmodulos() {
        List<Modulos> listaModulos;
        listaModulos = persistenciaModulos.buscarModulos(em);
        return listaModulos;
    }
    
    @Override
    public void borrarInforeporte(Inforeportes inforeportes) {
        persistenciaInforeportes.borrar(em, inforeportes);
    }

    @Override
    public void crearInforeporte(Inforeportes inforeportes) {
        persistenciaInforeportes.crear(em, inforeportes);
    }
    
    @Override
    public void modificarInforeporte(List<Inforeportes> listaInforeportesModificar) {
        for (int i = 0; i < listaInforeportesModificar.size(); i++) {
            System.out.println("Modificando...");
            if (listaInforeportesModificar.get(i).getAficion() == null) {
                        listaInforeportesModificar.get(i).setAficion(null);
                    }
                    if (listaInforeportesModificar.get(i).getCiudad() == null) {
                        listaInforeportesModificar.get(i).setCiudad(null);
                    }
                    if (listaInforeportesModificar.get(i).getCodigo()== null) {
                        listaInforeportesModificar.get(i).setCodigo(null);
                    }
                    if (listaInforeportesModificar.get(i).getContador()== null) {
                        listaInforeportesModificar.get(i).setContador(null);
                    }
                    if (listaInforeportesModificar.get(i).getDeporte()== null) {
                        listaInforeportesModificar.get(i).setDeporte(null);
                    }
                    if (listaInforeportesModificar.get(i).getEmdesde()== null) {
                        listaInforeportesModificar.get(i).setEmdesde(null);
                    }
                    if (listaInforeportesModificar.get(i).getEmhasta()== null) {
                        listaInforeportesModificar.get(i).setEmhasta(null);
                    }
                    if (listaInforeportesModificar.get(i).getEnviomasivo()== null) {
                        listaInforeportesModificar.get(i).setEnviomasivo(null);
                    }
                    if (listaInforeportesModificar.get(i).getEstado()== null) {
                        listaInforeportesModificar.get(i).setEstado(null);
                    }
                    if (listaInforeportesModificar.get(i).getEstadocivil()== null) {
                        listaInforeportesModificar.get(i).setEstadocivil(null);
                    }
                    if (listaInforeportesModificar.get(i).getFecdesde()== null) {
                        listaInforeportesModificar.get(i).setFecdesde(null);
                    }
                    if (listaInforeportesModificar.get(i).getFechasta()== null) {
                        listaInforeportesModificar.get(i).setFechasta(null);
                    }
                    if (listaInforeportesModificar.get(i).getGrupo()== null) {
                        listaInforeportesModificar.get(i).setGrupo(null);
                    }
                    if (listaInforeportesModificar.get(i).getIdioma()== null) {
                        listaInforeportesModificar.get(i).setIdioma(null);
                    }
                    if (listaInforeportesModificar.get(i).getJefedivision()== null) {
                        listaInforeportesModificar.get(i).setJefedivision(null);
                    }
                    if (listaInforeportesModificar.get(i).getLocalizacion()== null) {
                        listaInforeportesModificar.get(i).setLocalizacion(null);
                    }
                    if (listaInforeportesModificar.get(i).getModulo().getSecuencia()== null) {
                        listaInforeportesModificar.get(i).setModulo(null);
                    }
                    if (listaInforeportesModificar.get(i).getNombre()== null) {
                        listaInforeportesModificar.get(i).setNombre(null);
                    }
                    if (listaInforeportesModificar.get(i).getNombrereporte()== null) {
                        listaInforeportesModificar.get(i).setNombrereporte(null);
                    }
                    if (listaInforeportesModificar.get(i).getRodamiento()== null) {
                        listaInforeportesModificar.get(i).setRodamiento(null);
                    }
                    if (listaInforeportesModificar.get(i).getSolicitud()== null) {
                        listaInforeportesModificar.get(i).setSolicitud(null);
                    }
                    if (listaInforeportesModificar.get(i).getTercero()== null) {
                        listaInforeportesModificar.get(i).setTercero(null);
                    }
                    if (listaInforeportesModificar.get(i).getTipo()== null) {
                        listaInforeportesModificar.get(i).setTipo(null);
                    }
                    if (listaInforeportesModificar.get(i).getTipotelefono()== null) {
                        listaInforeportesModificar.get(i).setTipotelefono(null);
                    }
                    if (listaInforeportesModificar.get(i).getTipotrabajador()== null) {
                        listaInforeportesModificar.get(i).setTipotrabajador(null);
                    }
                    if (listaInforeportesModificar.get(i).getTrabajador()== null) {
                        listaInforeportesModificar.get(i).setTrabajador(null);
                    }
            persistenciaInforeportes.editar(em, listaInforeportesModificar.get(i));
        }
    }
}
