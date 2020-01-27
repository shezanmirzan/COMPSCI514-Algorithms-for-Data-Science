
### Project 3: Image Classification Server

An image classification server handles HTTP request containing the URLs of test images. Classification labels of images are returned by the server as the final output. Classification is done using DenseNet121 model of PyTorch. 

Tools and Libraries: Docker, PyTorch, Python, Flask

Repository Files: DockerFile, image_server.py, requirements.txt, map.jason

Note: Our server handles GET requests. The input image can be sent to the server either via a local file or via a URL appended at the end of the request link.

Follow these steps to run the docker:
```sh
$ sudo docker build -t python-barcode .
$ sudo docker run -p 5000:5000 python-barcode
```

To send an input image as a URL, append the image url at the end. An example to do so is shown here:
```sh
http://0.0.0.0:5000/search?url=http://www.absoluteafrica.com/Blog/wp-content/uploads/2013/08/porini-lion-camp-1346328256.jpg
```
To send a local image, run the following:
```sh
http://0.0.0.0:5000/search?file=dog.jpg
```
Note: The image should be in the same folder where image_server.py is located. 

Here are some of the sample links on which our server was tested. These are random images of animals taken from the internet.

```sh
Tiger: https://d.newsweek.com/en/full/389477/1102-tiger-attack-01.jpg
Lion: https://s3-us-east-2.amazonaws.com/buffalozoo/uploads/2015/10/17212209/Williot_-Lion_-2015.jpg
Dog: https://www.guidedogs.org/wp-content/uploads/2018/01/Mobile.jpg
Cat: http://www.bestpets.co/wp-content/uploads/2017/08/e38767b2d4005b865e1854c265e9ab7e.jpg
Penguin: http://nzbirdsonline.org.nz/sites/all/files/1200105Aptenodytes-forsteri_15125.jpg
Fish:vhttps://sunsetmediawave.files.wordpress.com/2014/07/ranchuedited.jpg
```

Note: Please use the URLs ending with .jpg/png only (i.e. Link leading directly to the image)
