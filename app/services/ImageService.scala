package services

import models.Image
import play.api.Logger
/**
	* Created by locnguyen on 4/8/17.
	*/
trait IImageService extends BaseService[Image] {
	def insert(image: Image): Long
	def update(id: Long, image: Image): Boolean
	def remove(id: Long): Boolean
	def findById(id: Long): Option[Image]
	def findAll(): Option[List[Image]]
}

class ImageService extends IImageService {
	def insert(image: Image): Long = {
		Logger.info("insert image call")
		val id = idCounter.incrementAndGet()
		image.id = Some(id)
		inMemoryDB.put(id, image)
		id
	}

	def update(id: Long, image: Image): Boolean = {
		validate(id)
		Logger.info("update image call")
		update.id = Some(id)
		inMemoryDB.put(id, image)
		true
	}

	def remove(id: Long): Boolean = {
		validate(id)
		Logger.info("remove image call")
		inMemoryDB.remove(id)
	}

	def findById(id: Long): Option[Image] = {
		Logger.info("Find image by id call")
		inMemoryDB.get(id)
	}

	def findAll(): Option[List[Image]] = {
		if (inMemoryDB.values.toList == null || inMemoryDB.values.toList.size == 0) None
		Some(inMemoryDB.values.toList)
	}

	private def validate(id: Long): Unit = {
		val entry = inMemoryDB.get(id)
		if (entry == null) throw new RuntimeException("Could not find image: " + id)
	}
}
