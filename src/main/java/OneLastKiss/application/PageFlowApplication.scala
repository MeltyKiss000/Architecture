package OneLastKiss.application

import OneLastKiss.common.TApplication
import OneLastKiss.controller.{HotCategoryController, PageFlowController}

object PageFlowApplication extends App with TApplication{

    start( "PageFlow" ){
        val controller = new PageFlowController()
        controller.execute()
    }
}
