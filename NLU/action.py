from rasa_core.actions import Action

import json
import requests


class ExecuteQueryAction(Action):
    def name(self):
        return 'action_execute_query'

    def run(self, dispatcher, tracker, domain):
        # get the location entity from the console
        subject = tracker.get_slot('subject')
        predicate = tracker.get_slot('predicate')

        resp = requests.get(
            'http://localhost:8080/nlp?subject=' + subject + '&predicate=' + predicate + '&onlyMatch=True', )
        if resp.status_code != 200:
            raise resp.status_code

        data = {}
        data['slots'] = tracker.current_slot_values()
        data['message'] = tracker.latest_message.parse_data
        data['object'] = subject
        data['predicate'] = predicate
        data['response'] = resp.text

        print json.dumps(data)

        dispatcher.utter_message(json.dumps(data))

        return []

    def build_query(self, objectPhrase, predicatePhrase):
        fragment = predicatePhrase
        if str(predicatePhrase) == 'population':
            fragment += 'Total'

        return 'SELECT ?label ?' + predicatePhrase + ' WHERE{?label rdfs:label "' + objectPhrase + '"@en ; dbo:' + fragment + ' ?' + predicatePhrase + ' .}'
