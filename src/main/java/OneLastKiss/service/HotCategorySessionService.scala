package OneLastKiss.service

import OneLastKiss.bean.UserVisitAction
import OneLastKiss.common.TService
import OneLastKiss.dao.{HotCategoryDao, HotCategorySessionDao}
import org.apache.spark.rdd.RDD

class HotCategorySessionService extends TService{

    private val hotCategorySessionDao = new HotCategorySessionDao()

    override def analysis( data : Any ) = {
        // 需求一的结果
        val array = data.asInstanceOf[Array[(String, (Int, Int, Int))]]
        val top10Ids = array.map(_._1)

        // TODO 将原始数据进行筛选过滤
        val actionRDD = hotCategorySessionDao.readFile("data/user_visit_action.txt")
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

        // TODO 保留前10热门品类的数据
        val filterActionObjs = actionObjs.filter(
            action => {
                if ( action.click_category_id != -1 ) {
                    top10Ids.contains(action.click_category_id.toString)
                } else {
                    false
                }
            }
        )


        // TODO Word(品类， session) - Count
        // (( 品类，会话 )，1）=> (( 品类，会话 )，sum）
        val reduceActionObjs = filterActionObjs.map(
            action => {
                (( action.click_category_id, action.session_id ), 1)
            }
        ).reduceByKey(_+_)

        // TODO 将统计的结果进行结构的转换
        // (( 品类，会话 )，sum）=> (品类，(会话 ，sum))
        val transformActionObjs = reduceActionObjs.map {
            case (( category, session ), sum) => {
                ( category, (session, sum) )
            }
        }

        // TODO 相同的品类放置在一起
        val groupActionObjs: RDD[(Long, Iterable[(String, Int)])] = transformActionObjs.groupByKey()

        // TODO 将分组后的数据根据点击数量进行排序并取前10
        groupActionObjs.mapValues(
            iter => {
                iter.toList.sortBy(_._2)(Ordering.Int.reverse).take(10)
            }
        ).collect
    }
}
