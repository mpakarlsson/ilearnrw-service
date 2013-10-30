package com.ilearnrw.usermanager.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="roles")
public class Role {
        
        @Id
        @GeneratedValue
        private Integer id;
        
        private String name;
        
        private Boolean hasRole;

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

		public Boolean getHasRole() {
			return hasRole;
		}

		public void setHasRole(Boolean hasRole) {
			this.hasRole = hasRole;
		}

}