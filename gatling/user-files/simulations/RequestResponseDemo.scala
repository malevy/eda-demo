package eda.eventing 

import io.gatling.core.Predef._ 
import io.gatling.http.Predef._
import scala.concurrent.duration._

class CreateOrderUsingRequestResponseDemo extends Simulation {

  val baseURL: String = sys.env.get("HOSTPORT") match {
      case Some(p) => "http://" + sys.env("HOSTNAME") + ":" + p
      case None => "http://" + sys.env("HOSTNAME")
  }

  println(baseURL)
  
  val httpConf = http 
    .baseURL(baseURL)
    .acceptHeader("application/json")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val createOrderScenario = scenario("CreateOrderSync")
    .repeat(5) {
      exec(
        http("post-order")
          .post("/orders/sync")
          .body(StringBody("""{"showId":"456765","status":"pending","seats":["1A","1B","1C"]}""")).asJSON
          .check(status.is(201))

          // save off the location header
          .check(header(HttpHeaderNames.Location).saveAs("fetchUrl"))) // [1]
        .pause(2 seconds)
        .exec(
          http("fetch-order")

            // fetch from the saved location
            .get("${fetchUrl}")
            .check(status.is(200)))
        .pause(1 seconds)

    }

  setUp( 
    createOrderScenario.inject(rampUsers(20) over (5 seconds))
  ).protocols(httpConf)
}

// [1] https://github.com/gatling/gatling/blob/master/gatling-http/src/main/scala/io/gatling/http/Headers.scala


