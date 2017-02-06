package exercises

/**
  * Created by nicknance on 2/6/17.
  */

trait UserDomain {
  type UserID = String

  case class User(id: UserID, name: String)
}

trait UserDB {
  import exercises.UserDomain._

  def findOne(userID: UserID): User
}

object DocumentRepository {
  import exercises.UserDB._

  def storeDocument(userDB: String, folderLocation: String): Boolean = {
    val user = userDB.findOne(userID)
    val folder = folderDB.findOne(folderLocation)
    if (!userAccess.canWrite(user, folder)) {
      fileStorage.save(fileStream, user, folder)
    } else {
      false
    }
  }
}
