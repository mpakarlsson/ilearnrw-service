package com.ilearnrw.usermanager.model;

import javax.validation.constraints.Size;

public class Permission {
        
        private Integer id;
        
        @Size(min = 4, max = 20)
        private String name;
        
        public Permission() {}
        
        public Permission(String id)
        {
        	this.id = Integer.valueOf(id);
        }

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
        
        @Override
        public boolean equals(Object obj) {
        	return ((Permission)obj).id == this.id;
        }
}