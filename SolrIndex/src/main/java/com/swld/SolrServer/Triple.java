package com.swld.SolrServer;

import org.apache.solr.client.solrj.beans.Field;

public class Triple {

        @Field
        private String s;

        @Field
        private String p;
        @Field
        private String o;

        public String getO() {
            return o;
        }

        public void setO(String o) {
            this.o = o;
        }

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return "s:"+s+" p:"+ p+" o:"+o;
        }
}
