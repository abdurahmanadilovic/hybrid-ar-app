from flask import Flask, jsonify
import random
app = Flask(__name__)

@app.route('/getInitialWorldPosition', methods=['GET'])
def get_initial_world_position():
    position = {
        "Location": {
            "X": round(random.uniform(-10, 10), 2),
            "Y": round(random.uniform(0, 5), 2),
            "Z": round(random.uniform(-10, 10), 2)
        }
    }
    return jsonify(position)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080)
