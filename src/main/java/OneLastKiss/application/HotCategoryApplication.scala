package OneLastKiss.application

import OneLastKiss.common.TApplication
import OneLastKiss.controller.{HotCategoryController, WordCountController}
import org.apache.spark.{SparkConf, SparkContext}

object HotCategoryApplication extends App with TApplication{

    start( "HotCategory" ){
        val controller = new HotCategoryController()
        controller.execute()
    }
}
