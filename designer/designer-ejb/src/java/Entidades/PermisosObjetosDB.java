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
@Table(name = "PERMISOSOBJETOSDB")
public class PermisosObjetosDB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    @Id
    private BigInteger secuencia;
    @JoinColumn(name = "PERFIL", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Perfiles perfil;
    @JoinColumn(name = "OBJETODB", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private ObjetosDB objetodb;
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
    boolean bs;
    @Transient
    boolean bi;
    @Transient
    boolean bd;
    @Transient
    boolean bu;
    @Transient
    boolean be;

    public PermisosObjetosDB() {
    }

    public PermisosObjetosDB(BigInteger secuencia, Perfiles perfil, ObjetosDB objetodb, String s, String i, String d, String u, String e) {
        this.secuencia = secuencia;
        this.perfil = perfil;
        this.objetodb = objetodb;
        this.s = s;
        this.i = i;
        this.d = d;
        this.u = u;
        this.e = e;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
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

    public ObjetosDB getObjetodb() {
        if(objetodb == null){
            objetodb = new ObjetosDB();
        }
        return objetodb;
    }

    public void setObjetodb(ObjetosDB objetodb) {
        this.objetodb = objetodb;
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

    public boolean isBs() {
        if ("S".equals(s)) {
            bs = true;
        } else {
            bs = false;
        }
        return bs;
    }

    public void setBs(boolean bs) {
        this.bs = bs;
        if (bs) {
            this.s = "S";
        } else {
            this.s = "N";
        }
    }

    public boolean isBi() {
        if ("S".equals(i)) {
            bi = true;
        } else {
            bi = false;
        }
        return bi;
    }

    public void setBi(boolean bi) {
        this.bi = bi;
        if (bi) {
            this.i = "S";
        } else {
            this.i = "N";
        }
    }

    public boolean isBd() {
        if ("S".equals(d)) {
            bd = true;
        } else {
            bd = false;
        }
        return bd;
    }

    public void setBd(boolean bd) {
        this.bd = bd;
        if (bd) {
            this.d = "S";
        } else {
            this.d = "N";
        }
    }

    public boolean isBu() {
        if ("S".equals(u)) {
            bu = true;
        } else {
            bu = false;
        }
        return bu;
    }

    public void setBu(boolean bu) {
        this.bu = bu;
        if (bu) {
            this.u = "S";
        } else {
            this.u = "N";
        }
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
        if (!(object instanceof PermisosObjetosDB)) {
            return false;
        }
        PermisosObjetosDB other = (PermisosObjetosDB) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.PermisosObjetosDB[ secuencia=" + secuencia + " ]";
    }

}
