/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kevin
 */
@Entity
@Table(name = "alumnoapoderado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alumnoapoderado.findAll", query = "SELECT a FROM Alumnoapoderado a")
    , @NamedQuery(name = "Alumnoapoderado.findById", query = "SELECT a FROM Alumnoapoderado a WHERE a.id = :id")})
public class Alumnoapoderado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "idalumno", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Alumno idalumno;
    @JoinColumn(name = "idapoderado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Apoderado idapoderado;

    public Alumnoapoderado() {
    }

    public Alumnoapoderado(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Alumno getIdalumno() {
        return idalumno;
    }

    public void setIdalumno(Alumno idalumno) {
        this.idalumno = idalumno;
    }

    public Apoderado getIdapoderado() {
        return idapoderado;
    }

    public void setIdapoderado(Apoderado idapoderado) {
        this.idapoderado = idapoderado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alumnoapoderado)) {
            return false;
        }
        Alumnoapoderado other = (Alumnoapoderado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Alumnoapoderado[ id=" + id + " ]";
    }
    
}
