package OneLastKiss.controller

import OneLastKiss.common.TController
import OneLastKiss.service.HotCategoryService

class HotCategoryController extends TController{

    private val hotCategoryService = new HotCategoryService()

    override def execute(): Unit = {
        val result = hotCategoryService.analysis()
        result.foreach(println)
    }
}
