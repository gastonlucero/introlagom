package datahack.bootcamp.google.impl

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.ServiceCall
import datahack.bootcamp.google.api.GeoGoogleService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GeoGoogleServiceImpl extends GeoGoogleService{

  def googleMapService(latlong:String) :Future[String]= {
    Future("Esta es la direccion real")
  }

  override def direccion(latlong: String): ServiceCall[NotUsed, String] = {
    _ => googleMapService(latlong)
  }
}
