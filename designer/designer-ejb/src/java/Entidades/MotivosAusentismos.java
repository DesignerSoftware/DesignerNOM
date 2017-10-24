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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "MOTIVOSAUSENTISMOS")
public class MotivosAusentismos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO")
    private Integer codigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "REMUNERADO")
    private String remunerado;
    @Column(name = "CLASEAUSENCIA")
    private String claseausencia;
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "DESCUENTATIEMPOCONTINUO")
    private String descuentatiempocontinuo;
    @Transient
    private boolean checkRemunerado;

    public MotivosAusentismos() {
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRemunerado() {
        return remunerado;
    }

    public void setRemunerado(String remunerado) {
        this.remunerado = remunerado;
    }

    public String getClaseausencia() {
        return claseausencia;
    }

    public void setClaseausencia(String claseausencia) {
        this.claseausencia = claseausencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescuentatiempocontinuo() {
        return descuentatiempocontinuo;
    }

    public void setDescuentatiempocontinuo(String descuentatiempocontinuo) {
        this.descuentatiempocontinuo = descuentatiempocontinuo;
    }

    public boolean isCheckRemunerado() {
        if ("S".equals(remunerado)) {
            checkRemunerado = true;
        } else {
            checkRemunerado = false;
        }
        return checkRemunerado;
    }

    public void setCheckRemunerado(boolean checkRemunerado) {
        this.checkRemunerado = checkRemunerado;
        if (checkRemunerado == true) {
            remunerado = "S";
        } else {
            remunerado = "N";
        }
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
        if (!(object instanceof MotivosAusentismos)) {
            return false;
        }
        MotivosAusentismos other = (MotivosAusentismos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.MotivosAusentismos[ secuencia=" + secuencia + " ]";
    }

}
