from flask import Flask, request
import tensorflow as tf
import numpy as np

model_path = 'nawara-ML/toxicity_classifier/model/model_20220526-181030'
model = tf.keras.models.load_model(model_path)

app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello, Nawara!'


@app.route('/predict', methods=['POST'])
def predict():
    data = request.get_json()
    data = data["comment"]
    prediction = model.predict(np.expand_dims(data, 0)).astype(int)
    return str(prediction[0,0])

# @app.route('/create_table', methods=['POST'])
# def add_to_database():
#     data = request.get_json()
#     #create engine sqlalchemy.c
#     #establish connection

if __name__ == "__main__":
    app.run()