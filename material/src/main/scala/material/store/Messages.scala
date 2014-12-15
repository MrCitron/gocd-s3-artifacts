package material.store

import org.joda.time.DateTime
import java.util.Date

trait FSOperationStatus {
  def isSuccess: Boolean
  def message: String
}
trait OpSuccess extends FSOperationStatus {
  override def isSuccess = true
  override def message = "Operation succeeded"
}
trait OpFailure extends FSOperationStatus {
  override def isSuccess = false
  override def message = "Operation failed"
}

case class CreateBucketSuccess(createdAt: DateTime, ownedBy: String) extends FSOperationStatus with OpSuccess
case class CopySuccess(md5: String) extends FSOperationStatus with OpSuccess
case class MoveSuccess(md5: String) extends FSOperationStatus with OpSuccess
case class Exists(numberOfObjects: Long) extends FSOperationStatus with OpSuccess
case class RevisionSuccess(revision: Revision, lastModified: Date, trackBackUrl: String, revisionComments: String) extends FSOperationStatus with OpSuccess
case class RevisionSinceSuccess(revisions: List[Revision]) extends FSOperationStatus with OpSuccess

case class OperationFailure(th: Throwable) extends FSOperationStatus with OpFailure {
  override def message = th.getStackTrace.map(_.toString).mkString("\n")
}

case class Artifact(pipelineName: String, stageName: String, jobName: String, revision: Option[Revision] = None) {
  val prefix = s"$pipelineName/$stageName/$jobName/"
  val withRevision = revision.fold(prefix)(rev => s"$prefix$rev/")
}
case class Revision(revision: String) extends Ordered[Revision] {
  val parts = revision.split('.').map(_.toInt)
  val major = parts(0)
  val minor = parts(1)
  val patch = if (parts.length == 3) parts(2) else 0
  def max(that: Revision) = if(this.compare(that) > 0) this else that

  def compare(that: Revision) = implicitly[Ordering[(Int, Int, Int)]].compare((this.major, this.minor, this.patch), (that.major, that.minor, that.patch))
}