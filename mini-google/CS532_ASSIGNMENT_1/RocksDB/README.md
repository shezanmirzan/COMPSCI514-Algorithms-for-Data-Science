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
Tool: Maven
Codes in Maven Eclipse Project: Key_value_Store.java, id_url_map.java, test.java 
#### Step1: Store Key-Value Pairs in DB
```sh
Run Key_value_Store.java
```
Inputs:
1: Path to inverted index created by Spark
2: Path to store key-value pairs DB

#### Step2: Store ID-URL pairs in DB
```sh
Run id_url_map.java
```
Inputs: 
1: Path to id_url_pairs.txt  
2:  Path to store output mapped file

### Step3: Run Testcases
```sh
Run test.java
```
Inputs: 
1: Path to key-value pairs DB generated in Step 1 
2: Path to output mapped file generated in Step 2
3: Path to store results of testcases
4: Keyword to be fetched

The final output is a file with testcase keywords and their assocoated URLs. 
