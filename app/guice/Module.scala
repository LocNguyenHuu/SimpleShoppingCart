package guice

import java.time.Clock

import com.google.inject.AbstractModule
import services._

/**
	* Created by locnguyen on 4/8/17.
	*/
class Module extends AbstractModule {
	override def configure() = {
		bind(classOf[Clock]).toInstance(Clock.systemDefaultZone())

		bind(classOf[IProductService]).to(classOf[ProductService]).asEagerSingleton()
		bind(classOf[IReviewService]).to(classOf[ReviewService]).asEagerSingleton()
		bind(classOf[IImageService]).to(classOf[ImageService]).asEagerSingleton()
	}
}
