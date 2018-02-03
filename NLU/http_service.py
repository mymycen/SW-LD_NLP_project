from flask import Flask, request

from message_handler import MessageHandler

app = Flask(__name__)
handler = None


@app.route("/service", methods=['GET', 'POST'])
def converse():
    query = request.args.get('query')

    if handler is None:
        return 'Services is not started.'

    response = handler.handle_message(query)
    return response, {'Content-Type': 'application/json'}


if __name__ == "__main__":
    handler = MessageHandler()

    app.run(host='0.0.0.0', debug=False)
