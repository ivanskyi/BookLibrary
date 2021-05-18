# Book Library 

## Overview
This project contains a REST application that allows a management book library.


Project tech stack: 
- Spring Boot,
- Hibernate,
- MySQL
- Lombok

## Information about functional:

1. Operation with creating, update, delete Book.

>These endpoints located in */BookController.java file.

- To create a book we use the address:
```
Address: */book
Request type: POST
Request param: body (object Book)
```

- To update a book we use the address:
```
Address: */book
Request type: PUT
Request param: body (object Book)
```

- To delete a book we use the address:
```
Address: */book
Request type: DELETE
Request param: body (object Book)           
```

2. Operation with creating, update, delete Author.

>These endpoints located in */AuthorController.java file.

- To create an author we use the address:
```
Address: */author
Request type: POST
Request param: body (object Author)
```

- To update an author  we use the address:
```
Address: */author
Request type: PUT
Request param: body (object Author)
```

- To delete an author we use the address:
```
Address: */author
Request type: DELETE
Request param: body (object Author)             
```

3. The operations to get some information about books.

> These endpoints located in */AuthorController.java file.

- Show all books by author name:
```
Address: */author/all-books/{authorName}
Request type: GET
Request param: PathVariable (String authorName)             
```

- Show most selling book by max soldAmount field:
```
Address: */author/most-selling-book/{authorName}
Request type: GET
Request param: PathVariable (String authorName)         
```

- Show most published book by max publishedAmount field:
```
Address: */author/most-published-book/{authorName}
Request type: GET
Request param: PathVariable (String authorName)         
```

- Show list of a most selling book by author name use partial search by author name.
```
Address: */author/most-selling-books
Request type: GET
Request param: RequestParam (String name)         
```

- Show list of a most published book by author name use partial search by author name.
```
Address: */author/most-published-books
Request type: GET
Request param: RequestParam (String name)         
```

- Show list of most successful book by author name:

```
Address: */author/most-successful-books
Request type: GET
Request param: RequestParam (String name)         
```

- Show most successful author:

```
Address: */author/most-successful
Request type: GET
Request param: none        
```

# Hints

### In this app, we have two other ways to add a new book:

  - The first way to create a new book:

When creating Author object (using `*/author` address) we also can add some books to this request. 
In this way, Hibernate will create the new table, which will contain author_id
and book_id for this author. And after all, if we will want to get books by 
this author we must get this author object and use method getBooks() to get all author books.
Use this way field author_id in Book object get author id automatically.
>JSON request body to create new book use this way:
```
{
   "authorName":"Rudyard Kipling",
   "birthDate":"1865-12-03T10:15:30",
   "phone":"+380",
   "email":"Kipling@gmail.com",
   "books":[
      {
         "bookName":"Kim",
         "publishedAmount":700,
         "soldAmount":600
      },
      {
         "bookName":"The Cat that Walked by Himself",
         "publishedAmount":50,
         "soldAmount":45
      },
      {
         "bookName":"Selected Poems",
         "publishedAmount":90,
         "soldAmount":11
      }
   ]
}
```
- The second way to create a new book.

Also, we can add new book use `*/book` address, but in this way, 
we must know the author id for a book which we will persist.
>JSON request body to create new book use this way:
```
{
   "bookName":"The Jungle Book",
   "authorId":5,
   "publishedAmount":100,
   "soldAmount":70
}
```

Symbols ```*``` in URL means our application address.
- Port for connection with application:
```
host:8083
```

- Port for connection with database:
```
host:3000
```
- Database access info:
```
user:root
password:pass
```

## Testing REST APIs using Postman:

Use postman file which located by address: 
```/src/main/resources/data/postman/*.json```

