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
public class BloquesPantallas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    @Id
    private BigInteger secuencia;
    @Size(max = 50)
    @Column(name = "NOMBRE")
    @Basic(optional = false)
    @NotNull
    private String nombre;
    @JoinColumn(name = "PANTALLA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private PantallasSeguras pantalla;

    public BloquesPantallas() {
    }

    public BloquesPantallas(BigInteger secuencia, String nombre, PantallasSeguras pantalla) {
        this.secuencia = secuencia;
        this.nombre = nombre;
        this.pantalla = pantalla;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getNombre() {
        if (nombre == null) {
            nombre = "";
        }
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public PantallasSeguras getPantalla() {
        return pantalla;
    }

    public void setPantalla(PantallasSeguras pantalla) {
        this.pantalla = pantalla;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia
                != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BloquesPantallas)) {
            return false;
        }
        BloquesPantallas other = (BloquesPantallas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.BloquesPantallas[ secuencia=" + secuencia + " ]";
    }

}
