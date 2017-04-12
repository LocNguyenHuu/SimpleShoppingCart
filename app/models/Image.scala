package models

/**
	* Created by locnguyen on 4/8/17.
	*/
case class Image(var id: Option[Long], var productId: Option[Long], var url : String) {
	override def toString: String = {
		"Image { productId: " + productId.getOrElse(0) + ", url: " + url + "}"
	}
}
