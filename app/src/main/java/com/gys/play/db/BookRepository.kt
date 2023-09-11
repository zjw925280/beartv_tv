package com.gys.play.db

import com.gys.play.db.dao.BookDao
import com.gys.play.db.entity.Book

class BookRepository(val dao: BookDao) {
//    val all = dao.getAll()

    fun insert(book: Book) {
        dao.insert(book)
    }

    fun getBook(bookId: Int): Book {
        return dao.getBook(bookId)
    }
    fun removeBookshelf(ids:String){
        return dao.removeBookshelf(ids)
    }
    fun setBookshelf(id:Int,isAddBookshelf :Int){
          dao.getBook(id)?.run {
              bookshelf =isAddBookshelf
              insert(this)
          }
    }

}