/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Deportes;
import Entidades.Empleados;
import Entidades.VigenciasDeportes;
import InterfaceAdministrar.AdministrarVigenciaDeporteInterface;
import InterfacePersistencia.PersistenciaDeportesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaVigenciasDeportesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarVigenciaDeporte implements AdministrarVigenciaDeporteInterface{

    @EJB
    PersistenciaVigenciasDeportesInterface persistenciaVigenciasDeportes;
    @EJB
    PersistenciaDeportesInterface persistenciaDeportes;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
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
    public List<VigenciasDeportes> listVigenciasDeportesPersona(BigInteger secuenciaP) {
        try {
            List<VigenciasDeportes> retorno = persistenciaVigenciasDeportes.deportesTotalesSecuenciaPersona(em, secuenciaP);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error listVigenciasDeportesPersona Admi : "+e.toString());
            return null;
        }
    }
    
    @Override
    public void crearVigenciasDeportes(List<VigenciasDeportes> listaVD){
        try{
            for(int i = 0;i<listaVD.size();i++){
                if(listaVD.get(i).getDeporte().getSecuencia() == null){
                    listaVD.get(i).setDeporte(null);
                }
                persistenciaVigenciasDeportes.crear(em, listaVD.get(i));
            }
        }catch(Exception e){
            System.out.println("Error crearVigenciasDeportes Admi : "+e.toString());
        }
    }
    
    @Override
    public void editarVigenciasDeportes(List<VigenciasDeportes> listaVD){
        try{
            for(int i = 0;i<listaVD.size();i++){
                if(listaVD.get(i).getDeporte().getSecuencia() == null){
                    listaVD.get(i).setDeporte(null);
                }
                persistenciaVigenciasDeportes.editar(em, listaVD.get(i));
            }
        }catch(Exception e){
            System.out.println("Error editarVigenciasDeportes Admi : "+e.toString());
        }
    }
    
    @Override
    public void borrarVigenciasDeportes(List<VigenciasDeportes> listaVD){
        try{
            for(int i = 0;i<listaVD.size();i++){
                if(listaVD.get(i).getDeporte().getSecuencia() == null){
                    listaVD.get(i).setDeporte(null);
                }
                persistenciaVigenciasDeportes.borrar(em, listaVD.get(i));
            }
        }catch(Exception e){
            System.out.println("Error borrarVigenciasDeportes Admi : "+e.toString());
        }
    }
    
    @Override
    public List<Deportes> listDeportes(){
        try{
        List<Deportes> retorno = persistenciaDeportes.buscarDeportes(em);
        return retorno;
        }catch(Exception  e){
            System.out.println("Error listDeportes Admi : "+e.toString());
            return null;
        }
    }
    
    @Override
    public Empleados empleadoActual(BigInteger secuenciaP){
        try{
        Empleados retorno = persistenciaEmpleado.buscarEmpleado(em, secuenciaP);
        return retorno;
        }catch(Exception  e){
            System.out.println("Error empleadoActual Admi : "+e.toString());
            return null;
        }
    }
}
