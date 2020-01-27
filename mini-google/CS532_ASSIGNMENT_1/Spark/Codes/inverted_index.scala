/*import scala.io.Source
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
object InvertedIndex {

  /**
   * Build an inverted index from a file with this format:
   *   doc0 w0 w1 ...
   *   doc1 w2 w4 ...
   * Each elements of a line is separated by whitespaces
   *
   * @param path Path to the file containing data to invert
   *
   * @return An inverted index
   *   w0 -> doc0 doc2 ...
   *   w1 -> doc0 doc5 ...
   */

//def main(args: String)= {*/
    val args = sc.getConf.get("spark.driver.args").split("\\s+")  
    val lineRE = """^\s*\(([^,:_.]+),(.*)\)\s*$""".r
    val data = sc.wholeTextFiles(args(0))
    .map {
     (text) => (text._1.split("/").last.dropRight(4), text._2.toLowerCase)
    }
    data.take(2)	
    data.flatMap{ 
    case (path ,text) => 
     text.split("""\W+""") .map { 
      word => (word,path)
     }
    }
    .map{ 
     case(word,path) =>((word,path),1)
    } 
    .reduceByKey {
      case(n1,n2) => n1+n2 
    } 
    .map { 
      case((word,path),n) => (word,(path,n)) 
    }
    .groupBy{ 
       case(word,(path,n)) => word 
    }
    .map{ 
      case(word,seq) => 
       val seq2 =seq map{
         case(_,(path,n)) => (path,n)
       }
       (word,seq2.mkString(","))
    }
    .saveAsTextFile(args(1))
 
 //}
//}

