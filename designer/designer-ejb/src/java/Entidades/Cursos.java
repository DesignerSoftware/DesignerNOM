package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "CURSOS")
public class Cursos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private Short codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 400)
    @Column(name = "OBJETIVO")
    private String objetivo;
    @JoinColumn(name = "TIPOCURSO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private TiposCursos tipocurso;

    public Cursos() {
    }

    public Cursos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Cursos(BigInteger secuencia, String nombre, String objetivo) {
        this.secuencia = secuencia;
        this.nombre = nombre;
        this.objetivo = objetivo;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Short getCodigo() {
        return codigo;
    }

    public void setCodigo(Short codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        if (nombre == null) {
            nombre = " ";
            return nombre;
        } else {
            return nombre.toUpperCase();
        }
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public TiposCursos getTipocurso() {
        return tipocurso;
    }

    public void setTipocurso(TiposCursos tipocurso) {
        this.tipocurso = tipocurso;
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
        if (!(object instanceof Cursos)) {
            return false;
        }
        Cursos other = (Cursos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Cursos[ secuencia=" + secuencia + " ]";
    }
    
}
