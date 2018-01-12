from rasa_core.actions import Action
from SPARQLWrapper import SPARQLWrapper, JSON

import json
import requests


class ExecuteQueryAction(Action):
    # the name should match the action to the utterance
    def name(self):
        return 'action_execute_query'
    # the run executes when the action is called
    def run(self, dispatcher, tracker, domain):
        # get the location entity from the console
        objectPhrase = tracker.get_slot('subject')
        predicatePhrase = tracker.get_slot('predicate')
	resp = requests.get('http://localhost:8080/nlp?subject'+objectPhrase+'=&predicate='+predicatePhrase,)
    if resp.status_code != 200:
      checker = 0
      raise ApiError('GET /tasks/ {}'.format(resp.status_code))

        sparql = SPARQLWrapper("http://dbpedia.org/sparql")
        sparql.setReturnFormat(JSON)

        query = self.build_query(objectPhrase, predicatePhrase)

        sparql.setQuery(query)

        data = {}
        data['slots'] = tracker.current_slot_values()
        data['message'] = tracker.latest_message.parse_data
        data['query'] = query
        data['response'] = sparql.query().convert()

        print json.dumps(data)

        dispatcher.utter_message(json.dumps(data))

        return []

    def build_query(self, objectPhrase, predicatePhrase):
        fragment = predicatePhrase
        if str(predicatePhrase) == 'population':
           fragment += 'Total'


        return 'SELECT ?label ?' + predicatePhrase + ' WHERE{?label rdfs:label "' + objectPhrase + '"@en ; dbo:' + fragment + ' ?' + predicatePhrase + ' .}'
