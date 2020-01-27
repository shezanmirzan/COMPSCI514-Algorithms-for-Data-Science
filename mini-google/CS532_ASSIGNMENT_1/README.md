
# COMPSCI532: Systems for Data Science
### Assignment 1: Mini-Google

 Implementation a mockup of the core functionality of a Web search engine. Components:
  - Hadoop: Data Storage
  - Spark: Inverted Index Generation 
  - Rocksdb: Key-Value Pairs Storage and Evaluation
  
The following write-up is a step by step guide to run the project. 

### Running Hadoop
Java Version: java8
Hadoop Version: 3.1.2
Username: hadoop
Password: hadoop
To open the hadoop, run:

```sh
$ su - hadoop
```
This will create: 
Primary namenode: Localhost  
Secondary namenode: dhruti

Run script:
```sh
$ start-all.sh
```
Namenode information: dhruti:9870/
For browsing files, use path: 
```sh 
dhruti/hadoop/Project1_data/
```
### Running Spark
Java Version: java8
Spark Version: 2.4.4
Language: Scala
Current Directory: Directory where the entire project is stored
To generate Inverted Indexes in spark shell, use the command:
```sh 
$ spark-shell -i ./CS532_ASSIGNMENT_1/Spark/inverted_index.scala --conf spark.driver.args= "args1 args2"
```
args1 = path to fetch data = "hdfs://localhost:9000/dhruti/hadoop/Project1_data/"

args2 = path to store inverted index = "./CS532_ASSIGNMENT_1/Spark/output_ID/"

### Running Rocksdb
IDE: Eclipse 2019

Tools: Maven, WebServlet

To start the application on localhost: 
```sh
$ Run index.html
$ Enter the search keyword
$ Enter the path to current directory
```
Note: 

1: Import the project folder "first" as a Maven Project.

2: File 'index.html' is in subfolder 'Web Content' within the project folder.

3:  All the output files will be stored in current directory

4:  If the search string is a multiple-word term, then the URLs associated with the first term will be returned. 

A set of URLs associated with the keyword will be returned. It will further store the results of the testcases in 'results.txt' in path to current directory submitted by the user. 

