/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "HISTORICOSUSUARIOS")
public class HistoricosUsuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "FECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicial;
    @Column(name = "FECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinal;
    @NotNull
    @JoinColumn(name = "USUARIO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Usuarios usuario;
    @NotNull
    @JoinColumn(name = "PERFIL", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Perfiles perfil;
    @NotNull
    @JoinColumn(name = "PERSONA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Personas persona;
    @Column(name = "DESCRIPCION")
    @Size(max = 50)
    private String descripcion;
    @Transient
    private String estadoDescripcion;

    public HistoricosUsuarios() {
    }

    public HistoricosUsuarios(BigInteger secuencia, Date fechainicial, Date fechafinal, Usuarios usuario, Perfiles perfil, Personas persona, String descripcion) {
        this.secuencia = secuencia;
        this.fechainicial = fechainicial;
        this.fechafinal = fechafinal;
        this.usuario = usuario;
        this.perfil = perfil;
        this.persona = persona;
        this.descripcion = descripcion;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFechainicial() {
        return fechainicial;
    }

    public void setFechainicial(Date fechainicial) {
        this.fechainicial = fechainicial;
    }

    public Date getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(Date fechafinal) {
        this.fechafinal = fechafinal;
    }

    public Usuarios getUsuario() {
        if(usuario == null){
            usuario = new Usuarios();
        }
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Perfiles getPerfil() {
        if(perfil == null){
            perfil = new Perfiles();
        }
        return perfil;
    }

    public void setPerfil(Perfiles perfil) {
        this.perfil = perfil;
    }

    public Personas getPersona() {
        if(persona ==  null){
            persona = new Personas();
        }
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public String getDescripcion() {
        if (descripcion == null) {
            descripcion = "REGISTRO MANUAL";
        }
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstadoDescripcion() {
        if(estadoDescripcion == null){
          if(descripcion == null)  {
            estadoDescripcion = "REGISTRO MANUAL";
          }else{
            if (descripcion.equals("REGISTRO INSERTADO")) {
                estadoDescripcion = "REGISTRO INSERTADO";
            } else if (descripcion.equals("REGISTRO ACTUALIZADO")) {
                estadoDescripcion = "REGISTRO ACTUALIZADO";
            } else if (descripcion.equals("REGISTRO MANUAL")) {
                estadoDescripcion = "REGISTRO MANUAL";
            } else if (descripcion.equals("REGISTRO MODIFICADO")) {
                estadoDescripcion = "REGISTRO MODIFICADO";
            }
          }
        }
        
        return estadoDescripcion;
    }

    public void setEstadoDescripcion(String estadoDescripcion) {
        this.estadoDescripcion = estadoDescripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HistoricosUsuarios)) {
            return false;
        }
        HistoricosUsuarios other = (HistoricosUsuarios) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.HistoricosUsuarios[ secuencia=" + secuencia + " ]";
    }

}
