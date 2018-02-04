# Search index for semantic data

1. cd to repo folder  
``cd SolrIndex``
2. Run all the below commands  
 ```
 wget http://archive.apache.org/dist/lucene/solr/7.1.0/solr-7.1.0.tgz
 tar -zxvf solr-7.1.0.tgz
 mv search.tar.gz solr-7.1.0/server/solr
 cd solr-7.1.0/server/solr
 tar -zxvf search.tar.gz
 rm search.tar.gz
 ```
3. Run `(cd ../../ && ./bin/solr start -p 8983)`
4. Load the repository into IDE.
5. Locate Main.java and run the class
6. Now you must be able to send and retrieve information about Berlin on

http://localhost:5050/search/get?search=what is the capital of Germany?
