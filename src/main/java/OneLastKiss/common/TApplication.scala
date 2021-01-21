package OneLastKiss.common

import OneLastKiss.controller.WordCountController
import OneLastKiss.util.EnvUtil
import org.apache.spark.{SparkConf, SparkContext}

trait TApplication {

    // 传递参数的方式 - 1
    //def start( controller : TController ): Unit = {
    // 使用抽象方法传递数据
    //def getController():TController
    // 控制抽象，将代码作为参数传递给函数执行
    def start(app:String, master:String="local[*]")( op : => Unit ): Unit = {
        // TODO 通过Spark框架完成WC功能的实现
        val sparkConf = new SparkConf().setMaster(master).setAppName(app)
        val sc = new SparkContext(sparkConf)
        EnvUtil.setEnv(sc)

        //getController.execute()
        try {
            op
        } catch {
            case e : Exception => println(e.getMessage)
        }

        // 关闭环境
        sc.stop()
    }

}
