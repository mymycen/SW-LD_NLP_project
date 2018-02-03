package org.ncbo.stanford.sparql.examples;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.core.DatasetImpl;

/**
 * This is an example built on top of the Jena ARQ library.
 * See: http://jena.sourceforge.net/ARQ/documentation.html
 */
public class JenaARQFederationExample {
	
	public JenaARQFederationExample() {
	}
	public ResultSet executeQuery(String queryString) throws Exception {

		 QueryExecution exec =  QueryExecutionFactory.create(QueryFactory.create(queryString), new
				 DatasetImpl(ModelFactory.createDefaultModel()));
		 return exec.execSelect();

	}
	public static void main(String[] args) throws Exception {
		
		/**
		 * For documentation please read "Federated SPARQL queries"
		 *  at http://www.bioontology.org/wiki/index.php/SPARQL_BioPortal 
		 */
		JenaARQFederationExample test = new JenaARQFederationExample();

//		String query = "PREFIX map: <http://protege.stanford.edu/ontologies/mappings/mappings.rdfs#> \n" +
//				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
//				"PREFIX skos: <http://www.w3.org/2004/02/skos/core#> \n" + 
//				"SELECT DISTINCT ?mappedParent WHERE {\n" + 
//				"    SERVICE <http://sparql.bioontology.org/mappings/sparql/?apikey=YOUR API KEY HERE> {\n" + 
//				"        ?mapping map:target <http://purl.bioontology.org/ontology/CSP/0468-5952> .\n" + 
//				"        ?mapping map:source ?source .\n" + 
//				"    }\n" + 
//				"    SERVICE <http://sparql.bioontology.org/ontologies/sparql/?apikey=YOUR API KEY HERE> {\n" + 
//				"        ?source rdfs:subClassOf ?mappedParent .\n" +
//				"    }\n" +
//				"}";
		
		String query = "PREFIX imdb: <http://data.linkedmdb.org/resource/movie/>\r\n" + 
				"PREFIX dcterms: <http://purl.org/dc/terms/>\r\n" + 
				"PREFIX dbpo: <http://dbpedia.org/ontology/>\r\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"\r\n" + 
				"SELECT ?birthDate ?spouseName ?movieTitle ?movieDate {\r\n" + 
				"  { SERVICE <http://dbpedia.org/sparql>\r\n" + 
				"    { SELECT ?birthDate ?spouseName WHERE {\r\n" + 
				"        ?actor rdfs:label \"Arnold Schwarzenegger\"@en ;\r\n" + 
				"               dbpo:birthDate ?birthDate ;\r\n" + 
				"               dbpo:spouse ?spouseURI .\r\n" + 
				"        ?spouseURI rdfs:label ?spouseName .\r\n" + 
				"        FILTER ( lang(?spouseName) = \"en\" )\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"  { SERVICE <http://data.linkedmdb.org/sparql>\r\n" + 
				"    { SELECT ?actor ?movieTitle ?movieDate WHERE {\r\n" + 
				"      ?actor imdb:actor_name \"Arnold Schwarzenegger\".\r\n" + 
				"      ?movie imdb:actor ?actor ;\r\n" + 
				"             dcterms:title ?movieTitle ;\r\n" + 
				"             dcterms:date ?movieDate .\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}";
		ResultSet results = test.executeQuery(query);
	    for ( ; results.hasNext() ; ) {
	      QuerySolution soln = results.nextSolution() ;
	      System.out.println(soln);
	    }
	}
}
