/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Diagnosticoscapitulos;
import Entidades.Diagnosticoscategorias;
import Entidades.Diagnosticossecciones;
import InterfaceAdministrar.AdministrarDiagnosticosCategoriasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaDiagnosticosCategoriasInterface;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarDiagnosticosCategorias implements AdministrarDiagnosticosCategoriasInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaDiagnosticosCategoriasInterface persistenciaDiagnosticos;
    private EntityManager em;
    
    @Override
    public void obtenerConexion(String idSesion) {
        try{
        em = administrarSesiones.obtenerConexionSesion(idSesion);
        } catch(Exception e){
            System.out.println("error AdministrarDiagnosticosCategorias.obtenerConexion : " + e.toString());
        }
    }

    @Override
    public void crearDiagnosticoCategoria(List<Diagnosticoscategorias> categorias) {
        try {
            for (int i = 0; i < categorias.size(); i++) {
                persistenciaDiagnosticos.crear(em,categorias.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error AdministrarDiagnosticosCategorias.crearDiagnosticoCategoria :" + e.toString());
        }
    }

    @Override
    public void editarDiagnosticoCategoria(List<Diagnosticoscategorias> categorias) {
        try {
            for (int i = 0; i < categorias.size(); i++) {
                persistenciaDiagnosticos.editar(em,categorias.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error AdministrarDiagnosticosCategorias.editarDiagnosticoCategoria :" + e.toString());
        }
    }

    @Override
    public void borrarDiagnosticoCategoria(List<Diagnosticoscategorias> categorias) {
        try {
            for (int i = 0; i < categorias.size(); i++) {
                persistenciaDiagnosticos.borrar(em,categorias.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error AdministrarDiagnosticosCategorias.borrarDiagnosticoCategoria :" + e.toString());
        }
    }

    
    @Override
    public List<Diagnosticoscategorias> consultarDiagnosticoCategoria(BigDecimal secSeccion) {
        List<Diagnosticoscategorias> lista = persistenciaDiagnosticos.buscarCategorias(em,secSeccion);
        return lista;
    }

    @Override
    public List<Diagnosticoscapitulos> consultarDiagnosticoCapitulo() {
        List<Diagnosticoscapitulos> listacapitulos = persistenciaDiagnosticos.buscarCapitulo(em);
        return listacapitulos;
    }

    @Override
    public void crearDiagnosticoCapitulo(List<Diagnosticoscapitulos> capitulo) {
        try {
            for (int i = 0; i < capitulo.size(); i++) {
                persistenciaDiagnosticos.crearCapitulo(em,capitulo.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error AdministrarDiagnosticosCategorias.crearDiagnosticoCapitulo :" + e.toString());
        }
    }

    @Override
    public void editarDiagnosticoCapitulo(List<Diagnosticoscapitulos> capitulo) {
        try {
            for (int i = 0; i < capitulo.size(); i++) {
                persistenciaDiagnosticos.editarCapitulo(em,capitulo.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error AdministrarDiagnosticosCategorias.editarDiagnosticoCapitulo :" + e.toString());
        }
    }

    @Override
    public void borrarDiagnosticoCapitulo(List<Diagnosticoscapitulos> capitulo) {
        try {
            for (int i = 0; i < capitulo.size(); i++) {
                persistenciaDiagnosticos.borrarCapitulo(em,capitulo.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error AdministrarDiagnosticosCategorias.borrarDiagnosticoCapitulo :" + e.toString());
        }
    }

    @Override
    public List<Diagnosticossecciones> consultarDiagnosticoSeccion(BigDecimal secCapitulo) {
        List<Diagnosticossecciones> listasecciones = persistenciaDiagnosticos.buscarSeccion(em,secCapitulo);
        return listasecciones;
    }

    @Override
    public void crearDiagnosticoSeccion(List<Diagnosticossecciones> seccion) {
        try {
            for (int i = 0; i < seccion.size(); i++) {
                persistenciaDiagnosticos.crearSeccion(em,seccion.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error AdministrarDiagnosticosCategorias.crearDiagnosticoSección :" + e.toString());
        }
    }

    @Override
    public void editarDiagnosticoSeccion(List<Diagnosticossecciones> seccion) {
        try {
            for (int i = 0; i < seccion.size(); i++) {
                persistenciaDiagnosticos.editarSeccion(em,seccion.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error AdministrarDiagnosticosCategorias.editarDiagnosticoSección :" + e.toString());
        }
    }

    @Override
    public void borrarDiagnosticoSeccion(List<Diagnosticossecciones> seccion) {
        try {
            for (int i = 0; i < seccion.size(); i++) {
                persistenciaDiagnosticos.borrarSeccion(em,seccion.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error AdministrarDiagnosticosCategorias.borrarDiagnosticoSección :" + e.toString());
        }
    }

}
