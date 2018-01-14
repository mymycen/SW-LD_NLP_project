from __future__ import unicode_literals

nlu_training_data = 'data/nlu_training_data.json'

def train_model():
    from rasa_nlu.config import RasaNLUConfig
    from rasa_nlu.converters import load_data
    from rasa_nlu.model import Trainer

    training_data = load_data(nlu_training_data)
    trainer = Trainer(RasaNLUConfig('nlu_model_config.json'))
    trainer.train(training_data)
    trainer.persist('models/nlu/', fixed_model_name='current')


if __name__ == '__main__':
    train_model()
