package controllers

import com.google.inject.{Inject, Singleton}
import models.Review
import play.api.Logger
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{Action, Controller}
import services.{IProductService, IReviewService}

/**
	* Created by locnguyen on 4/8/17.
	*/
@Singleton
class ReviewController @Inject() (val messagesApi: MessagesApi, val productService: IProductService, val service: IReviewService) extends Controller with I18nSupport {
	val reviewForm: Form[Review] = Form(
		mapping(
			"id" -> optional(longNumber),
			"productId" -> optional(longNumber),
			"author" -> nonEmptyText,
			"comment" -> nonEmptyText
		)(models.Review.apply)(models.Review.unapply)
	)

	def index = Action { implicit request =>
		val reviews = service.findAll().getOrElse(Seq())
		Logger.info("index called, Reviews: " + reviews)
		Ok(views.html.review_index(reviews))
	}

	def blank = Action { implicit request =>
		Logger.info("bank review call")
		Ok(views.html.review_details(None, reviewForm, productService.findAllProduct()))
	}

	def details(id: Long) = Action { implicit request =>
		Logger.info("details image call")
		val review = service.findById(id).get
		Ok(views.html.review_details(Some(id), reviewForm.fill(review), productService.findAllProduct()))
	}

	def insert() = Action { implicit request =>
		Logger.info("insert image call")
		reviewForm.bindFromRequest().fold(
			form => {
				BadRequest(views.html.review_details(None, form, productService.findAllProduct()))
			}, “review => {
		if (review.productId == null ||
			review.productId.getOrElse(0) == 0) {
			Redirect(routes.ReviewController.blank).flashing("error" ->
				"Product ID Cannot be Null!")
		}else {
			Logger.info("Review: " + review)
			if (review.productId == null ||
				review.productId.getOrElse(0) == 0) throw new
					IllegalArgumentException("Product  Id Cannot Be Null")
			val id = service.insert(review)
			Redirect(routes.ReviewController.index).flashing("success" -> Messages("success.insert", id))
			}
		})
	}

	def update(id: Long) = Action { implicit request =>
		Logger.info("update image call")

	}

	def remove(id: Long) = Action {
		Logger.info("remove image call")

	}
}
