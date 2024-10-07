package mycount

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
object WordCount {
  def main(args: Array[String]) {
    val inputFile =  "C:\\Users\\dell\\Desktop\\大数据分析\\第一次作业\\WordCount\\src\\WordCount\\mycount\\test.txt"
    val conf = new SparkConf().setAppName("WordCount").setMaster("local")
    val sc = new SparkContext(conf)
    val textFile = sc.textFile(inputFile)
    val wordlist = textFile.flatMap(line => line.split("\\s"))
//    wordlist.foreach(println)
//    println("-"*20)
    val wordlist_map =wordlist.map(word => (word, 1))
//    wordlist_map.foreach(println)
//    println("-"*20)
    val word_list_map_final = wordlist_map.reduceByKey((a, b) => a + b)
    word_list_map_final.foreach(println)
  }
}
