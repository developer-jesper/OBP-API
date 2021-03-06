package code.remotedata

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.event.Logging
import akka.util.Timeout
import code.metadata.tags.{MappedTags, RemotedataTagsCaseClasses}
import code.model._
import net.liftweb.common._
import net.liftweb.util.ControlHelpers.tryo

import scala.concurrent.duration._


class RemotedataTagsActor extends Actor with ActorHelper {

  val logger = Logging(context.system, this)

  val mapper = MappedTags
  val cc = RemotedataTagsCaseClasses

  def receive = {

    case cc.getTags(bankId, accountId, transactionId, viewId) =>
      logger.info("getTags(" + bankId +", "+ accountId +", "+ transactionId +", "+ viewId +")")
      sender ! extractResult(mapper.getTags(bankId, accountId, transactionId)(viewId))

    case cc.addTag(bankId, accountId, transactionId, userId, viewId, text, datePosted) =>
      logger.info("addTag(" + bankId +", "+ accountId +", "+ transactionId +", "+ text +", "+ text +", "+ datePosted +")")
      sender ! extractResult(mapper.addTag(bankId, accountId, transactionId)(userId, viewId, text, datePosted))

    case cc.deleteTag(bankId : BankId, accountId : AccountId, transactionId: TransactionId, tagId : String) =>
      logger.info("deleteTag(" + bankId +", "+ accountId +", "+ transactionId + tagId +")")
      sender ! extractResult(mapper.deleteTag(bankId, accountId, transactionId)(tagId))

    case cc.bulkDeleteTags(bankId: BankId, accountId: AccountId) =>
      logger.info("bulkDeleteTags(" + bankId +", "+ accountId + ")")
      sender ! extractResult(mapper.bulkDeleteTags(bankId, accountId))

    case message => logger.info("[AKKA ACTOR ERROR - REQUEST NOT RECOGNIZED] " + message)

  }

}


