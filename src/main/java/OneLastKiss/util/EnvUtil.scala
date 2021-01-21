package OneLastKiss.util

import org.apache.spark.SparkContext

object EnvUtil {

    private val envLocal = new ThreadLocal[SparkContext]()

    def setEnv( sc : SparkContext ): Unit = {
        envLocal.set(sc)
    }

    def getEnv(): SparkContext = {
        envLocal.get()
    }

    def clear(): Unit = {
        envLocal.remove()
    }
}
