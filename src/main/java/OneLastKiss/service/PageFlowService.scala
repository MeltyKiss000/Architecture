package OneLastKiss.service

import OneLastKiss.bean.UserVisitAction
import OneLastKiss.common.TService
import OneLastKiss.dao.{PageFlowDao, WordCountDao}
import org.apache.spark.rdd.RDD

class PageFlowService extends TService {

    private val pageFlowDao = new PageFlowDao()

    // 数据分析
    override def analysis() = {

        // TODO 1. 读取原始数据，转换为对象
        val actionRDD = pageFlowDao.readFile("data/user_visit_action.txt")
        // TODO 将原始数据转换为类（对象）使用
        val actionObjs = actionRDD.map(
            line => {
                val datas = line.split("_")
                UserVisitAction(
                    datas(0),
                    datas(1).toLong,
                    datas(2),
                    datas(3).toLong,
                    datas(4),
                    datas(5),
                    datas(6).toLong,
                    datas(7).toLong,
                    datas(8),
                    datas(9),
                    datas(10),
                    datas(11),
                    datas(12).toLong
                )
            }
        )
        actionObjs.cache()
        // TODO 2. 计算分母
        val pageCountMap = actionObjs.map(
            action => {
                (action.page_id, 1)
            }
        ).reduceByKey(_+_).collect.toMap

        // TODO 3. 计算分子
        // 根据session进行分组
        val groupRDD: RDD[(String, Iterable[UserVisitAction])] = actionObjs.groupBy(_.session_id)
        // 分组后根据的时间进行排序
        val zipRDD: RDD[(String, List[(String, Int)])] = groupRDD.mapValues(
            iter => {
                val actions: List[UserVisitAction] = iter.toList.sortBy(_.action_time)

                // 1,2,3,4,5
                // 2,3,4,5
                val flowids = actions.map(
                    action => {
                        (action.page_id)
                    }
                )
                // 1-2, 2-3, 3-4,4-5
                val idToid: List[(Long, Long)] = flowids.zip(flowids.tail)

                idToid.map {
                    case (id1, id2) => {
                        (id1 + "-" + id2, 1)
                    }
                }
            }
        )
        val listRDD: RDD[List[(String, Int)]] = zipRDD.map(_._2)
        val flatRDD: RDD[(String, Int)] = listRDD.flatMap(list=>list)
        val resultRDD = flatRDD.reduceByKey(_+_)

        // TODO 计算页面单跳转换率
        resultRDD.foreach{
            case (pageflow, sum) => {
                // 分子 ： sum
                // 页面1 - 页面2 / 页面1
                val pageid1 = pageflow.split("-")(0)
                // 分母
                val total = pageCountMap.getOrElse(pageid1.toLong, 1)

                println(s"页面跳转【${pageflow}】转换率 = " + sum.toDouble / total)
            }
        }

    }
}
