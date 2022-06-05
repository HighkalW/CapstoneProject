# Machine Learning Directory of The Nawara Project
This is the sub-directory of the Nawara project containing all the machine learning bits and pieces.


## Table of Contents

* [General Info](#general-info)
* [Technologies](#technologies)
* [Replication Method](#replication-method)

*Note: contents are still being updated*

## General Info
The Nawara Project uses Machine Learning to do Natural Language Processing which filters out comments for toxicity before they are posted on the application.

## Technologies
The Machine Learning section of this project is created with:
* Python version: 3.10.2
* TensorFlow library version: 2.8.0
* Pandas library version: 1.4.1
* PySastrawi library version: 1.2.0
* numpy library version: 1.22.2

## Replication Method

1. Download the dataset from Kaggle and unzip the files
2. Change incorrect labels
3. Preprocess the dataset (removing stopwords, removing unnecessary and non-alphanumeric characters, stemming, lowercasing and removing 'alay' words)
4. Check and change incorrect labels again
5. Apply text vectorization with the TextVectorization module of tensorflow.keras.layers
6. Split the dataset into training (80%) and validation (20%) datasets
7. Build the model
8. Testing and validation
9. Convert the model to JSON format
10. Upload model to AI Platform for deployment

