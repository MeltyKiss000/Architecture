package OneLastKiss.controller

import OneLastKiss.common.TController
import OneLastKiss.service.{PageFlowService, WordCountService}


class PageFlowController extends TController{
    private val pageFlowService = new PageFlowService()

    def execute(): Unit = {
        val result = pageFlowService.analysis()
        //result.foreach(println)
    }
}
