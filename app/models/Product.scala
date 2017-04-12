package models

/**
	* Created by locnguyen on 4/8/17.
	*/
case class Product(var id: Option[Long], var name: String, var details: String, var price: BigDecimal ) {
	override def toString: String = {
		"Product { id: " + id.getOrElse(0) + ",name: " + name + ", details: " + details + ", prices: " + price + "}"
	}
}
