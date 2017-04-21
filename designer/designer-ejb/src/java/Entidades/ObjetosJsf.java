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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "OBJETOSJSF")
public class ObjetosJsf implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    @Id
    private BigInteger secuencia;
    @Size(max = 50)
    @Column(name = "IDENTIFICADOR")
    @Basic(optional = false)
    @NotNull
    private String identificador;
    @JoinColumn(name = "OBJETOFRM", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private ObjetosBloques objetofrm;
    @Size(max = 50)
    @Column(name = "COMENTARIO")
    @Basic(optional = false)
    private String comentario;
    @Transient
    private String enable;

    public ObjetosJsf() {
    }

    public ObjetosJsf(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getIdentificador() {
        if (identificador == null) {
            identificador = "";
        }
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public ObjetosBloques getObjetofrm() {
        return objetofrm;
    }

    public void setObjetofrm(ObjetosBloques objetofrm) {
        this.objetofrm = objetofrm;
    }

    public String getComentario() {
        if (comentario == null) {
            comentario = "";
        }
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the secuencia fields are not set
        if (!(object instanceof ObjetosJsf)) {
            return false;
        }
        ObjetosJsf other = (ObjetosJsf) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ObjetosJsf[ secuencia=" + secuencia + " ]";
    }

}
