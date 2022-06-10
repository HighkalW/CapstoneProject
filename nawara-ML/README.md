# Machine Learning Directory of The Nawara Project
This is the sub-directory of the Nawara project containing all the machine learning bits and pieces.


## Table of Contents

* [General Info](#general-info)
* [Technologies](#technologies)
* [Datasets](#datasets)
* [Replication Method](#replication-method)

## General Info
The Nawara Project uses Machine Learning to do Natural Language Processing which filters out comments for toxicity before they are posted on the application.

## Technologies
The Machine Learning section of this project is created with:
* Python version: 3.10.2
* Scikit-learn version: 1.1.1
* TensorFlow library version: 2.8.0
* Pandas library version: 1.4.1
* PySastrawi library version: 1.2.0
* numpy library version: 1.22.2

## Datasets
* Indonesian Abusive and Hate Speech Twitter Text

  https://www.kaggle.com/datasets/ilhamfp31/indonesian-abusive-and-hate-speech-twitter-text
* Indonesian Stoplist

  https://www.kaggle.com/datasets/oswinrh/indonesian-stoplist

## Replication Method

1. Download the dataset from Kaggle and unzip the files
2. Use toxic words dataset and change toxic label into 1 if there is any toxic words no matter what the context is
3. Preprocess the dataset (removing stopwords, removing unnecessary and non-alphanumeric characters, stemming, lowercasing and removing 'alay' words)
4. Merge hate speech and toxic label columns into 1 columns
5. Apply text vectorization with the TextVectorization module of tensorflow.keras.layers
6. Split the dataset into training (80%) and validation (20%) datasets and convert them into tensors
7. Build the model architecture
8. Train and validate the model, tune the parameter until satisfied
9. Save the model in SavedModel format
10. Create a Flask Application to serve prediction results from requests
11. Upload model to a Compute Engine instance, upload the files, install required modules, then install & deploy using Nginx and Gunicorn.


