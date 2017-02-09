package exercises

import exercises.UserDomain.UserRepository
import exercises.FolderDomain.FolderRepository

/**
  * Created by nicknance on 2/6/17.
  */

object SecurityDomain {
  type AccessRights = Int
}

object UserDomain {
  type UserID = String

  case class User(id: UserID,
                  name: String,
                  rights: SecurityDomain.AccessRights)

  type FindUser = UserID => Option[User]

  trait UserRepository {
    def findOne: FindUser
  }
}

object FolderDomain {
  type Location = String
  type FileStream = String

  case class Folder(path: String, minRights: SecurityDomain.AccessRights)

  type FindOne = Location => Folder

  trait FolderRepository {
    def findOne: FindOne
  }
}

case class FileStorage() {
  import UserDomain._
  import FolderDomain._

  def canWrite(u: User, f: Folder): Boolean =
    u.rights >= f.minRights

  def save(fs: FileStream, u: User, f: Folder): Boolean = true
}

trait DocumentStore {
  import UserDomain._
  import FolderDomain._

  def storeDocument: (UserID, Location, FileStream) => Boolean

  protected def storeDocumentWithDeps(findUser: FindUser,
                                      findFolder: FolderDomain.FindOne,
                                      fileStorage: FileStorage,
                                      userID: UserID,
                                      folderLocation: Location,
                                      fileStream: FileStream): Boolean = {

    findUser(userID).exists(u => {
      val folder = findFolder(folderLocation)
      if (fileStorage.canWrite(u, folder)) {
        fileStorage.save(fileStream, u, folder)
      } else {
        false
      }
    })
  }
}

case class DocumentInjector(userDB: UserRepository,
                            folderRepository: FolderRepository,
                            storage: FileStorage) extends DocumentStore {

  def storeDocument = storeDocumentWithDeps(userDB.findOne, folderRepository.findOne, storage, _, _, _)
}

case class UserMemoryRepository(users: List[UserDomain.User]) extends UserRepository {
  import UserDomain._

  def findOne: FindUser = userId =>
    users.find(u => u.id == userId)
}

case class FolderMemoryFS() extends FolderRepository {
  import FolderDomain._

  def findOne: FindOne = loc =>
    Folder(loc, 50)
}

object DocumentApp {
  import UserDomain._

  def main(args: Array[String]): Unit = {

    val users = List(User("1", "Test User", 100))
    val userDB = UserMemoryRepository(users)
    val folderRepo = FolderMemoryFS()

    userDB.findOne("1").foreach(_ => println("User found"))
    userDB.findOne("2").foreach(_ => println("Error: User found"))

    val folder = folderRepo.findOne("test")
    println("Found folder " + folder.path)
    userDB.findOne("1").foreach(u =>
      println("User 1 can write to folder " + FileStorage().canWrite(u, folder)))

    val store = DocumentInjector(userDB, folderRepo, FileStorage())
    if (store.storeDocument("1", "test", "")) println("User able to store document")
  }
}