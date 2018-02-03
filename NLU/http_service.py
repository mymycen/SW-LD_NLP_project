from flask import Flask, request

from message_handler import MessageHandler

import json

app = Flask(__name__)
handler = None


@app.route("/service", methods=['GET', 'POST'])
def converse():
    query = request.args.get('query')

    response = handler.handle_message(query)
    return response, {'Content-Type': 'application/json'}


@app.route("/service/setup", methods=['GET'])
def setup():
    data = {'message': 'The service is setup. Visit the Index.html to start searching.'}
    return json.dumps(data), {'Content-Type': 'application/json'}


if __name__ == "__main__":
    handler = MessageHandler()

    app.run(host='0.0.0.0', debug=False)
