/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "RIESGOSPROFESIONALES")
public class RiesgosProfesionales implements Serializable {

      private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "FECHAVIGENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechavigencia;
    @NotNull
    @Column(name = "RIESGO")
    private BigDecimal riesgo;
    @JoinColumn(name = "TIPOCENTROCOSTO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    @NotNull
    private TiposCentrosCostos tipocentrocosto;
    @NotNull
    @Column(name = "COMENTARIO")
    private String comentario;

    public RiesgosProfesionales() {
    }

   
    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFechavigencia() {
        return fechavigencia;
    }

    public void setFechavigencia(Date fechavigencia) {
        this.fechavigencia = fechavigencia;
    }

    public BigDecimal getRiesgo() {
        return riesgo;
    }

    public void setRiesgo(BigDecimal riesgo) {
        this.riesgo = riesgo;
    }

    public TiposCentrosCostos getTipocentrocosto() {
        return tipocentrocosto;
    }

    public void setTipocentrocosto(TiposCentrosCostos tipocentrocosto) {
        this.tipocentrocosto = tipocentrocosto;
    }

    public String getComentario() {
        if(comentario == null){
            comentario = "";
        }
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RiesgosProfesionales)) {
            return false;
        }
        RiesgosProfesionales other = (RiesgosProfesionales) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.RiesgosProfesionales[ secuencia=" + secuencia + " ]";
    }
    
}
