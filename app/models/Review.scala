package models

/**
	* Created by locnguyen on 4/8/17.
	*/
case class Review(var id: Option[Long], var productId: Option[Long], var author: String, var comment: String) {
	override def toString: String = {
		"Review { id:  " + id + ", ProducutId: " + productId.getOrElse(0) + ", author: " + author + ", comment: " + comment
	}
}
