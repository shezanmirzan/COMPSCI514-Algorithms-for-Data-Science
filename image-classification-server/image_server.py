import torch
import torchvision.models as models
import numpy as np
from PIL import Image
from torchvision import transforms
import urllib
import json



from flask import Flask, request, jsonify

app = Flask(__name__)

model= models.densenet121(pretrained=True)
model.eval()

with open("map.json", 'r') as f:
    idx_class = json.load(f)

@app.route('/search', methods=['GET'])
def search():
    url = request.args.get('url', None)
    filename = request.args.get('file', None)
    if url:
        filename = get_img_filename(url)
    label = get_results(filename)
    return label

def get_img_filename(url):
    filename = url.split('/')[-1]
    try: urllib.URLopener().retrieve(url, filename)
    except: urllib.request.urlretrieve(url, filename)
    return filename

def get_results(filename):
    
    input_image = Image.open(filename)
    preprocess = transforms.Compose([
    transforms.Resize(256),
    transforms.CenterCrop(224),
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),])
    input_tensor = preprocess(input_image)
    input_batch = input_tensor.unsqueeze(0) # create a mini-batch as expected by the model

    # move the input and model to GPU for speed if available
    if torch.cuda.is_available():
        input_batch = input_batch.to('cuda')
        model.to('cuda')

    with torch.no_grad():
        output = model(input_batch)
# Tensor of shape 1000, with confidence scores over Imagenet's 1000 classes
# The output has unnormalized scores. To get probabilities, you can run a softmax on it.
    a = torch.nn.functional.softmax(output[0], dim=0)
    predicted_label = idx_class[str(np.argmax(a).item())]
    return predicted_label

if __name__ == "__main__":
    app.secret_key = "shdjehdie3u92edhw2"
    app.run(debug=True,host='0.0.0.0')

app.secret_key = "shdjehdie3u92edhw2"
app.run(debug=True, host='0.0.0.0')
