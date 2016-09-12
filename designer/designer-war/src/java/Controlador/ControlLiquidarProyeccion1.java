/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import utilidadesUI.PrimefacesContextUI;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlLiquidarProyeccion1 implements Serializable {

    private int numero = 1;
    private boolean b1;
    private String texto;
    private String titulo;
    private String tituloBoton;
    private String color;

    /**
     * Creates a new instance of ControlLiquidarProyeccion1
     */
    public ControlLiquidarProyeccion1() {
        numero = 1;
        texto = "Realice la liquidación de Aumentos y Retroactivos Salariales a partir de un proceso de parámetros que contienen un porcentaje o valor arbitratio de aumento, aumentos por análisis salarial, rango de valores y/o porcentajes, uniformes o por cargos. Ademas tiene la posibilidad de referencias a los conceptos de retroactivos. luego de Ejecución de la liquidación usted puede deshacer el proceso cuantas veces sea necesario hasta que se acomode a su presupuesto teniendo en cuenta que en parámetros aumentos debe esta en modo No Final. Cuando desee hacer efectivo el Aumento o el retroactivo indique en Parámetros de Aumentos Modo final";
        titulo = "";
        tituloBoton = "";
        color = "";
    }

    public void mostrar(int num) {
        this.numero = num;
        System.out.println("numero seleccionado =" + numero);
        RequestContext context = RequestContext.getCurrentInstance();
        if (numero == 1) {
            titulo = "Aumentos Salariales";
            tituloBoton = "Listar Retroactivos";
            b1 = false;
            color = "darkblue";
            texto = "Realice la liquidación de Aumentos y Retroactivos Salariales a partir de un proceso de parámetros que contienen un porcentaje o valor arbitratio de aumento, aumentos por análisis salarial, rango de valores y/o porcentajes, uniformes o por cargos. Ademas tiene la posibilidad de referencias a los conceptos de retroactivos. luego de Ejecución de la liquidación usted puede deshacer el proceso cuantas veces sea necesario hasta que se acomode a su presupuesto teniendo en cuenta que en parámetros aumentos debe esta en modo No Final. Cuando desee hacer efectivo el Aumento o el retroactivo indique en Parámetros de Aumentos Modo final";

        } else if (numero == 2) {
            tituloBoton = "Listar Proyecciones";
            titulo = "proyecciones";
            color = "blue";
            b1 = true;
            texto = "Realice la liquidación de proyecciones Salariales a partir de los parámetros de liquidacion como fecha, centro de costo empleados, conceptos etc. Primero ingrese los parametros a liquidar, luego detalle de los conceptos, despúes genere la liquidaciñon y revice los detalles del calculo de proyecciones";

        }
        PrimefacesContextUI.actualizar("form:b1");
        PrimefacesContextUI.actualizar("form:b6");
        PrimefacesContextUI.actualizar("form:textomostrado");
        PrimefacesContextUI.actualizar("form:titulo");
        PrimefacesContextUI.actualizar("form:t1");
        PrimefacesContextUI.actualizar("form:t2");
        PrimefacesContextUI.actualizar("form:t3");
        PrimefacesContextUI.actualizar("form:t4");
        PrimefacesContextUI.actualizar("form:t5");
        PrimefacesContextUI.actualizar("form:t6");
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
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
