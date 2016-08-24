/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlDestajo implements Serializable {

    private String piePagina;
    private String titulo;
    private String tituloSeleccion;

    /**
     * Creates a new instance of ControlDestajo
     */
    public ControlDestajo() {
        piePagina = "Sistema Integral de Talento Humano y Nómina Designer .RHN - Todos los derechos Reservados. WWW.NOMINA.COM.CO";
        titulo = " Bienvenido al Sistema Designer DESTAJO, el cual fue diseñado teniendo como principal objetivo brindar una herramienta sencilla para registrar unidades de producción para el empleado(Destajo).";
        tituloSeleccion = "Por favor seleccione la opción que desea trabajar";
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPiePagina() {
        return piePagina;
    }

    public void setPiePagina(String piePagina) {
        this.piePagina = piePagina;
    }

    public String getTituloSeleccion() {
        return tituloSeleccion;
    }

    public void setTituloSeleccion(String tituloSeleccion) {
        this.tituloSeleccion = tituloSeleccion;
    }

}
