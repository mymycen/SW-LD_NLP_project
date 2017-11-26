from rasa_core.actions import Action
from SPARQLWrapper import SPARQLWrapper, JSON



class ExecuteQueryAction(Action):
    # the name should match the action to the utterance
    def name(self):
        return 'action_execute_query'
    # the run executes when the action is called
    def run(self, dispatcher, tracker, domain):
        # get the location entity from the console
        objectPhrase = tracker.get_slot('object')
        predicatePhrase = tracker.get_slot('predicate')

        sparql = SPARQLWrapper("http://dbpedia.org/sparql")
        sparql.setReturnFormat(JSON)

        query = self.build_query(objectPhrase, predicatePhrase)
        print query

        sparql.setQuery(query)
        print sparql.query().convert()

        dispatcher.utter_message('Your looking for the ' + predicatePhrase + ' of ' + objectPhrase)

        return []

    def build_query(self, objectPhrase, predicatePhrase):
        fragment = predicatePhrase
        if str(predicatePhrase) == 'population':
           fragment += 'Total'


        return 'SELECT ?label ?' + predicatePhrase + ' WHERE{?label rdfs:label "' + objectPhrase + '"@en ; dbo:' + fragment + ' ?' + predicatePhrase + ' .}'
