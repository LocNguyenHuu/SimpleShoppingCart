package services

import com.google.inject.Singleton
import models._
/**
	* Created by locnguyen on 4/8/17.
	*/
trait IProductService extends BaseService[Product] {
	def insert(product: Product): Long
	def update(id: Long, product: Product): Boolean
	def remove(id: Long): Boolean
	def findById(id: Long): Option[Product]
	def findAll(): Option[List[Product]]
	def findAllProduct(): Seq[(String, String)]
}

@Singleton
class ProductService extends IProductService {
	override def insert(product: Product) = {
		val id = idCounter.incrementAndGet()
		product.id = Some(id)
		inMemoryDB.put(id, product)
		id
	}

	override def update(id: Long, product: Product) = {
		validateId(id)
		product.id = Some(id)
		inMemoryDB.put(id, product)
		true
	}

	override def remove(id: Long) = {
		validateId(id)
		inMemoryDB.remove(id)
		true
	}

	override def findById(id: Long) = {
		inMemoryDB.get(id)
	}

	override def findAll() = {
		if (inMemoryDB.values == Nil || inMemoryDB.values.toList.size == 0) None
		Some(inMemoryDB.values.toList)
	}

	override def findAllProduct() = {
		val products: Seq[(String, String)] = findAll().getOrElse(List(Product(Some(0), "", "", 0))).toSeq.map { product =>
			(product.id.get.toString, product.name)
		}
	}

	private def validateId(id: Long): Unit = {
		val entry = inMemoryDB.get(id)
		if (entry == null) throw RuntimeException

	}
}
