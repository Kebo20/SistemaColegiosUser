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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "agendaclase")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Agendaclase.findAll", query = "SELECT a FROM Agendaclase a")
    , @NamedQuery(name = "Agendaclase.findById", query = "SELECT a FROM Agendaclase a WHERE a.id = :id")
    , @NamedQuery(name = "Agendaclase.findByFecha", query = "SELECT a FROM Agendaclase a WHERE a.fecha = :fecha")
    , @NamedQuery(name = "Agendaclase.findByDescripcion", query = "SELECT a FROM Agendaclase a WHERE a.descripcion = :descripcion")})
public class Agendaclase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "fecha")
    private String fecha;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "idclase", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Clase idclase;

    public Agendaclase() {
    }

    public Agendaclase(Integer id) {
        this.id = id;
    }

    public Agendaclase(Integer id, String fecha, String descripcion) {
        this.id = id;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Clase getIdclase() {
        return idclase;
    }

    public void setIdclase(Clase idclase) {
        this.idclase = idclase;
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
        if (!(object instanceof Agendaclase)) {
            return false;
        }
        Agendaclase other = (Agendaclase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Agendaclase[ id=" + id + " ]";
    }
    
}
