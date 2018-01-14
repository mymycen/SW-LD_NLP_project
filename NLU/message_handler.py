from __future__ import unicode_literals
from rasa_nlu.config import RasaNLUConfig
from rasa_nlu.model import Interpreter

import json
import requests


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

        #TODO Check message intent confidence and entities

        entities = nlu_json_response['entities']
        subject = None
        predicate = None

        for entity in entities:
            if entity['entity'] == 'subject':
                subject = entity['value']
            elif entity['entity'] == 'predicate':
                predicate = entity['value']

        resp = requests.get('http://localhost:8080/nlp?subject=' + subject + '&predicate=' + predicate + '&onlyMatch=True')
        if resp.status_code != 200:
            raise resp.status_code

        data = {}
        data['message'] = message
        data['subject'] = subject
        data['predicate'] = predicate
        data['response'] = resp.text

        return json.dumps(data)