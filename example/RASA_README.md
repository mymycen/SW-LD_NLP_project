### Manual installation and deployment

Install a virtualenv in a desired location

```
sudo pip install virtualenv
#cd to our desired location
virtualenv -p python venv
```

Start your venv

```
source venv/bin/activate
```

Install Rasa NLU

```
pip install rasa_nlu
```

Install necessary libraries

```
pip install requests 
pip install -U spacy
python -m spacy download en
pip install -U scikit-learn scipy sklearn-crfsuite
```

Install Rasa Core

```
pip install rasa_core
```

Replace the _Rasa Core/run.py_ in _venv/lib/python2.7/site-packages/rasa_core_
Add the _Rasa Core/bot.py_ to _venv/lib/python2.7/site-packages/rasa_core/channels_

### Running Rasa Core
Your venv needs to be started and the 
Train the models and Rasa Core HTTP server

```
source venv/bin/activate
python -m rasa_nlu.train -c nlu_model_config.json
python -m rasa_core.train -s data/stories.md -d domain.yml -o project/dialogue
python -m rasa_core.run -d project/dialogue -u project/nlu/current
```

http://localhost:5002//bot/session/execute?query=What%20is%20the%20population%20of%20Berlin?
