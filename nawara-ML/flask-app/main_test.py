from flask import Flask, request
import tensorflow as tf
import numpy as np

model_path = 'nawara-ML/toxicity_classifier/model/model_20220608-214838/'
model = tf.keras.models.load_model(model_path)

print(model.summary())

data = "apa sih bodoh"
print(model.predict(np.expand_dims(data, 0)))