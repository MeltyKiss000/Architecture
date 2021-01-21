package OneLastKiss.service

import OneLastKiss.common.TService
import OneLastKiss.dao.WordCountDao
import org.apache.spark.rdd.RDD

class WordCountService extends TService {

    private val wordCountDao = new WordCountDao()

    // 数据分析
    override def analysis() = {

        val lineRDD = wordCountDao.readFile("data/word.txt")

        val wordRDD: RDD[String] = lineRDD.flatMap(_.split(" "))

        val wordToOneRDD: RDD[(String, Int)] = wordRDD.map(
            word => {
                println(word)
                (word, 1)
            }
        )

        val wordToCountRDD: RDD[(String, Int)] = wordToOneRDD.reduceByKey(_+_)

        wordToCountRDD.collect()
    }
}
