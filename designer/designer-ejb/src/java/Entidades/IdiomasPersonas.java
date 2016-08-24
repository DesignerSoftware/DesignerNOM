package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "IDIOMASPERSONAS")
public class IdiomasPersonas implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "HABLA")
    private BigInteger habla;
    @Column(name = "LECTURA")
    private BigInteger lectura;
    @Column(name = "ESCRITURA")
    private BigInteger escritura;
    @JoinColumn(name = "PERSONA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Personas persona;
    @JoinColumn(name = "IDIOMA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Idiomas idioma;

    public IdiomasPersonas() {
    }

    public IdiomasPersonas(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getHabla() {
        return habla;
    }

    public void setHabla(BigInteger habla) {
        this.habla = habla;
    }

    public BigInteger getLectura() {
        return lectura;
    }

    public void setLectura(BigInteger lectura) {
        this.lectura = lectura;
    }

    public BigInteger getEscritura() {
        return escritura;
    }

    public void setEscritura(BigInteger escritura) {
        this.escritura = escritura;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public Idiomas getIdioma() {
        return idioma;
    }

    public void setIdioma(Idiomas idioma) {
        this.idioma = idioma;
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
        if (!(object instanceof IdiomasPersonas)) {
            return false;
        }
        IdiomasPersonas other = (IdiomasPersonas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.IdiomasPersonas[ secuencia=" + secuencia + " ]";
    }
    
}
