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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "PARAMETROSCORRECCIONAUTOL")
public class ParametrosCorreccionesAutoL implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ANO")
    private short ano;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MES")
    private short mes;
    @JoinColumn(name = "TIPOTRABAJADOR", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposTrabajadores tipotrabajador;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;
    @Size(max =1)
    @Column (name="TIPOPLANILLA")
    private String tipoplanilla;
    @Size(max =30)
    @Column (name="CORRIGEPLANILLA")
    private String corrigeplanilla;
    @Column (name="FECHAPLANILLA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaplanilla;
    @Transient
    private String strMes;

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public short getAno() {
        return ano;
    }

    public void setAno(short ano) {
        this.ano = ano;
    }

    public short getMes() {
        return mes;
    }

    public void setMes(short mes) {
        this.mes = mes;
    }

    public TiposTrabajadores getTipotrabajador() {
        return tipotrabajador;
    }

    public void setTipotrabajador(TiposTrabajadores tipotrabajador) {
        this.tipotrabajador = tipotrabajador;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public String getTipoplanilla() {
        return tipoplanilla;
    }

    public void setTipoplanilla(String tipoplanilla) {
        this.tipoplanilla = tipoplanilla;
    }

    public String getCorrigeplanilla() {
        return corrigeplanilla;
    }

    public void setCorrigeplanilla(String corrigeplanilla) {
        this.corrigeplanilla = corrigeplanilla;
    }

    public Date getFechaplanilla() {
        return fechaplanilla;
    }

    public void setFechaplanilla(Date fechaplanilla) {
        this.fechaplanilla = fechaplanilla;
    }

    public String getStrMes() {
        getMes();
        if (mes == 1) {
            strMes = "ENERO";
        }
        if (mes == 2) {
            strMes = "FEBRERO";
        }
        if (mes == 3) {
            strMes = "MARZO";
        }
        if (mes == 4) {
            strMes = "ABRIL";
        }
        if (mes == 5) {
            strMes = "MAYO";
        }
        if (mes == 6) {
            strMes = "JUNIO";
        }
        if (mes == 7) {
            strMes = "JULIO";
        }
        if (mes == 8) {
            strMes = "AGOSTO";
        }
        if (mes == 9) {
            strMes = "SEPTIEMBRE";
        }
        if (mes == 10) {
            strMes = "OCTUBRE";
        }
        if (mes == 11) {
            strMes = "NOVIEMBRE";
        }
        if (mes == 12) {
            strMes = "DICIEMBRE";
        }

        return strMes;
    }

    public void setStrMes(String strMes) {
        this.strMes = strMes;
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
        if (!(object instanceof ParametrosCorreccionesAutoL)) {
            return false;
        }
        ParametrosCorreccionesAutoL other = (ParametrosCorreccionesAutoL) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ParametrosCorreccionesAutoL[ id=" + secuencia + " ]";
    }
    
}
