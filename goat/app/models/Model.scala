package models

import java.util.UUID

/**
 * Created by fred on 04/11/2015.
 */
trait Model {

  def state = UUID.randomUUID().toString()

}
