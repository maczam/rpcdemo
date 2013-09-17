namespace java info.hexin.rpcdemo.thrift  
struct User {  
  1: i32    userId,  
  2: string loginName,  
  3: string password,  
  4: string name  
}  
exception UserNotFound {  
  1: string message  
}  
service UserService {  
  User getUser(1:string loginName) throws (1:UserNotFound unf),  
  list<User> getUsers()  
}