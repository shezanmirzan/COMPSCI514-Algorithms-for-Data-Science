FROM alpine
FROM python:3.7
MAINTAINER Your Name "xyz@ed"


COPY ./requirements.txt /app/requirements.txt

WORKDIR /app

RUN apt-get update
RUN pip install -r requirements.txt
COPY . /app
CMD [ "python","./image_server.py" ]

