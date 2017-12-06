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
    @Transient
    private boolean checkS;
    @Transient
    private boolean checkD;
    @Transient
    private boolean checkE;
    @Transient
    private boolean checkI;
    @Transient
    private boolean checkU;
    @Transient
    private String strS;
    @Transient
    private String strD;
    @Transient
    private String strE;
    @Transient
    private String strI;
    @Transient
    private String strU;

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
        if (objetofrm == null) {
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

    public boolean isCheckS() {
        getS();
        if (s == null || s.equalsIgnoreCase("N")) {
            checkS = false;
        } else {
            checkS = true;
        }
        return checkS;
    }

    public void setCheckS(boolean checkS) {
        if (checkS == false) {
            s = "N";
        } else {
            s = "S";
        }
        this.checkS = checkS;
    }

    public boolean isCheckD() {
        getD();
        if (d == null || d.equalsIgnoreCase("N")) {
            checkD = false;
        } else {
            checkD = true;
        }
        return checkD;
    }

    public void setCheckD(boolean checkD) {
        if (checkD == false) {
            d = "N";
        } else {
            d = "S";
        }
        this.checkD = checkD;
    }

    public boolean isCheckE() {
        getE();
        if (e == null || e.equalsIgnoreCase("N")) {
            checkE = false;
        } else {
            checkE = true;
        }
        return checkE;
    }

    public void setCheckE(boolean checkE) {
        if (checkE == false) {
            e = "N";
        } else {
            e = "S";
        }
        this.checkE = checkE;
    }

    public boolean isCheckI() {
        getI();
        if (i == null || i.equalsIgnoreCase("N")) {
            checkI = false;
        } else {
            checkI = true;
        }
        return checkI;
    }

    public void setCheckI(boolean checkI) {
        if (checkI == false) {
            i = "N";
        } else {
            i = "S";
        }
        this.checkI = checkI;
    }

    public boolean isCheckU() {
        getU();
        if (u == null || u.equalsIgnoreCase("N")) {
            checkU = false;
        } else {
            checkU = true;
        }
        return checkU;
    }

    public void setCheckU(boolean checkU) {
        if (checkU == false) {
            u = "N";
        } else {
            u = "S";
        }
        this.checkU = checkU;
    }

    public String getStrS() {
        getS();
        if (s == null || s.equalsIgnoreCase("N")) {
            strS = "NO";
        } else {
            strS = "SI";
        }
        return strS;
    }

    public void setStrS(String strS) {
        this.strS = strS;
    }

    public String getStrD() {
        getD();
        if (d == null || d.equalsIgnoreCase("N")) {
            strD = "NO";
        } else {
            strD = "SI";
        }
        return strD;
    }

    public void setStrD(String strD) {
        this.strD = strD;
    }

    public String getStrE() {
        getE();
        if (e == null || e.equalsIgnoreCase("N")) {
            strE = "NO";
        } else {
            strE = "SI";
        }
        return strE;
    }

    public void setStrE(String strE) {
        this.strE = strE;
    }

    public String getStrI() {
        getI();
        if (i == null || i.equalsIgnoreCase("N")) {
            strI = "NO";
        } else {
            strI = "SI";
        }
        return strI;
    }

    public void setStrI(String strI) {
        this.strI = strI;
    }

    public String getStrU() {
        getU();
        if (u == null || u.equalsIgnoreCase("N")) {
            strU = "NO";
        } else {
            strU = "SI";
        }
        return strU;
    }

    public void setStrU(String strU) {
        this.strU = strU;
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
