### Requirements

Before start installation make sure you have Python 3.6 installed, other versions might work but are not tested.

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

### Running NLU
Make sure you are in the NLU project folder, start the venv:

```
source venv/bin/activate
```

And train the model:

```
python -m train
```

Start the server
```
python -m http_service
```

Check if the service is working:

```
http://localhost:5000/service/setup
```
