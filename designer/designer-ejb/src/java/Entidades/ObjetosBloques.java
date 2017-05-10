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
public class ObjetosBloques implements Serializable {

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
    @JoinColumn(name = "BLOQUE", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private BloquesPantallas bloque;
    @Size(max = 50)
    @Column(name = "COMENTARIO")
    private String comentario;

    public ObjetosBloques() {
    }

    public ObjetosBloques(BigInteger secuencia) {
        this.secuencia = secuencia;
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

    public BloquesPantallas getBloque() {
        if(bloque == null){
            bloque = new BloquesPantallas();
        }
        return bloque;
    }

    public void setBloque(BloquesPantallas bloque) {
        this.bloque = bloque;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ObjetosBloques)) {
            return false;
        }
        ObjetosBloques other = (ObjetosBloques) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ObjetosBloques[ secuencia=" + secuencia + " ]";
    }

}
