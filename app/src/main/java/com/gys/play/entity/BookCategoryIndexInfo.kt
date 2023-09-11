package com.gys.play.entity

data class BookCategoryIndexInfo(
    var category: MutableList<ClassifyLabel>,
    var area: MutableList<ClassifyLabel>,
    var year: MutableList<ClassifyLabel>,
    var pay_type: MutableList<ClassifyLabel>
)