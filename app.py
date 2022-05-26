from flask import Flask, request
import tensorflow as tf
import numpy as np

model = tf.keras.models.load_model('./toxicity_classifier/model/model_20220525-235635')
# input_text = vectorizer('bego lu anjing')
# res = model.predict(np.expand_dims(input_text,0))
# print((res > 0.5).astype(int))
# print(res.shape)