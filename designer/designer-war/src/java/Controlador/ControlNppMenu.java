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
public class ControlNppMenu implements Serializable {

    private String mensaje;

    /**
     * Creates a new instance of ControlNppMenu
     */
    public ControlNppMenu() {

        mensaje = "El proceso indentifica los diferentes pagos generados por nomina por pagar acorde a los parametros propios de este proceso. Primero adicione un registro en el bloque de giros y despues de pagar, posteriormente haga click en ejectutar";
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
