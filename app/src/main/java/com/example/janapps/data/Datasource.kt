package com.example.janapps.data
import com.example.janapps.model.ListLazy
import com.example.janapps.R

class Datasource {
    fun loadLists(): List<ListLazy> {
        return listOf<ListLazy>(
            ListLazy(R.string.List1, R.string.List5, R.drawable.image1, R.string.hobby1,),
            ListLazy(R.string.List2,R.string.List6, R.drawable.image2, R.string.hobby2,),
            ListLazy(R.string.List3,R.string.List7, R.drawable.image3, R.string.hobby3,),
            ListLazy(R.string.List4,R.string.List8, R.drawable.image4, R.string.hobby4,))
    }
}