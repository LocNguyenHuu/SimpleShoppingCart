package controllers

import com.google.inject.{Inject, Singleton}
import models.Image
import play.api.Logger
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.data._
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import services.{IImageService, ProductService}

/**
	* Created by locnguyen on 4/8/17.
	*/
@Singleton
class ImageController @Inject() (val messagesApi: MessagesApi, val productService: ProductService, val service: IImageService) extends Controller with I18nSupport {
	val imageForm: Form[Image] = Form(
		mapping(
			"id" -> optional(longNumber),
			"productId" -> optional(longNumber),
			"url" -> text
		)(models.Image.apply)(models.Image.unapply)
	)

	def index() = Action {implicit request =>
		Logger.info("index image call")
		val images = service.findAll().getOrElse(Seq())
		Ok(views.html.image_index(images))
	}

	def blank = Action {implicit request =>
		Logger.info("blank image call")
		Ok(views.html.image_details(None, imageForm, productService.findAllProduct()))
	}

	def details(id: Long) = Action {implicit request =>
		Logger.info("details image call")
		val image = service.findById(id).get
		Ok(views.html.image_details(Some(id), imageForm.fill(image), productService.findAllProduct()))
	}

	def insert()= Action { implicit request =>
		Logger.info("insert called.")
		imageForm.bindFromRequest.fold(
			form => {
				BadRequest(views.html.image_details(None, form,
					productService.findAllProducts))
			},
			image => {
				If (image.productId==null ||
					image.productId.getOrElse(0)==0) {
					Redirect(routes.ImageController.blank).
						flashing("error" -> "Product ID Cannot be Null!")
				}else {
					if (image.url==null || "".equals(image.url)) image.url
						= "/assets/images/default_product.png"
					val id = service.insert(image)
					Redirect(routes.ImageController.index).
						flashing("success" -> Messages("success.insert", id))
				}
			})
	}

	def update(id: Long) = Action {implicit request =>
		Logger.info("update image call")
		imageForm.bindFromRequest().fold(
			form => {
				Ok(views.html.image_details(Some(id), form, mull)).flashing("error" -> "Fix the error!")
			},
			image => {
				service.update(id, image)
				Redirect(routes.ImageController.index).flashing("success" -> Messages("success.update", image.id))
			}
		)
	}

	def remove(id: Long) = Action {implicit request =>
		Logger.info("remove image call")
		service.findById(id).map { image =>
			service.remove(id)
			Redirect(routes.ImageController.index).flashing("success" -> Messages("success.delete"), image.id)
		}.getOrElse(NotFound)
	}

}
