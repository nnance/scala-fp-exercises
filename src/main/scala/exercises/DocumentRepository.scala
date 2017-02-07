package exercises

/**
  * Created by nicknance on 2/6/17.
  */

object UserDomain {
  type UserID = String
  type UserRepository = List[User]

  case class User(id: UserID, name: String)
}

object UserDB {
  import UserDomain._

  type FindOne = UserID => Option[User]
}

case class UserMemoryDB(users: List[UserDomain.User]) {
  import UserDomain._

  def findOne(userId: UserID): Option[User] =
    users.find(u => u.id == userId)
}

object FolderDomain {
  type Location = String
  type FileStream = String

  case class Folder(path: String)
}

trait FolderDB {
  import FolderDomain._

  def FindOne: Location => Folder
}

trait FileStorage {
  import UserDomain._
  import FolderDomain._

  def canWrite: (User, Folder) => Boolean
  def save: (FileStream, User, Folder) => Boolean
}

object DocumentRepository {
  import UserDomain._
  import FolderDomain._
  import UserDB._

  def storeDocument(findOne: FindOne, folderDB: FolderDB, fileStorage: FileStorage,
                    userID: UserID, folderLocation: Location, fileStream: FileStream): Boolean = {
    findOne(userID).exists(u => {
      val folder = folderDB.findOne(folderLocation)
      if (!fileStorage.canWrite(u, folder)) {
        fileStorage.save(fileStream, u, folder)
      } else {
        false
      }
    })
  }
}

object DocumentTest {
  def main(args: Array[String]): Unit = {

    System.out.println("")
  }
}