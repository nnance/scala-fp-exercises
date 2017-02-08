package exercises

import exercises.UserDomain.FindOne

/**
  * Created by nicknance on 2/6/17.
  */

object SecurityDomain {
  type AccessRights = Int
}

object UserDomain {
  type UserID = String
  type UserRepository = List[User]

  case class User(id: UserID, name: String, rights: SecurityDomain.AccessRights)

  type FindOne = UserID => Option[User]
}

object FolderDomain {
  type Location = String
  type FileStream = String

  case class Folder(path: String, minRights: SecurityDomain.AccessRights)

  type FindOne = Location => Folder
}

case class FileStorage() {
  import UserDomain._
  import FolderDomain._

  def canWrite(u: User, f: Folder): Boolean =
    u.rights >= f.minRights

  def save(fs: FileStream, u: User, f: Folder): Boolean = true
}

object DocumentRepository {
  import UserDomain._
  import FolderDomain._

  def storeDocument(findUser: UserDomain.FindOne, findFolder: FolderDomain.FindOne, fileStorage: FileStorage,
                    userID: UserID, folderLocation: Location, fileStream: FileStream): Boolean = {
    findUser(userID).exists(u => {
      val folder = findFolder(folderLocation)
      if (fileStorage.canWrite(u, folder)) {
        fileStorage.save(fileStream, u, folder)
      } else {
        false
      }
    })
  }

  def storeDocumentWithDeps: (UserDomain.FindOne, FolderDomain.FindOne, FileStorage) =>
    (UserID, Location, FileStream) => Boolean = (findUser, findFolder, fileStorage) =>
    storeDocument(findUser, findFolder, fileStorage, _, _, _)
}

case class UserMemoryDB(users: List[UserDomain.User]) {
  import UserDomain._

  def findOne: FindOne = userId =>
    users.find(u => u.id == userId)
}

case class FolderMemoryFS() {
  import FolderDomain._

  def findOne: FolderDomain.FindOne = loc =>
    Folder(loc, 50)
}

object DocumentTest {
  import UserDomain._
  import DocumentRepository._

  def main(args: Array[String]): Unit = {

    val users = List(User("1", "Test User", 100))
    val findUser = UserMemoryDB(users).findOne
    val findFolder = FolderMemoryFS().findOne

    val storeDocument = storeDocumentWithDeps(findUser, findFolder, FileStorage())

    findUser("1").foreach(_ => println("User found"))
    findUser("2").foreach(_ => println("Error: User found"))

    val folder = findFolder("test")
    println("Found folder " + folder.path)
    findUser("1").foreach(u =>
      println("User 1 can write to folder " + FileStorage().canWrite(u, folder)))

    if (storeDocument("1", "test", "")) println("User able to store document")
  }
}