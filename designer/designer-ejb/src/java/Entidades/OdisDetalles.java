/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
public class OdisDetalles implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @NotNull
    @JoinColumn(name = "ODICABECERA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private OdisCabeceras odicabecera;
    @NotNull
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empleados empleado;
    @JoinColumn(name = "RELACIONINCAPACIDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private RelacionesIncapacidades relacionincapacidad;
    @Column(name = "VALOR")
    private BigInteger valor;
    @Size(min = 1, max = 20)
    @Column(name = "NUMERODETALLE")
    private String numerodetalle;
    @Size(min = 1, max = 200)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Column(name = "PARAMETROPRESUPUESTO")
    private BigInteger parametropresupuesto;

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public OdisCabeceras getOdicabecera() {
        return odicabecera;
    }

    public void setOdicabecera(OdisCabeceras odicabecera) {
        this.odicabecera = odicabecera;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public RelacionesIncapacidades getRelacionincapacidad() {
        return relacionincapacidad;
    }

    public void setRelacionincapacidad(RelacionesIncapacidades relacionincapacidad) {
        this.relacionincapacidad = relacionincapacidad;
    }

    public BigInteger getValor() {
        return valor;
    }

    public void setValor(BigInteger valor) {
        this.valor = valor;
    }

    public String getNumerodetalle() {
        return numerodetalle;
    }

    public void setNumerodetalle(String numerodetalle) {
        this.numerodetalle = numerodetalle;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigInteger getParametropresupuesto() {
        return parametropresupuesto;
    }

    public void setParametropresupuesto(BigInteger parametropresupuesto) {
        this.parametropresupuesto = parametropresupuesto;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OdisDetalles)) {
            return false;
        }
        OdisDetalles other = (OdisDetalles) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.OdisDetalles[ secuencia=" + secuencia + " ]";
    }
    
}
