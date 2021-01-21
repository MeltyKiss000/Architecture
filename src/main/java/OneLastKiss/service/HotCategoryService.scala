package OneLastKiss.service

import OneLastKiss.common.TService
import OneLastKiss.dao.HotCategoryDao

class HotCategoryService extends TService{

    private val hotCategoryDao = new HotCategoryDao()

    override def analysis() = {
        val actionRDD = hotCategoryDao.readFile("data/user_visit_action.txt")

        val mapRDD = actionRDD.flatMap(
            line => {
                val datas = line.split("_")
                if ( datas(6) != "-1" ) {
                    List((datas(6), (1, 0, 0)))
                } else if ( datas(8) != "null" ) {
                    val ids = datas(8).split(",")
                    ids.map(id => (id, (0, 1, 0)))
                } else if ( datas(10) != "null" ) {
                    val ids = datas(10).split(",")
                    ids.map(id => (id, (0, 0, 1)))
                } else {
                    List()
                }
            }
        )
        val cntRDD = mapRDD.reduceByKey(
            (t1, t2) => {
                (t1._1 + t2._1, t1._2 + t2._2, t1._3 + t2._3)
            }
        )
        val sortRDD = cntRDD.sortBy(_._2, false)
        sortRDD.take(10)
    }
}
