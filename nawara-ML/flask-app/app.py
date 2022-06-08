import os
import sys
from flask import Flask, request
import googleapiclient.discovery
from google.api_core.client_options import ClientOptions

app = Flask(__name__)

# Setup environment credentials (you'll need to change these)
file_dir = os.path.dirname(__file__)
sys.path.append(file_dir)
file_path = os.path.join(file_dir, 'nawara-351814-3584fbe1498e.json')

os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = file_path # change for your GCP key
project = "nawara-351814" # change for your GCP project
region = "asia-southeast1" # change for your GCP region (where your model is hosted)
model_name = 'nawara_toxic_classifier'

@app.route('/predict', methods=['POST'])
def predict():
    instance = request.get_json()
    instance = [[instance["comment"]]]
    prediction = predict_json(project, region, model_name, instance)
    return str(prediction[0][0])

def predict_json(project, region, model, instances, version=None):
    """Send json data to a deployed model for prediction.

    Args:
        project (str): project where the Cloud ML Engine Model is deployed.
        region (str): regional endpoint to use; set to None for ml.googleapis.com
        model (str): model name.
        instances ([Mapping[str: Any]]): Keys should be the names of Tensors
            your deployed model expects as inputs. Values should be datatypes
            convertible to Tensors, or (potentially nested) lists of datatypes
            convertible to tensors.
        version: str, version of the model to target.
    Returns:
        Mapping[str: any]: dictionary of prediction results defined by the
            model.
    """
    # Create the ML Engine service object.
    # To authenticate set the environment variable
    # GOOGLE_APPLICATION_CREDENTIALS=<path_to_service_account_file>
    prefix = f"{region}-ml" if region else "ml"
    api_endpoint = f"https://{prefix}.googleapis.com"
    client_options = ClientOptions(api_endpoint=api_endpoint)
    service = googleapiclient.discovery.build('ml', 'v1', client_options=client_options)
    name = f"projects/{project}/models/{model}"

    if version is not None:
        name += f"/versions/{version}"
    # print(name)
    response = service.projects().predict(name=name, body={'instances': instances}).execute()
    if 'error' in response:
        raise RuntimeError(response['error'])
    return response['predictions']


if __name__ == "__main__":
    app.run()