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
@Table(name = "PERMISOSPANTALLAS")
public class PermisosPantallas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    @Id
    private BigInteger secuencia;
    @JoinColumn(name = "PERFIL", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Perfiles perfil;
    @Size(max = 30)
    @Column(name = "TIPO")
    private String tipo;
    @JoinColumn(name = "OBJETOFRM", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private ObjetosBloques objetofrm;
    @Size(max = 1)
    @Column(name = "S")
    private String s;
    @Size(max = 1)
    @Column(name = "I")
    private String i;
    @Size(max = 1)
    @Column(name = "D")
    private String d;
    @Size(max = 1)
    @Column(name = "U")
    private String u;
    @Size(max = 1)
    @Column(name = "E")
    private String e;
    @Transient
    boolean be;

    public PermisosPantallas() {
    }

    public PermisosPantallas(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Perfiles getPerfil() {
        if (perfil == null) {
            perfil = new Perfiles();
        }
        return perfil;
    }

    public void setPerfil(Perfiles perfil) {
        this.perfil = perfil;
    }

    public String getTipo() {
        if (tipo == null) {
            tipo = "";
        }
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ObjetosBloques getObjetofrm() {
        if(objetofrm == null){
            objetofrm = new ObjetosBloques();
        }
        return objetofrm;
    }

    public void setObjetofrm(ObjetosBloques objetofrm) {
        this.objetofrm = objetofrm;
    }

    public String getS() {
        if (s == null) {
            s = "N";
        }
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getI() {
        if (i == null) {
            i = "N";
        }
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getD() {
        if (d == null) {
            d = "N";
        }
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getU() {
        if (u == null) {
            u = "N";
        }
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    public String getE() {
        if (e == null) {
            e = "N";
        }
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public boolean isBe() {
        if ("S".equals(e)) {
            be = true;
        } else {
            be = false;
        }
        return be;
    }

    public void setBe(boolean be) {
        this.be = be;
        if (be) {
            this.e = "S";
        } else {
            this.e = "N";
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
        if (!(object instanceof PermisosPantallas)) {
            return false;
        }
        PermisosPantallas other = (PermisosPantallas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.PermisosPantallas[ secuencia=" + secuencia + " ]";
    }

}
