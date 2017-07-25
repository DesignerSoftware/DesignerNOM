/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlLiquidarProyeccion1 implements Serializable {

   private static Logger log = Logger.getLogger(ControlLiquidarProyeccion1.class);

    private String tipo;
    private boolean b1;
    private String texto;
    private String titulo;
    private String tituloBoton;
    private String color;

    /**
     * Creates a new instance of ControlLiquidarProyeccion1
     */
    public ControlLiquidarProyeccion1() {
        tipo = "AUMENTOS";
        titulo = "AUMENTOS SALARIALES";
        tituloBoton = "Listar Retroactivos";
        b1 = false;
        color = "#14388B";
        texto = "Realice la liquidación de Aumentos y Retroactivos Salariales a partir de un proceso de parámetros que contienen un porcentaje o valor arbitratio de aumento, aumentos por análisis salarial, rango de valores y/o porcentajes, uniformes o por cargos. Ademas tiene la posibilidad de referencias a los conceptos de retroactivos. luego de Ejecución de la liquidación usted puede deshacer el proceso cuantas veces sea necesario hasta que se acomode a su presupuesto teniendo en cuenta que en parámetros aumentos debe esta en modo No Final. Cuando desee hacer efectivo el Aumento o el retroactivo indique en Parámetros de Aumentos Modo final";
    }

    public void limpiarListasValor() {

    }

//    public void mostrar(int num) {
//        this.numero = num;
//        log.info("numero seleccionado =" + numero);
//        if (numero == 1) {
//            titulo = "Aumentos Salariales";
//            tituloBoton = "Listar Retroactivos";
//            b1 = false;
//            color = "black";
//
//            RequestContext.getCurrentInstance().update("form:t1");
//            RequestContext.getCurrentInstance().update("form:t2");
//            RequestContext.getCurrentInstance().update("form:t3");
//            RequestContext.getCurrentInstance().update("form:t4");
//            RequestContext.getCurrentInstance().update("form:t5");
//            RequestContext.getCurrentInstance().update("form:t6");
//
//        } else if (numero == 2) {
//            tituloBoton = "Listar Proyecciones";
//            titulo = "proyecciones";
//            color = "#14388B";
//            b1 = true;
//
//            RequestContext.getCurrentInstance().update("form:t1");
//            RequestContext.getCurrentInstance().update("form:t2");
//            RequestContext.getCurrentInstance().update("form:t3");
//            RequestContext.getCurrentInstance().update("form:t4");
//            RequestContext.getCurrentInstance().update("form:t5");
//            RequestContext.getCurrentInstance().update("form:t6");
//        }
//        
//    }
    public void cambiarTexto() {
        if (tipo.equalsIgnoreCase("AUMENTOS")) {
            titulo = "AUMENTOS SALARIALES";
            tituloBoton = "Listar Retroactivos";
            b1 = false;
            color = "#14388B";
            texto = "Realice la liquidación de Aumentos y Retroactivos Salariales a partir de un proceso de parámetros que contienen un porcentaje o valor arbitratio de aumento, aumentos por análisis salarial, rango de valores y/o porcentajes, uniformes o por cargos. Ademas tiene la posibilidad de referencias a los conceptos de retroactivos. luego de Ejecución de la liquidación usted puede deshacer el proceso cuantas veces sea necesario hasta que se acomode a su presupuesto teniendo en cuenta que en parámetros aumentos debe esta en modo No Final. Cuando desee hacer efectivo el Aumento o el retroactivo indique en Parámetros de Aumentos Modo final";
            RequestContext.getCurrentInstance().update("form:titulo");
            RequestContext.getCurrentInstance().update("form:textomostrado");
        } else if (tipo.equalsIgnoreCase("PROYECCIONES")) {
            titulo = "PROYECCIONES";
            tituloBoton = "Listar Proyecciones";
            color = "#14388B";
            b1 = true;
            texto = "Realice la liquidación de proyecciones Salariales a partir de los parámetros de liquidacion como fecha, centro de costo empleados, conceptos etc. Primero ingrese los parametros a liquidar, luego detalle de los conceptos, despúes genere la liquidaciñon y revice los detalles del calculo de proyecciones";
            RequestContext.getCurrentInstance().update("form:titulo");
            RequestContext.getCurrentInstance().update("form:textomostrado");
        }

        RequestContext.getCurrentInstance().update("form:b1");
        RequestContext.getCurrentInstance().update("form:b6");
        RequestContext.getCurrentInstance().update("form:textomostrado");
        RequestContext.getCurrentInstance().update("form:titulo");
        RequestContext.getCurrentInstance().update("form:t1");
        RequestContext.getCurrentInstance().update("form:t2");
        RequestContext.getCurrentInstance().update("form:t3");
        RequestContext.getCurrentInstance().update("form:t4");
        RequestContext.getCurrentInstance().update("form:t5");
        RequestContext.getCurrentInstance().update("form:t6");
    }

    /////////////SETS Y GETS////////
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isB1() {
        return b1;
    }

    public void setB1(boolean b1) {
        this.b1 = b1;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTitulo() {
        return titulo.toUpperCase();
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTituloBoton() {
        return tituloBoton;
    }

    public void setTituloBoton(String tituloBoton) {
        this.tituloBoton = tituloBoton;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
