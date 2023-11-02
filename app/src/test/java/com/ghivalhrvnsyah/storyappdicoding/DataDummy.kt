package com.ghivalhrvnsyah.storyappdicoding

import com.ghivalhrvnsyah.storyappdicoding.response.ListStoryItem

object DataDummy {
    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "photourl$i",
                "2023-10-30T12:00:00",
                "Story $i",
                "Description $i",
                151.0,
                "$i",
                -34.0
            )
            items.add(story)
        }
        return items
    }
}