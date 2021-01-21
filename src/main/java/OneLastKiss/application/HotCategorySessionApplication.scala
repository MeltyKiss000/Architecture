package OneLastKiss.application

import OneLastKiss.common.TApplication
import OneLastKiss.controller.{HotCategoryController, HotCategorySessionController}

object HotCategorySessionApplication extends App with TApplication{

    start( "HotCategory" ){
        val controller = new HotCategorySessionController()
        controller.execute()
    }
}
