package code.remotedata

import java.util.Date

import akka.actor.Actor
import akka.event.Logging
import code.metadata.narrative.{MappedNarratives, RemoteNarrativesCaseClasses}
import code.model._
import net.liftweb.util.ControlHelpers.tryo


class RemotedataNarrativesActor extends Actor with ActorHelper {

  val logger = Logging(context.system, this)

  val mapper = MappedNarratives
  val cc = RemoteNarrativesCaseClasses

  def receive = {

    case cc.getNarrative(bankId: BankId, accountId: AccountId, transactionId: TransactionId) =>
      logger.info("getNarrative(" + bankId + ", " + accountId + ", " + transactionId + ")")
      sender ! extractResult(mapper.getNarrative(bankId, accountId, transactionId)())

    case cc.setNarrative(bankId: BankId, accountId: AccountId, transactionId: TransactionId, narrative: String) =>
      logger.info("setNarrative(" + bankId + ", " + accountId + ", " + transactionId + ", " + narrative + ")")
      sender ! extractResult(mapper.setNarrative(bankId, accountId, transactionId)(narrative))

    case cc.bulkDeleteNarratives(bankId: BankId, accountId: AccountId) =>
      logger.info("bulkDeleteComments(" + bankId +", "+ accountId + ")")
      sender ! extractResult(mapper.bulkDeleteNarratives(bankId, accountId))

    case message => logger.info("[AKKA ACTOR ERROR - REQUEST NOT RECOGNIZED] " + message)

  }

}


