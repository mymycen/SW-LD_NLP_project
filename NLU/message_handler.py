from __future__ import unicode_literals

import json

import requests
from rasa_nlu.config import RasaNLUConfig
from rasa_nlu.model import Interpreter


class MessageHandler:
    interpreter = None
    dialogue_topics = None
    dialogue_models = {}

    nlu_model_path = 'models/nlu/default/current'

    def __init__(self):
        self.interpreter = Interpreter.load(self.nlu_model_path, RasaNLUConfig('nlu_model_config.json'))

    def handle_message(self, message):
        # Parse user input
        nlu_json_response = self.interpreter.parse(message)

        print(nlu_json_response)
        entities = nlu_json_response['entities']
        subject = None
        predicate = None

        if len(entities) < 2:
            resp = requests.get('http://localhost:8080/nlp?message=' + message)
            if resp.status_code != 200:
                raise resp.status_code
        else:
            for entity in entities:
                if entity['entity'] == 'subject':
                    subject = entity['value']
                elif entity['entity'] == 'predicate':
                    predicate = entity['value']

            resp = requests.get(
                'http://localhost:8080/nlp?message=' + message + '&subject=' + subject + '&predicate=' + predicate + '&onlyMatch=True')
            if resp.status_code != 200:
                raise resp.status_code

        data = dict()
        data['message'] = message
        data['subject'] = subject
        data['predicate'] = predicate
        data['response'] = resp.text

        return json.dumps(data)
