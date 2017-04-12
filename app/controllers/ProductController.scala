package controllers

import com.google.inject.{Inject, Singleton}
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{Action, Controller}
import services.IProductService
import models._
import play.api.Logger
/**
	* Created by locnguyen on 4/8/17.
	*/
@Singleton
class ProductController @Inject() (val messagesApi: MessagesApi, service: IProductService) extends Controller with I18nSupport {
	val productForm: Form[Product] = Form(mapping(
		"id" -> optional(longNumber),
		"name" -> nonEmptyText,
		"detail" -> text,
		"price" -> bigDecimal
	)(models.Product.apply)(models.Product.unapply))

	def index = Action { implicit request =>
		val products = service.findAll().getOrElse(Seq())
		Logger.info("index called. Products: " + products)
		Ok(views.html.product_index(products))
	}

	def blank = Action { implicit request =>
		Logger.info("blank called.")
		Ok(views.html.product_details(None, productForm))
	}

	def details(id: Long) = Action { implicit request =>
		Logger.info("details called.")
		val product = service.findById(id).get
		Ok(views.html.product_details(Some(id), productForm.fill(product)))
	}

	def insert() = Action { implicit request =>
		Logger.info("insert called.")
		productForm.bindFromRequest().fold(
			form => {
				BadRequest(views.html.product_details(None, form))
			}, product => {
				val id = service.insert(product)
				Redirect(routes.ProductController.index).flashing("success" -> Messages("success insert", id))
			}
		)
	}

	def update(id: Long) = Action {implicit request =>
		Logger.info("update call")
		productForm.bindFromRequest().fold(
			form => {
				BadRequest(views.html.product_details(None, form))
			}, product => {
				val id = service.update(id, product)
				Redirect(routes.ProductController.index).flashing("success" -> Messages("success update", product.name))
			}
		)
	}

	def remove(id: Long) = Action { implicit request =>
		Logger.info("remove call")
		service.findById(id).map {product =>
			service.remove(id)
			Redirect(routes.ProductController.index).flashing("success"-> Messages("successs remove", product.name))
		}.getOrElse(NotFound)
	}
}
